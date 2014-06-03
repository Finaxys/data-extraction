package provider;

import java.util.List;

import msg.Document;
import msg.Document.ContentType;
import msg.Document.DataType;

public interface FXRateProvider {
	public List<Document> getCurrentFXRates(ContentType format, DataType type) throws Exception;
}
