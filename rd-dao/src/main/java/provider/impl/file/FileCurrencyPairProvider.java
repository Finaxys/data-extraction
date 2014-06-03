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

import provider.CurrencyPairProvider;

public class FileCurrencyPairProvider implements CurrencyPairProvider {

	static Logger logger = Logger.getLogger(CurrencyPairProvider.class);

	public static final char FILE_PROVIDER_SYMB = '0';
	public static final String HOME_DATA_FOLDER = "home_data";
	public static final String CURRENCY_PAIRS_FILE = "currency_pairs";

	private Helper helper;

	public FileCurrencyPairProvider() {
		super();
		this.helper = new Helper();
		
	}

	public Helper getHelper() {
		return helper;
	}

	public void setHelper(Helper helper) {
		this.helper = helper;
	}

	public List<Document> getCurrencyPairs(ContentType format) throws Exception {
		List<Document> list = new ArrayList<Document>();
		File f = helper.getFile(helper.getPath(HOME_DATA_FOLDER, CURRENCY_PAIRS_FILE, format.getName()));
		list.add(new Document(format, DataType.Ref, DataClass.CurrencyPairs, FILE_PROVIDER_SYMB, helper.toBytes(f)));
		return list;
	}

}
