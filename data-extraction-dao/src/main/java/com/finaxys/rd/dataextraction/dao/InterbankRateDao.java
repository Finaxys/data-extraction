package com.finaxys.rd.dataextraction.dao;


import java.util.List;

import com.finaxys.rd.dataextraction.dao.exception.DataAccessException;
import com.finaxys.rd.dataextraction.domain.InterbankRate;


public interface InterbankRateDao {
	
	public List<InterbankRate> list(String prefix) throws DataAccessException;

	public List<InterbankRate> list(byte[] prefix) throws DataAccessException;
	
	public List<InterbankRate> listAll() throws DataAccessException;
	
	public List<InterbankRate> list(char provider) throws DataAccessException;
}
