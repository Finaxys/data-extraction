/*
 * 
 */
package com.finaxys.rd.dataextraction.dao;

import java.util.List;

import com.finaxys.rd.dataextraction.dao.exception.DataAccessException;


// TODO: Auto-generated Javadoc
/**
 * The Interface CurrencyPairDao.
 */
public interface BasicDao<T> {

	 boolean add(T bean) throws DataAccessException;

	 List<T> list(byte[] prefix) throws DataAccessException;

	 List<T> listAll() throws DataAccessException;

}
