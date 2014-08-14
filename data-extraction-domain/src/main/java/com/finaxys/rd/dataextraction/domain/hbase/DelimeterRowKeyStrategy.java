/*
 * 
 */
package com.finaxys.rd.dataextraction.domain.hbase;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import org.apache.hadoop.hbase.util.Bytes;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.springframework.util.Assert;

import com.finaxys.rd.dataextraction.domain.Enum.DataType;

public class DelimeterRowKeyStrategy extends VariableLengthRowKeyStrategy {

	private static final char DELIMETER = '~';

	@Override
	public byte[] createRowKey(TreeSet<RowKeyField> keyFields) {
		List<byte[]> rowkeyFields = convertFieldsToBytes(keyFields);
		byte[] rowkey = new byte[bytesSize(rowkeyFields)];
		int offset = 0;
		Iterator<byte[]> it = rowkeyFields.iterator();
		while (it.hasNext()) {
			byte[] field = it.next();
			offset = Bytes.putBytes(rowkey, offset, field, 0, field.length);
			if (it.hasNext())
				offset = Bytes.putByte(rowkey, offset, (byte) DELIMETER);
		}

		return rowkey;

	}

	@Override
	byte[] toByte(Object field) {
		Assert.notNull(field);
		if (field instanceof String)
			return Bytes.toBytes((String) field);
		if (field instanceof Long)
			return Bytes.toBytes((String) field);
		if (field instanceof Integer)
			return Bytes.toBytes((Integer) field);
		if (field instanceof LocalTime)
			return Bytes.toBytes(new LocalTime(field).toDateTimeToday().getMillis());
		if (field instanceof LocalDate)
			return Bytes.toBytes(new LocalDate(field).toDateTimeAtStartOfDay().getMillis());
		if (field instanceof Character)
			return new byte[] { (byte) ((Character) field).charValue() };
		if (field instanceof DateTime)
			return Bytes.toBytes(((DateTime) field).getMillis());
		if (field instanceof BigDecimal)
			return Bytes.toBytes((String) field);
		if (field instanceof BigInteger)
			return ((BigInteger) field).toByteArray();
		if (field instanceof DataType)
			return Bytes.toBytes(((DataType) field).getName());

		throw new IllegalArgumentException();

	}
}
