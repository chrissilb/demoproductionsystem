package de.gwasch.code.demoproductionsystem.models;

public class Configuration implements Cloneable {

	private static final int DEFAULT_AVGTICKPAUSE = 3000;
	private static final double DEFAULT_MAXDEVIATIONFACTOR = 0.5;
	private static final int DEFAULT_TIMEOUTFACTOR = 3;

	private int avgTickPause;
	private double maxDeviationFactor;
	private double timeoutFactor;
	
	public Configuration() {
		setAvgTickPause(DEFAULT_AVGTICKPAUSE);
		setMaxDeviationFactor(DEFAULT_MAXDEVIATIONFACTOR);
		setTimeoutFactor(DEFAULT_TIMEOUTFACTOR); 
	}
	
	public Configuration clone() {
		Configuration c = new Configuration();
		c.avgTickPause = avgTickPause;
		c.maxDeviationFactor = maxDeviationFactor;
		c.timeoutFactor = timeoutFactor;
		return c;
	}

	public int getAvgTickPause() {
		return avgTickPause;
	}
	
	public void setAvgTickPause(int avgTickPause) {
		this.avgTickPause = avgTickPause;
	}

	public double getMaxDeviationFactor() {
		return maxDeviationFactor;
	}

	public void setMaxDeviationFactor(double maxDeviationFactor) {
		this.maxDeviationFactor = maxDeviationFactor;
	}

	public double getTimeoutFactor() {
		return timeoutFactor;
	}

	public void setTimeoutFactor(double timeoutFactor) {
		this.timeoutFactor = timeoutFactor;
	}
	
	public boolean equals(Object obj) {
		Configuration cmp = (Configuration)obj;
		return avgTickPause == cmp.avgTickPause
			&& maxDeviationFactor == cmp.maxDeviationFactor
			&& timeoutFactor == cmp.timeoutFactor;
	}
}
