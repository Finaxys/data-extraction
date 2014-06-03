package dao.impl;

import helper.Helper;

import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
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

import dao.StockQuoteDao;
import domain.StockQuote;
import domain.StockQuote;
import domain.StockQuote;

public class StockQuoteDaoImpl implements StockQuoteDao {

	// static Logger logger = Logger.getLogger(StockDaoImpl.class);

	public static final byte[] SYMBOL_COL = Bytes.toBytes("symbol");
	public static final byte[] EXCH_SYMB_COL = Bytes.toBytes("exchSymb");
	public static final byte[] PROVIDER_COL = Bytes.toBytes("provider");
	public static final byte[] ADV_COL = Bytes.toBytes("avd");
	public static final byte[] CHANGE_COL = Bytes.toBytes("change");
	public static final byte[] DAYS_LOW_COL = Bytes.toBytes("dl");
	public static final byte[] DAYS_HIGH_COL = Bytes.toBytes("dh");
	public static final byte[] YEARS_LOW_COL = Bytes.toBytes("yl");
	public static final byte[] YEARS_HIGH_COL = Bytes.toBytes("yh");
	public static final byte[] MC_HIGH_COL = Bytes.toBytes("mc");
	public static final byte[] LTPO_COL = Bytes.toBytes("ltpo");
	public static final byte[] DAYS_RANG_COL = Bytes.toBytes("dr");
	public static final byte[] NAME_COL = Bytes.toBytes("name");
	public static final byte[] VOLUME_COL = Bytes.toBytes("volume");
	public static final byte[] TS_COL = Bytes.toBytes("ts");
	public static final byte[] TYPE_COL = Bytes.toBytes("t");

	public static final byte[] TABLE_NAME = Bytes.toBytes("stock_quote");
	public static final byte[] VALUE_FAM = Bytes.toBytes("v");

	private static final int longLength = 8;
	
	private HConnection connection;

	// Constructor

	public StockQuoteDaoImpl(HConnection connection) {
		this.connection = connection;

	}

	// Helpers

	private StockQuote toStock(Result r) {
		return null;// ToDo
	}

	private byte[] mkRowKey(StockQuote stockQuote) {
		return mkRowKey(stockQuote.getProvider(), stockQuote.getExchSymb(), stockQuote.getSymbol(), stockQuote.getTs(), stockQuote.getDataType());
	}

	private byte[] mkRowKey(char provider, String exchSymb, String symbol, Long ts, DataType dataType) {
		byte[] exchSymbHash = Helper.md5sum(exchSymb);
		byte provByte = (byte)provider;
		byte typeByte = dataType.getTByte();
		byte[] symbHash = Helper.md5sum(symbol);
		byte[] timestamp = Bytes.toBytes(ts);
		byte[] rowkey = new byte[2 + 2 * Helper.MD5_LENGTH + longLength]; 

		int offset = 0;
		offset = Bytes.putByte(rowkey, offset, provByte);
		offset = Bytes.putBytes(rowkey, offset, exchSymbHash, 0, Helper.MD5_LENGTH);
		offset = Bytes.putByte(rowkey, offset, typeByte);
		offset = Bytes.putBytes(rowkey, offset, symbHash, 0, Helper.MD5_LENGTH);
		Bytes.putBytes(rowkey, offset, timestamp, 0, timestamp.length);

		return rowkey;
	}

	private Get mkGet(char provider, String exchSymb, String symbol, Long ts, DataType dataType) {
		Get g = new Get(mkRowKey(provider, exchSymb, symbol, ts, dataType));
		return g;
	}

	private Put mkPut(StockQuote stockQuote) {
		Put p = new Put(mkRowKey(stockQuote));

		// Load all fields in the class (private included)
		Field[] stockAttributes = StockQuote.class.getDeclaredFields();
		Field[] stockCols = StockQuoteDaoImpl.class.getDeclaredFields();
		int i = 0;
		Object value;
		for (Field field : stockAttributes) {

			try {
				if ((value = PropertyUtils.getSimpleProperty(stockQuote, field.getName())) != null) {
					if (field.getType().equals(String.class))
						p.add(VALUE_FAM, (byte[]) stockCols[i].get(null), Bytes.toBytes((String) value));
					else if (field.getType().equals(BigDecimal.class))
						p.add(VALUE_FAM, (byte[]) stockCols[i].get(null), Bytes.toBytes((BigDecimal) value));
					else if (field.getType().equals(Integer.class))
						p.add(VALUE_FAM, (byte[]) stockCols[i].get(null), Bytes.toBytes((Integer) value));
					else if (field.getType().equals(Long.class))
						p.add(VALUE_FAM, (byte[]) stockCols[i].get(null), Bytes.toBytes((Long) value));
					if (field.getType().equals(char.class))
						p.add(VALUE_FAM, (byte[]) stockCols[i].get(null), Bytes.toBytes((Character) value));
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

	public boolean add(StockQuote stockQuote) {
		try {
			HTableInterface table = connection.getTable(TABLE_NAME);
			Put p = mkPut(stockQuote);
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

	public StockQuote get(char provider, String exchSymb, String symbol, Long ts, DataType dataType) throws IOException {
		HTableInterface table = connection.getTable(TABLE_NAME);
		Get g = mkGet(provider, exchSymb, symbol, ts, dataType);
		Result result = table.get(g);
		if (result.isEmpty())
			return null;
		StockQuote stockQuote = toStock(result);
		table.close();
		return stockQuote;
	}

	public List<StockQuote> list(String prefix) throws IOException {
		HTableInterface table = connection.getTable(TABLE_NAME);
		ResultScanner results = table.getScanner(mkScan(prefix));
		List<StockQuote> ret = new ArrayList<StockQuote>();
		for (Result r : results) {
			ret.add(toStock(r));
		}
		table.close();
		return ret;
	}

	public List<StockQuote> listAll() throws IOException {
		HTableInterface table = connection.getTable(TABLE_NAME);
		ResultScanner results = table.getScanner(mkScan());
		List<StockQuote> ret = new ArrayList<StockQuote>();
		for (Result r : results) {
			ret.add(toStock(r));
		}
		table.close();
		return ret;
	}
}
