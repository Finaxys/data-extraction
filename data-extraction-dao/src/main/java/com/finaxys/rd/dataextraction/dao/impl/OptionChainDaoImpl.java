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

import com.finaxys.rd.dataextraction.dao.OptionChainDao;
import com.finaxys.rd.dataextraction.dao.exception.DataAccessException;
import com.finaxys.rd.dataextraction.dao.helper.DaoHelper;
import com.finaxys.rd.dataextraction.domain.OptionChain;

// TODO: Auto-generated Javadoc
/**
 * The Class OptionChainDaoImpl.
 */
public class OptionChainDaoImpl implements OptionChainDao {

	

	/** The table name. */
	@Value("#{'${dao.optionChain.tableName:optionChain}'.bytes}")
	private byte[] TABLE_NAME;

	/** The info fam. */
	@Value("#{'${dao.optionChain.infoFam:i}'.bytes}")
	private byte[] INFO_FAM;


	/** The connection. */
	@Autowired
	private HConnection connection;

	// Constructor
	/**
	 * Instantiates a new optionChain dao impl.
	 */
	public OptionChainDaoImpl() {
		super();
	}

	/**
	 * Instantiates a new optionChain dao impl.
	 * 
	 * @param connection
	 *            the connection
	 */
	public OptionChainDaoImpl(HConnection connection) {
		super();
		this.connection = connection;

	}

	// Helpers

	/**
	 * To optionChain summary.
	 * 
	 * @param result
	 *            the result
	 * @return the optionChain
	 */
	private OptionChain toOptionChain(Result result) {
		OptionChain optionChain = null;

		try {
			optionChain = new OptionChain();
			List<Field> attributes = new ArrayList<Field>();
		    Class<?> tmpClass = optionChain.getClass();
		    while (tmpClass != null) {
		    	attributes.addAll(Arrays.asList(tmpClass.getDeclaredFields()));
		        tmpClass = tmpClass .getSuperclass();
		    }

			byte[] value;
			for (Field attribute : attributes) {

				if ((value = result.getValue(INFO_FAM, attribute.getName().getBytes())) != null)
					PropertyUtils.setSimpleProperty(optionChain,  attribute.getName(), DaoHelper.getTypedValue( attribute, value));
					

			}
			return optionChain;
		} catch (IllegalAccessException | InvocationTargetException
				| NoSuchMethodException | NullPointerException e) {
			throw new DataAccessException("Exception when reading data: ", e);
		}
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.finaxys.rd.marketdataprovider.dao.OptionChainDao#list(java.lang.String
	 * )
	 */
	public List<OptionChain> list(String prefix) throws DataAccessException {
		HTableInterface table;
		try {
			table = connection.getTable(TABLE_NAME);
		

		ResultScanner results = table.getScanner(DaoHelper.mkScan(prefix));
		List<OptionChain> ret = new ArrayList<OptionChain>();
		for (Result result : results) {
			ret.add(toOptionChain(result));
		}
		table.close();
		return ret;
		} catch (NullPointerException | IOException e) {
			throw new DataAccessException("Exception when reading data: ", e);
		}
	}
	
	
	 
	public List<OptionChain> list(byte[] prefix) throws DataAccessException {
		HTableInterface table;
		try {
			table = connection.getTable(TABLE_NAME);
		

		ResultScanner results = table.getScanner(DaoHelper.mkScan(prefix));
		List<OptionChain> ret = new ArrayList<OptionChain>();
		for (Result result : results) {
			ret.add(toOptionChain(result));
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
	 * @see com.finaxys.rd.marketdataprovider.dao.OptionChainDao#listAll()
	 */
	public List<OptionChain> listAll() throws DataAccessException {
		HTableInterface table;
		try {
			table = connection.getTable(TABLE_NAME);
		
		ResultScanner results = table.getScanner(DaoHelper.mkScan());
		List<OptionChain> ret = new ArrayList<OptionChain>();
		for (Result result : results) {
			ret.add(toOptionChain(result));
		}
		table.close();
		return ret;
		} catch (NullPointerException | IOException e) {
			throw new DataAccessException("Exception when reading data: ", e);
		}
	}

	public List<OptionChain> list(char provider) throws DataAccessException {
		byte provByte = (byte) provider;
		byte[] prefix = new byte[1];

		int offset = 0;
		offset = Bytes.putByte(prefix, offset, provByte);

		return list(prefix);

	}
	
}
