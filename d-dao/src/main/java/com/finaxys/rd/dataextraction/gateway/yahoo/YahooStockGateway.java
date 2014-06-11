package com.finaxys.rd.dataextraction.gateway.yahoo;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.nio.client.methods.HttpAsyncMethods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.finaxys.rd.dataextraction.gateway.StockGateway;
import com.finaxys.rd.dataextraction.msg.Document.ContentType;

public class YahooStockGateway implements StockGateway {

	/** The y provider symb. */
	@Value("${provider.yahoo.symbol:1}")
	public char Y_PROVIDER_SYMB;
	
	/** The yql default env. */
	@Value("${provider.yahoo.yqlDefaultEnv}")
	private String YQL_DEFAULT_ENV;
	
	/** The yql stock query. */
	@Value("${provider.yahoo.yqlStockQuery}")
	private String YQL_STOCK_QUERY;

	/** The client. */
	@Autowired
	private CloseableHttpAsyncClient client;

	/**
	 * Instantiates a new yahoo stock provider.
	 */
	public YahooStockGateway() {
		super();
	}

	/**
	 * Instantiates a new yahoo stock provider.
	 *
	 * @param client the client
	 */
	public YahooStockGateway(CloseableHttpAsyncClient client) {
		super();
		this.client = client;
	}
	
	/**
	 * Gets the client.
	 *
	 * @return the client
	 */
	public CloseableHttpAsyncClient getClient() {
		return client;
	}

	/**
	 * Sets the client.
	 *
	 * @param client the new client
	 */
	public void setClient(CloseableHttpAsyncClient client) {
		this.client = client;
	}

	@Override
	public char getProviderSymb() {
		return Y_PROVIDER_SYMB;
	}
	
	@Override
	public File getStocks(ContentType format) {
		
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
			return f;
		} catch (URISyntaxException e) {
			e.printStackTrace();
			return null;
		}catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (InterruptedException e) {
			e.printStackTrace();
			return null;
		} catch (ExecutionException e) {
			e.printStackTrace();
			return null;
		} finally {
			// close() method call shutdown() method so we will not be able to
			// start it again with start() method
			// apache.googlesource.com/httpasyncclient/+/13388984c55daabdd2c3eb139809eeddc76da783/httpasyncclient/src/main/java/org/apache/http/impl/nio/client/InternalHttpAsyncClient.java
			// client.close();
		}
	}

}
