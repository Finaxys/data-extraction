package com.finaxys.rd.dataextraction.dao.impl;

import java.util.List;

import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Logger;

import com.finaxys.rd.dataextraction.dao.InterbankRateDao;
import com.finaxys.rd.dataextraction.dao.exception.DataAccessException;
import com.finaxys.rd.dataextraction.domain.InterbankRate;

public class InterbankRateDaoImpl extends AbstractBasicDao<InterbankRate> implements InterbankRateDao {

	private static Logger logger = Logger.getLogger(InterbankRateDaoImpl.class);

	public InterbankRateDaoImpl() {
		super();
	}

	public InterbankRateDaoImpl(HConnection connection) {
		super(connection);
	}

	public List<InterbankRate> list(char provider) throws DataAccessException {
		byte provByte = (byte) provider;
		byte[] prefix = new byte[1];

		int offset = 0;
		offset = Bytes.putByte(prefix, offset, provByte);

		return list(prefix);

	}
}
