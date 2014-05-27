package utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.hadoop.hbase.util.Bytes;

public class Md5Utils {

	public static final int MD5_LENGTH = 16; // bytes

	public static byte[] md5sum(String s) {
		MessageDigest d;
		try {
			d = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("MD5 algorithm not available!", e);
		}

		return d.digest(Bytes.toBytes(s));
	}
	
	
	public static void main(String[] args) {
		System.out.println(Md5Utils.mkRowKey(1, "", ""));		
	}
	
	public static byte[] mkRowKey(Integer provider, String suffix, String mic) {
		byte[] suffixHash = Md5Utils.md5sum(suffix);
		byte[] provHash = Md5Utils.md5sum(provider + suffixHash.toString());
		byte[] micb = Bytes.toBytes(mic);
		byte[] rowkey = new byte[Md5Utils.MD5_LENGTH + micb.length]; // mic code length = 4
		int offset = 0;
		offset = Bytes.putBytes(rowkey, offset, provHash, 0, Md5Utils.MD5_LENGTH);
		Bytes.putBytes(rowkey, offset, micb, 0, micb.length);

		return rowkey;
	}

}
