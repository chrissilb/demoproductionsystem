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

import de.gwasch.code.demoproductionsystem.interfaces.agents.ProductionSystem;
import de.gwasch.code.demoproductionsystem.interfaces.roles.PSRoleEnum;

public class ProductionSystemDialog extends JDialog {

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
	

	private static final long serialVersionUID = -1424692879994458298L;

	private MainFrame parent;
	private ProductionSystem model;
	
	private JTextField txtName;
	private JTextField txtVolume;
	private JTextField txtRole;
	private JCheckBox chkDefect;
	
	
	public ProductionSystemDialog(MainFrame parent, ProductionSystem model) {
		super(parent);
	
		this.parent = parent;
		
		this.model = model;
		
		setModal(true);
		setTitle(model.getName());
	    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLayout(null);
		setSize(290, 220);
	    
		JLabel label;
		
		label = new JLabel("Name:");
		label.setBounds(10, 10, 100, 20);
		add(label);
				
		txtName = new JTextField(model.getName());
		txtName.setBounds(110, 10, 150, 20);
		add(txtName);

		label = new JLabel("Volume:");
		label.setBounds(10, 40, 100, 20);
		add(label);
				
		txtVolume = new JTextField("" + model.getVolume());
		txtVolume.setBounds(110, 40, 150, 20);
		add(txtVolume);
	    
		label = new JLabel("Role:");
		label.setBounds(10, 70, 100, 20);
		add(label);

		String s = model.getPSRoleEnum().toString().substring(2);
		txtRole = new JTextField(s);
		txtRole.setBounds(110, 70, 150, 20);
		txtRole.setEnabled(false);
		add(txtRole);
		
		chkDefect = new JCheckBox("Defect", model.getPSRoleEnum() == PSRoleEnum.PSDefect);
		chkDefect.setBounds(110, 100, 150, 20);
		add(chkDefect);

		JPanel panel = new JPanel();
		panel.setBounds(5, 140, 250, 40);
		panel.setLayout(new FlowLayout(FlowLayout.LEFT));
		add(panel);
		
		JButton cmdok = new JButton("OK");
		getRootPane().setDefaultButton(cmdok);
		panel.add(cmdok);
		JButton cmdcancel = new JButton("Cancel");
		panel.add(cmdcancel);
		JButton cmdapply = new JButton("Apply");
		panel.add(cmdapply);
		
		cmdok.addActionListener(new OkHandler());
		ActionListener cancelHandler = new CancelHandler();
		cmdcancel.addActionListener(cancelHandler);
		cmdapply.addActionListener(new ApplyHandler());

		getRootPane().registerKeyboardAction(cancelHandler, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
		
	    setVisible(true);	
	}
	
	private boolean apply() {
		
		if (!Validator.validate(txtVolume, "Volume", 0, Integer.MAX_VALUE)) {
			return false;
		}

		model.setName(txtName.getText());

		int ival = Integer.parseInt(txtVolume.getText());
		model.setVolume(ival);
		
		if ((model.getPSRoleEnum() == PSRoleEnum.PSDefect) != chkDefect.isSelected()) {
			if (chkDefect.isSelected()) {
				model.destroy();
			}
			else {
				model.repair();
			}
			
			txtRole.setText(model.getPSRoleEnum().toString().substring(2));
		}
		
		parent.updateState();
		
		return true;
	}
}
