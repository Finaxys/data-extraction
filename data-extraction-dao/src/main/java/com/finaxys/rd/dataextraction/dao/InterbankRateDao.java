package com.finaxys.rd.dataextraction.dao;

import java.util.List;

import com.finaxys.rd.dataextraction.dao.exception.DataAccessException;
import com.finaxys.rd.dataextraction.domain.InterbankRate;


public interface InterbankRateDao extends BasicDao<InterbankRate>{
	
	 List<InterbankRate> list(char provider) throws DataAccessException;
}
