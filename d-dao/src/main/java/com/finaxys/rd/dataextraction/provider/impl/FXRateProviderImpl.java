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

import com.finaxys.rd.dataextraction.gateway.FXRateGateway;
import com.finaxys.rd.dataextraction.gateway.yahoo.HttpResponseConsumer;
import com.finaxys.rd.dataextraction.helper.ProviderHelper;
import com.finaxys.rd.dataextraction.msg.Document;
import com.finaxys.rd.dataextraction.msg.Document.ContentType;
import com.finaxys.rd.dataextraction.msg.Document.DataClass;
import com.finaxys.rd.dataextraction.msg.Document.DataType;
import com.finaxys.rd.dataextraction.provider.FXRateProvider;
import com.finaxys.rd.marketdataprovider.dao.CurrencyPairDao;


public class FXRateProviderImpl implements FXRateProvider {

	private FXRateGateway gateway;

	/** The currency pair dao. */
	private CurrencyPairDao currencyPairDao;

	/**
	 * Instantiates a new yahoo fx rate provider.
	 */
	public FXRateProviderImpl() {
		super();
	}

	public FXRateProviderImpl(FXRateGateway gateway, CurrencyPairDao currencyPairDao) {
		super();
		this.gateway = gateway;
		this.currencyPairDao = currencyPairDao;
	}

	public FXRateGateway getGateway() {
		return gateway;
	}

	public void setGateway(FXRateGateway gateway) {
		this.gateway = gateway;
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
	 * @param currencyPairDao
	 *            the new currency pair dao
	 */
	public void setCurrencyPairDao(CurrencyPairDao currencyPairDao) {
		this.currencyPairDao = currencyPairDao;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.finaxys.rd.dataextraction.provider.FXRateProvider#getCurrentFXRates
	 * (com.finaxys.rd.dataextraction.msg.Document.ContentType,
	 * com.finaxys.rd.dataextraction.msg.Document.DataType)
	 */
	public List<Document> getFXRates(ContentType format, DataType type) throws Exception {
		List<Document> list = new ArrayList<Document>();
		List<String> symbList = currencyPairDao.listAllSymbols();
		for (String symbs : symbList) {
			File f = gateway.getFXRates(format, symbs);
			if (f.length() > 0)
				list.add(new Document(format, type, DataClass.FXRates, gateway.getProviderSymb(), ProviderHelper
						.toBytes(f)));
		}
		return list;
	}

}
