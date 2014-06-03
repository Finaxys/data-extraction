package dao;

import java.io.IOException;
import java.util.List;

import domain.Stock;

public interface StockDao {
	public boolean add(Stock stock);
	public Stock get(char provider, String exchSymb, String symbol) throws IOException;
	public List<Stock> list(String prefix) throws IOException;
	public List<Stock> listAll() throws IOException;
	public List<String> listAllSymbols() throws IOException;
}
