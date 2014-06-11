/*
 * 
 */
package com.finaxys.rd.dataextraction.provider.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.finaxys.rd.dataextraction.gateway.CurrencyPairGateway;
import com.finaxys.rd.dataextraction.helper.ProviderHelper;
import com.finaxys.rd.dataextraction.msg.Document;
import com.finaxys.rd.dataextraction.msg.Document.ContentType;
import com.finaxys.rd.dataextraction.msg.Document.DataClass;
import com.finaxys.rd.dataextraction.msg.Document.DataType;
import com.finaxys.rd.dataextraction.provider.CurrencyPairProvider;
import com.finaxys.rd.dataextraction.provider.ExchangeProvider;


public class CurrencyPairProviderImpl implements CurrencyPairProvider {

	/** The logger. */
	static Logger logger = Logger.getLogger(CurrencyPairProvider.class);

	private CurrencyPairGateway gateway;

	/**
	 * Instantiates a new file currency pair provider.
	 */
	public CurrencyPairProviderImpl() {
		super();
	}

	public CurrencyPairProviderImpl(CurrencyPairGateway gateway) {
		super();
		this.gateway = gateway;
	}

	public CurrencyPairGateway getGateway() {
		return gateway;
	}

	public void setGateway(CurrencyPairGateway gateway) {
		this.gateway = gateway;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.finaxys.rd.dataextraction.provider.CurrencyPairProvider#getCurrencyPairs
	 * (com.finaxys.rd.dataextraction.msg.Document.ContentType)
	 */
	public List<Document> getCurrencyPairs(ContentType format) throws IOException {
		List<Document> list = new ArrayList<Document>();
		File f = gateway.getCurrencyPairs(format);
		list.add(new Document(format, DataType.Ref, DataClass.CurrencyPairs, gateway.getProviderSymb(), ProviderHelper
				.toBytes(f)));
		return list;
	}

}
