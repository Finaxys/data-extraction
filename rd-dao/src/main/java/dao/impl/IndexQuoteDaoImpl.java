package dao.impl;

import helper.Helper;

import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import msg.Document.DataType;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.util.Bytes;

import dao.IndexQuoteDao;
import domain.Exchange;
import domain.IndexQuote;

public class IndexQuoteDaoImpl implements IndexQuoteDao {

	public static final byte[] SYMBOL_COL = Bytes.toBytes("s");
	public static final byte[] PROVIDER_COL = Bytes.toBytes("p");
	public static final byte[] EXCH_SYMB_COL = Bytes.toBytes("exchSymb");
	public static final byte[] LTPO_COL = Bytes.toBytes("ltpo");
	public static final byte[] TS_COL = Bytes.toBytes("ts");
	public static final byte[] CHANGE_COL = Bytes.toBytes("ch");
	public static final byte[] OPEN_COL = Bytes.toBytes("o");
	public static final byte[] DAYS_HIGH_COL = Bytes.toBytes("dh");
	public static final byte[] DAYS_LOW_COL = Bytes.toBytes("dl");
	public static final byte[] VOLUME_COL = Bytes.toBytes("v");
	public static final byte[] TYPE_COL = Bytes.toBytes("t");

	public static final byte[] TABLE_NAME = Bytes.toBytes("index_quote");
	public static final byte[] VALUE_FAM = Bytes.toBytes("v");
	

	private HConnection connection;

	public IndexQuoteDaoImpl(HConnection connection) {
		super();
		this.connection = connection;
	}

	// Helpers

	private IndexQuote toIndexQuote(Result r) {
		return null;// ToDo
	}

	private byte[] mkRowKey(IndexQuote index) {
		return mkRowKey(index.getProvider(), index.getExchSymb(), index.getSymbol(), index.getTs(), index.getDataType());
	}

	private byte[] mkRowKey(char provider, String exchSymb, String symbol, Long ts, DataType dataType) {
		byte provByte = (byte)provider;
		byte[] exchSymbHash = Helper.md5sum(exchSymb);
		byte typeByte = dataType.getTByte();
		byte[] symbHash = Helper.md5sum(symbol);
		byte[] timestamp = Bytes.toBytes(ts);
		byte[] rowkey = new byte[2 + 2 * Helper.MD5_LENGTH];

		int offset = 0;
		offset = Bytes.putByte(rowkey, offset, provByte);
		offset = Bytes.putBytes(rowkey, offset, exchSymbHash, 0, Helper.MD5_LENGTH);
		offset = Bytes.putByte(rowkey, offset, typeByte);
		offset = Bytes.putBytes(rowkey, offset, symbHash, 0, Helper.MD5_LENGTH);
		Bytes.putBytes(rowkey, offset, timestamp, 0, timestamp.length);

		return rowkey;
	}

	private Put mkPut(IndexQuote index) {
		Put p = new Put(mkRowKey(index));

		// Load all fields in the class (private included)
		Field[] ind_attributes = IndexQuote.class.getDeclaredFields();
		Field[] ind_cols = IndexQuoteDaoImpl.class.getDeclaredFields();
		int i = 0;
		Object value;
		for (Field field : ind_attributes) {

			try {
				if ((value = PropertyUtils.getSimpleProperty(index, field.getName())) != null) {
					if (field.getType().equals(String.class))
						p.add(VALUE_FAM, (byte[]) ind_cols[i].get(null), Bytes.toBytes((String) value));
					if (field.getType().equals(BigDecimal.class))
						p.add(VALUE_FAM, (byte[]) ind_cols[i].get(null), Bytes.toBytes((BigDecimal) value));
					if (field.getType().equals(Long.class))
						p.add(VALUE_FAM, (byte[]) ind_cols[i].get(null), Bytes.toBytes((Long) value));
					if (field.getType().equals(Integer.class))
						p.add(VALUE_FAM, (byte[]) ind_cols[i].get(null), Bytes.toBytes((Integer) value));
					if (field.getType().equals(char.class))
						p.add(VALUE_FAM, (byte[]) ind_cols[i].get(null), Bytes.toBytes((Character) value));
						
				}
				i++;

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return p;
	}

	private Scan mkScan() {
		Scan scan = new Scan();
		return scan;
	}

	private Scan mkScan(String prefix) {
		Scan scan = new Scan();
		org.apache.hadoop.hbase.filter.RegexStringComparator prefixFilter = new org.apache.hadoop.hbase.filter.RegexStringComparator(
				"^" + prefix + "*");
		RowFilter rowFilter = new RowFilter(CompareOp.EQUAL, prefixFilter);
		scan.setFilter(rowFilter);

		return scan;
	}

	public boolean add(IndexQuote index) {
		try {
			HTableInterface table = connection.getTable(TABLE_NAME);
			Put p = mkPut(index);
			table.put(p);
			table.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			// logger.info(e.toString());
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			// logger.info(e.toString());
			return false;
		}
	}

	public List<IndexQuote> list(String prefix) throws IOException {
		HTableInterface table = connection.getTable(TABLE_NAME);
		ResultScanner results = table.getScanner(mkScan(prefix));
		List<IndexQuote> ret = new ArrayList<IndexQuote>();
		for (Result r : results) {
			ret.add(toIndexQuote(r));
		}
		table.close();
		return ret;
	}

	public List<IndexQuote> listAll() throws IOException {
		HTableInterface table = connection.getTable(TABLE_NAME);
		ResultScanner results = table.getScanner(mkScan());
		List<IndexQuote> ret = new ArrayList<IndexQuote>();
		for (Result r : results) {
			ret.add(toIndexQuote(r));
		}
		table.close();
		return ret;
	}
}
