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

import com.finaxys.rd.dataextraction.dao.OptionDao;
import com.finaxys.rd.dataextraction.dao.exception.DataAccessException;
import com.finaxys.rd.dataextraction.dao.helper.DaoHelper;
import com.finaxys.rd.dataextraction.domain.Option;

// TODO: Auto-generated Javadoc
/**
 * The Class OptionDaoImpl.
 */
public class OptionDaoImpl implements OptionDao {

	/** The table name. */
	@Value("#{'${dao.option.tableName:option}'.bytes}")
	private byte[] TABLE_NAME;

	/** The info fam. */
	@Value("#{'${dao.option.infoFam:i}'.bytes}")
	private byte[] INFO_FAM;

	/** The connection. */
	@Autowired
	private HConnection connection;

	// Constructor
	/**
	 * Instantiates a new option dao impl.
	 */
	public OptionDaoImpl() {
		super();
	}

	/**
	 * Instantiates a new option dao impl.
	 *
	 * @param connection
	 *            the connection
	 */
	public OptionDaoImpl(HConnection connection) {
		super();
		this.connection = connection;

	}

	// Helpers

	/**
	 * To option summary.
	 *
	 * @param result
	 *            the result
	 * @return the option
	 */
	private Option toOption(Result result) {
		Option option = null;

		try {
			option = new Option();
			List<Field> attributes = new ArrayList<Field>();
			Class<?> tmpClass = option.getClass();
			while (tmpClass != null) {
				attributes.addAll(Arrays.asList(tmpClass.getDeclaredFields()));
				tmpClass = tmpClass.getSuperclass();
			}

			byte[] value;
			for (Field attribute : attributes) {

				if ((value = result.getValue(INFO_FAM, attribute.getName()
						.getBytes())) != null)
					PropertyUtils.setSimpleProperty(option,
							attribute.getName(),
							DaoHelper.getTypedValue(attribute, value));

			}

			return option;
		} catch (IllegalAccessException | InvocationTargetException
				| NoSuchMethodException | NullPointerException e) {
			throw new DataAccessException("Exception when reading data: ", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.finaxys.rd.marketdataprovider.dao.OptionDao#list(java.lang.String )
	 */
	public List<Option> list(String prefix) throws DataAccessException {
		HTableInterface table;
		try {
			table = connection.getTable(TABLE_NAME);

			ResultScanner results = table.getScanner(DaoHelper.mkScan(prefix));
			List<Option> ret = new ArrayList<Option>();
			for (Result result : results) {
				ret.add(toOption(result));
			}
			table.close();
			return ret;
		} catch (NullPointerException | IOException e) {
			throw new DataAccessException("Exception when reading data: ", e);
		}
	}

	public List<Option> list(byte[] prefix) throws DataAccessException {
		HTableInterface table;
		try {
			table = connection.getTable(TABLE_NAME);

			ResultScanner results = table.getScanner(DaoHelper.mkScan(prefix));
			List<Option> ret = new ArrayList<Option>();
			for (Result result : results) {
				ret.add(toOption(result));
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
	 * @see com.finaxys.rd.marketdataprovider.dao.OptionDao#listAll()
	 */
	public List<Option> listAll() throws DataAccessException {
		HTableInterface table;
		try {
			table = connection.getTable(TABLE_NAME);

			ResultScanner results = table.getScanner(DaoHelper.mkScan());
			List<Option> ret = new ArrayList<Option>();
			for (Result result : results) {
				ret.add(toOption(result));
			}
			table.close();
			return ret;
		} catch (NullPointerException | IOException e) {
			throw new DataAccessException("Exception when reading data: ", e);
		}
	}

	public List<Option> list(char provider, String exchSymb)
			throws DataAccessException {
		try {
			byte provByte = (byte) provider;
			byte[] exchSymbHash = DaoHelper.md5sum(exchSymb);
			byte[] prefix = new byte[DaoHelper.MD5_LENGTH + 1];

			int offset = 0;
			offset = Bytes.putByte(prefix, offset, provByte);
			Bytes.putBytes(prefix, offset, exchSymbHash, 0, DaoHelper.MD5_LENGTH);

			return list(prefix);
		} catch (Exception e) {
			throw new DataAccessException("Exception when reading data: ", e);
		}

	}

}
