package dao;

import java.io.IOException;
import java.util.List;

import domain.StockQuote;

public interface StockQuoteDao {
	public boolean add(StockQuote exchange);
	public StockQuote get(Integer provider, String exchSymb, String symbol, Long ts) throws IOException;
	public List<StockQuote> list(String prefix) throws IOException;
	public List<StockQuote> listAll() throws IOException;
}
