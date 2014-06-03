package provider;

import java.util.List;

import msg.Document;
import msg.Document.ContentType;

public interface CurrencyPairProvider {
	public List<Document> getCurrencyPairs(ContentType format) throws Exception;
}
