package provider;

import java.util.List;

import msg.Document;
import msg.Document.ContentType;
import msg.Document.DataType;

public interface IndexQuoteProvider {
	public List<Document> getCurrentIndexQuotes(ContentType format, DataType type) throws Exception;
}
