/*
 * 
 */
package com.finaxys.rd.dataextraction.provider.impl;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.nio.client.methods.HttpAsyncMethods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.finaxys.rd.dataextraction.gateway.StockQuoteGateway;
import com.finaxys.rd.dataextraction.gateway.yahoo.HttpResponseConsumer;
import com.finaxys.rd.dataextraction.helper.ProviderHelper;
import com.finaxys.rd.dataextraction.msg.Document;
import com.finaxys.rd.dataextraction.msg.Document.ContentType;
import com.finaxys.rd.dataextraction.msg.Document.DataClass;
import com.finaxys.rd.dataextraction.msg.Document.DataType;
import com.finaxys.rd.dataextraction.provider.StockQuoteProvider;
import com.finaxys.rd.marketdataprovider.dao.StockDao;


public class StockQuoteProviderImpl implements StockQuoteProvider {

	private StockQuoteGateway gateway;
	/** The stock dao. */
	private StockDao stockDao;

	/**
	 * Instantiates a new yahoo stock quote provider.
	 */
	public StockQuoteProviderImpl() {
		super();
	}

	public StockQuoteProviderImpl(StockQuoteGateway gateway, StockDao stockDao) {
		super();
		this.gateway = gateway;
		this.stockDao = stockDao;
	}

	public StockQuoteGateway getGateway() {
		return gateway;
	}

	public void setGateway(StockQuoteGateway gateway) {
		this.gateway = gateway;
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
	 * @param stockDao
	 *            the new stock dao
	 */
	public void setStockDao(StockDao stockDao) {
		this.stockDao = stockDao;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.finaxys.rd.dataextraction.provider.StockQuoteProvider#
	 * getCurrentStocksQuotes
	 * (com.finaxys.rd.dataextraction.msg.Document.ContentType,
	 * com.finaxys.rd.dataextraction.msg.Document.DataType)
	 */
	public List<Document> getCurrentStocksQuotes(ContentType format, DataType type) throws Exception {
		List<Document> list = new ArrayList<Document>();

		List<String> symbList = stockDao.listAllSymbols();

		for (String symbs : symbList) {
			File f = gateway.getCurrentStocksQuotes(format, type, symbs);
			if (f.length() > 0)
				list.add(new Document(format, type, DataClass.StockQuotes, gateway.getProviderSymb(), ProviderHelper
						.toBytes(f)));
		}
		return list;

	}

}