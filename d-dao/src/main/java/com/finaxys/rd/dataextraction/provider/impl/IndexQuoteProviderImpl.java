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

import com.finaxys.rd.dataextraction.gateway.IndexQuoteGateway;
import com.finaxys.rd.dataextraction.gateway.yahoo.HttpResponseConsumer;
import com.finaxys.rd.dataextraction.helper.ProviderHelper;
import com.finaxys.rd.dataextraction.msg.Document;
import com.finaxys.rd.dataextraction.msg.Document.ContentType;
import com.finaxys.rd.dataextraction.msg.Document.DataClass;
import com.finaxys.rd.dataextraction.msg.Document.DataType;
import com.finaxys.rd.dataextraction.provider.IndexQuoteProvider;
import com.finaxys.rd.marketdataprovider.dao.IndexInfoDao;

public class IndexQuoteProviderImpl implements IndexQuoteProvider {

	private IndexQuoteGateway gateway;

	/** The index info dao. */
	private IndexInfoDao indexInfoDao;

	/**
	 * Instantiates a new yahoo index quote provider.
	 */
	public IndexQuoteProviderImpl() {
		super();
	}

	public IndexQuoteProviderImpl(IndexQuoteGateway gateway, IndexInfoDao indexInfoDao) {
		super();
		this.gateway = gateway;
		this.indexInfoDao = indexInfoDao;
	}

	public IndexQuoteGateway getGateway() {
		return gateway;
	}

	public void setGateway(IndexQuoteGateway gateway) {
		this.gateway = gateway;
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
	 * @param indexInfoDao
	 *            the new index info dao
	 */
	public void setIndexInfoDao(IndexInfoDao indexInfoDao) {
		this.indexInfoDao = indexInfoDao;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.finaxys.rd.dataextraction.provider.IndexQuoteProvider#
	 * getCurrentIndexQuotes
	 * (com.finaxys.rd.dataextraction.msg.Document.ContentType,
	 * com.finaxys.rd.dataextraction.msg.Document.DataType)
	 */
	public List<Document> getCurrentIndexQuotes(ContentType format, DataType type) throws Exception {
		List<Document> list = new ArrayList<Document>();

		List<String> symbList = indexInfoDao.listAllSymbols();
		for (String symbs : symbList) {
			File f = gateway.getCurrentIndexQuotes(format, type, symbs);
			if (f.length() > 0)
				list.add(new Document(format, type, DataClass.IndexInfos, gateway.getProviderSymb(), ProviderHelper
						.toBytes(f)));
		}
		return list;

	}

}
