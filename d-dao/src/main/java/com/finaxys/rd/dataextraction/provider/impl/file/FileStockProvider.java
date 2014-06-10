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
import org.springframework.stereotype.Repository;

import com.finaxys.rd.dataextraction.helper.ProviderHelper;
import com.finaxys.rd.dataextraction.msg.Document;
import com.finaxys.rd.dataextraction.msg.Document.ContentType;
import com.finaxys.rd.dataextraction.msg.Document.DataClass;
import com.finaxys.rd.dataextraction.msg.Document.DataType;
import com.finaxys.rd.dataextraction.provider.StockProvider;

// TODO: Auto-generated Javadoc
/**
 * The Class FileStockProvider.
 */
public class FileStockProvider implements StockProvider {

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
	public FileStockProvider() {
		super();
	}

	/* (non-Javadoc)
	 * @see com.finaxys.rd.dataextraction.provider.StockProvider#getStocks(com.finaxys.rd.dataextraction.msg.Document.ContentType)
	 */
	public List<Document> getStocks(ContentType format) throws Exception {
		List<Document> list = new ArrayList<Document>();
		File f = ProviderHelper.getResourceFile(ProviderHelper.getPath(DATA_FOLDER, STOCKS_FILE, format.getName()));
		list.add(new Document(format, DataType.Ref, DataClass.Stocks, FILE_PROVIDER_SYMB, ProviderHelper.toBytes(f)));
		return list;
	}

}
