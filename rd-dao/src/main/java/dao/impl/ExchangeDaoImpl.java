package dao.impl;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
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
import org.apache.log4j.Logger;
import org.joda.time.DateTime;

import utils.Md5Utils;
import domain.Exchange;
import dao.ExchangeDao;

public class ExchangeDaoImpl implements ExchangeDao {

	// static Logger logger = Logger.getLogger(ExchangeDaoImpl.class);

	public static final byte[] MIC_COL = Bytes.toBytes("mic");
	public static final byte[] SYMBOL_COL = Bytes.toBytes("symbol");
	public static final byte[] SUFFIX_COL = Bytes.toBytes("suffix");
	public static final byte[] PROVIDER_COL = Bytes.toBytes("provider");
	public static final byte[] NAME_COL = Bytes.toBytes("name");
	public static final byte[] TYPE_COL = Bytes.toBytes("type");
	public static final byte[] CONTINENT_COL = Bytes.toBytes("continent");
	public static final byte[] COUNTRY_COL = Bytes.toBytes("country");
	public static final byte[] CURRENCY_COL = Bytes.toBytes("currency");
	public static final byte[] OPEN_TIME_COL = Bytes.toBytes("ot");
	public static final byte[] CLOSE_TIME_COL = Bytes.toBytes("ct");
	public static final byte[] STATUS_COL = Bytes.toBytes("status");

	public static final byte[] TABLE_NAME = Bytes.toBytes("exchange");
	public static final byte[] INFO_FAM = Bytes.toBytes("i");

	
	private HConnection connection;

	// Constructor
	public ExchangeDaoImpl(HConnection connection) {
		this.connection = connection;
	}

	// Helpers

	private Exchange toExchange(Result r) {
		return new Exchange(r.getValue(INFO_FAM, MIC_COL), r.getValue(INFO_FAM, SYMBOL_COL), r.getValue(INFO_FAM, SUFFIX_COL), r.getValue(INFO_FAM,
				PROVIDER_COL), r.getValue(INFO_FAM, NAME_COL), r.getValue(INFO_FAM, TYPE_COL), r.getValue(INFO_FAM,
				CONTINENT_COL), r.getValue(INFO_FAM, COUNTRY_COL), r.getValue(INFO_FAM, CURRENCY_COL), r.getValue(
				INFO_FAM, OPEN_TIME_COL), r.getValue(INFO_FAM, CLOSE_TIME_COL), r.getValue(INFO_FAM, STATUS_COL));

	}

	private byte[] mkRowKey(Exchange exchange) {
		return mkRowKey(exchange.getProvider(), exchange.getSuffix(), exchange.getMic());
	}

	private byte[] mkRowKey(char provider, String suffix, String mic) {
		byte provBytes = (byte)provider;
		byte[] suffixHash = Md5Utils.md5sum(suffix);
		byte[] micb = Bytes.toBytes(mic);
		byte[] rowkey = new byte[1 + Md5Utils.MD5_LENGTH + micb.length]; 
		int offset = 0;
		offset = Bytes.putByte(rowkey, offset, provBytes);
		offset = Bytes.putBytes(rowkey, offset, suffixHash, 0, Md5Utils.MD5_LENGTH);
		Bytes.putBytes(rowkey, offset, micb, 0, micb.length);

		return rowkey;
	}

	private Get mkGet(char provider, String suffix, String mic) {
		Get g = new Get(mkRowKey(provider, suffix, mic));
		return g;
	}

	private Put mkPut(Exchange exchange) {
		Put p = new Put(mkRowKey(exchange));

		// Load all fields in the class (private included)
		Field[] exch_attributes = Exchange.class.getDeclaredFields();
		Field[] exch_cols = ExchangeDaoImpl.class.getDeclaredFields();
		int i = 0;
		Object value;
		for (Field field : exch_attributes) {

			try {
				if ((value = PropertyUtils.getSimpleProperty(exchange, field.getName())) != null) {
					if (field.getType().equals(String.class))
						p.add(INFO_FAM, (byte[]) exch_cols[i].get(null), Bytes.toBytes((String) value));
					if (field.getType().equals(Boolean.class))
						p.add(INFO_FAM, (byte[]) exch_cols[i].get(null), Bytes.toBytes((Boolean) value));
					if (field.getType().equals(Long.class))
						p.add(INFO_FAM, (byte[]) exch_cols[i].get(null), Bytes.toBytes((Long) value));
					if (field.getType().equals(Integer.class))
						p.add(INFO_FAM, (byte[]) exch_cols[i].get(null), Bytes.toBytes((Integer) value));
					if (field.getType().equals(char.class))
						p.add(INFO_FAM, (byte[]) exch_cols[i].get(null), Bytes.toBytes((Character) value));
			
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

	// CRUDmi	
	public boolean add(Exchange exchange) {
		try {
			HTableInterface table = connection.getTable(TABLE_NAME);
			Put p = mkPut(exchange);
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

	public Exchange get(char provider, String suffix, String mic) throws IOException {
		HTableInterface table = connection.getTable(TABLE_NAME);
		Get g = mkGet(provider, suffix, mic);
		Result result = table.get(g);
		if (result.isEmpty())
			return null;
		Exchange exchange = toExchange(result);
		table.close();
		return exchange;
	}

	public List<Exchange> list(String prefix) throws IOException {
		HTableInterface table = connection.getTable(TABLE_NAME);
		ResultScanner results = table.getScanner(mkScan(prefix));
		List<Exchange> ret = new ArrayList<Exchange>();
		for (Result r : results) {
			ret.add(toExchange(r));
		}
		table.close();
		return ret;
	}

	public List<Exchange> listAll() throws IOException {
		HTableInterface table = connection.getTable(TABLE_NAME);
		ResultScanner results = table.getScanner(mkScan());
		List<Exchange> ret = new ArrayList<Exchange>();
		for (Result r : results) {
			ret.add(toExchange(r));
		}
		table.close();
		return ret;
	}

}
