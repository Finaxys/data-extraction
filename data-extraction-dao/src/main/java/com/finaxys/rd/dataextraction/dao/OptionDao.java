/*
 * 
 */
package com.finaxys.rd.dataextraction.dao;

import java.util.List;

import com.finaxys.rd.dataextraction.dao.exception.DataAccessException;
import com.finaxys.rd.dataextraction.domain.Option;

// TODO: Auto-generated Javadoc
/**
 * The Interface OptionDao.
 */
public interface OptionDao extends BasicDao<Option> {

	public List<Option> list(char provider, String exchSymb) throws DataAccessException;

}
