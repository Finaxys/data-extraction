package publisher;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Publisher {
	static Logger logger = Logger.getLogger(Publisher.class);

	public static final String DIRECT_QUEUE = "direct";
	public static final String DIRECT_EXCHANGE_NAME = "myDExch";
	public static final String EXCHANGES_ROUTING_KEY = "exchanges";
	public static final String STOCK_SUMMARIES_ROUTING_KEY = "stockSummaries";
	public static final String HIST_DATA_ROUTING_KEY = "histData";
	public static final String CURRENCY_PAIRS_ROUTING_KEY = "currencyPairs";



	public void publish(byte[] data, String routingKey) throws IOException {
		
		// create a connection to rabitmq server
		logger.info("creating a connection to rabitmq server");
		ConnectionFactory factory = new ConnectionFactory();
		logger.info("connection to rabitmq server created");

		factory.setHost("localhost");
		Connection connection = factory.newConnection();

		logger.info("creating a channel...");
		Channel channel = connection.createChannel();
		logger.info("channel created...");

		// Declare exchange
		channel.exchangeDeclare(DIRECT_EXCHANGE_NAME, DIRECT_QUEUE);

		logger.info("publishing the message...");
		channel.basicPublish(DIRECT_EXCHANGE_NAME, routingKey, null, data);
		
		logger.info("msg published");
		channel.close();
		logger.info("channel closed");
		connection.close();
		logger.info("connection closed");

	}


}
