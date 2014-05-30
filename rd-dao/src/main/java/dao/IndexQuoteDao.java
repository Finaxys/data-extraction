package dao;

import java.io.IOException;
import java.util.List;

import domain.IndexQuote;

public interface IndexQuoteDao {
	public boolean add(IndexQuote index);
	public List<IndexQuote> list(String prefix) throws IOException;
	public List<IndexQuote> listAll() throws IOException;
}
