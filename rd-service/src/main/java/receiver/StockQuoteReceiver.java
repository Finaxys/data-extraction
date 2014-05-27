package receiver;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;

import org.apache.log4j.Logger;

import dao.StockQuoteDao;
import domain.StocksQuotes;
import domain.StockQuote;

public class StockQuoteReceiver implements Receiver{
	static Logger logger = Logger.getLogger(StockQuoteReceiver.class);
	public static final String ROUTING_KEY = "stocksQuotes";
	private StockQuoteDao stockQuoteDao;

	public StockQuoteReceiver(StockQuoteDao stockQuoteDao) {
		super();
		this.stockQuoteDao = stockQuoteDao;
	}

	public StockQuoteDao getStockSummaryDao() {
		return stockQuoteDao;
	}

	public void setStockSummaryDao(StockQuoteDao stockQuoteDao) {
		this.stockQuoteDao = stockQuoteDao;
	}

	public boolean receive(byte[] msg) {
		logger.info("msg received: " + new String(msg));
		try {
			InputStream is = new ByteArrayInputStream(msg);

			JAXBContext context = JAXBContext.newInstance(StocksQuotes.class);
			Unmarshaller um = context.createUnmarshaller();

			um.setEventHandler(new ValidationEventHandler() {
				public boolean handleEvent(ValidationEvent event) {
					throw new RuntimeException(event.getMessage(), event.getLinkedException());
				}
			});

			StocksQuotes stocksQuotes = (StocksQuotes) um.unmarshal(is);
			List<StockQuote> list = stocksQuotes.getQuotesList();
			if (list != null) {
				logger.info("stocks quotes size " + list.size());
				for (StockQuote stockQuote : list) {
					stockQuoteDao.add(stockQuote);
				}
			} else
				logger.info("null stocks quotes");

			return true;
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
			return false;
		}
	}


}
