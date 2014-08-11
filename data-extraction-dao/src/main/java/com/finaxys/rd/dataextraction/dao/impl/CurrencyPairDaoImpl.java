/*
 * 
 */
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

import com.finaxys.rd.dataextraction.dao.CurrencyPairDao;
import com.finaxys.rd.dataextraction.dao.exception.DataAccessException;
import com.finaxys.rd.dataextraction.dao.helper.DaoHelper;
import com.finaxys.rd.dataextraction.domain.CurrencyPair;

// TODO: Auto-generated Javadoc
/**
 * The Class CurrencyPairDaoImpl.
 */
public class CurrencyPairDaoImpl implements CurrencyPairDao {

	/** The table name. */
	@Value("#{'${dao.currencyPair.tableName:currency_pair}'.bytes}")
	private byte[] TABLE_NAME;

	/** The info fam. */
	@Value("#{'${dao.currencyPair.infoFam:i}'.bytes}")
	private byte[] INFO_FAM;

	/** The connection. */
	@Autowired
	private HConnection connection;

	// Constructors

	/**
	 * Instantiates a new currency pair dao impl.
	 */
	public CurrencyPairDaoImpl() {
		super();
	}

	/**
	 * Instantiates a new currency pair dao impl.
	 *
	 * @param connection
	 *            the connection
	 */
	public CurrencyPairDaoImpl(HConnection connection) {
		super();
		this.connection = connection;
	}

	// Helpers

	/**
	 * To currency pair.
	 *
	 * @param result
	 *            the result
	 * @return the currency pair
	 */
	private CurrencyPair toCurrencyPair(Result result)
			throws DataAccessException {
		CurrencyPair currencyPair = null;
		try {
			currencyPair = new CurrencyPair();
			List<Field> attributes = new ArrayList<Field>();
			Class<?> tmpClass = currencyPair.getClass();
			while (tmpClass != null) {
				attributes.addAll(Arrays.asList(tmpClass.getDeclaredFields()));
				tmpClass = tmpClass.getSuperclass();
			}

			byte[] value;
			for (Field attribute : attributes) {

				if ((value = result.getValue(INFO_FAM, attribute.getName()
						.getBytes())) != null)

					PropertyUtils.setSimpleProperty(currencyPair,
							attribute.getName(),
							DaoHelper.getTypedValue(attribute, value));

			}

			return currencyPair;
		} catch (IllegalAccessException | InvocationTargetException
				| NoSuchMethodException | NullPointerException e) {
			throw new DataAccessException("Exception when reading data: ", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.finaxys.rd.marketdataprovider.dao.CurrencyPairDao#list(java.lang.
	 * String)
	 */
	public List<CurrencyPair> list(String prefix) throws DataAccessException {
		HTableInterface table;
		try {
			table = connection.getTable(TABLE_NAME);

			ResultScanner results = table.getScanner(DaoHelper.mkScan(prefix));
			List<CurrencyPair> ret = new ArrayList<CurrencyPair>();
			for (Result result : results) {
				ret.add(toCurrencyPair(result));
			}
			table.close();

			return ret;
		} catch (NullPointerException | IOException e) {
			throw new DataAccessException("Exception when reading data: ", e);
		}
	}

	public List<CurrencyPair> list(byte[] prefix) throws DataAccessException {
		HTableInterface table;
		try {
			table = connection.getTable(TABLE_NAME);

			ResultScanner results = table.getScanner(DaoHelper.mkScan(prefix));
			List<CurrencyPair> ret = new ArrayList<CurrencyPair>();
			for (Result result : results) {
				ret.add(toCurrencyPair(result));
			}
			table.close();
			return ret;
		} catch (NullPointerException | IOException e) {
			throw new DataAccessException("Exception when reading data: ", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.finaxys.rd.marketdataprovider.dao.CurrencyPairDao#listAll()
	 */
	public List<CurrencyPair> listAll() throws DataAccessException {
		HTableInterface table;
		try {
			table = connection.getTable(TABLE_NAME);

			ResultScanner results = table.getScanner(DaoHelper.mkScan());
			List<CurrencyPair> ret = new ArrayList<CurrencyPair>();
			for (Result result : results) {
				ret.add(toCurrencyPair(result));
			}
			table.close();
			return ret;
		} catch (NullPointerException | IOException e) {
			throw new DataAccessException("Exception when reading data: ", e);
		}
	}

	public List<CurrencyPair> list(char provider) throws DataAccessException {
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

}
