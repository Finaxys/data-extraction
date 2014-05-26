package provider;

import java.io.File;

import org.apache.log4j.Logger;

import publisher.Publisher;
import converter.home.CurrencyPairsConverter;
import converter.home.ExchangesConverter;

public class HomeDataProvider implements DataProvider {

	public static final int H_PROVIDER_SYMB = 0;
	static Logger logger = Logger.getLogger(HomeDataProvider.class);
	public static final String HOME_DATA_FOLDER = "home_data";
	public static final String EXCHANGES_FILE = "exchanges.xls";
	public static final String CURRENCY_PAIRS_FILE = "currency_pairs.xls";

	public HomeDataProvider() {
		super();
	}

	public void getExchanges(String format) {
		logger.info("import exchanges file...");
		try {
			File f = new File(HOME_DATA_FOLDER + "/" + EXCHANGES_FILE);
			logger.info("importing exchanges file finished");
			logger.info("publishing exchanges list...");
			Publisher publisher = new Publisher();
			publisher.publish(new ExchangesConverter().convert(f), Publisher.EXCHANGES_ROUTING_KEY);
			logger.info("exchanges list published");
		} catch (Exception e) {
			logger.error(e.toString());
			e.printStackTrace();
		}

	}

	public void getStockSummaries(String format) throws Exception {
		// TODO Auto-generated method stub

	}

	public void getCurrencyPairs(String format) throws Exception {
		logger.info("import currency pairs file...");
		try {
			File f = new File(HOME_DATA_FOLDER + "/" + CURRENCY_PAIRS_FILE);
			logger.info("importing currency pairs file finished");
			logger.info("publishing currency pairs list...");
			Publisher publisher = new Publisher();
			publisher.publish(new CurrencyPairsConverter().convert(f), Publisher.CURRENCY_PAIRS_ROUTING_KEY);
			logger.info("currency pairs list published");
		} catch (Exception e) {
			logger.error(e.toString());
			e.printStackTrace();
		}

	}


}
