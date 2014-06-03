package provider;

import java.util.List;

import msg.Document;
import msg.Document.ContentType;
import msg.Document.DataType;

public interface StockQuoteProvider {
	public List<Document> getCurrentStocksQuotes(ContentType format, DataType types) throws Exception;
}
