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

import dao.IndexQuoteDao;
import domain.IndexQuote;
import domain.IndexQuotes;

public class IndexQuoteReceiver  implements Receiver {


	static Logger logger = Logger.getLogger(IndexQuoteReceiver.class);
	public static final String BINDING_KEY = "indexQuotes";
	private IndexQuoteDao indexQuoteDao;

	public IndexQuoteReceiver(IndexQuoteDao indexQuoteDao) {
		super();
		this.indexQuoteDao = indexQuoteDao;
	}

	public IndexQuoteDao getIndexQuoteDao() {
		return indexQuoteDao;
	}

	public void setIndexQuoteDao(IndexQuoteDao indexQuoteDao) {
		this.indexQuoteDao = indexQuoteDao;
	}

	public boolean receive(Message msg) {
		try {
			InputStream is = new ByteArrayInputStream(msg.getBody().getContent());
			
			JAXBContext context = JAXBContext.newInstance(IndexQuotes.class);
			Unmarshaller um = context.createUnmarshaller();

			um.setEventHandler(new ValidationEventHandler() {
				public boolean handleEvent(ValidationEvent event) {
					throw new RuntimeException(event.getMessage(), event.getLinkedException());
				}
			});

			IndexQuotes indexQuotes = (IndexQuotes) um.unmarshal(is);
			List<IndexQuote> list = indexQuotes.getIndexQuotesList();
			boolean resp = true;
			if (list != null) {
				logger.info("index quotes size " + list.size());
				for (IndexQuote indexQuote : list) {
					resp = resp && indexQuoteDao.add(indexQuote);
				}
			} else
				logger.info("null index quotes");

			return resp;
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
			return false;
		}
	}

}
