package de.gwasch.code.demoproductionsystem.distribution;

import java.util.TreeMap;


public class Distributor {

	TreeMap<EntryGroup, EntryGroup> entryGroups;

	public Distributor() {
		entryGroups = new TreeMap<EntryGroup, EntryGroup>();
	}

	
	protected void clear() {
		entryGroups.clear();
	}
	

	public void addEntry(int value) {
		addEntries(value, 1);
	}

	
	public void removeEntry(int value) {
		removeEntries(value, 1);
	}

	
	public void addEntries(int value, int count) {

		EntryGroup eg = new EntryGroup(value, count);

		if (entryGroups.containsKey(eg)) {
			eg = entryGroups.get(eg);
			eg.count += count;
		} else
			entryGroups.put(eg, eg);
	}

	
	public void removeEntries(int value, int count) {

		EntryGroup eg = new EntryGroup(value, count);

		if (!entryGroups.containsKey(eg))
			return;
		eg = entryGroups.get(eg);

		if (eg.count < count)
			eg.count = 0;
		else
			eg.count -= count;
	}

	
	public int countEntries(int value) {

		EntryGroup eg = new EntryGroup(value);

		if (!entryGroups.containsKey(eg))
			return 0;
		else
			return entryGroups.get(eg).count;
	}

	
	public Distribution distribute(int volume) {
		Distribution d = distribute(volume, entryGroups.firstKey(), 0,
				new Distribution());
		if (d.getOptTolerance() == Integer.MAX_VALUE)
			return null;
		else
			return d;
	}

	
	private Distribution distribute(int volume, EntryGroup start, int count, Distribution d) {

		for (EntryGroup eg : entryGroups.tailMap(start).keySet()) {

			if (eg.count == 0)
				continue;

			if (eg.value == volume) {
				
				if (count + 1 < d.getOptCount()) {
					d.clear();
					d.addEntry(eg.value);
					d.setOpt(count + 1);
					break;
				}
			} 
			else if (eg.value < volume) {
				
				removeEntry(eg.value);

				int oc = d.getOptCount();
				int otc = d.getOptTolCount();
				int ot = d.getOptTolerance();
				d = distribute(volume - eg.value, eg, count + 1, d);
				
				if (   d.getOptCount() != oc 
					|| d.getOptTolCount() != otc
					|| d.getOptTolerance() != ot) {
					d.addEntry(eg.value);
				}

				addEntry(eg.value);
			} 
			else {
				if (   eg.value - volume < d.getOptTolerance()
					|| eg.value - volume == d.getOptTolerance()
					   && count + 1 < d.getOptTolCount()) {
					
					d.clear();
					d.addEntry(eg.value);
					d.setTolOpt(count + 1, eg.value - volume);
				}
			}
		}

		return d;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();

		for (EntryGroup eg : entryGroups.keySet()) {
			sb.append(eg.value);
			sb.append(":");
			sb.append(eg.count);
			sb.append("  ");
		}
		return sb.toString();
	}
}
