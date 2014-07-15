package com.finaxys.rd.dataextraction.dao.integration.yahoo;

import java.io.File;
import java.net.URI;
import java.util.concurrent.Future;

import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.nio.client.methods.HttpAsyncMethods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.finaxys.rd.dataextraction.dao.integration.OptionChainGateway;
import com.finaxys.rd.dataextraction.domain.msg.Document;
import com.finaxys.rd.dataextraction.domain.msg.Document.ContentType;
import com.finaxys.rd.dataextraction.domain.msg.Document.DataClass;
import com.finaxys.rd.dataextraction.domain.msg.Document.DataType;
import com.finaxys.rd.dataextraction.helper.GatewayHelper;

public class YahooOptionChainGateway implements OptionChainGateway{


	/** The y provider symb. */
	@Value("${gateway.yahoo.symbol:1}")
	public char Y_PROVIDER_SYMB;

	/** The yql default env. */
	@Value("${gateway.yahoo.yqlDefaultEnv}")
	private String YQL_DEFAULT_ENV;

	/** The yql option chain query. */
	@Value("${gateway.yahoo.yqlOptionChainQuery}")
	private String YQL_OPTION_CHAIN_QUERY;

	/** The client. */
	@Autowired
	private CloseableHttpAsyncClient client;

	/** The content type. */
	private ContentType contentType;

	/**
	 * Instantiates a new yahoo option gateway.
	 * 
	 * @param client
	 *            the client
	 */
	public YahooOptionChainGateway(CloseableHttpAsyncClient client) {
		super();
		this.client = client;
	}
	

	public YahooOptionChainGateway(ContentType contentType) {
		super();
		this.contentType = contentType;
	}

	public YahooOptionChainGateway(CloseableHttpAsyncClient client, ContentType contentType) {
		super();
		this.client = client;
		this.contentType = contentType;
	}

	public ContentType getContentType() {
		return contentType;
	}

	public void setContentType(ContentType contentType) {
		this.contentType = contentType;
	}



	/**
	 * Gets the client.
	 * 
	 * @return the client
	 */
	public CloseableHttpAsyncClient getClient() {
		return client;
	}

	/**
	 * Sets the client.
	 * 
	 * @param client
	 *            the new client
	 */
	public void setClient(CloseableHttpAsyncClient client) {
		this.client = client;
	}


	public Document getOptionChains(String symbols) throws Exception {
		try {
			client.start();
			String query = GatewayHelper.constructQuery(YQL_OPTION_CHAIN_QUERY, symbols);
			URI uri = GatewayHelper.contructYqlUri(query, contentType, YQL_DEFAULT_ENV);
			File tFile = File.createTempFile("optionChainsTemp", "." + contentType);
			Future<File> future = client
					.execute(HttpAsyncMethods.createGet(uri), new HttpResponseConsumer(tFile), null);
			File file = future.get();
			if (file != null && file.length() > 0)
				return new Document(contentType, DataType.Ref, DataClass.OptionChain, Y_PROVIDER_SYMB,
						GatewayHelper.toBytes(file));
			else
				return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			// client.close();
		}
	}


	

}
