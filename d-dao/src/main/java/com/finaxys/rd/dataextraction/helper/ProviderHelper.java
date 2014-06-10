/*
 * 
 */
package com.finaxys.rd.dataextraction.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;

import com.finaxys.rd.dataextraction.msg.Document.ContentType;

// TODO: Auto-generated Javadoc
/**
 * The Class ProviderHelper.
 */
public class ProviderHelper {

	/** The Constant MD5_LENGTH. */
	public static final int MD5_LENGTH = Integer.valueOf(Configuration.MD5_LENGTH.get()); // bytes

	/** The Constant YQL_HOST. */
	private static final String YQL_HOST = Configuration.YQL_HOST.get();
	
	/** The Constant YQL_PATH. */
	private static final String YQL_PATH = Configuration.YQL_PATH.get();
	
	/** The Constant YQL_QUERY_PARAM. */
	private static final String YQL_QUERY_PARAM = Configuration.YQL_QUERY_PARAM.get();
	
	/** The Constant YQL_FORMAT_PARAM. */
	private static final String YQL_FORMAT_PARAM = Configuration.YQL_FORMAT_PARAM.get();
	
	/** The Constant YQL_ENV_PARAM. */
	private static final String YQL_ENV_PARAM = Configuration.YQL_ENV_PARAM.get();

	/**
	 * Construct query.
	 *
	 * @param query the query
	 * @param symbs the symbs
	 * @return the string
	 */
	public static String constructQuery(String query, String symbs) {
		return query.replaceFirst("\\?", symbs);
	}

	/**
	 * Contruct yql uri.
	 *
	 * @param query the query
	 * @param format the format
	 * @param env the env
	 * @return the uri
	 * @throws URISyntaxException the URI syntax exception
	 */
	public static URI contructYqlUri(String query, ContentType format, String env) throws URISyntaxException {

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair(YQL_QUERY_PARAM, query));
		params.add(new BasicNameValuePair(YQL_FORMAT_PARAM, format.getName()));
		params.add(new BasicNameValuePair(YQL_ENV_PARAM, env));

		URIBuilder builder = new URIBuilder().setScheme("https").setHost(YQL_HOST).setPath(YQL_PATH)
				.setParameters(params);
		return builder.build();
	}

	/**
	 * Gets the path.
	 *
	 * @param folder the folder
	 * @param file the file
	 * @param ext the ext
	 * @return the path
	 */
	public static String getPath(String folder, String file, String ext) {
		return folder + "/" + file + "." + ext;
	}

	/**
	 * Gets the resource file.
	 *
	 * @param path the path
	 * @return the resource file
	 */
	public static File getResourceFile(String path){
		File f = new File(ProviderHelper.class.getResource(path).getPath());
		return f;

	}

	/**
	 * To bytes.
	 *
	 * @param file the file
	 * @return the byte[]
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static byte[] toBytes(File file) throws IOException {
		return FileUtils.readFileToByteArray(file);
	}
	

	/**
	 * Md5sum.
	 *
	 * @param s the s
	 * @return the byte[]
	 */
	public static byte[] md5sum(String s) {
		MessageDigest d;
		try {
			d = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("MD5 algorithm not available!", e);
		}

		return d.digest(Bytes.toBytes(s));
	}
	
	/**
	 * The Enum Configuration.
	 */
	public enum Configuration {

		/** The yql host. */
		YQL_HOST("provider.yahoo.yql.host"),
		
		/** The yql path. */
		YQL_PATH("provider.yahoo.yql.path"),
		
		/** The yql query param. */
		YQL_QUERY_PARAM("provider.yahoo.yql.query_param"),
		
		/** The yql format param. */
		YQL_FORMAT_PARAM("provider.yahoo.yql.format_param"),
		
		/** The yql env param. */
		YQL_ENV_PARAM("provider.yahoo.yql.env_param"),
		
		/** The M d5_ length. */
		MD5_LENGTH("provider.helper.md5Length");

	    /** The key. */
    	private final String key;


	    /**
    	 * Instantiates a new configuration.
    	 *
    	 * @param key the key
    	 */
    	Configuration(String key) {
	        this.key = key;
	    }
	    
    	/** The Constant logger. */
    	private final static Logger logger = Logger.getLogger(Configuration.class);
	    //TODo Share resources (properties files) inter modules
	    /** The Constant CONFIG_FILE. */
    	private final static String CONFIG_FILE = "/provider.properties";
	    
    	/** The Constant configuration. */
    	private final static Map<Configuration, String> configuration = new EnumMap<>(Configuration.class);

	    static {
	        readConfigurationFrom(CONFIG_FILE);
	    }

	    /**
    	 * Read configuration from.
    	 *
    	 * @param fileName the file name
    	 */
    	private static void readConfigurationFrom(String fileName)  {
	        try (InputStream resource = Configuration.class.getResourceAsStream(fileName);) {
	            Properties properties = new Properties();
	            properties.load(resource); //throws a NPE if resource not founds
	            for (String key : properties.stringPropertyNames()) {
	            	Configuration c = getConfigurationKey(key);
	            	if(c!=null)
	                configuration.put(c, properties.getProperty(key));
	            }
	        } catch (IllegalArgumentException | IOException | NullPointerException e) {
	           e.printStackTrace();
	        }
	    }

	    /**
    	 * Gets the configuration key.
    	 *
    	 * @param key the key
    	 * @return the configuration key
    	 */
    	private static Configuration getConfigurationKey(String key) {
	        for (Configuration c : values()) {
	            if (c.key.equals(key)) {
	                return c;
	            }
	        }
	        return null;
	    }

	

	    /**
    	 * Gets the.
    	 *
    	 * @return the property corresponding to the key or null if not found
    	 */
	    public String get() {
	        String c = configuration.get(this);
	        if(c == null) throw new IllegalArgumentException();
	        return c;
	    }
	}
}
