package provider;

import java.io.File;

import org.apache.log4j.Logger;

import publisher.Publisher;
import converter.home.CurrencyPairsConverter;
import converter.home.ExchangesConverter;
import converter.home.IndexInfoConverter;
import converter.home.StocksConverter;

public class HomeDataProvider implements DataProvider {

	public static final int H_PROVIDER_SYMB = 0;
	static Logger logger = Logger.getLogger(HomeDataProvider.class);
	public static final String HOME_DATA_FOLDER = "home_data";
	public static final String EXCHANGES_FILE = "exchanges.xls";
	public static final String CURRENCY_PAIRS_FILE = "currency_pairs.xls";
	public static final String INDEX_INFOS_FILE = "index_infos.xls";
	public static final String 	STOCKS_FILE = "stocks.xls";

	public HomeDataProvider() {
		super();
	}

	private File getFile(String path) throws Exception {
		logger.info("importing file...");
		File f = new File(path);
		logger.info("file imported");
		return f;

	}

	public void getExchanges(String format) throws Exception {
		
		File f = getFile(HOME_DATA_FOLDER + "/" + EXCHANGES_FILE);
		logger.info("publishing exchanges list...");
		Publisher publisher = new Publisher();
		publisher.publish(new ExchangesConverter().convert(f), Publisher.EXCHANGES_ROUTING_KEY);
		logger.info("exchanges list published");
		
	}

	public void getStocks(String format) throws Exception {
		File f = getFile(HOME_DATA_FOLDER + "/" + STOCKS_FILE);
		logger.info("publishing stocks list...");
		Publisher publisher = new Publisher();
		publisher.publish(new StocksConverter().convert(f), Publisher.STOCKS_ROUTING_KEY);
		logger.info("stocks published");

	}

	public void getCurrencyPairs(String format) throws Exception {

		File f = getFile(HOME_DATA_FOLDER + "/" + CURRENCY_PAIRS_FILE);
		logger.info("publishing currency pairs list...");
		Publisher publisher = new Publisher();
		publisher.publish(new CurrencyPairsConverter().convert(f), Publisher.CURRENCY_PAIRS_ROUTING_KEY);
		logger.info("currency pairs list published");

	}

	public void getIndexInfos(String format) throws Exception {
		System.out.println(11);

			File f = getFile(HOME_DATA_FOLDER + "/" + INDEX_INFOS_FILE);
			logger.info("publishing index infos list...");
			Publisher publisher = new Publisher();
			publisher.publish(new IndexInfoConverter().convert(f), Publisher.INDEX_INFOS_ROUTING_KEY);
			logger.info("index infos list published");
	
	}

	public static void main(String[] args) throws Exception {
		DataProvider dp = new HomeDataProvider();
		// dp.getExchanges("");
//		dp.getCurrencyPairs("");
//		dp.getIndexInfos("");
		dp.getStocks("");

	}

	public void getStocksQuotes(String format) throws Exception {
		// TODO Auto-generated method stub
		
	}

	public void getFXRates(String format) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
