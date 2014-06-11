package com.finaxys.rd.dataextraction.gateway.file;

import java.io.File;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;

import com.finaxys.rd.dataextraction.gateway.IndexInfoGateway;
import com.finaxys.rd.dataextraction.helper.ProviderHelper;
import com.finaxys.rd.dataextraction.msg.Document.ContentType;
import com.finaxys.rd.dataextraction.provider.IndexInfoProvider;

public class FileIndexInfoGateway implements IndexInfoGateway{

	/** The logger. */
	static Logger logger = Logger.getLogger(IndexInfoProvider.class);
	
	/** The file provider symb. */
	@Value("${provider.file.symbol:0}")
	public char FILE_PROVIDER_SYMB;
	
	/** The data folder. */
	@Value("${provider.file.dataFolder:home_data}")
	public String DATA_FOLDER;
	
	/** The index infos file. */
	@Value("${provider.fle.indexInfosFile:index_infos}")
	public String INDEX_INFOS_FILE;
	

	/**
	 * Instantiates a new file index info provider.
	 */
	public FileIndexInfoGateway() {
		super();
	}

	@Override
	public char getProviderSymb() {
		return FILE_PROVIDER_SYMB;
	}
	
	@Override
	public File getIndexInfos(ContentType format) {
		return ProviderHelper.getResourceFile(ProviderHelper.getPath(DATA_FOLDER, INDEX_INFOS_FILE, format.getName()));
		
	}

}
