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

import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.nio.client.methods.HttpAsyncMethods;

import provider.StockProvider;
import provider.impl.HttpResponseConsumer;

public class YahooStockProvider implements StockProvider {

	public static final char Y_PROVIDER_SYMB = '1';

	private static final String YQL_DEFAULT_ENV = "store://datatables.org/alltableswithkeys";
	private static final String YQL_STOCK_QUERY = "select * from yahoo.finance.stocks" + "where symbol in "
			+ "(select company.symbol from yahoo.finance.industry" + "where id in "
			+ "(select industry.id from yahoo.finance.sectors))";

	private CloseableHttpAsyncClient client;
	private Helper helper;

	public YahooStockProvider() {
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

	public List<Document> getStocks(ContentType format) throws Exception {
		List<Document> list = new ArrayList<Document>();
		try {
			client.start();
			// URI uri = helper.contructYqlUri(YQL_STOCK_QUERY, format,
			// YQL_DEFAULT_ENV);
			URI uri = new URIBuilder()
					.setScheme("https")
					.setHost(
							"query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.stocks%20where%20symbol%20in%20(select%20company.symbol%20from%20yahoo.finance.industry%20where%20id%20in%20(select%20industry.id%20from%20yahoo.finance.sectors))&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys")
					.build();
			File tfile = File.createTempFile("stocksTemp", "." + format);
			Future<File> future = client
					.execute(HttpAsyncMethods.createGet(uri), new HttpResponseConsumer(tfile), null);
			File f = future.get();
			if (f.length() > 0)
				list.add(new Document(format, DataType.Ref, DataClass.Stocks, Y_PROVIDER_SYMB, helper.toBytes(f)));
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			client.close();
		}
	}

}
