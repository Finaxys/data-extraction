package com.finaxys.rd.dataextraction.gateway.file;

import java.io.File;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;

import com.finaxys.rd.dataextraction.gateway.CurrencyPairGateway;
import com.finaxys.rd.dataextraction.helper.ProviderHelper;
import com.finaxys.rd.dataextraction.msg.Document.ContentType;
import com.finaxys.rd.dataextraction.provider.CurrencyPairProvider;

public class FileCurrencyPairGateway implements CurrencyPairGateway {

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

	// private final char FILE_PROVIDER_SYMB;
	// private final String DATA_FOLDER;
	// private final String CURRENCY_PAIRS_FILE;
	//
	//
	//
	//
	// public FileCurrencyPairGateway(final @Value("${provider.file.symbol:0}")
	// char fILE_PROVIDER_SYMB,final @Value("${provider.file.symbol:0}") String
	// dATA_FOLDER,final @Value("${provider.file.symbol:0}") String
	// cURRENCY_PAIRS_FILE) {
	// super();
	// FILE_PROVIDER_SYMB = fILE_PROVIDER_SYMB;
	// DATA_FOLDER = dATA_FOLDER;
	// CURRENCY_PAIRS_FILE = cURRENCY_PAIRS_FILE;
	// }

	/**
	 * Instantiates a new file currency pair provider.
	 */
	public FileCurrencyPairGateway() {
		super();
	}


	@Override
	public char getProviderSymb() {
		return FILE_PROVIDER_SYMB;
	}
	
	@Override
	public File getCurrencyPairs(ContentType format) {
		return ProviderHelper
				.getResourceFile(ProviderHelper.getPath(DATA_FOLDER, CURRENCY_PAIRS_FILE, format.getName()));
	}



}
