package dao;

import java.io.IOException;
import java.util.List;

import domain.IndexInfo;

public interface IndexInfoDao {
	public boolean add(IndexInfo exchange);
	public IndexInfo get(char provider, String exchSymb, String symbol) throws IOException;
	public List<IndexInfo> list(String prefix) throws IOException;
	public List<IndexInfo> listAll() throws IOException;
	public List<String> listAllSymbols() throws IOException ;
}
