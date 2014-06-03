package job;
import java.util.List;

import msg.Document;
import msg.Message;
import msg.Document.ContentType;
import msg.Document.DataType;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import provider.FXRateProvider;
import provider.impl.yahoo.YahooFXRateProvider;
import publisher.MomPublisher;
import publisher.Publisher;
import converter.Converter;
import converter.yahoo.FXRatesConverter;


public class FXRatesJob implements Job {

	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		FXRateProvider md = new YahooFXRateProvider();
		List<Document> l;
		try {
			l = md.getCurrentFXRates(ContentType.XML, DataType.EOD);

			Converter converter = new FXRatesConverter();
			Publisher p = new MomPublisher();
			for (Document d : l) {
				Message m = new Message(d, MomPublisher.FXRATES_ROUTING_KEY);
				converter.convert(m);
				p.publish(m);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
