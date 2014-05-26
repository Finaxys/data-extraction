package subscriber;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.client.HConnectionManager;
import org.apache.log4j.Logger;

import receiver.CurrencyPairReceiver;
import receiver.IndexInfoReceiver;
import receiver.Receiver;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;

import dao.impl.CurrencyPairDaoImpl;
import dao.impl.IndexInfoDaoImpl;

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

	public static void main(String[] args) {

		Thread optThread = new Thread() {
			public void run() {
				HConnection hConnection;
				try {
					ConnectionFactory factory = new ConnectionFactory();
					factory.setHost("localhost");
					Connection connection = factory.newConnection();
					hConnection = HConnectionManager.createConnection(HBaseConfiguration.create());
//					Receiver receiver = new ExchangeReceiver(new ExchangeDaoImpl(hConnection));
//					Subscriber subscriber = new Subscriber(connection, receiver);
//					subscriber.subscribe(ExchangeReceiver.ROUTING_KEY);
					
//					Receiver receiver = new StockSummaryReceiver(new StockSummaryDaoImpl(hConnection));
//					Subscriber subscriber = new Subscriber(connection, receiver);
//					subscriber.subscribe(StockSummaryReceiver.ROUTING_KEY);
					
//					Receiver receiver = new CurrencyPairReceiver(new CurrencyPairDaoImpl(hConnection));
//					Subscriber subscriber = new Subscriber(connection, receiver);
//					subscriber.subscribe(CurrencyPairReceiver.ROUTING_KEY);
			
					Receiver receiver = new IndexInfoReceiver(new IndexInfoDaoImpl(hConnection));
					Subscriber subscriber = new Subscriber(connection, receiver);
					subscriber.subscribe(IndexInfoReceiver.ROUTING_KEY);
			} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		optThread.start();

	}
}
