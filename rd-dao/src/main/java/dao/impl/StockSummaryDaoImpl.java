package dao.impl;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

import utils.Md5Utils;
import dao.StockSummaryDao;
import domain.StockSummary;

public class StockSummaryDaoImpl implements StockSummaryDao {

	// static Logger logger = Logger.getLogger(StockSummaryDaoImpl.class);

	public static final byte[] TABLE_NAME = Bytes.toBytes("stock_summary");
	public static final byte[] INFO_FAM = Bytes.toBytes("i");
	public static final byte[] MIC_COL = Bytes.toBytes("symbol");
	public static final byte[] NAME_COL = Bytes.toBytes("company_name");
	public static final byte[] TYPE_COL = Bytes.toBytes("start");
	public static final byte[] CONTINENT_COL = Bytes.toBytes("end");
	public static final byte[] COUNTRY_COL = Bytes.toBytes("sector");
	public static final byte[] CURRENCY_COL = Bytes.toBytes("industry");
	public static final byte[] OPEN_TIME_COL = Bytes.toBytes("full_time_employees");

	// private static final int longLength = 8; // bytes

	private HConnection connection;

	// Constructor

	public StockSummaryDaoImpl(HConnection connection) {
		this.connection = connection;

	}

	// Helpers

	private StockSummary resultToStockSummary(Result r) {
		return null;// ToDo
	}

	private byte[] mkRowKey(StockSummary stock) {
		return mkRowKey(stock.getProvider(), stock.getExchSymb(), stock.getSymbol());
	}

	private byte[] mkRowKey(Integer provider, String exchSymb, String symbol) {
		byte[] exchSymbHash = Md5Utils.md5sum(exchSymb);
		byte[] provHash = Md5Utils.md5sum(provider + exchSymbHash.toString());
		byte[] symbHash = Md5Utils.md5sum(symbol);
		byte[] rowkey = new byte[2 * Md5Utils.MD5_LENGTH]; // mic code length =
															// 4

		int offset = 0;
		offset = Bytes.putBytes(rowkey, offset, provHash, 0, Md5Utils.MD5_LENGTH);
		Bytes.putBytes(rowkey, offset, symbHash, 0, Md5Utils.MD5_LENGTH);

		return rowkey;
	}

	private Get mkGet(Integer provider, String exchSymb, String symbol) {
		Get g = new Get(mkRowKey(provider, exchSymb, symbol));
		return g;
	}

	private Put mkPut(StockSummary stock) {
		Put p = new Put(mkRowKey(stock));

		// Load all fields in the class (private included)
		Field[] stockAttributes = StockSummary.class.getDeclaredFields();
		Field[] stockCols = StockSummaryDaoImpl.class.getDeclaredFields();
		int i = 0;
		Object value;
		for (Field field : stockAttributes) {

			try {
				if ((value = PropertyUtils.getSimpleProperty(stock, field.getName())) != null) {
					if (field.getType().equals(String.class))
						p.add(INFO_FAM, (byte[]) stockCols[i].get(null), Bytes.toBytes((String) value));
					if (field.getType().equals(Date.class))
						p.add(INFO_FAM, (byte[]) stockCols[i].get(null), Bytes.toBytes(((Date) value).getTime()));
					if (field.getType().equals(Integer.class))
						p.add(INFO_FAM, (byte[]) stockCols[i].get(null), Bytes.toBytes((Integer) value));
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

	// CRUD

	public boolean add(StockSummary stock) {

		try {
			HTableInterface table = connection.getTable(TABLE_NAME);

			Put p = mkPut(stock);
			table.put(p);

			table.close();

			return true;

		} catch (IOException e) {
			// logger.info(e.toString());
			return false;
		} catch (Exception e) {
			// logger.info(e.toString());
			return false;
		}

	}

	public StockSummary get(Integer provider, String exchSymb, String symbol) throws IOException {

		HTableInterface table = connection.getTable(TABLE_NAME);

		Get g = mkGet(provider, exchSymb, symbol);
		Result result = table.get(g);

		if (result.isEmpty())
			return null;

		StockSummary stock = new StockSummary();// ToDo
		table.close();
		return stock;
	}

	public List<StockSummary> list(String prefix) throws IOException {

		HTableInterface table = connection.getTable(TABLE_NAME);
		

		ResultScanner results = table.getScanner(mkScan(prefix));
		List<StockSummary> ret = new ArrayList<StockSummary>();
		for (Result r : results) {
			ret.add(resultToStockSummary(r));
		}

		table.close();
		return ret;
	}

	public List<StockSummary> listAll() throws IOException {

		HTableInterface table = connection.getTable(TABLE_NAME);

		ResultScanner results = table.getScanner(mkScan());
		List<StockSummary> ret = new ArrayList<StockSummary>();
		for (Result r : results) {
			ret.add(resultToStockSummary(r));
		}

		table.close();
		return ret;
	}

}
