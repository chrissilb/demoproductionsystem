package de.gwasch.code.demoproductionsystem.ui;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractListModel;

import de.gwasch.code.escframework.utils.logging.Filters;



public class LogListModel extends AbstractListModel<String> {
	
	private static final long serialVersionUID = 1642804345924080236L;
	private List<String> logs;
	private List<Integer> visibleLogLines;
	private Filters filters;
	
	public LogListModel() {
		logs = new ArrayList<String>();
		visibleLogLines = new ArrayList<Integer>();
	}
	
	public void installFilters(Filters filters) {
		this.filters = filters;
		
		int oldSize = getSize();
		visibleLogLines.clear();
		
		for (int i = 0; i < logs.size(); i++) {
			filterLog(logs.get(i), i);
		}
		
		fireContentsChanged(this, 0, oldSize);
	}
	
	public void clear() {
		int size = visibleLogLines.size();
		logs.clear();
		visibleLogLines.clear();
		fireIntervalRemoved(this, 0, size - 1);
	}
	
	public void add(String log) {
		
		logs.add(log);
		if (filterLog(log, logs.size() - 1)) {
			fireIntervalAdded(this, visibleLogLines.size() - 1, visibleLogLines.size() - 1);
		}
	}
	
	private boolean filterLog(String log, int index) {
		if (filters == null || filters.accept(log)) {
			visibleLogLines.add(index);
			return true;
		}
		
		return false;
	}
	
	public String getElementAt(int index) {
		return logs.get(visibleLogLines.get(index));
	}
	
	public int getUnfilteredIndex(int filteredindex) {
		return visibleLogLines.get(filteredindex);
	}
	
	public int getFilteredIndex(int unfilteredindex) {
		if (visibleLogLines.size() == 0) return -1;
		
		int i;
		for (i = 0; i < visibleLogLines.size(); i++) {
			if (visibleLogLines.get(i) >= unfilteredindex) {
				break;
			}
		}
		
		return i;
	}

	public int getSize() {
		return visibleLogLines.size();
	}
}
