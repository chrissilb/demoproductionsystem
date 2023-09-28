package de.gwasch.code.demoproductionsystem.ui;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;



public class ButtonGroupModel<T> extends ButtonGroup {

	private static final long serialVersionUID = 554560832138745131L;

	@SuppressWarnings("unchecked")
	public T getValue() {
		ButtonGroupButtonModel<T> model = (ButtonGroupButtonModel<T>)getSelection();
		return model.getValue();
	}
	
	@SuppressWarnings("unchecked")
	public void setValue(T value) {
		for (AbstractButton b : buttons)  {
			ButtonGroupButtonModel<T> model = (ButtonGroupButtonModel<T>)b.getModel();
			if (model.getValue() == value) b.setSelected(true);
		}
	}
}
