package job;
import java.util.List;

import msg.Document;
import msg.Message;
import msg.Document.ContentType;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import provider.IndexInfoProvider;
import provider.impl.file.FileIndexInfoProvider;
import publisher.MomPublisher;
import publisher.Publisher;
import converter.Converter;
import converter.file.IndexInfosConverter;


public class IndexInfosJob implements Job {

	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		IndexInfoProvider md = new FileIndexInfoProvider();
		List<Document> l;
		try {
			l = md.getIndexInfos(ContentType.XLS);

			Converter converter = new IndexInfosConverter();
			Publisher p = new MomPublisher();
			for (Document d : l) {
				Message m = new Message(d, MomPublisher.INDEX_INFOS_ROUTING_KEY);
				converter.convert(m);
				p.publish(m);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
