package provider;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.client.HConnectionManager;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.nio.client.methods.HttpAsyncMethods;
import org.apache.http.nio.client.methods.ZeroCopyConsumer;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import publisher.Publisher;
import converter.Converter;
import converter.yahoo.FXRatesConverter;
import converter.yahoo.HistDataConverter;
import converter.yahoo.StocksConverter;
import converter.yahoo.StocksQuotesConverter;
import dao.CurrencyPairDao;
import dao.FXRateDao;
import dao.StockDao;
import dao.impl.CurrencyPairDaoImpl;
import dao.impl.FXRateDaoImpl;
import dao.impl.StockDaoImpl;

public class YahooDataProvider implements DataProvider {

	public static final int Y_PROVIDER_SYMB = 1;

	private static final String YQL_HOST = "query.yahooapis.com";
	private static final String YQL_PATH = "/v1/public/yql";
	private static final String YQL_QUERY_PARAM = "q";
	private static final String YQL_FORMAT_PARAM = "format";
	private static final String YQL_ENV_PARAM = "env";
	private static final String YQL_DEFAULT_ENV = "store://datatables.org/alltableswithkeys";
	private static final String YQL_HIST_DATA_QUERY = "select * from yahoo.finance.historicaldata where symbol = \"?\" and startDate = \"?\" and endDate = \"?\"";
	private static final String YQL_STOCK_SUMMARY_QUERY = "select * from yahoo.finance.stocks" + "where symbol in "
			+ "(select company.symbol from yahoo.finance.industry" + "where id in "
			+ "(select industry.id from yahoo.finance.sectors))";
	private static final String YQL_QUOTE_QUERY = "select * from yahoo.finance.quote where symbol in (?)";
	private static final String YQL_FXRATE_QUERY = "select * from yahoo.finance.xchange where pair in (?)";

	private CloseableHttpAsyncClient client;

	public YahooDataProvider() {
		client = HttpAsyncClients.createDefault();
	}

	public CloseableHttpAsyncClient getClient() {
		return client;
	}

	public void setClient(CloseableHttpAsyncClient client) {
		this.client = client;
	}

	private URI contructUri(String query, String format, String env) throws URISyntaxException {

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair(YQL_QUERY_PARAM, query));
		params.add(new BasicNameValuePair(YQL_FORMAT_PARAM, format));
		params.add(new BasicNameValuePair(YQL_ENV_PARAM, env));

		URIBuilder builder = new URIBuilder().setScheme("https").setHost(YQL_HOST).setPath(YQL_PATH)
				.setParameters(params);

		return builder.build();

	}

	public void getHistData(String symbol, String startDate, String endDate, String format) throws URISyntaxException,
			ClientProtocolException, IOException {

		try {

			client.start();

			String query = YQL_HIST_DATA_QUERY.replaceFirst("\\?", symbol).replaceFirst("\\?", startDate)
					.replaceFirst("\\?", endDate);

			URI uri = contructUri(query, format, YQL_DEFAULT_ENV);

			// create a temp file
			File tfile = File.createTempFile("histDataTemp", "." + format);

			Future<Boolean> future = client.execute(HttpAsyncMethods.createGet(uri), new HttpResponseConsumer(tfile,
					new HistDataConverter(), Publisher.HIST_DATA_ROUTING_KEY), null);
			Boolean result = future.get();
			if (result != null && result.booleanValue()) {
				System.out.println("Request successfully executed");
			} else {
				System.out.println("Request failed");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			client.close();
		}

	}

	public void getStocks(String format) throws Exception {

		try {

			client.start();

			// URI uri = contructUri(YQL_STOCK_SUMMARY_QUERY, format,
			// YQL_DEFAULT_ENV);
			URI uri = new URIBuilder()
					.setScheme("https")
					.setHost(
							"query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.stocks%20where%20symbol%20in%20(select%20company.symbol%20from%20yahoo.finance.industry%20where%20id%20in%20(select%20industry.id%20from%20yahoo.finance.sectors))&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys")
					.build();

			// create a temp file
			File tfile = File.createTempFile("stocksTemp", "." + format);

			Future<Boolean> future = client.execute(HttpAsyncMethods.createGet(uri), new HttpResponseConsumer(tfile,
					new StocksConverter(), Publisher.STOCKS_ROUTING_KEY), null);
			Boolean result = future.get();
			if (result != null && result.booleanValue()) {
				System.out.println("Request successfully executed");
			} else {
				System.out.println("Request failed");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			client.close();
		}
	}

	public void getExchanges(String format) throws Exception {
		// TODO Auto-generated method stub
	}

	public void getCurrencyPairs(String format) throws Exception {
		// TODO Auto-generated method stub

	}

	public void getIndexInfos(String format) throws Exception {
		// TODO Auto-generated method stub

	}

	public void getStocksQuotes(String format) throws Exception {
		try {
			HConnection hConnection = HConnectionManager.createConnection(HBaseConfiguration.create());
			StockDao d = new StockDaoImpl(hConnection);
			client.start();
			List<String> symbList = d.listAllSymbols();
			String query ="";
			for(String symbs : symbList){
			 query = YQL_QUOTE_QUERY.replaceFirst("\\?", symbs);
			URI uri = contructUri(query, format, YQL_DEFAULT_ENV);
			// create a temp file
			File tfile = File.createTempFile("stocksQuotesTemp", "." + format);
			Future<Boolean> future = client.execute(HttpAsyncMethods.createGet(uri), new HttpResponseConsumer(tfile,
					new StocksQuotesConverter(), Publisher.STOCKS_QUOTES_ROUTING_KEY), null);
			Boolean result = future.get();
			if (result != null && result.booleanValue()) {
				System.out.println("Request successfully executed");
			} else {
				System.out.println("Request failed");
			}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			client.close();
		}
	}

	public void getFXRates(String format) throws Exception {
		try {
			HConnection hConnection = HConnectionManager.createConnection(HBaseConfiguration.create());
			CurrencyPairDao d = new CurrencyPairDaoImpl(hConnection);
			client.start();
			List<String> symbList = d.listAllSymbols();
			String query ="";
			for(String symbs : symbList){
			 query = YQL_FXRATE_QUERY.replaceFirst("\\?", symbs);
			URI uri = contructUri(query, format, YQL_DEFAULT_ENV);
			// create a temp file
			File tfile = File.createTempFile("fxRatesTemp", "." + format);
			Future<Boolean> future = client.execute(HttpAsyncMethods.createGet(uri), new HttpResponseConsumer(tfile,
					new FXRatesConverter(), Publisher.FXRATES_ROUTING_KEY), null);
			Boolean result = future.get();
			if (result != null && result.booleanValue()) {
				System.out.println("Request successfully executed");
			} else {
				System.out.println("Request failed");
			}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			client.close();
		}
	}
	static class HttpResponseConsumer extends ZeroCopyConsumer<Boolean> {
		Converter converter;
		String routingKey;

		public HttpResponseConsumer(File file) throws FileNotFoundException {
			super(file);
		}

		public HttpResponseConsumer(File file, Converter converter, String routingKey) throws FileNotFoundException {
			super(file);
			this.converter = converter;
			this.routingKey = routingKey;
		}

		@Override
		protected Boolean process(HttpResponse response, File file, ContentType contentType) throws Exception {

//			BufferedReader reader = new BufferedReader(
//					new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
//			StringBuilder builder = new StringBuilder();
//
//			for (String line = null; (line = reader.readLine()) != null;) {
//				builder.append(line).append("\n");
//			}
//			System.out.println(builder);
			try {
				Publisher publisher = new Publisher();

				// send data to mom
				publisher.publish(converter.convert(file), routingKey);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}

		}
	}

	public static void main(String[] args) {

		DataProvider yql = new YahooDataProvider();
		try {
			// yql.getHistData("YHOO", "2009-09-11", "2010-03-10", "xml");
//			yql.getStocksQuotes("xml");
//			yql.getStocks("xml");
			yql.getFXRates("xml");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}



}
