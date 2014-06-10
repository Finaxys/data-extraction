/*
 * 
 */
package com.finaxys.rd.dataextraction.provider.impl.yahoo;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.nio.client.methods.HttpAsyncMethods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.finaxys.rd.dataextraction.helper.ProviderHelper;
import com.finaxys.rd.dataextraction.msg.Document;
import com.finaxys.rd.dataextraction.msg.Document.ContentType;
import com.finaxys.rd.dataextraction.msg.Document.DataClass;
import com.finaxys.rd.dataextraction.msg.Document.DataType;
import com.finaxys.rd.dataextraction.provider.StockQuoteProvider;
import com.finaxys.rd.dataextraction.provider.impl.HttpResponseConsumer;
import com.finaxys.rd.marketdataprovider.dao.StockDao;

// TODO: Auto-generated Javadoc
/**
 * The Class YahooStockQuoteProvider.
 */
public class YahooStockQuoteProvider implements StockQuoteProvider {

	/** The y provider symb. */
	@Value("${provider.yahoo.symbol:1}")
	public char Y_PROVIDER_SYMB;
	
	/** The yql default env. */
	@Value("${provider.yahoo.yqlDefaultEnv}")
	private String YQL_DEFAULT_ENV;
	
	/** The yql quote query. */
	@Value("${provider.yahoo.yqlQuoteQuery}")
	private String YQL_QUOTE_QUERY;

	/** The client. */
	@Autowired
	private CloseableHttpAsyncClient client;

	/** The stock dao. */
	@Autowired
	private StockDao stockDao;

	/**
	 * Instantiates a new yahoo stock quote provider.
	 */
	public YahooStockQuoteProvider() {
		super();
	}

	/**
	 * Instantiates a new yahoo stock quote provider.
	 *
	 * @param client the client
	 * @param stockDao the stock dao
	 */
	public YahooStockQuoteProvider(CloseableHttpAsyncClient client, StockDao stockDao) {
		super();
		this.client = client;
		this.stockDao = stockDao;
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

	/**
	 * Gets the stock dao.
	 *
	 * @return the stock dao
	 */
	public StockDao getStockDao() {
		return stockDao;
	}

	/**
	 * Sets the stock dao.
	 *
	 * @param stockDao the new stock dao
	 */
	public void setStockDao(StockDao stockDao) {
		this.stockDao = stockDao;
	}

	/* (non-Javadoc)
	 * @see com.finaxys.rd.dataextraction.provider.StockQuoteProvider#getCurrentStocksQuotes(com.finaxys.rd.dataextraction.msg.Document.ContentType, com.finaxys.rd.dataextraction.msg.Document.DataType)
	 */
	public List<Document> getCurrentStocksQuotes(ContentType format, DataType type) throws Exception {
		List<Document> list = new ArrayList<Document>();
		try {
			client.start();
			List<String> symbList = stockDao.listAllSymbols();
			String query = "";
			for (String symbs : symbList) {
				query = ProviderHelper.constructQuery(YQL_QUOTE_QUERY, symbs);
				URI uri = ProviderHelper.contructYqlUri(query, format, YQL_DEFAULT_ENV);
				File tfile = File.createTempFile("stocksQuotesTemp", "." + format);
				Future<File> future = client.execute(HttpAsyncMethods.createGet(uri), new HttpResponseConsumer(tfile),
						null);
				File f = future.get();
				if (f.length() > 0)
					list.add(new Document(format, type, DataClass.StockQuotes, Y_PROVIDER_SYMB, ProviderHelper
							.toBytes(f)));
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
//			client.close();
		}
	}

}