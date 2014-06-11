package com.finaxys.rd.dataextraction.gateway.file;

import java.io.File;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;

import com.finaxys.rd.dataextraction.gateway.RateGateway;
import com.finaxys.rd.dataextraction.helper.ProviderHelper;
import com.finaxys.rd.dataextraction.msg.Document.ContentType;

public class FileRateGateway implements RateGateway {

	/** The logger. */
	static Logger logger = Logger.getLogger(FileRateGateway.class);

	/** The file provider symb. */
	@Value("${provider.file.symbol:0}")
	public char FILE_PROVIDER_SYMB;

	/** The data folder. */
	@Value("${provider.file.dataFolder:home_data}")
	public String DATA_FOLDER;

	/** The stocks file. */
	@Value("${provider.fle.ratesFile:rates}")
	public String RATES_FILE;

	/**
	 * Instantiates a new file stock provider.
	 */
	public FileRateGateway() {
		super();
	}


	@Override
	public char getProviderSymb() {
		return FILE_PROVIDER_SYMB;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.finaxys.rd.dataextraction.provider.RateProvider#getRates(com.finaxys
	 * .rd.dataextraction.msg.Document.ContentType)
	 */
	public File getRates(ContentType format) {
		return ProviderHelper.getResourceFile(ProviderHelper.getPath(DATA_FOLDER, RATES_FILE, format.getName()));
	}


}
