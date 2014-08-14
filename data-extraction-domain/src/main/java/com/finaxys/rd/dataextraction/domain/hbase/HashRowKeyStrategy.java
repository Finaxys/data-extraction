/*
 * 
 */
package com.finaxys.rd.dataextraction.domain.hbase;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.TreeSet;

import org.apache.hadoop.hbase.util.Bytes;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.springframework.util.Assert;

import com.finaxys.rd.dataextraction.domain.Enum.DataType;
import com.finaxys.rd.dataextraction.domain.helper.Helper;

public class HashRowKeyStrategy extends FixedLengthRowKeyStrategy {

	@Override
	public byte[] createRowKey(TreeSet<RowKeyField> keyFields) {
		List<byte[]> rowkeyFields = convertFieldsToBytes(keyFields);
		byte[] rowkey = new byte[bytesSize(rowkeyFields)];
		int offset = 0;
		for (byte[] field : rowkeyFields) {
			if (field == null)
				throw new IllegalArgumentException();
			offset = Bytes.putBytes(rowkey, offset, field, 0, field.length);
		}
		return rowkey;

	}

	@Override
	byte[] toByte(Object field) {
		Assert.notNull(field);
		if (field instanceof String)
			return Helper.md5sum((String) field);
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