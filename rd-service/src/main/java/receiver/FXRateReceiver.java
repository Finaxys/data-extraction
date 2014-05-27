package receiver;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;

import org.apache.log4j.Logger;

import dao.FXRateDao;
import domain.FXRate;
import domain.FXRates;

public class FXRateReceiver implements Receiver{
	static Logger logger = Logger.getLogger(FXRateReceiver.class);
	public static final String ROUTING_KEY = "fxRates";
	private FXRateDao fxRateDao;

	public FXRateReceiver(FXRateDao fxRateDao) {
		super();
		this.fxRateDao = fxRateDao;
	}

	public FXRateDao getStockSummaryDao() {
		return fxRateDao;
	}

	public void setStockSummaryDao(FXRateDao fxRateDao) {
		this.fxRateDao = fxRateDao;
	}

	public boolean receive(byte[] msg) {
		logger.info("msg received: " + new String(msg));
		try {
			InputStream is = new ByteArrayInputStream(msg);

			JAXBContext context = JAXBContext.newInstance(FXRates.class);
			Unmarshaller um = context.createUnmarshaller();

			um.setEventHandler(new ValidationEventHandler() {
				public boolean handleEvent(ValidationEvent event) {
					throw new RuntimeException(event.getMessage(), event.getLinkedException());
				}
			});

			FXRates fxRates = (FXRates) um.unmarshal(is);
			List<FXRate> list = fxRates.getRatesList();
			if (list != null) {
				logger.info("fx rates size " + list.size());
				for (FXRate stockQuote : list) {
					fxRateDao.add(stockQuote);
				}
			} else
				logger.info("null fx rates");

			return true;
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
			return false;
		}
	}


}
