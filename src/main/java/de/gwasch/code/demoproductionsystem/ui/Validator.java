package de.gwasch.code.demoproductionsystem.ui;

import java.awt.Component;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javax.swing.JOptionPane;
import javax.swing.text.JTextComponent;

import de.gwasch.code.demoproductionsystem.interfaces.agents.ProductionLine;
import de.gwasch.code.demoproductionsystem.models.Model;

public class Validator {
	public static boolean validate(JTextComponent component, String name, int minvalue, int maxvalue) {
		
		int value;
		
		try {
			value = Integer.parseInt(component.getText());
			if (value < minvalue || value > maxvalue) throw new NumberFormatException();
			return true;
		}
		catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(component.getRootPane().getParent(), 
					"Invalid " + name + ".", "Input Error", JOptionPane.ERROR_MESSAGE);
			component.requestFocusInWindow();
			return false;
		}
	}
	
	public static boolean validate(JTextComponent component, String name, double minvalue, double maxvalue) {
		
		double value;
		
		try {
			value = Double.parseDouble(component.getText());
			if (value < minvalue || value > maxvalue) throw new NumberFormatException();
			return true;
		}
		catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(component.getRootPane().getParent(), 
					"Invalid " + name + ".", "Input Error", JOptionPane.ERROR_MESSAGE);
			component.requestFocusInWindow();
			return false;
		}
	}
	
	public static boolean validateUniqueName(JTextComponent component, Model workModel, ProductionLine productionLine) {
		
		String value = component.getText();
		
		for (ProductionLine pl : workModel.productionLines) {
			
			if (pl.getProductionLine() == productionLine) {
				continue;
			}
			if (value.equals(pl.getName())) {
				JOptionPane.showMessageDialog(component.getRootPane().getParent(), 
						"Name already used by other ProductionLine.", "Input Error", JOptionPane.ERROR_MESSAGE);
				component.requestFocusInWindow();
				return false;
			}
		}
		
		return true;
	}
	
	public static boolean validatePattern(Component component, String pattern) {
		try {
			Pattern.compile(pattern);
			return true;
		}
		catch(PatternSyntaxException e) {
			JOptionPane.showMessageDialog(component.getParent(), 
					"Invalid pattern.", "Input Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}
}
