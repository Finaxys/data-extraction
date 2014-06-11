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

import com.finaxys.rd.dataextraction.gateway.ExchangeGateway;
import com.finaxys.rd.dataextraction.helper.ProviderHelper;
import com.finaxys.rd.dataextraction.msg.Document;
import com.finaxys.rd.dataextraction.msg.Document.ContentType;
import com.finaxys.rd.dataextraction.msg.Document.DataClass;
import com.finaxys.rd.dataextraction.msg.Document.DataType;
import com.finaxys.rd.dataextraction.provider.ExchangeProvider;

public class ExchangeProviderImpl implements ExchangeProvider {

	/** The logger. */
	static Logger logger = Logger.getLogger(ExchangeProvider.class);

	private ExchangeGateway gateway;

	/**
	 * Instantiates a new file exchange provider.
	 */
	public ExchangeProviderImpl() {
		super();
	}



	public ExchangeProviderImpl(ExchangeGateway gateway) {
		super();
		this.gateway = gateway;
	}



	public ExchangeGateway getGateway() {
		return gateway;
	}



	public void setGateway(ExchangeGateway gateway) {
		this.gateway = gateway;
	}



	/* (non-Javadoc)
	 * @see com.finaxys.rd.dataextraction.provider.ExchangeProvider#getExchanges(com.finaxys.rd.dataextraction.msg.Document.ContentType)
	 */
	public List<Document> getExchanges(ContentType format) throws Exception {
		List<Document> list = new ArrayList<Document>();
		File f = gateway.getExchanges(format);
		list.add(new Document(format, DataType.Ref, DataClass.Exchanges, gateway.getProviderSymb(), ProviderHelper.toBytes(f)));
		return list;

	}

}
