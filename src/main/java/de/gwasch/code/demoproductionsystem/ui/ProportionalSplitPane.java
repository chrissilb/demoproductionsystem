package de.gwasch.code.demoproductionsystem.ui;

import javax.swing.JSplitPane;

public class ProportionalSplitPane extends JSplitPane {

	public enum ComponentSelection {
		Left, Right, Top, Bottom, Both
	}
	
	private static final long serialVersionUID = -8576308440514642591L;
	private double lastProportionalLocation;
	
	public ProportionalSplitPane() {
		super(JSplitPane.HORIZONTAL_SPLIT, true);
		
		setOneTouchExpandable(true);
		lastProportionalLocation = 0.5;
	}
	
	public void setOrientation(int orientation) {
		double pdl = getProportionalDividerLocation();
		super.setOrientation(orientation);
		setDividerLocation(pdl);
	}
	
	public void setDividerLocation(int location) {
		super.setDividerLocation(location);
		
		setResizeWeight(getProportionalDividerLocation());
		
		if (location <= getMinimumDividerLocation() || location >= getMaximumDividerLocation()) {
			return;
		}
		
		lastProportionalLocation = getProportionalDividerLocation();
	}
	
	
	public double getProportionalDividerLocation() {
		if (getOrientation() == JSplitPane.HORIZONTAL_SPLIT) {
			return 1.0 * getDividerLocation() / (getWidth() - getDividerSize());
		}
		else {
			return 1.0 * getDividerLocation() / (getHeight() - getDividerSize());
		}
	}
	
	public double getLastProportionalDividerLocation() {
		return lastProportionalLocation;
	}
	
	public void toggleSplit() {
		
		if (getOrientation() == JSplitPane.HORIZONTAL_SPLIT) {
			setOrientation(JSplitPane.VERTICAL_SPLIT);
		}
		else {
			setOrientation(JSplitPane.HORIZONTAL_SPLIT);

		}
	}

	public void showComponent(ComponentSelection selection) {
		switch (selection) {
			case Left:
			case Top: 	setDividerLocation(1.0);						
					  	break;
			case Right:
			case Bottom:setDividerLocation(0.0);						
						break;
			case Both:
			default: 	setDividerLocation(getLastProportionalDividerLocation());
				   		break;
		}
	}
	
	public ComponentSelection getComponentSelection() {
		if (getDividerLocation() >= getMaximumDividerLocation()) {
			return ComponentSelection.Left;
		}
		else if (getDividerLocation() <= getMinimumDividerLocation()) {
			return ComponentSelection.Right;
		}
		else {
			return ComponentSelection.Both;
		}
	}

}
