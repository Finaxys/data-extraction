package com.finaxys.rd.dataextraction.service.integration.publisher;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.xml.stream.XMLStreamException;

import org.apache.log4j.Logger;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;

import com.finaxys.rd.dataextraction.domain.msg.Message;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.client.urlconnection.HTTPSProperties;

public class ScaledRiskPublisher implements Publisher {

	/** The logger. */
	static Logger logger = Logger.getLogger(ScaledRiskPublisher.class);
	
	@Value("${publisher.scaledrisk.url}")
	private String url;

	@Value("${publisher.scaledrisk.auth.url}")
	private String authUrl;

	@Value("${publisher.scaledrisk.auth.login}")
	private String login;
	@Value("${publisher.scaledrisk.auth.password}")
	private String password;
	@Value("${publisher.scaledrisk.auth.loginVal}")
	private String loginVal;
	@Value("${publisher.scaledrisk.auth.passwordVal}")
	private String passwordVal;
	@Value("${publisher.scaledrisk.json.docHeader}")
	private String docHeader;
	@Value("${publisher.scaledrisk.json.activityId}")
	private String activityId;
	@Value("${publisher.scaledrisk.json.userId}")
	private String userId;
	@Value("${publisher.scaledrisk.json.activityIdVal}")
	private String activityIdVal;
	@Value("${publisher.scaledrisk.json.userIdVal}")
	private String userIdVal;
	@Value("${publisher.scaledrisk.json.sourceTypeName}")
	private String sourceTypeName;
	@Value("${publisher.scaledrisk.json.data}")
	private String data;

	public ScaledRiskPublisher() {
		super();
	}

	public Client hostIgnoringClient() {
		try {

			TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				public void checkClientTrusted(X509Certificate[] certs, String authType) {
				}

				public void checkServerTrusted(X509Certificate[] certs, String authType) {
				}
			} };

			SSLContext sslcontext = SSLContext.getInstance("TLS");
			sslcontext.init(null, trustAllCerts, new SecureRandom());
			DefaultClientConfig config = new DefaultClientConfig();
			Map<String, Object> properties = config.getProperties();
			HTTPSProperties httpsProperties = new HTTPSProperties(new HostnameVerifier() {

				@Override
				public boolean verify(String s, SSLSession sslSession) {

					return true;
				}

			}, sslcontext);
			properties.put(HTTPSProperties.PROPERTY_HTTPS_PROPERTIES, httpsProperties);
			config.getClasses().add(JacksonJsonProvider.class);
			return Client.create(config);
		} catch (KeyManagementException | NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void publish(Message message) throws IOException, XMLStreamException {
		//
		// ClientConfig config = new DefaultClientConfig();
		// Client client = Client.create(config);
		
		Client client = hostIgnoringClient();
		try {
			logger.info("Authentification pour scaledRisk ws");
			WebResource authService = client.resource(authUrl);

			JSONObject authInput = new JSONObject().append(login, loginVal).append(password, passwordVal)
					.append(docHeader, new JSONObject().append(activityId, activityIdVal).append(userId, userIdVal));

			ClientResponse authResponse = authService.accept(MediaType.APPLICATION_JSON).post(ClientResponse.class,
					authInput.toString().replace('[', ' ').replace(']', ' '));

			logger.info("status: " + authResponse.getStatus());
			logger.info("response: " + authResponse.getEntity(String.class));
			logger.info("cookies: " + authResponse.getCookies().toString());


			logger.info("publishing data to scaledRisk...");
			

			logger.info("body: " + new String(message.getBody().getContent()));
			
			
			WebResource service = client.resource(url);// https://82.123.93.226:7006/ScaledRisk/toypes?name=loadCsvData
			WebResource.Builder builder = service.getRequestBuilder();
			for (NewCookie c : authResponse.getCookies())
				builder = builder.cookie(c);
			
			JSONObject jsonInput = new JSONObject().append(sourceTypeName, message.getRoutingKey())
					.append(data, new String(message.getBody().getContent()))
					.append(docHeader, new JSONObject().append(activityId, activityIdVal).append(userId, userIdVal));

			ClientResponse response = builder.accept(MediaType.APPLICATION_JSON).post(ClientResponse.class,
					jsonInput.toString().replace('[', ' ').replace(']', ' '));

			logger.info("Status: " + response.getStatus());
			logger.info("response: " + response.getEntity(String.class));

		} catch (JSONException e) {
			e.printStackTrace();
		}

		client.destroy();
		
	}

	@Override
	public void publish(List<Message> messages) throws Exception {
		for (Message m : messages)
			this.publish(m);

	}

}