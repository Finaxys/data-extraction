package dao;

import java.io.IOException;
import java.util.List;

import domain.StockSummary;

public interface StockSummaryDao {
	public boolean add(StockSummary stock);
	public StockSummary get(Integer provider, String exchSymb, String symbol) throws IOException;
	public List<StockSummary> list(String prefix) throws IOException;
	public List<StockSummary> listAll() throws IOException;
}
