package dao;


import java.util.List;

import msg.Document;
import msg.Document.ContentType;

public interface MarketDataDao {

	public List<Document> getExchanges(ContentType format) throws Exception;
	public List<Document> getStocksQuotes(ContentType format) throws Exception;
	public List<Document> getIndexQuotes(ContentType format) throws Exception;
	public List<Document> getCurrencyPairs(ContentType format) throws Exception;
	public List<Document> getIndexInfos(ContentType format) throws Exception;
	public List<Document> getStocks(ContentType format) throws Exception;
	public List<Document> getFXRates(ContentType format) throws Exception;
	
}
