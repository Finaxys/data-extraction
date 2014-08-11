package com.finaxys.rd.dataextraction.dao;
/*
 * 
 */


import java.util.List;

import com.finaxys.rd.dataextraction.dao.exception.DataAccessException;
import com.finaxys.rd.dataextraction.domain.OptionChain;


/**
 * The Interface OptionChainDao.
 */
public interface OptionChainDao {

	
	public List<OptionChain> list(String prefix) throws DataAccessException;

	public List<OptionChain> list(byte[] prefix) throws DataAccessException;

	public List<OptionChain> listAll() throws DataAccessException;

	public List<OptionChain> list(char provider) throws DataAccessException;
}
