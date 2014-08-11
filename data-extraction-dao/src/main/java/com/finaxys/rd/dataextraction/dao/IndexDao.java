package com.finaxys.rd.dataextraction.dao;
/*
 * 
 */


import java.util.List;

import com.finaxys.rd.dataextraction.dao.exception.DataAccessException;
import com.finaxys.rd.dataextraction.domain.Index;

// TODO: Auto-generated Javadoc
/**
 * The Interface IndexInfoDao.
 */
public interface IndexDao {

	public List<Index> list(String prefix) throws DataAccessException;
	
	public List<Index> list(byte[] prefix) throws DataAccessException;
	
	public List<Index> listAll() throws DataAccessException;
	
	public List<Index> list(char provider, String exchSymb) throws DataAccessException ;
}
