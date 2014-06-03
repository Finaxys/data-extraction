package provider.impl.yahoo;

import helper.Helper;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import msg.Document;
import msg.Document.ContentType;
import msg.Document.DataClass;
import msg.Document.DataType;

import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.client.HConnectionManager;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.nio.client.methods.HttpAsyncMethods;

import provider.StockQuoteProvider;
import provider.impl.HttpResponseConsumer;
import dao.StockDao;
import dao.impl.StockDaoImpl;

public class YahooStockQuoteProvider implements StockQuoteProvider {
	public static final char Y_PROVIDER_SYMB = '1';

	private static final String YQL_DEFAULT_ENV = "store://datatables.org/alltableswithkeys";
	private static final String YQL_QUOTE_QUERY = "select * from yahoo.finance.quote where symbol in (?)";
	
	private CloseableHttpAsyncClient client;
	private Helper helper;

	public YahooStockQuoteProvider() {
		client = HttpAsyncClients.createDefault();
		this.helper = new Helper();
	}

	public CloseableHttpAsyncClient getClient() {
		return client;
	}

	public void setClient(CloseableHttpAsyncClient client) {
		this.client = client;
	}

	public Helper getHelper() {
		return helper;
	}

	public void setHelper(Helper helper) {
		this.helper = helper;
	}



	public List<Document> getCurrentStocksQuotes(ContentType format, DataType type) throws Exception {
		List<Document> list = new ArrayList<Document>();
		try {
			HConnection hConnection = HConnectionManager.createConnection(HBaseConfiguration.create());
			StockDao d = new StockDaoImpl(hConnection);
			client.start();
			List<String> symbList = d.listAllSymbols();
			String query = "";
			for (String symbs : symbList) {
				query = helper.constructQuery(YQL_QUOTE_QUERY, symbs);
				URI uri = helper.contructYqlUri(query, format, YQL_DEFAULT_ENV);
				File tfile = File.createTempFile("stocksQuotesTemp", "." + format);
				Future<File> future = client.execute(HttpAsyncMethods.createGet(uri), new HttpResponseConsumer(tfile),
						null);
				File f = future.get();
				if (f.length() > 0)
					list.add(new Document(format, type, DataClass.StockQuotes, Y_PROVIDER_SYMB, helper.toBytes(f)));
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			client.close();
		}
	}

}