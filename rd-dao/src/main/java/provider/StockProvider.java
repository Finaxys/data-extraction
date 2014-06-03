package provider;

import java.util.List;

import msg.Document;
import msg.Document.ContentType;

public interface StockProvider {
	public List<Document> getStocks(ContentType format) throws Exception;
}
