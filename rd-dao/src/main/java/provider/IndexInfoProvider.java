package provider;

import java.util.List;

import msg.Document;
import msg.Document.ContentType;

public interface IndexInfoProvider {
	public List<Document> getIndexInfos(ContentType format) throws Exception;
}
