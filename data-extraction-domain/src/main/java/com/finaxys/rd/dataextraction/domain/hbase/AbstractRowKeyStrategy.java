/*
 * 
 */
package com.finaxys.rd.dataextraction.domain.hbase;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public abstract class AbstractRowKeyStrategy implements RowKeyStrategy {
	protected List<byte[]> convertFieldsToBytes(TreeSet<RowKeyField> fields) {
		List<byte[]> rowkeyFields = new ArrayList<byte[]>();
		for (RowKeyField field : fields) {
			rowkeyFields.add(toByte(field.getValue()));
		}
		return rowkeyFields;
	}

	protected int bytesSize(List<byte[]> rowkeyFields) {
		int size = 0;
		for (byte[] field : rowkeyFields)
			size += field.length;
		return size;
	}

	abstract byte[] toByte(Object field);
}
