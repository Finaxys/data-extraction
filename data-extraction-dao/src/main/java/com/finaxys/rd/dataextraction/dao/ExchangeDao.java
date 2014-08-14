/*
 * 
 */
package com.finaxys.rd.dataextraction.dao;

import java.util.List;

import com.finaxys.rd.dataextraction.dao.exception.DataAccessException;
import com.finaxys.rd.dataextraction.domain.Exchange;

// TODO: Auto-generated Javadoc
/**
 * The Interface ExchangeDao.
 */
public interface ExchangeDao extends BasicDao<Exchange>{
	
	public List<Exchange> list(char provider) throws DataAccessException;
}
