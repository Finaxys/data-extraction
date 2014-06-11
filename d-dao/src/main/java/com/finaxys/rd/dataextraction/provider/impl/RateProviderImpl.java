package com.finaxys.rd.dataextraction.provider.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;

import com.finaxys.rd.dataextraction.gateway.CurrencyPairGateway;
import com.finaxys.rd.dataextraction.gateway.RateGateway;
import com.finaxys.rd.dataextraction.helper.ProviderHelper;
import com.finaxys.rd.dataextraction.msg.Document;
import com.finaxys.rd.dataextraction.msg.Document.ContentType;
import com.finaxys.rd.dataextraction.msg.Document.DataClass;
import com.finaxys.rd.dataextraction.msg.Document.DataType;
import com.finaxys.rd.dataextraction.provider.RateProvider;
import com.finaxys.rd.dataextraction.provider.StockProvider;

public class RateProviderImpl implements RateProvider {

	/** The logger. */
	static Logger logger = Logger.getLogger(StockProvider.class);
	

	private RateGateway gateway;
	/**
	 * Instantiates a new file stock provider.
	 */
	public RateProviderImpl() {
		super();
	}

	public RateProviderImpl(RateGateway gateway) {
		super();
		this.gateway = gateway;
	}

	public RateGateway getGateway() {
		return gateway;
	}

	public void setGateway(RateGateway gateway) {
		this.gateway = gateway;
	}

	/* (non-Javadoc)
	 * @see com.finaxys.rd.dataextraction.provider.RateProvider#getRates(com.finaxys.rd.dataextraction.msg.Document.ContentType)
	 */
	public List<Document> getRates(ContentType format) throws Exception {
		List<Document> list = new ArrayList<Document>();
		File f = gateway.getRates(format);
		list.add(new Document(format, DataType.Ref, DataClass.Stocks, gateway.getProviderSymb(), ProviderHelper.toBytes(f)));
		return list;
	}

}
