package io.spring2go.promdemo.httpsimulator;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@Configuration
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class SimulatorOpts {

	// Endpoints, Weighted map of endpoints to simulate
	@Value("${opts.endpoints}")
	private String[] endopints;
	
	// RequestRate, requests per second
	@Value("${opts.request_rate}")
	private int requestRate;
	
	// RequestRateUncertainty, Percentage of uncertainty when generating requests (+/-)
	@Value("${opts.request_rate_uncertainty}")
	private int requestRateUncertainty;
	
	// LatencyMin in milliseconds
	@Value("${opts.latency_min}")
	private int latencyMin;

	// LatencyP50 in milliseconds
	@Value("${opts.latency_p50}")
	private int latencyP50;
	
	// LatencyP90 in milliseconds
	@Value("${opts.latency_p90}")
	private int latencyP90;
	
	// LatencyP99 in milliseconds
	@Value("${opts.latency_p99}")
	private int latencyP99;
	
	// LatencyMax in milliseconds
	@Value("${opts.latency_max}")
	private int latencyMax;
	
	// LatencyUncertainty, Percentage of uncertainty when generating latency (+/-)
	@Value("${opts.latency_uncertainty}")
	private int latencyUncertainty;
	
	// ErrorRate, Percentage of chance of requests causing 500
	@Value("${opts.error_rate}")
	private int errorRate;
	
	// SpikeStartChance, Percentage of chance of entering spike mode
	@Value("${opts.spike_start_chance}")
	private int spikeStartChance;
	
	// SpikeStartChance, Percentage of chance of exiting spike mode
	@Value("${opts.spike_end_chance}")
	private int spikeEndChance;
	
	// SpikeModeStatus ON/OFF/RANDOM
	private SpikeMode spikeMode = SpikeMode.OFF;

	public String[] getEndopints() {
		return endopints;
	}

	public void setEndopints(String[] endopints) {
		this.endopints = endopints;
	}

	public int getRequestRate() {
		return requestRate;
	}

	public void setRequestRate(int requestRate) {
		this.requestRate = requestRate;
	}

	public int getRequestRateUncertainty() {
		return requestRateUncertainty;
	}

	public void setRequestRateUncertainty(int requestRateUncertainty) {
		this.requestRateUncertainty = requestRateUncertainty;
	}

	public int getLatencyMin() {
		return latencyMin;
	}

	public void setLatencyMin(int latencyMin) {
		this.latencyMin = latencyMin;
	}

	public int getLatencyP50() {
		return latencyP50;
	}

	public void setLatencyP50(int latencyP50) {
		this.latencyP50 = latencyP50;
	}

	public int getLatencyP90() {
		return latencyP90;
	}

	public void setLatencyP90(int latencyP90) {
		this.latencyP90 = latencyP90;
	}

	public int getLatencyP99() {
		return latencyP99;
	}

	public void setLatencyP99(int latencyP99) {
		this.latencyP99 = latencyP99;
	}

	public int getLatencyMax() {
		return latencyMax;
	}

	public void setLatencyMax(int latencyMax) {
		this.latencyMax = latencyMax;
	}

	public int getLatencyUncertainty() {
		return latencyUncertainty;
	}

	public void setLatencyUncertainty(int latencyUncertainty) {
		this.latencyUncertainty = latencyUncertainty;
	}

	public int getErrorRate() {
		return errorRate;
	}

	public void setErrorRate(int errorRate) {
		this.errorRate = errorRate;
	}

	public int getSpikeStartChance() {
		return spikeStartChance;
	}

	public void setSpikeStartChance(int spikeStartChance) {
		this.spikeStartChance = spikeStartChance;
	}

	public int getSpikeEndChance() {
		return spikeEndChance;
	}

	public void setSpikeEndChance(int spikeEndChance) {
		this.spikeEndChance = spikeEndChance;
	}

	public SpikeMode getSpikeMode() {
		return spikeMode;
	}

	public void setSpikeMode(SpikeMode spikeMode) {
		this.spikeMode = spikeMode;
	}

	@Override
	public String toString() {
		return "SimulatorOpts [endopints=" + Arrays.toString(endopints) + ", requestRate=" + requestRate
				+ ", requestRateUncertainty=" + requestRateUncertainty + ", latencyMin=" + latencyMin + ", latencyP50="
				+ latencyP50 + ", latencyP90=" + latencyP90 + ", latencyP99=" + latencyP99 + ", latencyMax="
				+ latencyMax + ", latencyUncertainty=" + latencyUncertainty + ", errorRate=" + errorRate
				+ ", spikeStartChance=" + spikeStartChance + ", spikeEndChance=" + spikeEndChance + ", spikeMode="
				+ spikeMode + "]";
	}
	
}
