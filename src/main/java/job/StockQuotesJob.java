package job;
import java.util.List;

import msg.Document;
import msg.Message;
import msg.Document.ContentType;
import msg.Document.DataType;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import provider.StockQuoteProvider;
import provider.impl.yahoo.YahooStockQuoteProvider;
import publisher.MomPublisher;
import publisher.Publisher;
import converter.Converter;
import converter.yahoo.StockQuotesConverter;

public class StockQuotesJob implements Job {

	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		StockQuoteProvider md = new YahooStockQuoteProvider();
		List<Document> l;
		try {
			l = md.getCurrentStocksQuotes(ContentType.XML, DataType.EOD);

			Converter converter = new StockQuotesConverter();
			Publisher p = new MomPublisher();
			for (Document d : l) {
				Message m = new Message(d, MomPublisher.STOCKS_QUOTES_ROUTING_KEY);
				converter.convert(m);
				p.publish(m);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
