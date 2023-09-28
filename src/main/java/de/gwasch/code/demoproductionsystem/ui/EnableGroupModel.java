package de.gwasch.code.demoproductionsystem.ui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonModel;

public class EnableGroupModel<T> {

	private List<ButtonModel> buttonModels;
	
	public EnableGroupModel() {
		buttonModels = new ArrayList<ButtonModel>();
	}
	
	public void add(ButtonModel model) {
		buttonModels.add(model);
	}
	
	public void setEnabled(T value) {
		
	}
}
