/*
 * 
 */
package com.finaxys.rd.dataextraction.provider.impl.file;


import java.io.File;
import java.io.IOException;
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
import com.finaxys.rd.dataextraction.provider.CurrencyPairProvider;

// TODO: Auto-generated Javadoc
/**
 * The Class FileCurrencyPairProvider.
 */
public class FileCurrencyPairProvider implements CurrencyPairProvider {

	/** The logger. */
	static Logger logger = Logger.getLogger(CurrencyPairProvider.class);
	
	/** The file provider symb. */
	@Value("${provider.file.symbol:0}")
	private char FILE_PROVIDER_SYMB;
	
	/** The data folder. */
	@Value("${provider.file.dataFolder:home_data}")
	private String DATA_FOLDER;
	
	/** The currency pairs file. */
	@Value("${provider.file.currencyPairsFile:currency_pairs}")
	private String CURRENCY_PAIRS_FILE;
	
//	private final char FILE_PROVIDER_SYMB;
//	private final String DATA_FOLDER;
//	private final String CURRENCY_PAIRS_FILE;
//	
//
//
//
//	public FileCurrencyPairProvider(final @Value("${provider.file.symbol:0}") char fILE_PROVIDER_SYMB,final @Value("${provider.file.symbol:0}") String dATA_FOLDER,final @Value("${provider.file.symbol:0}") String cURRENCY_PAIRS_FILE) {
//		super();
//		FILE_PROVIDER_SYMB = fILE_PROVIDER_SYMB;
//		DATA_FOLDER = dATA_FOLDER;
//		CURRENCY_PAIRS_FILE = cURRENCY_PAIRS_FILE;
//	}

	/**
 * Instantiates a new file currency pair provider.
 */
public FileCurrencyPairProvider() {
		super();
	}

	/* (non-Javadoc)
	 * @see com.finaxys.rd.dataextraction.provider.CurrencyPairProvider#getCurrencyPairs(com.finaxys.rd.dataextraction.msg.Document.ContentType)
	 */
	public List<Document> getCurrencyPairs(ContentType format) throws IOException {
		List<Document> list = new ArrayList<Document>();
		File f = ProviderHelper.getResourceFile(ProviderHelper.getPath(DATA_FOLDER, CURRENCY_PAIRS_FILE, format.getName()));
		list.add(new Document(format, DataType.Ref, DataClass.CurrencyPairs, FILE_PROVIDER_SYMB, ProviderHelper.toBytes(f)));
		return list;
	}

}
