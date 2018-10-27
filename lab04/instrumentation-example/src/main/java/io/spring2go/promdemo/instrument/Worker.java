package io.spring2go.promdemo.instrument;

import java.util.UUID;

import io.prometheus.client.Histogram;

public class Worker extends Thread {
	
	private static final Histogram jobsCompletionDurationSeconds  = Histogram.build()
            .name("jobs_completion_duration_seconds")
            .help("Histogram of job completion time")
            .linearBuckets(4, 1, 16)
            .register();
	
	private String id;
	
	private JobQueue queue;
	
	private volatile boolean shutdown;
		
	public Worker(JobQueue queue) {
		this.queue = queue;
		this.id = UUID.randomUUID().toString();
	}
	
	@Override
	public void run() {
		System.out.println(String.format("[Worker %s] Starting", this.id));
		while(!shutdown) {
			this.pullJobAndRun();
		}
		System.out.println(String.format("[Worker %s] Stopped", this.id));
	}
	
	public void shutdown() {
		this.shutdown = true;
		System.out.println(String.format("[Worker %s] Shutting down", this.id));
	}
	
	public void pullJobAndRun() {
		Job job = this.queue.pull();
		if (job != null) {
			long jobStart = System.currentTimeMillis();
			System.out.println(String.format("[Worker %s] Starting job: %s", this.id, job.getId()));
			job.run();
			System.out.println(String.format("[Worker %s] Finished job: %s", this.id, job.getId()));
			int duration = (int)((System.currentTimeMillis() - jobStart) / 1000);
			jobsCompletionDurationSeconds.observe(duration);
		} else {
			System.out.println(String.format("[Worker %s] Queue is empty. Backing off 5 seconds", this.id));
			try {
				Thread.sleep(5 * 1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
