package job;
import java.util.List;

import msg.Document;
import msg.Document.ContentType;
import msg.Message;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import converter.Converter;
import converter.file.StocksConverter;
import provider.StockProvider;
import provider.impl.file.FileStockProvider;
import publisher.MomPublisher;
import publisher.Publisher;

public class StocksJob implements Job {

	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		StockProvider md = new FileStockProvider();
		List<Document> l;
		try {
			l = md.getStocks(ContentType.XLS);
			Converter converter = new StocksConverter();
			Publisher p = new MomPublisher();
			for (Document d : l) {
				Message m = new Message(d, MomPublisher.STOCKS_ROUTING_KEY);
				converter.convert(m);
				p.publish(m);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
