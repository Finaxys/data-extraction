package receiver;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;

import org.apache.log4j.Logger;

import dao.CurrencyPairDao;
import dao.CurrencyPairDao;
import domain.CurrencyPair;
import domain.CurrencyPairs;
import domain.CurrencyPair;
import domain.CurrencyPairs;

public class CurrencyPairReceiver implements Receiver{

		static Logger logger = Logger.getLogger(CurrencyPairReceiver.class);
		public static final String ROUTING_KEY = "currencyPairs";
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

		public boolean receive(byte[] msg) {
			logger.info("msg received: " + new String(msg));
			try {
				InputStream is = new ByteArrayInputStream(msg);

				JAXBContext context = JAXBContext.newInstance(CurrencyPairs.class);
				Unmarshaller um = context.createUnmarshaller();

				um.setEventHandler(new ValidationEventHandler() {
					public boolean handleEvent(ValidationEvent event) {
						throw new RuntimeException(event.getMessage(), event.getLinkedException());
					}
				});

				CurrencyPairs currencyPairs = (CurrencyPairs) um.unmarshal(is);
				List<CurrencyPair> list = currencyPairs.getCurrencyPairsList();
				if (list != null) {
					logger.info("currency pairs size " + list.size());
					for (CurrencyPair currencyPair : list) {
						currencyPairDao.add(currencyPair);
					}
				} else
					logger.info("null currency pairs");

				return true;
			} catch (Exception e) {
				logger.error(e);
				e.printStackTrace();
				return false;
			}
		}

	}
