package provider;

import java.util.List;

import msg.Document;
import msg.Document.ContentType;

public interface ExchangeProvider {
	public List<Document> getExchanges(ContentType format) throws Exception;
}
