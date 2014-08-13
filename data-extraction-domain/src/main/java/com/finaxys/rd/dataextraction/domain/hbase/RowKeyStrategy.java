/*
 * 
 */
package com.finaxys.rd.dataextraction.domain.hbase;

import java.util.TreeSet;


public interface RowKeyStrategy {

	public byte[] createRowKey(TreeSet<RowKeyField> keyFields);

}
