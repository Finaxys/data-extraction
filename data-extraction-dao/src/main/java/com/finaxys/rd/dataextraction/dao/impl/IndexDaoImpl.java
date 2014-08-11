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

import com.finaxys.rd.dataextraction.dao.IndexDao;
import com.finaxys.rd.dataextraction.dao.exception.DataAccessException;
import com.finaxys.rd.dataextraction.dao.helper.DaoHelper;
import com.finaxys.rd.dataextraction.domain.Index;

// TODO: Auto-generated Javadoc
/**
 * The Class IndexDaoImpl.
 */
public class IndexDaoImpl implements IndexDao {

	/** The table name. */
	@Value("#{'${dao.index.tableName:index_info}'.bytes}")
	private byte[] TABLE_NAME;

	/** The info fam. */
	@Value("#{'${dao.index.infoFam:i}'.bytes}")
	private byte[] INFO_FAM;

	/** The connection. */
	@Autowired
	private HConnection connection;

	// Constructors

	/**
	 * Instantiates a new index dao impl.
	 */
	public IndexDaoImpl() {
		super();
	}

	/**
	 * Instantiates a new index dao impl.
	 *
	 * @param connection
	 *            the connection
	 */
	public IndexDaoImpl(HConnection connection) {
		super();
		this.connection = connection;
	}

	// Helpers

	/**
	 * To index.
	 *
	 * @param result
	 *            the result
	 * @return the index
	 */
	private Index toIndex(Result result) {
		Index index = null;

		try {
			index = new Index();
			List<Field> attributes = new ArrayList<Field>();
			Class<?> tmpClass = index.getClass();
			while (tmpClass != null) {
				attributes.addAll(Arrays.asList(tmpClass.getDeclaredFields()));
				tmpClass = tmpClass.getSuperclass();
			}

			byte[] value;
			for (Field attribute : attributes) {

				if ((value = result.getValue(INFO_FAM, attribute.getName()
						.getBytes())) != null)
					PropertyUtils.setSimpleProperty(index, attribute.getName(),
							DaoHelper.getTypedValue(attribute, value));

			}

			return index;
		} catch (IllegalAccessException | InvocationTargetException
				| NoSuchMethodException | NullPointerException e) {
			throw new DataAccessException("Exception when reading data: ", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.finaxys.rd.marketdataprovider.dao.IndexDao#list(java.lang.String)
	 */
	public List<Index> list(String prefix) throws DataAccessException {
		HTableInterface table;
		try {
			table = connection.getTable(TABLE_NAME);

			ResultScanner results = table.getScanner(DaoHelper.mkScan(prefix));
			List<Index> ret = new ArrayList<Index>();
			for (Result result : results) {
				ret.add(toIndex(result));
			}
			table.close();
			return ret;
		} catch (NullPointerException | IOException e) {
			throw new DataAccessException("Exception when reading data: ", e);
		}
	}

	public List<Index> list(byte[] prefix) throws DataAccessException {
		HTableInterface table;
		try {
			table = connection.getTable(TABLE_NAME);

			ResultScanner results = table.getScanner(DaoHelper.mkScan(prefix));
			List<Index> ret = new ArrayList<Index>();
			for (Result result : results) {
				ret.add(toIndex(result));
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
	 * @see com.finaxys.rd.marketdataprovider.dao.IndexDao#listAll()
	 */
	public List<Index> listAll() throws DataAccessException {
		HTableInterface table;
		try {
			table = connection.getTable(TABLE_NAME);

			ResultScanner results = table.getScanner(DaoHelper.mkScan());
			List<Index> ret = new ArrayList<Index>();
			for (Result result : results) {
				ret.add(toIndex(result));
			}
			table.close();
			return ret;
		} catch (NullPointerException | IOException e) {
			throw new DataAccessException("Exception when reading data: ", e);
		}
	}

	public List<Index> list(char provider, String exchSymb)
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
}
