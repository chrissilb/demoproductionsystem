package de.gwasch.code.demoproductionsystem.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import de.gwasch.code.demoproductionsystem.models.Model;

public class LogFilterDialog extends JDialog {

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
	
	class AddHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			
			if (filterTable.isEditing()) {
				filterTable.getCellEditor().stopCellEditing();
			}
			
			filterTableModel.addEmptyRow();
			int lastindex = filterTableModel.getRowCount() - 1;
			filterTable.editCellAt(lastindex, 1);
			filterTable.requestFocusInWindow();
			filterSelectionModel.setSelectionInterval(lastindex, lastindex);
		}
	}
	
	class InsertHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			
			if (filterTable.isEditing()) {
				filterTable.getCellEditor().stopCellEditing();
			}
			
			int index = filterSelectionModel.getMinSelectionIndex();
			filterTableModel.insertEmptyRow(index);
			filterTable.editCellAt(index, 1);
			filterTable.requestFocusInWindow();
			filterSelectionModel.setSelectionInterval(index, index);
		}
	}
	
	class RemoveHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			
			if (filterTable.isEditing()) {
				filterTable.getCellEditor().stopCellEditing();
			}
			
			int index = filterSelectionModel.getMinSelectionIndex();
			filterTableModel.removeRow(index);
			
			int size = filterTableModel.getRowCount();
			if (size == index) index--;
			
			if (size < 0) {
				filterSelectionModel.clearSelection();
			}
			else {
				filterSelectionModel.setSelectionInterval(index, index);
			}
		}
	}
	
	class MoveUpHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			
			if (filterTable.isEditing()) {
				filterTable.getCellEditor().stopCellEditing();
			}
			
			int index = filterSelectionModel.getMinSelectionIndex();
			filterTableModel.moveUp(index);
			filterSelectionModel.setSelectionInterval(index - 1, index - 1);
		}
	}
	
	class MoveDownHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			
			if (filterTable.isEditing()) {
				filterTable.getCellEditor().stopCellEditing();
			}
			
			int index = filterSelectionModel.getMinSelectionIndex();
			filterTableModel.moveDown(index);
			filterSelectionModel.setSelectionInterval(index + 1, index + 1);
		}
	}
	
	class ListSelectionHandler implements ListSelectionListener {
		public void valueChanged(ListSelectionEvent lsevent) {
			int selrow = filterSelectionModel.getMinSelectionIndex();
			int lastrow = filterTableModel.getRowCount() - 1;
			
			cmdInsert.setEnabled(selrow != -1);
			cmdRemove.setEnabled(selrow != -1);
			cmdMoveUp.setEnabled(selrow != -1 && selrow > 0);
			cmdMoveDown.setEnabled(selrow != -1 && selrow < lastrow);
		}
	}


	private static final long serialVersionUID = -6827563575734788656L;
	
	private JTable filterTable;
	private LogFilterTableModel filterTableModel;
	private ListSelectionModel filterSelectionModel;
	private JButton cmdInsert;
	private JButton cmdRemove;
	private JButton cmdMoveUp;
	private JButton cmdMoveDown;
	
	private MainFrame parent;
	private Model model;

	public LogFilterDialog(MainFrame parent, Model model) {
		super(parent);
	
		this.parent = parent;
		this.model = model;
		
		setModal(true);
		setTitle("Log Filters");
	    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(570, 510);
	    
		filterTableModel = new LogFilterTableModel(model.logFilters.clone());
		filterTable = new JTable(filterTableModel);
		
		filterSelectionModel = filterTable.getSelectionModel();
		filterSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		filterSelectionModel.addListSelectionListener(new ListSelectionHandler());
		
		filterTable.getColumnModel().getColumn(0).setMaxWidth(80);
		filterTable.getColumnModel().getColumn(2).setMaxWidth(80);
		filterTable.getColumnModel().getColumn(3).setMaxWidth(80);
		
		JScrollPane scrollpane = new JScrollPane(filterTable);
		JPanel titlepane = new JPanel();
		titlepane.setBorder(BorderFactory.createTitledBorder(BorderFactory
				.createEtchedBorder(), "Log Filters"));
		titlepane.setLayout(new BorderLayout());
		titlepane.add(scrollpane);
		
		add(titlepane);
	    
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout(FlowLayout.LEFT));
		add(BorderLayout.SOUTH, panel);
		
		JButton cmdok = new JButton("OK");
		panel.add(cmdok);
		JButton cmdcancel = new JButton("Cancel");
		panel.add(cmdcancel);
		JButton cmdapply = new JButton("Apply");
		panel.add(cmdapply);
		JButton cmdadd = new JButton("Add");
		panel.add(cmdadd);
		cmdInsert = new JButton("Insert");
		cmdInsert.setEnabled(false);
		panel.add(cmdInsert);
		cmdRemove = new JButton("Remove");
		cmdRemove.setEnabled(false);
		panel.add(cmdRemove);
		cmdMoveUp = new JButton("Up");
		cmdMoveUp.setEnabled(false);
		panel.add(cmdMoveUp);
		cmdMoveDown = new JButton("Down");
		cmdMoveDown.setEnabled(false);
		panel.add(cmdMoveDown);
		
		cmdok.addActionListener(new OkHandler());
		ActionListener cancelHandler = new CancelHandler();
		cmdcancel.addActionListener(cancelHandler);
		cmdapply.addActionListener(new ApplyHandler());
		cmdadd.addActionListener(new AddHandler());
		cmdInsert.addActionListener(new InsertHandler());
		cmdRemove.addActionListener(new RemoveHandler());
		cmdMoveUp.addActionListener(new MoveUpHandler());
		cmdMoveDown.addActionListener(new MoveDownHandler());
		
		getRootPane().registerKeyboardAction(cancelHandler, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
		setVisible(true);	
	}
	
	private boolean apply() {
					
		if (filterTable.isEditing()) {
			filterTable.getCellEditor().stopCellEditing();
		}
		
		for (int i = 0; i < filterTableModel.getRowCount(); i++) {
			String s = (String)filterTableModel.getValueAt(i, 1);
			
			if (!Validator.validatePattern(filterTable, s)) {
					filterTable.editCellAt(i, 1);
					filterTable.requestFocusInWindow();
					filterSelectionModel.setSelectionInterval(i, i);
					return false;
			}
		}
		
		model.logFilters = filterTableModel.getFilters().clone();
		
		parent.updateFilters();
		parent.updateState();
		return true;
	}
}
