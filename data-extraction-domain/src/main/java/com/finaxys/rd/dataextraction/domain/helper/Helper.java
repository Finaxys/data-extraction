/*
 * 
 */
package com.finaxys.rd.dataextraction.domain.helper;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Logger;

// TODO: Auto-generated Javadoc
/**
 * The Class DaoHelper.
 */
public class Helper {

	static Logger logger = Logger.getLogger(Helper.class);

	public static byte[] md5sum(String s) {
		MessageDigest d;
		try {
			d = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("MD5 algorithm not available!", e);
		}

		return d.digest(Bytes.toBytes(s));
	}

}
