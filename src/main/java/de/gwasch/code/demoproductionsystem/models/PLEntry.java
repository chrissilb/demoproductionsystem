package de.gwasch.code.demoproductionsystem.models;

import de.gwasch.code.demoproductionsystem.interfaces.agents.ProductionLine;

public class PLEntry {

	private ProductionLine productionLine;
	private long timeStamp;
	
	public PLEntry(ProductionLine pl, long timeStamp) {
		productionLine = pl;
		this.timeStamp = timeStamp;
	}
	
	public ProductionLine getProductionLine() {
		return productionLine;
	}
	
	public long getTimeStamp() {
		return timeStamp;
	}
	
	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}
}
