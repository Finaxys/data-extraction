package com.finaxys.rd.dataextraction.dao.integration.yahoo;

import java.io.File;
import java.net.URI;
import java.util.concurrent.Future;

import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.nio.client.methods.HttpAsyncMethods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.finaxys.rd.dataextraction.dao.integration.StockQuoteGateway;
import com.finaxys.rd.dataextraction.domain.msg.Document;
import com.finaxys.rd.dataextraction.domain.msg.Document.ContentType;
import com.finaxys.rd.dataextraction.domain.msg.Document.DataClass;
import com.finaxys.rd.dataextraction.domain.msg.Document.DataType;
import com.finaxys.rd.dataextraction.helper.GatewayHelper;

public class YahooStockQuoteGateway implements StockQuoteGateway {

	/** The y provider symb. */
	@Value("${gateway.yahoo.symbol:1}")
	public char Y_PROVIDER_SYMB;

	/** The yql default env. */
	@Value("${gateway.yahoo.yqlDefaultEnv}")
	private String YQL_DEFAULT_ENV;

	/** The yql quote query. */
	@Value("${gateway.yahoo.yqlQuoteQuery}")
	private String YQL_QUOTE_QUERY;

	/** The client. */
	@Autowired
	private CloseableHttpAsyncClient client;

	/** The content type. */
	private ContentType contentType;

	public YahooStockQuoteGateway(ContentType contentType) {
		super();
		this.contentType = contentType;
	}

	public YahooStockQuoteGateway(CloseableHttpAsyncClient client, ContentType contentType) {
		super();
		this.client = client;
		this.contentType = contentType;
	}

	public ContentType getContentType() {
		return contentType;
	}

	public void setContentType(ContentType contentType) {
		this.contentType = contentType;
	}

	/**
	 * Instantiates a new yahoo stock quote gateway.
	 * 
	 * @param client
	 *            the client
	 */
	public YahooStockQuoteGateway(CloseableHttpAsyncClient client) {
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

	public Document getCurrentStocksQuotes( String symbols) throws Exception {
		try {
			client.start();
			String query = GatewayHelper.constructQuery(YQL_QUOTE_QUERY, symbols);
			URI uri = GatewayHelper.contructYqlUri(query, contentType, YQL_DEFAULT_ENV);
			File tFile = File.createTempFile("stocksQuotesTemp", "." + contentType);
			Future<File> future = client
					.execute(HttpAsyncMethods.createGet(uri), new HttpResponseConsumer(tFile), null);
			File file = future.get();
			if (file != null && file.length() > 0)
				return new Document(contentType, DataType.INTRA, DataClass.StockQuote, Y_PROVIDER_SYMB, GatewayHelper.toBytes(file));

			else
				return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			// client.close();
		}
	}

}