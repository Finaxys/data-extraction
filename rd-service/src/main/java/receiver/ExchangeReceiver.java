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

import dao.ExchangeDao;
import domain.Exchange;
import domain.Exchanges;

public class ExchangeReceiver implements Receiver {
	static Logger logger = Logger.getLogger(ExchangeReceiver.class);
	public static final String BINDING_KEY = "exchanges";
	private ExchangeDao exchangeDao;

	public ExchangeReceiver(ExchangeDao exchangeDao) {
		super();
		this.exchangeDao = exchangeDao;
	}

	public ExchangeDao getExchangeDao() {
		return exchangeDao;
	}

	public void setExchangeDao(ExchangeDao exchangeDao) {
		this.exchangeDao = exchangeDao;
	}
	
	public boolean receive(Message msg) {
		try {
			InputStream is = new ByteArrayInputStream(msg.getBody().getContent());
			
			JAXBContext context = JAXBContext.newInstance(Exchanges.class);
			Unmarshaller um = context.createUnmarshaller();

			um.setEventHandler(new ValidationEventHandler() {
				public boolean handleEvent(ValidationEvent event) {
					throw new RuntimeException(event.getMessage(), event.getLinkedException());
				}
			});

			Exchanges exchanges = (Exchanges) um.unmarshal(is);
			List<Exchange> list = exchanges.getExchangesList();
			boolean resp = true;
			if (list != null) {
				logger.info("exchanges size " + list.size());
				for (Exchange exchange : list) {
					resp  = resp && exchangeDao.add(exchange);
				}
			} else
				logger.info("null exchanges");

			return resp;
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
			return false;
		}
	}

}
