package provider;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.http.client.ClientProtocolException;
import org.apache.log4j.Logger;

import publisher.Publisher;
import converter.Converter;
import converter.HomeExchangeConverter;

public class HomeDataProvider implements DataProvider {

	public static final int H_PROVIDER_SYMB = 0;
	static Logger logger = Logger.getLogger(HomeDataProvider.class);
	public static final String EXCHANGES_FOLDER = "exchanges";
	public static final String EXCHANGES_FILE = "exchanges.xls";

	public HomeDataProvider() {
		super();
	}

	public File getExchanges() {
		logger.info("import exchanges file...");
		try {
			File f = new File(EXCHANGES_FOLDER + "/" + EXCHANGES_FILE);
			logger.info("importing exchanges file finished");
			logger.info("publishing exchanges list...");
			Publisher publisher = new Publisher();
			publisher.publish(new HomeExchangeConverter().convert(f), Publisher.EXCHANGES_ROUTING_KEY);
			logger.info("exchanges list published");
			return f;
		} catch (Exception e) {
			logger.error(e.toString());
			e.printStackTrace();
			return null;
		}

	}

	public void getStockSummaries(String format) throws Exception {
		// TODO Auto-generated method stub

	}



}
