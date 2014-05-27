package dao;

import java.io.IOException;
import java.util.List;

import domain.CurrencyPair;

public interface CurrencyPairDao {
	public boolean add(CurrencyPair currencyPair);
	public CurrencyPair get(String symbol) throws IOException;
	public List<CurrencyPair> list(String prefix) throws IOException;
	public List<CurrencyPair> listAll() throws IOException;
	public List<String> listAllSymbols() throws IOException;
}
