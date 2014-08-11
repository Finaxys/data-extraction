package com.finaxys.rd.dataextraction.dao.impl;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.finaxys.rd.dataextraction.dao.InterbankRateDao;
import com.finaxys.rd.dataextraction.dao.exception.DataAccessException;
import com.finaxys.rd.dataextraction.dao.helper.DaoHelper;
import com.finaxys.rd.dataextraction.domain.InterbankRate;

public class InterbankRateDaoImpl implements InterbankRateDao {

	/** The table name. */
	@Value("#{'${dao.interbank_rate.tableName:interbank_rate}'.bytes}")
	private byte[] TABLE_NAME;

	/** The info fam. */
	@Value("#{'${dao.interbank_rate.infoFam:i}'.bytes}")
	private byte[] INFO_FAM;

	@Autowired
	private HConnection connection;

	// Constructor
	/**
	 * Instantiates a new stock dao impl.
	 */
	public InterbankRateDaoImpl() {
		super();
	}

	/**
	 * Instantiates a new stock dao impl.
	 * 
	 * @param connection
	 *            the connection
	 */
	InterbankRateDaoImpl(HConnection connection) {
		super();
		this.connection = connection;

	}

	// Helpers
	/**
	 * To stock summary.
	 * 
	 * @param result
	 *            the result
	 * @return the stock
	 */
	private InterbankRate toInterbankRate(Result result) {
		InterbankRate interbankRate = null;

		try {
			interbankRate = new InterbankRate();
			List<Field> attributes = new ArrayList<Field>();
			Class<?> tmpClass = interbankRate.getClass();
			while (tmpClass != null) {
				attributes.addAll(Arrays.asList(tmpClass.getDeclaredFields()));
				tmpClass = tmpClass.getSuperclass();
			}

			byte[] value;
			for (Field attribute : attributes) {

				if ((value = result.getValue(INFO_FAM, attribute.getName()
						.getBytes())) != null)
					PropertyUtils.setSimpleProperty(interbankRate,
							attribute.getName(),
							DaoHelper.getTypedValue(attribute, value));

			}

			return interbankRate;
		} catch (IllegalAccessException | InvocationTargetException
				| NoSuchMethodException | NullPointerException e) {
			throw new DataAccessException("Exception when reading data: ", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.finaxys.rd.marketdataprovider.dao.RateDao#listAll()
	 */
	public List<InterbankRate> list(String prefix) throws DataAccessException {
		HTableInterface table;
		try {
			table = connection.getTable(TABLE_NAME);

			ResultScanner results = table.getScanner(DaoHelper.mkScan(prefix));
			List<InterbankRate> ret = new ArrayList<InterbankRate>();
			for (Result result : results) {
				ret.add(toInterbankRate(result));
			}
			table.close();
			return ret;
		} catch (NullPointerException | IOException e) {
			throw new DataAccessException("Exception when reading data: ", e);
		}
	}

	public List<InterbankRate> list(byte[] prefix) throws DataAccessException {
		HTableInterface table;
		try {
			table = connection.getTable(TABLE_NAME);

			ResultScanner results = table.getScanner(DaoHelper.mkScan(prefix));
			List<InterbankRate> ret = new ArrayList<InterbankRate>();
			for (Result result : results) {
				ret.add(toInterbankRate(result));
			}
			table.close();
			return ret;
		} catch (NullPointerException | IOException e) {
			throw new DataAccessException("Exception when reading data: ", e);
		}

	}

	public List<InterbankRate> list(char provider) throws DataAccessException {
		try {

			byte provByte = (byte) provider;
			byte[] prefix = new byte[1];

			int offset = 0;
			offset = Bytes.putByte(prefix, offset, provByte);

			return list(prefix);
		} catch (Exception e) {
			throw new DataAccessException("Exception when reading data: ", e);
		}
	}

	@Override
	public List<InterbankRate> listAll() throws DataAccessException {
		HTableInterface table;
		try {
			table = connection.getTable(TABLE_NAME);

			ResultScanner results = table.getScanner(DaoHelper.mkScan());
			List<InterbankRate> ret = new ArrayList<InterbankRate>();
			for (Result result : results) {
				ret.add(toInterbankRate(result));
			}
			table.close();
			return ret;
		} catch (NullPointerException | IOException e) {
			throw new DataAccessException("Exception when reading data: ", e);
		}
	}
}
