package dao.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import msg.Document.DataType;

import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.util.Bytes;

import dao.FXRateDao;
import domain.FXRate;

public class FXRateDaoImpl implements FXRateDao{

	public static final byte[] SYMBOL_COL = Bytes.toBytes("s");
	public static final byte[] RATE_COL = Bytes.toBytes("r");
	public static final byte[] ASK_COL = Bytes.toBytes("a");
	public static final byte[] BID_COL = Bytes.toBytes("b");
	public static final byte[] PROVIDER_COL = Bytes.toBytes("p");
	public static final byte[] TS_COL = Bytes.toBytes("ts");
	public static final byte[] TYPE_COL = Bytes.toBytes("t");

	public static final byte[] TABLE_NAME = Bytes.toBytes("fx_rate");
	public static final byte[] VALUES_FAM = Bytes.toBytes("v");

	private static final int longLength = 8;
	private static final int intLength = 4;
	
	private HConnection connection;

	public FXRateDaoImpl(HConnection connection) {
		super();
		this.connection = connection;
	}

	// Helpers

	private FXRate toCurrencyPair(Result r) {
		return null;// ToDo
	}

	private byte[] mkRowKey(FXRate fxRate) {
		return mkRowKey(fxRate.getSymbol(), fxRate.getTs(), fxRate.getType());
	}

	private byte[] mkRowKey(String symbol, Long ts, DataType dataType) {

		byte[] symbBytes = Bytes.toBytes(symbol);
		byte[] typeBytes = dataType.getName().getBytes();
		byte[] timestamp = Bytes.toBytes(ts);
		byte[] rowkey = new byte[symbBytes.length + longLength]; 

		int offset = 0;
		offset = Bytes.putBytes(rowkey, offset, typeBytes, 0, intLength);
		offset = Bytes.putBytes(rowkey, offset, symbBytes, 0, symbBytes.length);
		Bytes.putBytes(rowkey, offset, timestamp, 0, longLength);
		
		return rowkey;
	}



	private Put mkPut(FXRate fxRate) {
		Put p = new Put(mkRowKey(fxRate));
		p.add(VALUES_FAM, SYMBOL_COL, Bytes.toBytes(fxRate.getSymbol()));
		p.add(VALUES_FAM, RATE_COL, Bytes.toBytes(fxRate.getRate()));
		p.add(VALUES_FAM, ASK_COL, Bytes.toBytes(fxRate.getAsk()));
		p.add(VALUES_FAM, BID_COL, Bytes.toBytes(fxRate.getBid()));
		p.add(VALUES_FAM, PROVIDER_COL, Bytes.toBytes(fxRate.getProvider()));
		p.add(VALUES_FAM, TS_COL, Bytes.toBytes(fxRate.getTs()));
		p.add(VALUES_FAM, TYPE_COL, Bytes.toBytes(fxRate.getType().getName()));
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
	
	private Scan mkScan(byte[] start, byte[] end) {
		Scan scan = new Scan(start, end);
		return scan;
	}

	
	public boolean add(FXRate stock) {
		try {
			HTableInterface table = connection.getTable(TABLE_NAME);
			Put p = mkPut(stock);
			table.put(p);
			table.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}


	public List<FXRate> list(String prefix) throws IOException {
		HTableInterface table = connection.getTable(TABLE_NAME);
		ResultScanner results = table.getScanner(mkScan(prefix));
		List<FXRate> ret = new ArrayList<FXRate>();
		for (Result r : results) {
			ret.add(toCurrencyPair(r));
		}
		table.close();
		return ret;
	}

	public List<FXRate> listAll() throws IOException {
		HTableInterface table = connection.getTable(TABLE_NAME);
		ResultScanner results = table.getScanner(mkScan());
		List<FXRate> ret = new ArrayList<FXRate>();
		for (Result r : results) {
			ret.add(toCurrencyPair(r));
		}
		table.close();
		return ret;
	}

	public List<FXRate> list(byte[] start, byte[] end) throws IOException {
		HTableInterface table = connection.getTable(TABLE_NAME);
		ResultScanner results = table.getScanner(mkScan(start, end));
		List<FXRate> ret = new ArrayList<FXRate>();
		for (Result r : results) {
			ret.add(toCurrencyPair(r));
		}
		table.close();
		return ret;
	}


}
