package com.finaxys.rd.dataextraction.gateway.file;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;

import com.finaxys.rd.dataextraction.gateway.StockGateway;
import com.finaxys.rd.dataextraction.helper.ProviderHelper;
import com.finaxys.rd.dataextraction.msg.Document;
import com.finaxys.rd.dataextraction.msg.Document.ContentType;
import com.finaxys.rd.dataextraction.msg.Document.DataClass;
import com.finaxys.rd.dataextraction.msg.Document.DataType;
import com.finaxys.rd.dataextraction.provider.StockProvider;

public class FileStockGateway implements StockGateway {

	/** The logger. */
	static Logger logger = Logger.getLogger(StockProvider.class);
	
	/** The file provider symb. */
	@Value("${provider.file.symbol:0}")
	public char FILE_PROVIDER_SYMB;
	
	/** The data folder. */
	@Value("${provider.file.dataFolder:home_data}")
	public String DATA_FOLDER;
	
	/** The stocks file. */
	@Value("${provider.fle.stocksFile:stocks}")
	public String STOCKS_FILE;

	/**
	 * Instantiates a new file stock provider.
	 */
	public FileStockGateway() {
		super();
	}
	
	@Override
	public char getProviderSymb() {
		return FILE_PROVIDER_SYMB;
	}

	@Override
	public File getStocks(ContentType format) {
		return ProviderHelper.getResourceFile(ProviderHelper.getPath(DATA_FOLDER, STOCKS_FILE, format.getName()));
	}

}
