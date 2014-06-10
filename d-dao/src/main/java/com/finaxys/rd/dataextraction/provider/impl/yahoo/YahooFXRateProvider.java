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
import com.finaxys.rd.dataextraction.provider.FXRateProvider;
import com.finaxys.rd.dataextraction.provider.impl.HttpResponseConsumer;
import com.finaxys.rd.marketdataprovider.dao.CurrencyPairDao;

// TODO: Auto-generated Javadoc
/**
 * The Class YahooFXRateProvider.
 */
public class YahooFXRateProvider implements FXRateProvider {

	/** The y provider symb. */
	@Value("${provider.yahoo.symbol:1}")
	public char Y_PROVIDER_SYMB;
	
	/** The yql default env. */
	@Value("${provider.yahoo.yqlDefaultEnv}")
	private String YQL_DEFAULT_ENV;
	
	/** The yql fxrate query. */
	@Value("${provider.yahoo.yqlFXRrateQuery}")
	private String YQL_FXRATE_QUERY;

	/** The client. */
	@Autowired
	private CloseableHttpAsyncClient client;

	/** The currency pair dao. */
	@Autowired
	private CurrencyPairDao currencyPairDao;

	/**
	 * Instantiates a new yahoo fx rate provider.
	 */
	public YahooFXRateProvider() {
		super();
	}

	/**
	 * Instantiates a new yahoo fx rate provider.
	 *
	 * @param client the client
	 * @param currencyPairDao the currency pair dao
	 */
	public YahooFXRateProvider(CloseableHttpAsyncClient client, CurrencyPairDao currencyPairDao) {
		super();
		this.client = client;
		this.currencyPairDao = currencyPairDao;
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
	 * Gets the currency pair dao.
	 *
	 * @return the currency pair dao
	 */
	public CurrencyPairDao getCurrencyPairDao() {
		return currencyPairDao;
	}

	/**
	 * Sets the currency pair dao.
	 *
	 * @param currencyPairDao the new currency pair dao
	 */
	public void setCurrencyPairDao(CurrencyPairDao currencyPairDao) {
		this.currencyPairDao = currencyPairDao;
	}

	/* (non-Javadoc)
	 * @see com.finaxys.rd.dataextraction.provider.FXRateProvider#getCurrentFXRates(com.finaxys.rd.dataextraction.msg.Document.ContentType, com.finaxys.rd.dataextraction.msg.Document.DataType)
	 */
	public List<Document> getCurrentFXRates(ContentType format, DataType type) throws Exception {
		List<Document> list = new ArrayList<Document>();
		try {
			List<String> symbList = currencyPairDao.listAllSymbols();
			String query = "";
			for (String symbs : symbList) {
				query = ProviderHelper.constructQuery(YQL_FXRATE_QUERY, symbs);
				URI uri = ProviderHelper.contructYqlUri(query, format, YQL_DEFAULT_ENV);
				File tfile = File.createTempFile("fxRatesTemp", "." + format);
				if (!client.isRunning())
					client.start();
				Future<File> future = client.execute(HttpAsyncMethods.createGet(uri), new HttpResponseConsumer(tfile),
						null);
				File f = future.get();
				if (f.length() > 0)
					list.add(new Document(format, type, DataClass.FXRates, Y_PROVIDER_SYMB, ProviderHelper.toBytes(f)));
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			// close() method call shutdown() method so we will not be able to
			// start it again with start() method
			// apache.googlesource.com/httpasyncclient/+/13388984c55daabdd2c3eb139809eeddc76da783/httpasyncclient/src/main/java/org/apache/http/impl/nio/client/InternalHttpAsyncClient.java
//			 client.close();
		}
	}

}
