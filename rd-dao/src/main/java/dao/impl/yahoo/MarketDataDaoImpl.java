package dao.impl.yahoo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
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
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.nio.client.methods.HttpAsyncMethods;
import org.apache.http.nio.client.methods.ZeroCopyConsumer;

import dao.CurrencyPairDao;
import dao.IndexInfoDao;
import dao.MarketDataDao;
import dao.StockDao;
import dao.impl.CurrencyPairDaoImpl;
import dao.impl.IndexInfoDaoImpl;
import dao.impl.StockDaoImpl;

public class MarketDataDaoImpl implements MarketDataDao {

	public static final int Y_PROVIDER_SYMB = 1;

	private static final String YQL_HOST = "query.yahooapis.com";
	private static final String YQL_PATH = "/v1/public/yql";
	private static final String YQL_QUERY_PARAM = "q";
	private static final String YQL_FORMAT_PARAM = "format";
	private static final String YQL_ENV_PARAM = "env";
	private static final String YQL_DEFAULT_ENV = "store://datatables.org/alltableswithkeys";
	private static final String YQL_HIST_DATA_QUERY = "select * from yahoo.finance.historicaldata where symbol = \"?\" and startDate = \"?\" and endDate = \"?\"";
	private static final String YQL_STOCK_QUERY = "select * from yahoo.finance.stocks" + "where symbol in "
			+ "(select company.symbol from yahoo.finance.industry" + "where id in "
			+ "(select industry.id from yahoo.finance.sectors))";
	private static final String YQL_QUOTE_QUERY = "select * from yahoo.finance.quote where symbol in (?)";
	private static final String YQL_FXRATE_QUERY = "select * from yahoo.finance.xchange where pair in (?)";
	private static final String YQL_INDEX_QUOTES_QUERY = "select * from yahoo.finance.quoteslist where symbol in (?)";

	private CloseableHttpAsyncClient client;

	public MarketDataDaoImpl() {
		client = HttpAsyncClients.createDefault();
	}

	public CloseableHttpAsyncClient getClient() {
		return client;
	}

	public void setClient(CloseableHttpAsyncClient client) {
		this.client = client;
	}



	private String constructQuery(String query, String symbs) throws URISyntaxException {
		return query.replaceFirst("\\?", symbs);
	}
	
	private URI contructUri(String query, ContentType format, String env) throws URISyntaxException {

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair(YQL_QUERY_PARAM, query));
		params.add(new BasicNameValuePair(YQL_FORMAT_PARAM, format.getName()));
		params.add(new BasicNameValuePair(YQL_ENV_PARAM, env));

		URIBuilder builder = new URIBuilder().setScheme("https").setHost(YQL_HOST).setPath(YQL_PATH)
				.setParameters(params);

		return builder.build();

	}

	private byte[] toBytes(File file) throws IOException {
		FileInputStream fileInputStream = null;
		byte[] bFile = new byte[(int) file.length()];
		fileInputStream = new FileInputStream(file);
		fileInputStream.read(bFile);
		fileInputStream.close();
		return bFile;
	}
	
	public List<Document> getStocks(ContentType format) throws Exception {
		List<Document> list = new ArrayList<Document>();
		try {
			client.start();
			// URI uri = contructUri(YQL_STOCK_QUERY, format,
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
			list.add(new Document(format, DataType.Ref, DataClass.Stocks, Y_PROVIDER_SYMB, toBytes(f)));
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			client.close();
		}
	}

	public List<Document> getStocksQuotes(ContentType format) throws Exception {
		List<Document> list = new ArrayList<Document>();
		try {
			HConnection hConnection = HConnectionManager.createConnection(HBaseConfiguration.create());
			StockDao d = new StockDaoImpl(hConnection);
			client.start();
			List<String> symbList = d.listAllSymbols();
			String query = "";
			for (String symbs : symbList) {
				constructQuery(YQL_QUOTE_QUERY,  symbs);
				URI uri = contructUri(query, format, YQL_DEFAULT_ENV);
				File tfile = File.createTempFile("stocksQuotesTemp", "." + format);
				Future<File> future = client.execute(HttpAsyncMethods.createGet(uri), new HttpResponseConsumer(tfile),
						null);
				File f = future.get();
				list.add(new Document(format, DataType.EOD, DataClass.StockQuotes, Y_PROVIDER_SYMB, toBytes(f)));
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			client.close();
		}
	}

	public List<Document> getFXRates(ContentType format) throws Exception {
		List<Document> list = new ArrayList<Document>();
		try {
			HConnection hConnection = HConnectionManager.createConnection(HBaseConfiguration.create());
			CurrencyPairDao d = new CurrencyPairDaoImpl(hConnection);
			client.start();
			List<String> symbList = d.listAllSymbols();
			String query = "";
			for (String symbs : symbList) {
				constructQuery(YQL_FXRATE_QUERY, symbs);
				URI uri = contructUri(query, format, YQL_DEFAULT_ENV);
				File tfile = File.createTempFile("fxRatesTemp", "." + format);
				Future<File> future = client.execute(HttpAsyncMethods.createGet(uri), new HttpResponseConsumer(tfile),
						null);
				File f = future.get();
				list.add(new Document(format, DataType.EOD, DataClass.FXRates, Y_PROVIDER_SYMB, toBytes(f)));
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			client.close();
		}
	}

	public List<Document> getIndexQuotes(ContentType format) throws Exception {
		List<Document> list = new ArrayList<Document>();
		try {
			HConnection hConnection = HConnectionManager.createConnection(HBaseConfiguration.create());
			IndexInfoDao d = new IndexInfoDaoImpl(hConnection);
			client.start();
			List<String> symbList = d.listAllSymbols();
			String query = "";
			for (String symbs : symbList) {
				System.out.println(symbs);
				constructQuery(YQL_INDEX_QUOTES_QUERY, symbs);
				URI uri = contructUri(query, format, YQL_DEFAULT_ENV);
				File tfile = File.createTempFile("indexQuotesTemp", "." + format);
				Future<File> future = client.execute(HttpAsyncMethods.createGet(uri), new HttpResponseConsumer(tfile),
						null);
				File f = future.get();
				list.add(new Document(format, DataType.Ref, DataClass.IndexInfos, Y_PROVIDER_SYMB, toBytes(f)));
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			client.close();
		}
	}

	public List<Document> getExchanges(ContentType format) throws Exception {
		return null;
	}

	public List<Document> getCurrencyPairs(ContentType format) throws Exception {
		return null;
	}

	public List<Document> getIndexInfos(ContentType format) throws Exception {
		return null;
	}

	static class HttpResponseConsumer extends ZeroCopyConsumer<File> {
		public HttpResponseConsumer(File file) throws FileNotFoundException {
			super(file);
		}

		@Override
		protected File process(HttpResponse response, File file, org.apache.http.entity.ContentType contentType)
				throws Exception {

			BufferedReader reader = new BufferedReader(
					new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
			StringBuilder builder = new StringBuilder();

			for (String line = null; (line = reader.readLine()) != null;) {
				builder.append(line).append("\n");
			}
			System.out.println(builder);
			return file;

		}
	}
}
