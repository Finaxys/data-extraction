package dao.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.client.HConnectionManager;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dao.CurrencyPairDao;
import dao.StockDao;
import domain.CurrencyPair;


public class CurrencyPairDaoImpl implements CurrencyPairDao {

	public static final byte[] SYMBOL_COL = Bytes.toBytes("symbol");
	public static final byte[] BASE_COL = Bytes.toBytes("base");
	public static final byte[] QUOTE_COL = Bytes.toBytes("quote");

	public static final byte[] TABLE_NAME = Bytes.toBytes("currency_pair");
	public static final byte[] INFO_FAM = Bytes.toBytes("i");

	private HConnection connection;

	public CurrencyPairDaoImpl(HConnection connection) {
		super();
		this.connection = connection;
	}

	// Helpers

	private CurrencyPair toCurrencyPair(Result r) {
		return null;// ToDo
	}

	private byte[] mkRowKey(CurrencyPair currencyPair) {
		return mkRowKey(currencyPair.getSymbol());
	}

	private byte[] mkRowKey(String symbol) {
		return symbol.getBytes();
	}

	private Get mkGet(String symbol) {
		Get g = new Get(mkRowKey(symbol));
		return g;
	}

	private Put mkPut(CurrencyPair currencyPair) {
		Put p = new Put(mkRowKey(currencyPair));
		p.add(INFO_FAM, SYMBOL_COL, Bytes.toBytes(currencyPair.getSymbol()));
		p.add(INFO_FAM, BASE_COL, Bytes.toBytes(currencyPair.getBaseCurrency()));
		p.add(INFO_FAM, QUOTE_COL, Bytes.toBytes(currencyPair.getQuoteCurrency()));
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

	public boolean add(CurrencyPair currencyPair) {
		try {
			HTableInterface table = connection.getTable(TABLE_NAME);
			Put p = mkPut(currencyPair);
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

	public CurrencyPair get(String symbol) throws IOException {
		HTableInterface table = connection.getTable(TABLE_NAME);
		Get g = mkGet(symbol);
		Result result = table.get(g);
		if (result.isEmpty())
			return null;
		CurrencyPair currencyPair =toCurrencyPair(result);
		table.close();
		return currencyPair;
	}


	public List<CurrencyPair> list(String prefix) throws IOException {
		HTableInterface table = connection.getTable(TABLE_NAME);
		ResultScanner results = table.getScanner(mkScan(prefix));
		List<CurrencyPair> ret = new ArrayList<CurrencyPair>();
		for (Result r : results) {
			ret.add(toCurrencyPair(r));
		}
		table.close();
		return ret;
	}

	public List<CurrencyPair> listAll() throws IOException {
		HTableInterface table = connection.getTable(TABLE_NAME);
		ResultScanner results = table.getScanner(mkScan());
		List<CurrencyPair> ret = new ArrayList<CurrencyPair>();
		for (Result r : results) {
			ret.add(toCurrencyPair(r));
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
		CurrencyPairDao d = new CurrencyPairDaoImpl(hConnection);
		System.out.println(d.listAllSymbols().size());
		System.out.println(d.listAllSymbols().get(0));
	}
}
