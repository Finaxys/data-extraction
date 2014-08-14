/*
 * 
 */
package com.finaxys.rd.dataextraction.dao;

import java.util.List;

import com.finaxys.rd.dataextraction.dao.exception.DataAccessException;
import com.finaxys.rd.dataextraction.domain.Stock;

// TODO: Auto-generated Javadoc
/**
 * The Interface StockDao.
 */
public interface StockDao extends BasicDao<Stock> {
	
	public List<Stock> list(char provider, String exchSymb) throws DataAccessException;
	
	public List<Stock> list(char provider) throws DataAccessException;
}
