package dao;

import java.io.IOException;
import java.util.List;

import domain.FXRate;


public interface FXRateDao {
	public boolean add(FXRate stock);
	public List<FXRate> list(String prefix) throws IOException;
	public List<FXRate> list(byte[] start, byte[] end) throws IOException;
	public List<FXRate> listAll() throws IOException;
}
