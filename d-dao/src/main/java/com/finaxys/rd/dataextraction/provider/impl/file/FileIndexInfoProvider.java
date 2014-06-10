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
import com.finaxys.rd.dataextraction.provider.IndexInfoProvider;

// TODO: Auto-generated Javadoc
/**
 * The Class FileIndexInfoProvider.
 */
public class FileIndexInfoProvider implements IndexInfoProvider {
	
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
	public FileIndexInfoProvider() {
		super();
	}


	/* (non-Javadoc)
	 * @see com.finaxys.rd.dataextraction.provider.IndexInfoProvider#getIndexInfos(com.finaxys.rd.dataextraction.msg.Document.ContentType)
	 */
	public List<Document> getIndexInfos(ContentType format) throws Exception {
		List<Document> list = new ArrayList<Document>();
		File f = ProviderHelper.getResourceFile(ProviderHelper.getPath(DATA_FOLDER, INDEX_INFOS_FILE, format.getName()));
		list.add(new Document(format, DataType.Ref, DataClass.IndexInfos, FILE_PROVIDER_SYMB, ProviderHelper.toBytes(f)));
		return list;
	}

}
