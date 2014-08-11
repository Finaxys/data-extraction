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
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.finaxys.rd.dataextraction.dao.StockDao;
import com.finaxys.rd.dataextraction.dao.exception.DataAccessException;
import com.finaxys.rd.dataextraction.dao.helper.DaoHelper;
import com.finaxys.rd.dataextraction.domain.Stock;

// TODO: Auto-generated Javadoc
/**
 * The Class StockDaoImpl.
 */
public class StockDaoImpl implements StockDao {

	/** The table name. */
	@Value("#{'${dao.stock.tableName:stock}'.bytes}")
	private byte[] TABLE_NAME;

	/** The info fam. */
	@Value("#{'${dao.stock.infoFam:i}'.bytes}")
	private byte[] INFO_FAM;

	/** The connection. */
	@Autowired
	private HConnection connection;

	// Constructor
	/**
	 * Instantiates a new stock dao impl.
	 */
	public StockDaoImpl() {
		super();
	}

	/**
	 * Instantiates a new stock dao impl.
	 * 
	 * @param connection
	 *            the connection
	 */
	public StockDaoImpl(HConnection connection) {
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
	private Stock toStock(Result result) {
		Stock stock = null;

		try {
			stock = new Stock();
			List<Field> attributes = new ArrayList<Field>();
			Class<?> tmpClass = stock.getClass();
			while (tmpClass != null) {
				attributes.addAll(Arrays.asList(tmpClass.getDeclaredFields()));
				tmpClass = tmpClass.getSuperclass();
			}

			byte[] value;
			for (Field attribute : attributes) {

				if ((value = result.getValue(INFO_FAM, attribute.getName()
						.getBytes())) != null)
					PropertyUtils.setSimpleProperty(stock, attribute.getName(),
							DaoHelper.getTypedValue(attribute, value));

			}

			return stock;
		} catch (IllegalAccessException | InvocationTargetException
				| NoSuchMethodException | NullPointerException e) {
			throw new DataAccessException("Exception when reading data: ", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.finaxys.rd.marketdataprovider.dao.StockDao#list(java.lang.String)
	 */
	public List<Stock> list(byte[] prefix) throws DataAccessException {
		HTableInterface table;
		try {
			table = connection.getTable(TABLE_NAME);

			ResultScanner results = table.getScanner(DaoHelper.mkScan(prefix));
			List<Stock> ret = new ArrayList<Stock>();
			for (Result result : results) {
				ret.add(toStock(result));
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
	 * @see com.finaxys.rd.marketdataprovider.dao.StockDao#listAll()
	 */
	public List<Stock> listAll() throws DataAccessException {
		HTableInterface table;
		try {
			table = connection.getTable(TABLE_NAME);

			ResultScanner results = table.getScanner(DaoHelper.mkScan());
			List<Stock> ret = new ArrayList<Stock>();
			for (Result result : results) {
				ret.add(toStock(result));
			}
			table.close();
			return ret;
		} catch (NullPointerException | IOException e) {
			throw new DataAccessException("Exception when reading data: ", e);
		}
	}

	public List<Stock> list(char provider, String exchSymb)
			throws DataAccessException {
		try {
			byte provByte = (byte) provider;
			byte[] exchSymbHash = DaoHelper.md5sum(exchSymb);
			byte[] prefix = new byte[DaoHelper.MD5_LENGTH + 1];

			int offset = 0;
			offset = Bytes.putByte(prefix, offset, provByte);
			Bytes.putBytes(prefix, offset, exchSymbHash, 0,
					DaoHelper.MD5_LENGTH);

			return list(prefix);
		} catch (Exception e) {
			throw new DataAccessException("Exception when reading data: ", e);
		}

	}

	public List<Stock> list(char provider) throws DataAccessException {
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
