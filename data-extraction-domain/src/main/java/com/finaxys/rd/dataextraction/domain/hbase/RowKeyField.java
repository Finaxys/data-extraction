/*
 * 
 */
package com.finaxys.rd.dataextraction.domain.hbase;


public class RowKeyField implements Comparable<RowKeyField> {


	private Object value;

	private int order;
	
	

	public RowKeyField(Object value, int order) {
		super();
		this.value = value;
		this.order = order;
	}



	public Object getValue() {
		return value;
	}



	public void setValue(Object value) {
		this.value = value;
	}



	public int getOrder() {
		return order;
	}



	public void setOrder(int order) {
		this.order = order;
	}



	@Override
	public int compareTo(RowKeyField field) {
		if(this.order < field.order) return -1;
		if(this.order > field.order) return 1;
		return 0;
	}

	

}
