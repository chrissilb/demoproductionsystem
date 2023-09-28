package de.gwasch.code.demoproductionsystem.ui;
import javax.swing.table.AbstractTableModel;

import de.gwasch.code.escframework.utils.logging.Filter;
import de.gwasch.code.escframework.utils.logging.Filters;

public class LogFilterTableModel extends AbstractTableModel {

	private static final long serialVersionUID = -5507284455509889342L;

	private Filters logFilters;

	public LogFilterTableModel(Filters logfilters) {
		logFilters = logfilters;
	}

	public Filters getFilters() {
		return logFilters;
	}
	
	public Class<?> getColumnClass(int col) {
		switch (col) {
			case 0: return Integer.class;
			case 2:
			case 3: return Boolean.class;
			default: return String.class;
		}
	}

	public String getColumnName(int col) {

		switch (col) {
			case 0:	return "Priority";
			case 1:	return "Pattern";
			case 2:	return "Allow";			
			case 3:	return "Deactivated";
			default: return null;
		}
	}

	public int getRowCount() {
		return logFilters.size();
	}

	public int getColumnCount() {
		return 4;
	}

	public Object getValueAt(int row, int col) {

		Filter filter = logFilters.get(row);
		switch (col) {
			case 0: return (row + 1);
			case 1:	return filter.pattern;
			case 2:	return filter.allow;
			case 3:	return filter.deactivated;
			default: return null;
		}
	}
	
	public boolean isCellEditable(int row, int col) {
		return col == 0 ? false : true;
	}

	public void setValueAt(Object value, int row, int col) {
		Filter filter = logFilters.get(row);
		switch (col) {
			case 1:	filter.pattern = (String)value; break;
			case 2:	filter.allow = (Boolean)value; break;
			case 3:	filter.deactivated = (Boolean)value; break;
		}
		fireTableRowsUpdated(row, row);
	}
	
	public void insertEmptyRow(int row) {
		logFilters.insert(row, "", false);
		fireTableRowsInserted(row, row);
	}
		
	public void addEmptyRow() {
		logFilters.add("", false);
		fireTableRowsInserted(logFilters.size() - 1, logFilters.size() - 1);
	}
		
	public void removeRow(int row) {
		logFilters.remove(row);
		fireTableRowsDeleted(row, row);
	}
	
	public void moveUp(int row) {
		Filter filter = logFilters.get(row - 1);
		logFilters.remove(row - 1);
		logFilters.insert(row, filter);
		fireTableRowsUpdated(row - 1, row);
	}
	
	public void moveDown(int row) {
		Filter filter = logFilters.get(row + 1);
		logFilters.remove(row + 1);
		logFilters.insert(row, filter);
		fireTableRowsUpdated(row, row + 1);
	}
}












