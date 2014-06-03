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

import provider.StockProvider;

public class FileStockProvider implements StockProvider {
	public static final char FILE_PROVIDER_SYMB = '0';
	static Logger logger = Logger.getLogger(StockProvider.class);
	public static final String HOME_DATA_FOLDER = "home_data";
	public static final String STOCKS_FILE = "stocks";
	
	private Helper helper;

	public FileStockProvider() {
		super();
		this.helper = new Helper();
	}

	public Helper getHelper() {
		return helper;
	}

	public void setHelper(Helper helper) {
		this.helper = helper;
	}

	public List<Document> getStocks(ContentType format) throws Exception {
		List<Document> list = new ArrayList<Document>();
		File f = helper.getFile(helper.getPath(HOME_DATA_FOLDER, STOCKS_FILE, format.getName()));
		list.add(new Document(format, DataType.Ref, DataClass.Stocks, FILE_PROVIDER_SYMB, helper.toBytes(f)));
		return list;
	}

}
