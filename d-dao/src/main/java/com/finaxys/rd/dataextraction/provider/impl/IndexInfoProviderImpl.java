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

import com.finaxys.rd.dataextraction.gateway.IndexInfoGateway;
import com.finaxys.rd.dataextraction.helper.ProviderHelper;
import com.finaxys.rd.dataextraction.msg.Document;
import com.finaxys.rd.dataextraction.msg.Document.ContentType;
import com.finaxys.rd.dataextraction.msg.Document.DataClass;
import com.finaxys.rd.dataextraction.msg.Document.DataType;
import com.finaxys.rd.dataextraction.provider.IndexInfoProvider;


public class IndexInfoProviderImpl implements IndexInfoProvider {
	
	/** The logger. */
	static Logger logger = Logger.getLogger(IndexInfoProvider.class);
	
	private IndexInfoGateway gateway;
	

	/**
	 * Instantiates a new file index info provider.
	 */
	public IndexInfoProviderImpl() {
		super();
	}


	public IndexInfoProviderImpl(IndexInfoGateway gateway) {
		super();
		this.gateway = gateway;
	}


	public IndexInfoGateway getGateway() {
		return gateway;
	}


	public void setGateway(IndexInfoGateway gateway) {
		this.gateway = gateway;
	}


	/* (non-Javadoc)
	 * @see com.finaxys.rd.dataextraction.provider.IndexInfoProvider#getIndexInfos(com.finaxys.rd.dataextraction.msg.Document.ContentType)
	 */
	public List<Document> getIndexInfos(ContentType format) throws Exception {
		List<Document> list = new ArrayList<Document>();
		File f =gateway.getIndexInfos(format);
		list.add(new Document(format, DataType.Ref, DataClass.IndexInfos, gateway.getProviderSymb(), ProviderHelper.toBytes(f)));
		return list;
	}

}
