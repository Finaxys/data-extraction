package dao.impl;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.client.HConnectionManager;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.util.Bytes;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import utils.Md5Utils;
import dao.StockDao;
import domain.Stock;

public class StockDaoImpl implements StockDao {

	// static Logger logger = Logger.getLogger(StockSummaryDaoImpl.class);

	public static final byte[] SYMBOL_COL = Bytes.toBytes("symbol");
	public static final byte[] EXCH_SYMB_COL = Bytes.toBytes("exchSymb");
	public static final byte[] PROVIDER_COL = Bytes.toBytes("provider");
	public static final byte[] CN_COL = Bytes.toBytes("company_name");
	public static final byte[] START_COL = Bytes.toBytes("start");
	public static final byte[] END_COL = Bytes.toBytes("end");
	public static final byte[] SECTOR_COL = Bytes.toBytes("sector");
	public static final byte[] INDUSTRY_COL = Bytes.toBytes("industry");
	public static final byte[] FTE_COL = Bytes.toBytes("full_time_employees");

	public static final byte[] TABLE_NAME = Bytes.toBytes("stock");
	public static final byte[] INFO_FAM = Bytes.toBytes("i");
	

	private HConnection connection;

	// Constructor

	public StockDaoImpl(HConnection connection) {
		this.connection = connection;

	}

	// Helpers

	private Stock toStockSummary(Result r) {
		return null;// ToDo
	}

	private byte[] mkRowKey(Stock stock) {
		return mkRowKey(stock.getProvider(), stock.getExchSymb(), stock.getSymbol());
	}

	private byte[] mkRowKey(char provider, String exchSymb, String symbol) {
		byte provByte = (byte)provider;
		byte[] exchSymbHash = Md5Utils.md5sum(exchSymb);
		byte[] symbHash = Md5Utils.md5sum(symbol);
		byte[] rowkey = new byte[2 * Md5Utils.MD5_LENGTH + 1]; 

		int offset = 0;
		offset = Bytes.putByte(rowkey, offset, provByte);
		offset = Bytes.putBytes(rowkey, offset, exchSymbHash, 0, Md5Utils.MD5_LENGTH);
		Bytes.putBytes(rowkey, offset, symbHash, 0, Md5Utils.MD5_LENGTH);

		return rowkey;
	}

	private Get mkGet(char provider, String exchSymb, String symbol) {
		Get g = new Get(mkRowKey(provider, exchSymb, symbol));
		return g;
	}

	private Put mkPut(Stock stock) {
		Put p = new Put(mkRowKey(stock));

		// Load all fields in the class (private included)
		Field[] stockAttributes = Stock.class.getDeclaredFields();
		Field[] stockCols = StockDaoImpl.class.getDeclaredFields();
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
					if (field.getType().equals(char.class))
						p.add(INFO_FAM, (byte[]) stockCols[i].get(null), Bytes.toBytes((Character) value));
			
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

	public boolean add(Stock stock) {
		try {
			HTableInterface table = connection.getTable(TABLE_NAME);
			Put p = mkPut(stock);
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

	public Stock get(char provider, String exchSymb, String symbol) throws IOException {
		HTableInterface table = connection.getTable(TABLE_NAME);
		Get g = mkGet(provider, exchSymb, symbol);
		Result result = table.get(g);
		if (result.isEmpty())
			return null;
		Stock stock = toStockSummary(result);
		table.close();
		return stock;
	}

	public List<Stock> list(String prefix) throws IOException {
		HTableInterface table = connection.getTable(TABLE_NAME);

		ResultScanner results = table.getScanner(mkScan(prefix));
		List<Stock> ret = new ArrayList<Stock>();
		for (Result r : results) {
			ret.add(toStockSummary(r));
		}
		table.close();
		return ret;
	}

	public List<Stock> listAll() throws IOException {
		HTableInterface table = connection.getTable(TABLE_NAME);
		ResultScanner results = table.getScanner(mkScan());
		List<Stock> ret = new ArrayList<Stock>();
		for (Result r : results) {
			ret.add(toStockSummary(r));
		}
		table.close();
		return ret;
	}

	public List<String> listAllSymbols() throws IOException {
		HTableInterface table = connection.getTable(TABLE_NAME);
		ResultScanner results = table.getScanner(mkScan());
		List<String> sp = new ArrayList<String>();
		StringBuilder sb = new StringBuilder();
		int i = 0;
		for (Result r : results) {
			sb.append("\"" + Bytes.toString(r.getValue(INFO_FAM, SYMBOL_COL)) + "\",");
			i++;

			// i=1000 => no response
			if (i == 500) {
				sp.add(sb.toString().replaceAll(",$", ""));
				sb.setLength(0);
				i = 0;
			}

		}
		sp.add(sb.toString().replaceAll(",$", ""));
		table.close();
		return sp;
	}

	public static void main(String[] args) throws IOException {
		HConnection hConnection = HConnectionManager.createConnection(HBaseConfiguration.create());
		StockDao d = new StockDaoImpl(hConnection);
		System.out.println(d.listAllSymbols().size());
		System.out.println(d.listAllSymbols().get(0));
	}
}
