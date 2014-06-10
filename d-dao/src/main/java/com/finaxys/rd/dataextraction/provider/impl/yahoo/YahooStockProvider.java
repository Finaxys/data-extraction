/*
 * 
 */
package com.finaxys.rd.dataextraction.provider.impl.yahoo;


import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.nio.client.methods.HttpAsyncMethods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.finaxys.rd.dataextraction.helper.ProviderHelper;
import com.finaxys.rd.dataextraction.msg.Document;
import com.finaxys.rd.dataextraction.msg.Document.ContentType;
import com.finaxys.rd.dataextraction.msg.Document.DataClass;
import com.finaxys.rd.dataextraction.msg.Document.DataType;
import com.finaxys.rd.dataextraction.provider.StockProvider;
import com.finaxys.rd.dataextraction.provider.impl.HttpResponseConsumer;

// TODO: Auto-generated Javadoc
/**
 * The Class YahooStockProvider.
 */
public class YahooStockProvider implements StockProvider {

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
	public YahooStockProvider() {
		super();
	}

	/**
	 * Instantiates a new yahoo stock provider.
	 *
	 * @param client the client
	 */
	public YahooStockProvider(CloseableHttpAsyncClient client) {
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


	/* (non-Javadoc)
	 * @see com.finaxys.rd.dataextraction.provider.StockProvider#getStocks(com.finaxys.rd.dataextraction.msg.Document.ContentType)
	 */
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
				list.add(new Document(format, DataType.Ref, DataClass.Stocks, Y_PROVIDER_SYMB, ProviderHelper.toBytes(f)));
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
//			client.close();
		}
	}

}
