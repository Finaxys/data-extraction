package provider.impl.file;


import helper.Helper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import msg.Document;
import msg.Document.ContentType;
import msg.Document.DataClass;
import msg.Document.DataType;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import provider.IndexInfoProvider;

public class FileIndexInfoProvider implements IndexInfoProvider {
	public static final char FILE_PROVIDER_SYMB = '0';
	static Logger logger = Logger.getLogger(IndexInfoProvider.class);
	public static final String HOME_DATA_FOLDER = "home_data";
	public static final String INDEX_INFOS_FILE = "index_infos";
	

	public FileIndexInfoProvider() {
		super();
	}


	public List<Document> getIndexInfos(ContentType format) throws Exception {
		List<Document> list = new ArrayList<Document>();
		File f = Helper.getFile(Helper.getPath(HOME_DATA_FOLDER, INDEX_INFOS_FILE, format.getName()));
		list.add(new Document(format, DataType.Ref, DataClass.IndexInfos, FILE_PROVIDER_SYMB, Helper.toBytes(f)));
		return list;
	}

}
