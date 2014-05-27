package receiver;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;

import org.apache.log4j.Logger;

import dao.StockDao;
import domain.Stock;
import domain.Stocks;

public class StockReceiver implements Receiver {
	static Logger logger = Logger.getLogger(StockReceiver.class);
	public static final String ROUTING_KEY = "stocks";
	private StockDao stockDao;

	public StockReceiver(StockDao stockDao) {
		super();
		this.stockDao = stockDao;
	}

	public StockDao getStockSummaryDao() {
		return stockDao;
	}

	public void setStockSummaryDao(StockDao stockDao) {
		this.stockDao = stockDao;
	}

	public boolean receive(byte[] msg) {
		logger.info("msg received: " + new String(msg));
		try {
			InputStream is = new ByteArrayInputStream(msg);

			JAXBContext context = JAXBContext.newInstance(Stocks.class);
			Unmarshaller um = context.createUnmarshaller();

			um.setEventHandler(new ValidationEventHandler() {
				public boolean handleEvent(ValidationEvent event) {
					throw new RuntimeException(event.getMessage(), event.getLinkedException());
				}
			});

			Stocks stocks = (Stocks) um.unmarshal(is);
			List<Stock> list = stocks.getStocksList();
			if (list != null) {
				logger.info("stocks size " + list.size());
				for (Stock stock : list) {
					stockDao.add(stock);
				}
			} else
				logger.info("null stocks");

			return true;
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
			return false;
		}
	}


}
