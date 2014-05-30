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

import dao.IndexInfoDao;
import domain.IndexInfo;
import domain.IndexInfos;

public class IndexInfoReceiver implements Receiver {


		static Logger logger = Logger.getLogger(IndexInfoReceiver.class);
		public static final String BINDING_KEY = "indexInfos";
		private IndexInfoDao indexInfoDao;

		public IndexInfoReceiver(IndexInfoDao indexInfoDao) {
			super();
			this.indexInfoDao = indexInfoDao;
		}

		public IndexInfoDao getIndexInfoDao() {
			return indexInfoDao;
		}

		public void setIndexInfoDao(IndexInfoDao indexInfoDao) {
			this.indexInfoDao = indexInfoDao;
		}

		public boolean receive(Message msg) {
			try {
				InputStream is = new ByteArrayInputStream(msg.getBody().getContent());
				
				JAXBContext context = JAXBContext.newInstance(IndexInfos.class);
				Unmarshaller um = context.createUnmarshaller();

				um.setEventHandler(new ValidationEventHandler() {
					public boolean handleEvent(ValidationEvent event) {
						throw new RuntimeException(event.getMessage(), event.getLinkedException());
					}
				});

				IndexInfos indexInfos = (IndexInfos) um.unmarshal(is);
				List<IndexInfo> list = indexInfos.getIndexInfosList();
				boolean resp = true;
				if (list != null) {
					logger.info("index infos size " + list.size());
					for (IndexInfo indexInfo : list) {
						resp = resp && indexInfoDao.add(indexInfo);
					}
				} else
					logger.info("null index infos");

				return resp;
			} catch (Exception e) {
				logger.error(e);
				e.printStackTrace();
				return false;
			}
		}

}
