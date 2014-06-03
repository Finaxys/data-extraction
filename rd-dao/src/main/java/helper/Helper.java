package helper;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import msg.Document;
import msg.Document.ContentType;

import org.apache.commons.io.FileUtils;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;

public class Helper {

	public static final int MD5_LENGTH = 16; // bytes

	private static final String YQL_HOST = "query.yahooapis.com";
	private static final String YQL_PATH = "/v1/public/yql";
	private static final String YQL_QUERY_PARAM = "q";
	private static final String YQL_FORMAT_PARAM = "format";
	private static final String YQL_ENV_PARAM = "env";

	public static String constructQuery(String query, String symbs) {
		return query.replaceFirst("\\?", symbs);
	}

	public static URI contructYqlUri(String query, ContentType format, String env) throws URISyntaxException {

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair(YQL_QUERY_PARAM, query));
		params.add(new BasicNameValuePair(YQL_FORMAT_PARAM, format.getName()));
		params.add(new BasicNameValuePair(YQL_ENV_PARAM, env));

		URIBuilder builder = new URIBuilder().setScheme("https").setHost(YQL_HOST).setPath(YQL_PATH)
				.setParameters(params);
		return builder.build();
	}

	public static String getPath(String folder, String file, String ext) {
		return folder + "/" + file + "." + ext;
	}

	public static File getFile(String path) throws Exception {
		File f = new File(path);
		return f;

	}

	public static byte[] toBytes(File file) throws IOException {
		return FileUtils.readFileToByteArray(file);
	}
	

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
