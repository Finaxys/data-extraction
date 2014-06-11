package com.finaxys.rd.dataextraction.gateway.file;

import java.io.File;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;

import com.finaxys.rd.dataextraction.gateway.ExchangeGateway;
import com.finaxys.rd.dataextraction.helper.ProviderHelper;
import com.finaxys.rd.dataextraction.msg.Document.ContentType;
import com.finaxys.rd.dataextraction.provider.ExchangeProvider;

public class FileExchangeGateway implements ExchangeGateway {

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
	public FileExchangeGateway() {
		super();
	}

	@Override
	public char getProviderSymb() {
		return FILE_PROVIDER_SYMB;
	}
	
	@Override
	public File getExchanges(ContentType format) {
		return ProviderHelper.getResourceFile(ProviderHelper.getPath(DATA_FOLDER, EXCHANGES_FILE, format.getName()));
	}

}
