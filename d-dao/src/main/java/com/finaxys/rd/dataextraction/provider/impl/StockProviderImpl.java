/*
 * 
 */
package com.finaxys.rd.dataextraction.provider.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.finaxys.rd.dataextraction.gateway.StockGateway;
import com.finaxys.rd.dataextraction.helper.ProviderHelper;
import com.finaxys.rd.dataextraction.msg.Document;
import com.finaxys.rd.dataextraction.msg.Document.ContentType;
import com.finaxys.rd.dataextraction.msg.Document.DataClass;
import com.finaxys.rd.dataextraction.msg.Document.DataType;
import com.finaxys.rd.dataextraction.provider.StockProvider;

public class StockProviderImpl implements StockProvider {

	/** The logger. */
	static Logger logger = Logger.getLogger(StockProvider.class);
	
	private StockGateway gateway;

	/**
	 * Instantiates a new file stock provider.
	 */
	public StockProviderImpl() {
		super();
	}

	public StockProviderImpl(StockGateway gateway) {
		super();
		this.gateway = gateway;
	}

	public StockGateway getGateway() {
		return gateway;
	}

	public void setGateway(StockGateway gateway) {
		this.gateway = gateway;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.finaxys.rd.dataextraction.provider.StockProvider#getStocks(com.finaxys
	 * .rd.dataextraction.msg.Document.ContentType)
	 */
	public List<Document> getStocks(ContentType format) throws Exception {
		List<Document> list = new ArrayList<Document>();
		File f = gateway.getStocks(format);
		list.add(new Document(format, DataType.Ref, DataClass.Stocks, gateway.getProviderSymb(), ProviderHelper.toBytes(f)));
		return list;
	}

}
