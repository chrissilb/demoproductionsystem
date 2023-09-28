package de.gwasch.code.demoproductionsystem.distribution;


public class Distribution extends Distributor {

	private int optCount;
	private int optTolerance;
	private int optTolCount;

	public Distribution() {
		optCount = Integer.MAX_VALUE;
		optTolerance = Integer.MAX_VALUE;
		optTolCount = Integer.MAX_VALUE;
	}

	protected void setOpt(int count) {
		optCount = count;
		optTolerance = 0;
	}

	protected void setTolOpt(int count, int tolerance) {
		optTolCount = count;
		optTolerance = tolerance;
	}

	public int getOptCount() {
		return optCount;
	}

	protected int getOptTolCount() {
		return optTolCount;
	}

	public int getOptTolerance() {
		return optTolerance;
	}
}
