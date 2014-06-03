package job;
import java.util.List;

import msg.Document;
import msg.Message;
import msg.Document.ContentType;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import provider.CurrencyPairProvider;
import provider.impl.file.FileCurrencyPairProvider;
import publisher.MomPublisher;
import publisher.Publisher;
import converter.Converter;
import converter.file.CurrencyPairsConverter;


public class CurrencyPairsJob implements Job {

	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		CurrencyPairProvider md = new FileCurrencyPairProvider();
		List<Document> l;
		try {
			l = md.getCurrencyPairs(ContentType.XLS);

			Converter converter = new CurrencyPairsConverter();
			Publisher p = new MomPublisher();
			for (Document d : l) {
				Message m = new Message(d, MomPublisher.CURRENCY_PAIRS_ROUTING_KEY);
				converter.convert(m);
				p.publish(m);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
