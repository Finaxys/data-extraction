/*
 * 
 */
package com.finaxys.rd.dataextraction.dao.helper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.io.FileUtils;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.PrefixFilter;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import com.finaxys.rd.dataextraction.domain.Enum.DataType;

// TODO: Auto-generated Javadoc
/**
 * The Class DaoHelper.
 */
public class DaoHelper {

	/** The Constant MD5_LENGTH. */
	public static final int MD5_LENGTH = 16;

	

	public static Scan mkScan() {
		Scan scan = new Scan();
		return scan;
	}


	public static Scan mkScan(byte[] start, byte[] end) {
		Scan scan = new Scan(start, end);
		return scan;
	}

	public static Scan mkScan(String prefix) {
		Scan scan = new Scan();
		org.apache.hadoop.hbase.filter.RegexStringComparator prefixFilter = new org.apache.hadoop.hbase.filter.RegexStringComparator(
				"^" + prefix + "*");
		RowFilter rowFilter = new RowFilter(CompareOp.EQUAL, prefixFilter);
		scan.setFilter(rowFilter);

		return scan;
	}

	public static Scan mkScan(byte[] prefix) {
		Scan scan = new Scan();
		PrefixFilter prefixFilter = new org.apache.hadoop.hbase.filter.PrefixFilter(
				prefix);
		scan.setFilter(prefixFilter);

		return scan;
	}
	
	public static Object getTypedValue(Field field, byte[] value) {

		if (value != null) {
	     	if (field.getType().equals(String.class))
				return Bytes.toString(value);
			else if (field.getType().equals(Long.class))
				return Bytes.toLong(value);
			else if (field.getType().equals(Integer.class))
				return Bytes.toInt(value);
			else if (field.getType().equals(LocalTime.class))
				return new LocalTime(Bytes.toLong(value));
			else if (field.getType().equals(LocalDate.class))
				return new LocalDate(Bytes.toLong(value));
			else if (field.getType().equals(char.class))
				return (char) value[3];
			else if (field.getType().equals(DateTime.class))
				return new DateTime(Bytes.toLong(value));
			else if (field.getType().equals(BigDecimal.class))
				return  Bytes.toBigDecimal(value);
			else if (field.getType().equals(BigInteger.class))
				return new BigInteger(value);
			else if (field.getType().equals(DataType.class))
				return DataType.valueOf(new String(value));
			return value;
		} else
			return null;

	}

	public static byte[] toBytes(Field field, Object value) throws IOException {

		if (value != null) {
			if (field.getType().equals(String.class))
				return Bytes.toBytes((String) value);
			else if (field.getType().equals(Long.class))
				return Bytes.toBytes((Long) value);
			else if (field.getType().equals(Integer.class))
				return Bytes.toBytes((Integer) value);
			else if (field.getType().equals(LocalTime.class))
				return Bytes.toBytes(new LocalTime(value).toDateTimeToday()
						.getMillis());
			else if (field.getType().equals(LocalDate.class))
				return Bytes.toBytes(new LocalDate(value)
						.toDateTimeAtStartOfDay().getMillis());
			else if (field.getType().equals(char.class))
				return Bytes.toBytes((Character) value);
			else if (field.getType().equals(DateTime.class))
				return Bytes.toBytes(((DateTime) value).getMillis());
			else if (field.getType().equals(BigDecimal.class))
				return Bytes.toBytes((BigDecimal) value);
			else if (field.getType().equals(BigInteger.class))
				return ((BigInteger) value).toByteArray();
			else if (field.getType().equals(DataType.class))
				return Bytes.toBytes(((DataType) value).getName());

			else {
				ByteArrayOutputStream b = new ByteArrayOutputStream();
				ObjectOutputStream o = new ObjectOutputStream(b);
				o.writeObject(value);
				return b.toByteArray();
			}
		} else
			return null;
	}

	/**
	 * Gets the path.
	 * 
	 * @param folder
	 *            the folder
	 * @param file
	 *            the file
	 * @param ext
	 *            the ext
	 * @return the path
	 */
	public static String getPath(String folder, String file, String ext) {
		return folder + "/" + file + "." + ext;
	}

	/**
	 * Gets the resource file.
	 * 
	 * @param path
	 *            the path
	 * @return the resource file
	 */
	public static File getResourceFile(String path) {
		File f = new File(DaoHelper.class.getResource(path).getPath());
		return f;

	}

	/**
	 * To bytes.
	 * 
	 * @param file
	 *            the file
	 * @return the byte[]
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static byte[] toBytes(File file) throws IOException {
		return FileUtils.readFileToByteArray(file);
	}

	/**
	 * Md5sum.
	 * 
	 * @param s
	 *            the s
	 * @return the byte[]
	 */
	public static byte[] md5sum(String s) {
		MessageDigest d;
		try {
			d = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("MD5 algorithm not available!", e);
		}

		return d.digest(Bytes.toBytes(s));
	}

}
