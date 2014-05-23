package receiver;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;

import org.apache.log4j.Logger;

import dao.StockSummaryDao;
import domain.StockSummary;
import domain.StockSummaries;

public class StockSummaryReceiver implements Receiver {
	static Logger logger = Logger.getLogger(StockSummaryReceiver.class);
	public static final String ROUTING_KEY = "stockSummaries";
	private StockSummaryDao stockSummaryDao;

	public StockSummaryReceiver(StockSummaryDao stockSummaryDao) {
		super();
		this.stockSummaryDao = stockSummaryDao;
	}

	public StockSummaryDao getStockSummaryDao() {
		return stockSummaryDao;
	}

	public void setStockSummaryDao(StockSummaryDao stockSummaryDao) {
		this.stockSummaryDao = stockSummaryDao;
	}

	public boolean receive(byte[] msg) {
		logger.info("msg received: " + new String(msg));
		try {
			InputStream is = new ByteArrayInputStream(msg);

			JAXBContext context = JAXBContext.newInstance(StockSummaries.class);
			Unmarshaller um = context.createUnmarshaller();

			um.setEventHandler(new ValidationEventHandler() {
				public boolean handleEvent(ValidationEvent event) {
					throw new RuntimeException(event.getMessage(), event.getLinkedException());
				}
			});

			StockSummaries stockSummaries = (StockSummaries) um.unmarshal(is);
			List<StockSummary> list = stockSummaries.getStocksList();
			if (list != null) {
				logger.info("stockSummaries size " + list.size());
				for (StockSummary stockSummary : list) {
					stockSummaryDao.add(stockSummary);
				}
			} else
				logger.info("null stockSummaries");

			return true;
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
			return false;
		}
	}


}
