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
import com.finaxys.rd.dataextraction.provider.IndexQuoteProvider;
import com.finaxys.rd.dataextraction.provider.impl.HttpResponseConsumer;
import com.finaxys.rd.marketdataprovider.dao.IndexInfoDao;

// TODO: Auto-generated Javadoc
/**
 * The Class YahooIndexQuoteProvider.
 */
public class YahooIndexQuoteProvider implements IndexQuoteProvider {

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

	/** The index info dao. */
	@Autowired
	private IndexInfoDao  indexInfoDao ;
	
	/**
	 * Instantiates a new yahoo index quote provider.
	 */
	public YahooIndexQuoteProvider() {
		super();
	}
	
	/**
	 * Instantiates a new yahoo index quote provider.
	 *
	 * @param client the client
	 * @param indexInfoDao the index info dao
	 */
	public YahooIndexQuoteProvider(CloseableHttpAsyncClient client, IndexInfoDao indexInfoDao) {
		super();
		this.client = client;
		this.indexInfoDao = indexInfoDao;
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
	 * Gets the index info dao.
	 *
	 * @return the index info dao
	 */
	public IndexInfoDao getIndexInfoDao() {
		return indexInfoDao;
	}

	/**
	 * Sets the index info dao.
	 *
	 * @param indexInfoDao the new index info dao
	 */
	public void setIndexInfoDao(IndexInfoDao indexInfoDao) {
		this.indexInfoDao = indexInfoDao;
	}

	/* (non-Javadoc)
	 * @see com.finaxys.rd.dataextraction.provider.IndexQuoteProvider#getCurrentIndexQuotes(com.finaxys.rd.dataextraction.msg.Document.ContentType, com.finaxys.rd.dataextraction.msg.Document.DataType)
	 */
	public List<Document> getCurrentIndexQuotes(ContentType format, DataType type) throws Exception {
		List<Document> list = new ArrayList<Document>();
		try {
			client.start();
			List<String> symbList = indexInfoDao.listAllSymbols();
			String query = "";
			for (String symbs : symbList) {
				query = ProviderHelper.constructQuery(YQL_INDEX_QUOTES_QUERY, symbs);
				URI uri = ProviderHelper.contructYqlUri(query, format, YQL_DEFAULT_ENV);
				File tfile = File.createTempFile("indexQuotesTemp", "." + format);
				Future<File> future = client.execute(HttpAsyncMethods.createGet(uri), new HttpResponseConsumer(tfile),
						null);
				File f = future.get();
				if(f.length()>0)
				list.add(new Document(format, type, DataClass.IndexInfos, Y_PROVIDER_SYMB, ProviderHelper.toBytes(f)));
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
