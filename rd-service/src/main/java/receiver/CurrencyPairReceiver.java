package receiver;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;

import msg.Message;

import org.apache.log4j.Logger;

import dao.CurrencyPairDao;
import dao.CurrencyPairDao;
import domain.CurrencyPair;
import domain.CurrencyPairs;
import domain.CurrencyPair;
import domain.CurrencyPairs;

public class CurrencyPairReceiver implements Receiver {

	static Logger logger = Logger.getLogger(CurrencyPairReceiver.class);
	public static final String BINDING_KEY = "currencyPairs";
	private CurrencyPairDao currencyPairDao;

	public CurrencyPairReceiver(CurrencyPairDao currencyPairDao) {
		super();
		this.currencyPairDao = currencyPairDao;
	}

	public CurrencyPairDao getCurrencyPairDao() {
		return currencyPairDao;
	}

	public void setCurrencyPairDao(CurrencyPairDao currencyPairDao) {
		this.currencyPairDao = currencyPairDao;
	}

	public boolean receive(Message msg) {
		try {
			InputStream is = new ByteArrayInputStream(msg.getBody().getContent());
			
			JAXBContext context = JAXBContext.newInstance(CurrencyPairs.class);
			Unmarshaller um = context.createUnmarshaller();

			um.setEventHandler(new ValidationEventHandler() {
				public boolean handleEvent(ValidationEvent event) {
					throw new RuntimeException(event.getMessage(), event.getLinkedException());
				}
			});

			CurrencyPairs currencyPairs = (CurrencyPairs) um.unmarshal(is);
			List<CurrencyPair> list = currencyPairs.getCurrencyPairsList();
			boolean resp = true;
			if (list != null) {
				logger.info("currency pairs size " + list.size());
				for (CurrencyPair currencyPair : list) {
					resp = resp && currencyPairDao.add(currencyPair);
				}
			} else
				logger.info("null currency pairs");

			return resp;
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
			return false;
		}
	}

}
