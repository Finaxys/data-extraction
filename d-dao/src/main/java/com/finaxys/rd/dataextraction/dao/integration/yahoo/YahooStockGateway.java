package com.finaxys.rd.dataextraction.dao.integration.yahoo;

import java.io.File;
import java.net.URI;
import java.util.concurrent.Future;

import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.nio.client.methods.HttpAsyncMethods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.finaxys.rd.dataextraction.dao.integration.StockGateway;
import com.finaxys.rd.dataextraction.domain.msg.Document;
import com.finaxys.rd.dataextraction.domain.msg.Document.ContentType;
import com.finaxys.rd.dataextraction.domain.msg.Document.DataClass;
import com.finaxys.rd.dataextraction.domain.msg.Document.DataType;
import com.finaxys.rd.dataextraction.helper.GatewayHelper;

public class YahooStockGateway implements StockGateway {

	/** The y provider symb. */
	@Value("${gateway.yahoo.symbol:1}")
	public char Y_PROVIDER_SYMB;

	/** The yql default env. */
	@Value("${gateway.yahoo.yqlDefaultEnv}")
	private String YQL_DEFAULT_ENV;

	/** The yql stock query. */
	@Value("${gateway.yahoo.yqlStockQuery}")
	private String YQL_STOCK_QUERY;

	/** The client. */
	@Autowired
	private CloseableHttpAsyncClient client;

	/** The content type. */
	private ContentType contentType;


	public YahooStockGateway(ContentType contentType) {
		super();
		this.contentType = contentType;
	}

	public YahooStockGateway(CloseableHttpAsyncClient client, ContentType contentType) {
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
	 * Instantiates a new yahoo stock gateway.
	 * 
	 * @param client
	 *            the client
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
	 * @param client
	 *            the new client
	 */
	public void setClient(CloseableHttpAsyncClient client) {
		this.client = client;
	}

	public Document getStocks() throws Exception {
		try {
			client.start();
			// URI uri = helper.contructYqlUri(YQL_STOCK_QUERY, format,
			// YQL_DEFAULT_ENV);
			URI uri = new URIBuilder()
					.setScheme("https")
					.setHost(
							"query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.stocks%20where%20symbol%20in%20(select%20company.symbol%20from%20yahoo.finance.industry%20where%20id%20in%20(select%20industry.id%20from%20yahoo.finance.sectors))&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys")
					.build();
			File tFile = File.createTempFile("stocksTemp", "." + contentType);
			Future<File> future = client
					.execute(HttpAsyncMethods.createGet(uri), new HttpResponseConsumer(tFile), null);
			File file = future.get();
			if (file != null && file.length() > 0)
				return new Document(contentType, DataType.Ref, DataClass.Stock, Y_PROVIDER_SYMB,
						GatewayHelper.toBytes(file));
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
