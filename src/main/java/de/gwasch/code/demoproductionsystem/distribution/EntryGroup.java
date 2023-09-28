package de.gwasch.code.demoproductionsystem.distribution;


class EntryGroup implements Comparable<EntryGroup> {

	int value;
	int count;

	EntryGroup(int value) {
		this(value, 0);
	}

	EntryGroup(int value, int count) {
		this.value = value;
		this.count = count;
	}

	public boolean equals(Object obj) {
		EntryGroup eg = (EntryGroup) obj;
		return value == eg.value;
	}

	public int compareTo(EntryGroup cmp) {
		return cmp.value - value;
	}
}
