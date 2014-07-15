package com.finaxys.rd.dataextraction.dao.integration.ebf;

import java.io.File;
import java.net.URI;
import java.util.concurrent.Future;

import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.nio.client.methods.HttpAsyncMethods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.finaxys.rd.dataextraction.dao.integration.HistInterbankRateDataGateway;
import com.finaxys.rd.dataextraction.dao.integration.InterbankRateDataGateway;
import com.finaxys.rd.dataextraction.dao.integration.yahoo.HttpResponseConsumer;
import com.finaxys.rd.dataextraction.domain.msg.Document;
import com.finaxys.rd.dataextraction.domain.msg.Document.ContentType;
import com.finaxys.rd.dataextraction.domain.msg.Document.DataClass;
import com.finaxys.rd.dataextraction.domain.msg.Document.DataType;
import com.finaxys.rd.dataextraction.helper.GatewayHelper;

public class EBFEuriborGateway implements InterbankRateDataGateway, HistInterbankRateDataGateway {


	@Value("${gateway.ebf.symbol:3}")
	public char EBF_PROVIDER_SYMB;



	/** The client. */
	@Autowired
	private CloseableHttpAsyncClient client;


	/** The content type. */
	private ContentType contentType;

	public EBFEuriborGateway(ContentType contentType) {
		super();
		this.contentType = contentType;
	}

	public EBFEuriborGateway(CloseableHttpAsyncClient client, ContentType contentType) {
		super();
		this.client = client;
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



	public ContentType getContentType() {
		return contentType;
	}

	public void setContentType(ContentType contentType) {
		this.contentType = contentType;
	}

	public Document getHistRatesData(Integer year) throws Exception {
		try {
			URI uri = GatewayHelper.contructEBFHistEuriborUri(year,contentType);
			File tFile = File.createTempFile("euriborTemp", "." + contentType);
			if (!client.isRunning())
				client.start();
			Future<File> future = client
					.execute(HttpAsyncMethods.createGet(uri), new HttpResponseConsumer(tFile), null);
			File file = future.get();
			if (file != null && file.length() > 0)
				return new Document(contentType, DataType.HIST, DataClass.InterbankRatesData, EBF_PROVIDER_SYMB, GatewayHelper.toBytes(file));
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

	public Document getCurrentRatesData() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}



}
