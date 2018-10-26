package io.spring2go.promdemo.httpsimulator;

import java.util.Random;

import io.prometheus.client.Counter;
import io.prometheus.client.Histogram;

public class ActivitySimulator implements Runnable {

	private SimulatorOpts opts;

	private Random rand = new Random();

	private boolean spikeMode = false;
	
	private volatile boolean shutdown = false;
	
	private final Counter httpRequestsTotal = Counter.build()
            .name("http_requests_total")
            .help("Total number of http requests by response status code")
            .labelNames("endpoint", "status")
            .register();
	
	private final Histogram httpRequestDurationMs = Histogram.build()
			.name("http_request_duration_milliseconds")
			.help("Http request latency histogram")
			.exponentialBuckets(25, 2, 7)
			.labelNames("endpoint", "status")
			.register();
	
	public ActivitySimulator(SimulatorOpts opts) {
		this.opts = opts;
		System.out.println(opts);
	}
	
	public void shutdown() {
		this.shutdown = true;
	}

	public void updateOpts(SimulatorOpts opts) {
		this.opts = opts;
	}
	
	public boolean setSpikeMode(String mode) {
		boolean result = true;
		switch (mode) {
		case "on":
			opts.setSpikeMode(SpikeMode.ON);
			System.out.println("Spike mode is set to " + mode);
			break;
		case "off":
			opts.setSpikeMode(SpikeMode.OFF);
			System.out.println("Spike mode is set to " + mode);
			break;
		case "random":
			opts.setSpikeMode(SpikeMode.RANDOM);
			System.out.println("Spike mode is set to " + mode);
			break;
		default:
			result = false;
			System.out.println("Can't recognize spike mode " + mode);
		}
		return result;
	}

	public void setErrorRate(int rate) {
		if (rate > 100) {
			rate = 100;
		}
		if (rate < 0) {
			rate = 0;
		}
		opts.setErrorRate(rate);
		System.out.println("Error rate is set to " + rate);
	}

	public SimulatorOpts getOpts() {
		return this.opts;
	}
	
	public void simulateActivity() {
		int requestRate = this.opts.getRequestRate();
		if (this.giveSpikeMode()) {
			requestRate *= (5 + this.rand.nextInt(10));
		}
		
		int nbRequests = this.giveWithUncertainty(requestRate, this.opts.getRequestRateUncertainty());
		for (int i = 0; i < nbRequests; i++) {
			String statusCode = this.giveStatusCode();
			String endpoint = this.giveEndpoint();
			this.httpRequestsTotal.labels(endpoint, statusCode).inc();
			int latency = this.giveLatency(statusCode);
			if (this.spikeMode) {
				latency *= 2;
			}
			this.httpRequestDurationMs.labels(endpoint, statusCode).observe(latency);
		}		
	}

	public boolean giveSpikeMode() {
		switch (this.opts.getSpikeMode()) {
		case ON:
			this.spikeMode = true;
			break;
		case OFF:
			this.spikeMode = false;
			break;
		case RANDOM:
			int n = rand.nextInt(100);
			if (!this.spikeMode && n < this.opts.getSpikeStartChance()) {
				this.spikeMode = true;
			} else if (this.spikeMode && n < this.opts.getSpikeEndChance()) {
				this.spikeMode = false;
			}
			break;
		}

		return this.spikeMode;
	}
	
	public int giveWithUncertainty(int n, int u) {
		int delta = this.rand.nextInt(n * u / 50) - (n * u / 100);
		return n + delta;
	}
	
	public String giveStatusCode() {
		if (this.rand.nextInt(100) < this.opts.getErrorRate()) {
			return "500";
		} else {
			return "200";
		}
	}
	
	public String giveEndpoint() {
		int n = this.rand.nextInt(this.opts.getEndopints().length);
		return this.opts.getEndopints()[n];
	}
	
	public int giveLatency(String statusCode) {
		if (!"200".equals(statusCode)) {
			return 5 + this.rand.nextInt(50);
		}
		
		int p = this.rand.nextInt(100);
		
		if (p < 50) {
			return this.giveWithUncertainty(this.opts.getLatencyMin() + this.rand.nextInt(this.opts.getLatencyP50() - this.opts.getLatencyMin()), this.opts.getLatencyUncertainty());
		}
		if (p < 90) {
			return this.giveWithUncertainty(this.opts.getLatencyP50() + this.rand.nextInt(this.opts.getLatencyP90() - this.opts.getLatencyP50()), this.opts.getLatencyUncertainty());
		}
		if (p < 99) {
			return this.giveWithUncertainty(this.opts.getLatencyP90() + this.rand.nextInt(this.opts.getLatencyP99() - this.opts.getLatencyP90()), this.opts.getLatencyUncertainty());		
		}
		
		return this.giveWithUncertainty(this.opts.getLatencyP99() + this.rand.nextInt(this.opts.getLatencyMax() - this.opts.getLatencyP99()), this.opts.getLatencyUncertainty());
	}

	@Override
	public void run() {
		while(!shutdown) {
			System.out.println("Simulator is running...");
			this.simulateActivity();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
