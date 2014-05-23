package provider;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.nio.IOControl;
import org.apache.http.nio.client.methods.AsyncCharConsumer;
import org.apache.http.nio.client.methods.HttpAsyncMethods;
import org.apache.http.nio.client.methods.ZeroCopyConsumer;
import org.apache.http.protocol.HttpContext;

import publisher.Publisher;
import converter.Converter;
import converter.YahooHistDataConverter;
import converter.YahooStockSummariesConverter;

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

			Future<Boolean> future = client.execute(HttpAsyncMethods.createGet(uri), new HttpResponseConsumer(tfile, new YahooHistDataConverter(), Publisher.HIST_DATA_ROUTING_KEY), null);
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

	public void getStockSummaries(String format) throws Exception{

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
			File tfile = File.createTempFile("stockSummariesTemp", "." + format);

			Future<Boolean> future = client.execute(HttpAsyncMethods.createGet(uri), new HttpResponseConsumer(tfile,
					new YahooStockSummariesConverter(), Publisher.STOCKS_SUMMARY_ROUTING_KEY), null);
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

	public File getExchanges() {
		return null;
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

			BufferedReader reader = new BufferedReader(
					new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
			StringBuilder builder = new StringBuilder();

			for (String line = null; (line = reader.readLine()) != null;) {
				builder.append(line).append("\n");
			}
			System.out.println(builder);
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
			// yql.getHistData("YHOO", "2009-09-11", "2010-03-10", "json");
			yql.getStockSummaries("json");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
