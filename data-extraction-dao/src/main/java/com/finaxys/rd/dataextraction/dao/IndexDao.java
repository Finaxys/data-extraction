/*
 * 
 */
package com.finaxys.rd.dataextraction.dao;

import java.util.List;

import com.finaxys.rd.dataextraction.dao.exception.DataAccessException;
import com.finaxys.rd.dataextraction.domain.Index;

// TODO: Auto-generated Javadoc
/**
 * The Interface IndexInfoDao.
 */
public interface IndexDao extends BasicDao<Index> {
	
	public List<Index> list(char provider, String exchSymb) throws DataAccessException ;
}
