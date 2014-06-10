/*
 * 
 */
package com.finaxys.rd.dataextraction.provider.impl.file;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.finaxys.rd.dataextraction.helper.ProviderHelper;
import com.finaxys.rd.dataextraction.msg.Document;
import com.finaxys.rd.dataextraction.msg.Document.ContentType;
import com.finaxys.rd.dataextraction.msg.Document.DataClass;
import com.finaxys.rd.dataextraction.msg.Document.DataType;
import com.finaxys.rd.dataextraction.provider.ExchangeProvider;

// TODO: Auto-generated Javadoc
/**
 * The Class FileExchangeProvider.
 */
public class FileExchangeProvider implements ExchangeProvider {

	/** The logger. */
	static Logger logger = Logger.getLogger(ExchangeProvider.class);

	/** The file provider symb. */
	@Value("${provider.file.symbol:0}")
	public char FILE_PROVIDER_SYMB;
	
	/** The data folder. */
	@Value("${provider.file.dataFolder:home_data}")
	public String DATA_FOLDER;
	
	/** The exchanges file. */
	@Value("${provider.fle.exchangesFile:exchanges}")
	public String EXCHANGES_FILE;
	

	/**
	 * Instantiates a new file exchange provider.
	 */
	public FileExchangeProvider() {
		super();
	}



	/* (non-Javadoc)
	 * @see com.finaxys.rd.dataextraction.provider.ExchangeProvider#getExchanges(com.finaxys.rd.dataextraction.msg.Document.ContentType)
	 */
	public List<Document> getExchanges(ContentType format) throws Exception {
		List<Document> list = new ArrayList<Document>();
		String path =ProviderHelper.getPath(DATA_FOLDER, EXCHANGES_FILE, format.getName());
		File f = ProviderHelper.getResourceFile(path);
		list.add(new Document(format, DataType.Ref, DataClass.Exchanges, FILE_PROVIDER_SYMB, ProviderHelper.toBytes(f)));
		return list;

	}

}
