package subscriber;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.apache.log4j.Logger;
import receiver.Receiver;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;

public class Subscriber {

	static Logger logger = Logger.getLogger(Subscriber.class);

	public static final String DIRECT_QUEUE = "direct";
	public static final String DIRECT_EXCHANGE_NAME = "myDExch";

	private Connection connection;
	private Receiver receiver;

	public Subscriber(Connection connection, Receiver receiver) throws IOException {
		super();
		this.connection = connection;
		this.receiver = receiver;
	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public Receiver getReceiver() {
		return receiver;
	}

	public void setReceiver(Receiver receiver) {
		this.receiver = receiver;
	}

	public void subscribe(String bindingKey) throws IOException, ShutdownSignalException, ConsumerCancelledException,
			InterruptedException, JAXBException {

		logger.info("creating a channel...");
		Channel channel = connection.createChannel();
		logger.info("channel created...");

		channel.exchangeDeclare(DIRECT_EXCHANGE_NAME, DIRECT_QUEUE);

		String queueName = channel.queueDeclare().getQueue();

		channel.queueBind(queueName, DIRECT_EXCHANGE_NAME, bindingKey);

		logger.info("waiting msgs...");
		channel.basicConsume(queueName, true, new QueueingConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
					byte[] body) throws IOException {
				logger.info("msg received : " + new String(body));
				receiver.receive(body);
			}
		});

	}


}
