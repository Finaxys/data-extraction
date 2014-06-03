package dao.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.util.Bytes;

import utils.Md5Utils;
import dao.IndexInfoDao;
import domain.IndexInfo;

public class IndexInfoDaoImpl implements IndexInfoDao {

	public static final byte[] SYMBOL_COL = Bytes.toBytes("symbol");
	public static final byte[] NAME_COL = Bytes.toBytes("name");
	public static final byte[] EXCH_SYMB_COL = Bytes.toBytes("exchSymb");
	public static final byte[] PROVIDER_COL = Bytes.toBytes("provider");

	public static final byte[] TABLE_NAME = Bytes.toBytes("index_info");
	public static final byte[] INFO_FAM = Bytes.toBytes("i");

	private HConnection connection;

	public IndexInfoDaoImpl(HConnection connection) {
		super();
		this.connection = connection;
	}

	// Helpers

	private IndexInfo toIndexInfo(Result r) {
		return null;// ToDo
	}

	private byte[] mkRowKey(IndexInfo index) {
		return mkRowKey(index.getProvider(), index.getExchSymb(), index.getSymbol());
	}

	private byte[] mkRowKey(char provider, String exchSymb, String symbol) {
		byte provByte = (byte)provider;
		byte[] exchSymbHash = Md5Utils.md5sum(exchSymb);
		byte[] symbHash = Md5Utils.md5sum(symbol);
		byte[] rowkey = new byte[1 + 2 * Md5Utils.MD5_LENGTH]; 

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

	private Put mkPut(IndexInfo indexInfo) {
		Put p = new Put(mkRowKey(indexInfo));
		p.add(INFO_FAM, SYMBOL_COL, Bytes.toBytes(indexInfo.getSymbol()));
		p.add(INFO_FAM, EXCH_SYMB_COL, Bytes.toBytes(indexInfo.getExchSymb()));
		p.add(INFO_FAM, PROVIDER_COL, Bytes.toBytes(indexInfo.getProvider()));
		p.add(INFO_FAM, NAME_COL, Bytes.toBytes(indexInfo.getName()));
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

	public boolean add(IndexInfo indexInfo) {
		try {
			HTableInterface table = connection.getTable(TABLE_NAME);
			Put p = mkPut(indexInfo);
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

	public IndexInfo get(char provider, String exchSymb, String symbol) throws IOException {
		HTableInterface table = connection.getTable(TABLE_NAME);
		Get g = mkGet(provider, exchSymb, symbol);
		Result result = table.get(g);
		if (result.isEmpty())
			return null;
		IndexInfo index = toIndexInfo(result);
		table.close();
		return index;
	}

	public List<IndexInfo> list(String prefix) throws IOException {
		HTableInterface table = connection.getTable(TABLE_NAME);
		ResultScanner results = table.getScanner(mkScan(prefix));
		List<IndexInfo> ret = new ArrayList<IndexInfo>();
		for (Result r : results) {
			ret.add(toIndexInfo(r));
		}
		table.close();
		return ret;
	}

	public List<IndexInfo> listAll() throws IOException {
		HTableInterface table = connection.getTable(TABLE_NAME);
		ResultScanner results = table.getScanner(mkScan());
		List<IndexInfo> ret = new ArrayList<IndexInfo>();
		for (Result r : results) {
			ret.add(toIndexInfo(r));
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
			if (i == 100) {
				sp.add(sb.toString().replaceAll(",$", ""));
				sb.setLength(0);
				i = 0;
			}

		}
		sp.add(sb.toString().replaceAll(",$", ""));
		table.close();
		return sp;
	}
}
