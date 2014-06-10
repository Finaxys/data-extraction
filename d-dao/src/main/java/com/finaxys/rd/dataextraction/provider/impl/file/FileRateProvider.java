package com.finaxys.rd.dataextraction.provider.impl.file;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;

import com.finaxys.rd.dataextraction.helper.ProviderHelper;
import com.finaxys.rd.dataextraction.msg.Document;
import com.finaxys.rd.dataextraction.msg.Document.ContentType;
import com.finaxys.rd.dataextraction.msg.Document.DataClass;
import com.finaxys.rd.dataextraction.msg.Document.DataType;
import com.finaxys.rd.dataextraction.provider.RateProvider;
import com.finaxys.rd.dataextraction.provider.StockProvider;

public class FileRateProvider implements RateProvider {

	/** The logger. */
	static Logger logger = Logger.getLogger(StockProvider.class);
	
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
	public FileRateProvider() {
		super();
	}

	/* (non-Javadoc)
	 * @see com.finaxys.rd.dataextraction.provider.RateProvider#getRates(com.finaxys.rd.dataextraction.msg.Document.ContentType)
	 */
	public List<Document> getRates(ContentType format) throws Exception {
		List<Document> list = new ArrayList<Document>();
		File f = ProviderHelper.getResourceFile(ProviderHelper.getPath(DATA_FOLDER, RATES_FILE, format.getName()));
		list.add(new Document(format, DataType.Ref, DataClass.Stocks, FILE_PROVIDER_SYMB, ProviderHelper.toBytes(f)));
		return list;
	}

}
