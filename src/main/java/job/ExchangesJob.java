package job;
import java.util.List;

import msg.Document;
import msg.Message;
import msg.Document.ContentType;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import provider.ExchangeProvider;
import provider.impl.file.FileExchangeProvider;
import publisher.MomPublisher;
import publisher.Publisher;
import converter.Converter;
import converter.file.ExchangesConverter;

public class ExchangesJob implements Job {

	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		ExchangeProvider md = new FileExchangeProvider();
		
		List<Document> l;
		try {
			l = md.getExchanges(ContentType.XLS);

			Converter converter = new ExchangesConverter();
			Publisher p = new MomPublisher();
			for (Document d : l) {
				Message m = new Message(d, MomPublisher.EXCHANGES_ROUTING_KEY);
				converter.convert(m);
				p.publish(m);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
