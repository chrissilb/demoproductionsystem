package de.gwasch.code.demoproductionsystem.ui;
import javax.swing.JToggleButton;


public class ButtonGroupButtonModel<T> extends JToggleButton.ToggleButtonModel {

	private static final long serialVersionUID = 4463120492975363192L;
	private T value;
	
	public ButtonGroupButtonModel(T value) {
		this.value = value;
	}
	
	public T getValue() {
		return value;
	}
}
