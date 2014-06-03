package publisher;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.List;

import msg.Document;
import msg.Document.ContentType;
import msg.Document.DataType;
import msg.Message;

import org.apache.log4j.Logger;

import provider.IndexQuoteProvider;
import provider.impl.yahoo.YahooIndexQuoteProvider;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import converter.Converter;

public class MomPublisher implements Publisher {
	static Logger logger = Logger.getLogger(MomPublisher.class);

	public static final String DIRECT_QUEUE = "direct";
	public static final String DIRECT_EXCHANGE_NAME = "myDExch";
	public static final String EXCHANGES_ROUTING_KEY = "exchanges";
	public static final String STOCKS_ROUTING_KEY = "stocks";
	public static final String HIST_DATA_ROUTING_KEY = "histData";
	public static final String CURRENCY_PAIRS_ROUTING_KEY = "currencyPairs";
	public static final String INDEX_INFOS_ROUTING_KEY = "indexInfos";
	public static final String STOCKS_QUOTES_ROUTING_KEY = "stocksQuotes";
	public static final String FXRATES_ROUTING_KEY = "fxRates";
	public static final String INDEX_QUOTES_ROUTING_KEY = "indexQuotes";

	public MomPublisher() {
		super();
	}

	public void publish(Message message) throws Exception {

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
		channel.basicPublish(DIRECT_EXCHANGE_NAME, message.getRoutingKey(), null, serializeMessage(message));

		logger.info("msg published");
		channel.close();
		logger.info("channel closed");
		connection.close();
		logger.info("connection closed");

	}

	private byte[] serializeMessage(Message message) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutput out = null;
		try {
			out = new ObjectOutputStream(bos);
			out.writeObject(message);
			return bos.toByteArray();
		} finally {
			if (out != null)
				out.close();
			bos.close();
		}
	}

}
