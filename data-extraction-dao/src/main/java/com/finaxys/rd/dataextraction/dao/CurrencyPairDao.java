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
public interface CurrencyPairDao extends BasicDao<CurrencyPair> {

	 List<CurrencyPair> list(char provider) throws DataAccessException;
}
