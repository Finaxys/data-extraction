package dao.impl.xls;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import msg.Document;
import msg.Document.ContentType;
import msg.Document.DataClass;
import msg.Document.DataType;

import org.apache.log4j.Logger;

import dao.MarketDataDao;

public class MarketDataDaoImpl implements MarketDataDao {

	public static final int XLS_PROVIDER_SYMB = 0;
	static Logger logger = Logger.getLogger(MarketDataDao.class);
	public static final String HOME_DATA_FOLDER = "home_data";
	public static final String EXCHANGES_FILE = "exchanges.xls";
	public static final String CURRENCY_PAIRS_FILE = "currency_pairs.xls";
	public static final String INDEX_INFOS_FILE = "index_infos.xls";
	public static final String STOCKS_FILE = "stocks.xls";

	public MarketDataDaoImpl() {
		super();
	}

	private File getFile(String path) throws Exception {
		logger.info("importing file...");
		File f = new File(path);
		logger.info("file imported");
		return f;

	}

	private byte[] toBytes(File file) throws IOException {
		FileInputStream fileInputStream = null;
		byte[] bFile = new byte[(int) file.length()];
		fileInputStream = new FileInputStream(file);
		fileInputStream.read(bFile);
		fileInputStream.close();
		return bFile;
	}

	public List<Document> getExchanges(ContentType format) throws Exception {
		List<Document> list = new ArrayList<Document>();
		File f = getFile(HOME_DATA_FOLDER + "/" + EXCHANGES_FILE);
		list.add(new Document(ContentType.XLS, DataType.Ref, DataClass.Exchanges, XLS_PROVIDER_SYMB, toBytes(f)));
		return list;

	}

	public List<Document> getStocks(ContentType format) throws Exception {
		List<Document> list = new ArrayList<Document>();
		File f = getFile(HOME_DATA_FOLDER + "/" + STOCKS_FILE);
		list.add(new Document(ContentType.XLS, DataType.Ref, DataClass.Stocks, XLS_PROVIDER_SYMB, toBytes(f)));
		return list;
	}

	public List<Document> getCurrencyPairs(ContentType format) throws Exception {
		List<Document> list = new ArrayList<Document>();
		File f = getFile(HOME_DATA_FOLDER + "/" + CURRENCY_PAIRS_FILE);
		list.add(new Document(ContentType.XLS, DataType.Ref, DataClass.CurrencyPairs, XLS_PROVIDER_SYMB, toBytes(f)));
		return list;
	}

	public List<Document> getIndexInfos(ContentType format) throws Exception {
		List<Document> list = new ArrayList<Document>();
		File f = getFile(HOME_DATA_FOLDER + "/" + INDEX_INFOS_FILE);
		list.add(new Document(ContentType.XLS, DataType.Ref, DataClass.IndexInfos, XLS_PROVIDER_SYMB, toBytes(f)));
		return list;
	}

	public List<Document> getStocksQuotes(ContentType format) throws Exception {
		return null;
	}

	public List<Document> getFXRates(ContentType format) throws Exception {
		return null;
	}

	public List<Document> getIndexQuotes(ContentType format) throws Exception {
		return null;
	}

}
