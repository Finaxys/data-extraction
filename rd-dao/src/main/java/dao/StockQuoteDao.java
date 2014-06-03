package dao;

import java.io.IOException;
import java.util.List;

import msg.Document.DataType;
import domain.StockQuote;

public interface StockQuoteDao {
	public boolean add(StockQuote index);
	public StockQuote get(char provider, String exchSymb, String symbol, Long ts, DataType dataType) throws IOException;
	public List<StockQuote> list(String prefix) throws IOException;
	public List<StockQuote> listAll() throws IOException;
}
