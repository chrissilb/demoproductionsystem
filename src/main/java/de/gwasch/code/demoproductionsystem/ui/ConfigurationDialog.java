package de.gwasch.code.demoproductionsystem.ui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import de.gwasch.code.demoproductionsystem.models.Configuration;

public class ConfigurationDialog extends JDialog {

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
	
	private static final long serialVersionUID = -6827563575734788656L;
	
	private Configuration model;
	
	private MainFrame parent;
	
	private JTextField txtAvgTickPause;
	private JTextField txtMaxDeviationFactor;
	private JTextField txtTimeoutFactor;
	
	
	public ConfigurationDialog(MainFrame parent, Configuration model) {
		super(parent);
	
		this.parent = parent;
		this.model = model;
		
		setModal(true);
		setTitle("Configuration");
	    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLayout(null);
		setSize(320, 210);
	    
		JLabel label;
		
		label = new JLabel("Average Tick Pause:");
		label.setBounds(10, 10, 170, 20);
		add(label);
				
		txtAvgTickPause = new JTextField("" + model.getAvgTickPause());
		txtAvgTickPause.setBounds(180, 10, 100, 20);
		add(txtAvgTickPause);
		
		label = new JLabel("Maximum Deviation Factor:");
		label.setBounds(10, 40, 170, 20);
		add(label);
				
		txtMaxDeviationFactor = new JTextField("" + model.getMaxDeviationFactor());
		txtMaxDeviationFactor.setBounds(180, 40, 100, 20);
		add(txtMaxDeviationFactor);
	    
		label = new JLabel("Timeout Factor:");
		label.setBounds(10, 70, 170, 20);
		add(label);

		txtTimeoutFactor = new JTextField("" + model.getTimeoutFactor());
		txtTimeoutFactor.setBounds(180, 70, 100, 20);
		add(txtTimeoutFactor);
		

		JPanel panel = new JPanel();
		panel.setBounds(5, 100, 310, 40);
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
		
		if (   !Validator.validate(txtAvgTickPause, "Average Tick Pause", 1, Integer.MAX_VALUE)
			|| !Validator.validate(txtMaxDeviationFactor, "Maximum Deviation Factor", 0.0, 1.0)
			|| !Validator.validate(txtTimeoutFactor, "Timeout Factor", 1.0, Double.MAX_VALUE)) {
				
				return false;
			}
			
			int ival = Integer.parseInt(txtAvgTickPause.getText());
			model.setAvgTickPause(ival);

			double dval = Double.parseDouble(txtMaxDeviationFactor.getText());
			model.setMaxDeviationFactor(dval);

			dval = Double.parseDouble(txtTimeoutFactor.getText());
			model.setTimeoutFactor(dval);
			
			parent.updateState();
			parent.updateTicks();
			
			return true;
	}
}
