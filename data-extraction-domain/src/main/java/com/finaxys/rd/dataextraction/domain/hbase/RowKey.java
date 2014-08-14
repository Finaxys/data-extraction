/*
 * 
 */
package com.finaxys.rd.dataextraction.domain.hbase;

import java.util.Arrays;
import java.util.TreeSet;

public class RowKey {

	private byte[] key;

	private TreeSet<RowKeyField> rowKeyFields;

	private RowKeyStrategy strategy;

	public RowKey() {
		super();
	}

	public RowKey(byte[] key) {
		super();
		this.key = key;
	}

	

	public RowKey(TreeSet<RowKeyField> rowKeyFields, RowKeyStrategy strategy) {
		super();
		this.rowKeyFields = rowKeyFields;
		this.strategy = strategy;
	}

	public byte[] getKey() {
		return key;
	}

	public void setKey(byte[] key) {
		if (key == null) {
			this.key = new byte[0];
		} else {
			this.key = Arrays.copyOf(key, key.length);
		}
	}

	public TreeSet<RowKeyField> getRowKeyFields() {
		return rowKeyFields;
	}

	public void setRowKeyFields(TreeSet<RowKeyField> rowKeyFields) {
		this.rowKeyFields = rowKeyFields;
	}

	public RowKeyStrategy getStrategy() {
		return strategy;
	}

	public void setStrategy(RowKeyStrategy strategy) {
		this.strategy = strategy;
	}

	public void createRowKey() {
		this.key = strategy.createRowKey(rowKeyFields);
	}

}
