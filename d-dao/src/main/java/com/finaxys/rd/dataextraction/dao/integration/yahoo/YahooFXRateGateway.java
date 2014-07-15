package com.finaxys.rd.dataextraction.dao.integration.yahoo;

import java.io.File;
import java.net.URI;
import java.util.concurrent.Future;

import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.nio.client.methods.HttpAsyncMethods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.finaxys.rd.dataextraction.dao.integration.FXRateGateway;
import com.finaxys.rd.dataextraction.domain.msg.Document;
import com.finaxys.rd.dataextraction.domain.msg.Document.ContentType;
import com.finaxys.rd.dataextraction.domain.msg.Document.DataClass;
import com.finaxys.rd.dataextraction.domain.msg.Document.DataType;
import com.finaxys.rd.dataextraction.helper.GatewayHelper;

public class YahooFXRateGateway implements FXRateGateway {

	/** The y provider symb. */
	@Value("${gateway.yahoo.symbol:1}")
	public char Y_PROVIDER_SYMB;

	/** The yql default env. */
	@Value("${gateway.yahoo.yqlDefaultEnv}")
	private String YQL_DEFAULT_ENV;

	/** The yql fxrate query. */
	@Value("${gateway.yahoo.yqlFXRateQuery}")
	private String YQL_FXRATE_QUERY;

	/** The client. */
	@Autowired
	private CloseableHttpAsyncClient client;

	/** The content type. */
	private ContentType contentType;


	public YahooFXRateGateway(ContentType contentType) {
		super();
		this.contentType = contentType;
	}

	public YahooFXRateGateway(CloseableHttpAsyncClient client, ContentType contentType) {
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

	
	public Document getCurrentFXRates(String symbols) throws Exception {
		try {
			String query = GatewayHelper.constructQuery(YQL_FXRATE_QUERY, symbols);
			URI uri = GatewayHelper.contructYqlUri(query, contentType, YQL_DEFAULT_ENV);
			File tFile = File.createTempFile("fxRatesTemp", "." + contentType);
			if (!client.isRunning())
				client.start();
			Future<File> future = client
					.execute(HttpAsyncMethods.createGet(uri), new HttpResponseConsumer(tFile), null);
			File file = future.get();
			if (file != null && file.length() > 0)
				return new Document(contentType, DataType.INTRA, DataClass.FXRate, Y_PROVIDER_SYMB, GatewayHelper.toBytes(file));
			else
				return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			// close() method call shutdown() method so we will not be able to
			// start it again with start() method
			// apache.googlesource.com/httpasyncclient/+/13388984c55daabdd2c3eb139809eeddc76da783/httpasyncclient/src/main/java/org/apache/http/impl/nio/client/InternalHttpAsyncClient.java
			// client.close();
		}
	}

}