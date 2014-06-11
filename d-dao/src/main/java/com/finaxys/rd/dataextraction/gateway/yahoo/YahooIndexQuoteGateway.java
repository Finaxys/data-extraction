package com.finaxys.rd.dataextraction.gateway.yahoo;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.nio.client.methods.HttpAsyncMethods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.finaxys.rd.dataextraction.gateway.IndexQuoteGateway;
import com.finaxys.rd.dataextraction.helper.ProviderHelper;
import com.finaxys.rd.dataextraction.msg.Document.ContentType;
import com.finaxys.rd.dataextraction.msg.Document.DataType;

public class YahooIndexQuoteGateway implements IndexQuoteGateway {

	/** The y provider symb. */
	@Value("${provider.yahoo.symbol:1}")
	public char Y_PROVIDER_SYMB;

	/** The yql default env. */
	@Value("${provider.yahoo.yqlDefaultEnv}")
	private String YQL_DEFAULT_ENV;

	/** The yql index quotes query. */
	@Value("${provider.yahoo.yqlIndexQuotesQuery}")
	private String YQL_INDEX_QUOTES_QUERY;

	/** The client. */
	@Autowired
	private CloseableHttpAsyncClient client;

	/**
	 * Instantiates a new yahoo index quote provider.
	 */
	public YahooIndexQuoteGateway() {
		super();
	}

	/**
	 * Instantiates a new yahoo index quote provider.
	 * 
	 * @param client
	 *            the client
	 * @param indexInfoDao
	 *            the index info dao
	 */
	public YahooIndexQuoteGateway(CloseableHttpAsyncClient client) {
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
	 * @param client
	 *            the new client
	 */
	public void setClient(CloseableHttpAsyncClient client) {
		this.client = client;
	}

	@Override
	public char getProviderSymb() {
		return Y_PROVIDER_SYMB;
	}
	
	
	@Override
	public File getCurrentIndexQuotes(ContentType format, DataType type, String symbs) {

		try {
			client.start();
			String query = ProviderHelper.constructQuery(YQL_INDEX_QUOTES_QUERY, symbs);
			URI uri = ProviderHelper.contructYqlUri(query, format, YQL_DEFAULT_ENV);
			File tfile = File.createTempFile("indexQuotesTemp", "." + format);
			Future<File> future = client
					.execute(HttpAsyncMethods.createGet(uri), new HttpResponseConsumer(tfile), null);
			File f = future.get();
			return f;

		} catch (URISyntaxException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (InterruptedException e) {
			e.printStackTrace();
			return null;
		} catch (ExecutionException e) {
			e.printStackTrace();
			return null;
		} finally {
			// client.close();
		}
	}

}
