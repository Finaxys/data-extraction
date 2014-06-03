package provider.impl.file;

import helper.Helper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import msg.Document;
import msg.Document.ContentType;
import msg.Document.DataClass;
import msg.Document.DataType;

import org.apache.log4j.Logger;

import provider.ExchangeProvider;

public class FileExchangeProvider implements ExchangeProvider {

	static Logger logger = Logger.getLogger(ExchangeProvider.class);

	public static final char FILE_PROVIDER_SYMB = '0';
	public static final String HOME_DATA_FOLDER = "home_data";
	public static final String EXCHANGES_FILE = "exchanges";
	
	private Helper helper;

	public FileExchangeProvider() {
		super();
		this.helper = new Helper();
	}

	public FileExchangeProvider(Helper helper) {
		super();
		this.helper = helper;
	}
	public Helper getHelper() {
		return helper;
	}

	public void setHelper(Helper helper) {
		this.helper = helper;
	}

	public List<Document> getExchanges(ContentType format) throws Exception {
		List<Document> list = new ArrayList<Document>();
		String path =helper.getPath(HOME_DATA_FOLDER, EXCHANGES_FILE, format.getName());
		File f = helper.getFile(path);
		list.add(new Document(format, DataType.Ref, DataClass.Exchanges, FILE_PROVIDER_SYMB, helper.toBytes(f)));
		return list;

	}

}
