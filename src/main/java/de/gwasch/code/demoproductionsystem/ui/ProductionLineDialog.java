package de.gwasch.code.demoproductionsystem.ui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import de.gwasch.code.demoproductionsystem.interfaces.agents.ProductionLine;
import de.gwasch.code.demoproductionsystem.interfaces.roles.PLRoleEnum;

public class ProductionLineDialog extends JDialog {

	class OkHandler implements ActionListener {
		
		public void actionPerformed(ActionEvent e) {
			
			if (apply()) {
				dispose();
			}
		}
	}

	class CancelHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			dispose();
		}
	}
	
	class ApplyHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			apply();
		}
	}
	
	class RemoveHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			parent.removeProductionLine(model);
			parent.getWorkModel().productionLines.remove(model);
			parent.updateState();
			dispose();
		}
	}

	private static final long serialVersionUID = -6827563575734788656L;
	
	private MainFrame parent;
	private ProductionLine model;
	
	private DialogMode mode;
		
	private JTextField txtName;
	private JTextField txtCapacity;
	private JTextField txtRole;
	private JCheckBox chkDefect;
	
	public ProductionLineDialog(MainFrame parent, ProductionLine model) {
		this(parent, model, DialogMode.Modify);
	}
	
	public ProductionLineDialog(MainFrame parent, ProductionLine model, DialogMode mode) {
		super(parent);
	
		this.parent = parent;
		this.model = model;
		this.mode = mode;
		
		setModal(true);
		setTitle(model.getName());
	    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLayout(null);
		setSize(340, 220);
	    
		JLabel label;
		
		label = new JLabel("Name:");
		label.setBounds(10, 10, 100, 20);
		add(label);
				
		txtName = new JTextField(model.getName());
		txtName.setBounds(110, 10, 150, 20);
		add(txtName);

		label = new JLabel("Capacity:");
		label.setBounds(10, 40, 100, 20);
		add(label);
				
		txtCapacity = new JTextField("" + model.getCapacity());
		txtCapacity.setBounds(110, 40, 150, 20);
		add(txtCapacity);
	    
		label = new JLabel("Role:");
		label.setBounds(10, 70, 100, 20);
		add(label);
		
		String s = model.getPLRoleEnum().toString().substring(2);
		txtRole = new JTextField(s);
		txtRole.setBounds(110, 70, 150, 20);
		txtRole.setEnabled(false);
		add(txtRole);
		
		chkDefect = new JCheckBox("Defect", model.getPLRoleEnum() == PLRoleEnum.PLDefect);
		chkDefect.setBounds(110, 100, 150, 20);
		add(chkDefect);

		JPanel panel = new JPanel();
		panel.setBounds(5, 140, 380, 40);
		panel.setLayout(new FlowLayout(FlowLayout.LEFT));
		add(panel);
		
		JButton cmdok = new JButton("OK");
		getRootPane().setDefaultButton(cmdok);
		panel.add(cmdok);
		JButton cmdcancel = new JButton("Cancel");
		panel.add(cmdcancel);
		JButton cmdapply = new JButton("Apply");
		panel.add(cmdapply);
		JButton cmdremove = new JButton("Remove PL");
		panel.add(cmdremove);
				
		cmdok.addActionListener(new OkHandler());
		ActionListener cancelHandler = new CancelHandler();
		cmdcancel.addActionListener(cancelHandler);
		cmdapply.addActionListener(new ApplyHandler());
		cmdremove.addActionListener(new RemoveHandler());
		
		getRootPane().registerKeyboardAction(cancelHandler, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);

	    setVisible(true);	
	}
	
	private boolean apply() {
		
		if (   !Validator.validateUniqueName(txtName, parent.getWorkModel(), model)
			|| !Validator.validate(txtCapacity, "Capacity", 1, Integer.MAX_VALUE)) {
			return false;
		}
					
		if (mode == DialogMode.Create) {
			parent.getWorkModel().productionLines.add(model);
			parent.addProductionLine(model);
			
		}
		
		model.setName(txtName.getText());

		int ival = Integer.parseInt(txtCapacity.getText());
		model.setCapacity(ival);
		
		if ((model.getPLRoleEnum() == PLRoleEnum.PLDefect) != chkDefect.isSelected()) {
			if (chkDefect.isSelected()) {
				model.destroy();
			}
			else {
				model.repair();
			}
			
			txtRole.setText(model.getPLRoleEnum().toString().substring(2));
		}
		
		parent.updateState();
		
		return true;
	}
}
