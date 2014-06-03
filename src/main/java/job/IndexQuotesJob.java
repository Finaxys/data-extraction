package job;
import java.util.List;

import msg.Document;
import msg.Message;
import msg.Document.ContentType;
import msg.Document.DataType;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import provider.IndexQuoteProvider;
import provider.impl.yahoo.YahooIndexQuoteProvider;
import publisher.MomPublisher;
import publisher.Publisher;
import converter.Converter;
import converter.yahoo.IndexQuotesConverter;

public class IndexQuotesJob implements Job {

	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		IndexQuoteProvider md = new YahooIndexQuoteProvider();
		List<Document> l;
		try {
			l = md.getCurrentIndexQuotes(ContentType.XML, DataType.EOD);

			Converter converter = new IndexQuotesConverter();
			Publisher p = new MomPublisher();
			for (Document d : l) {
				Message m = new Message(d, MomPublisher.INDEX_QUOTES_ROUTING_KEY);
				converter.convert(m);
				p.publish(m);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
