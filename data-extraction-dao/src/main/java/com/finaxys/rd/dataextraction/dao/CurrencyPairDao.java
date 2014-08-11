/*
 * 
 */

package com.finaxys.rd.dataextraction.dao;
import java.util.List;

import com.finaxys.rd.dataextraction.dao.exception.DataAccessException;
import com.finaxys.rd.dataextraction.domain.CurrencyPair;


// TODO: Auto-generated Javadoc
/**
 * The Interface CurrencyPairDao.
 */
public interface CurrencyPairDao {
	
	public List<CurrencyPair> list(String prefix) throws DataAccessException;

	public List<CurrencyPair> list(byte[] prefix) throws DataAccessException;
	
	public List<CurrencyPair> listAll() throws DataAccessException;
	
	public List<CurrencyPair> list(char provider) throws DataAccessException;
}
