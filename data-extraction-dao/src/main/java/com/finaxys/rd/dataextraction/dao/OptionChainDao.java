/*
 * 
 */
package com.finaxys.rd.dataextraction.dao;

import java.util.List;

import com.finaxys.rd.dataextraction.dao.exception.DataAccessException;
import com.finaxys.rd.dataextraction.domain.OptionChain;

// TODO: Auto-generated Javadoc
/**
 * The Interface OptionChainDao.
 */
public interface OptionChainDao extends BasicDao<OptionChain>{
	
	public List<OptionChain> list(char provider) throws DataAccessException;
}
