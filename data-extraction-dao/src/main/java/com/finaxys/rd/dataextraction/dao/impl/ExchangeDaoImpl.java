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

import com.finaxys.rd.dataextraction.dao.ExchangeDao;
import com.finaxys.rd.dataextraction.dao.exception.DataAccessException;
import com.finaxys.rd.dataextraction.dao.helper.DaoHelper;
import com.finaxys.rd.dataextraction.domain.Exchange;

// TODO: Auto-generated Javadoc
/**
 * The Class ExchangeDaoImpl.
 */
public class ExchangeDaoImpl implements ExchangeDao {

	/** The table name. */
	@Value("#{'${dao.exchange.tableName:exchange}'.bytes}")
	private byte[] TABLE_NAME;

	/** The info fam. */
	@Value("#{'${dao.exchange.infoFam:i}'.bytes}")
	private byte[] INFO_FAM;

	/** The connection. */
	@Autowired
	private HConnection connection;

	// Constructors

	/**
	 * Instantiates a new exchange dao impl.
	 */
	public ExchangeDaoImpl() {
	}

	/**
	 * Instantiates a new exchange dao impl.
	 * 
	 * @param connection
	 *            the connection
	 */
	public ExchangeDaoImpl(HConnection connection) {
		this.connection = connection;
	}

	/**
	 * Gets the connection.
	 * 
	 * @return the connection
	 */
	public HConnection getConnection() {
		return connection;
	}

	/**
	 * Sets the connection.
	 * 
	 * @param connection
	 *            the new connection
	 */
	public void setConnection(HConnection connection) {
		this.connection = connection;
	}

	// Helpers

	/**
	 * To exchange.
	 * 
	 * @param result
	 *            the result
	 * @return the exchange
	 */

	private Exchange toExchange(Result result) throws DataAccessException {

		Exchange exchange = null;
		try {
			exchange = new Exchange();
			List<Field> attributes = new ArrayList<Field>();
			Class<?> tmpClass = exchange.getClass();
			while (tmpClass != null) {
				attributes.addAll(Arrays.asList(tmpClass.getDeclaredFields()));
				tmpClass = tmpClass.getSuperclass();
			}

			byte[] value;
			for (Field attribute : attributes) {

				if ((value = result.getValue(INFO_FAM, attribute.getName()
						.getBytes())) != null)
					PropertyUtils.setSimpleProperty(exchange,
							attribute.getName(),
							DaoHelper.getTypedValue(attribute, value));

			}

			return exchange;

		} catch (IllegalAccessException | InvocationTargetException
				| NoSuchMethodException | NullPointerException e) {
			throw new DataAccessException("Exception when reading data: ", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.finaxys.rd.marketdataprovider.dao.ExchangeDao#list(java.lang.String)
	 */
	public List<Exchange> list(String prefix) throws DataAccessException {
		HTableInterface table;
		try {
			table = connection.getTable(TABLE_NAME);
		
		ResultScanner results = table.getScanner(DaoHelper.mkScan(prefix));
		List<Exchange> ret = new ArrayList<Exchange>();
		for (Result result : results) {
			ret.add(toExchange(result));
		}
		table.close();
		return ret;
		} catch (NullPointerException | IOException e) {
			throw new DataAccessException("Exception when reading data: ", e);
		}
	}

	public List<Exchange> list(byte[] prefix) throws DataAccessException {
		HTableInterface table;
		try {
			table = connection.getTable(TABLE_NAME);
		

		ResultScanner results = table.getScanner(DaoHelper.mkScan(prefix));
		List<Exchange> ret = new ArrayList<Exchange>();
		for (Result result : results) {
			ret.add(toExchange(result));
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
	 * @see com.finaxys.rd.marketdataprovider.dao.ExchangeDao#listAll()
	 */
	public List<Exchange> listAll() throws DataAccessException {
		HTableInterface table;
		try {
			table = connection.getTable(TABLE_NAME);
		
		ResultScanner results = table.getScanner(DaoHelper.mkScan());
		List<Exchange> ret = new ArrayList<Exchange>();
		for (Result result : results) {
			ret.add(toExchange(result));
		}
		table.close();
		return ret;
		} catch (NullPointerException | IOException e) {
			throw new DataAccessException("Exception when reading data: ", e);
		}
	}

	public List<Exchange> list(char provider) throws DataAccessException {
		try{
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
