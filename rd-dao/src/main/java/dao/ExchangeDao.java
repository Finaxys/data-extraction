package dao;

import java.io.IOException;
import java.util.List;

import domain.Exchange;

public interface ExchangeDao {
	public boolean add(Exchange exchange);
	public Exchange get(Integer provider, String symbol, String mic) throws IOException;
	public List<Exchange> list(String prefix) throws IOException;
	public List<Exchange> listAll() throws IOException;
}
