package subscriber;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.util.Date;

import javax.xml.bind.JAXBException;

import msg.Message;

import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.client.HConnectionManager;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Logger;

import receiver.CurrencyPairReceiver;
import receiver.ExchangeReceiver;
import receiver.FXRateReceiver;
import receiver.IndexInfoReceiver;
import receiver.IndexQuoteReceiver;
import receiver.Receiver;
import receiver.StockQuoteReceiver;
import receiver.StockReceiver;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;

import dao.impl.CurrencyPairDaoImpl;
import dao.impl.ExchangeDaoImpl;
import dao.impl.FXRateDaoImpl;
import dao.impl.IndexInfoDaoImpl;
import dao.impl.StockDaoImpl;
import dao.impl.StockQuoteDaoImpl;
import dao.impl.IndexQuoteDaoImpl;

public class MomSubscriber implements Subscriber {

	static Logger logger = Logger.getLogger(MomSubscriber.class);

	public static final String DIRECT_QUEUE = "direct";
	public static final String DIRECT_EXCHANGE_NAME = "myDExch";

	private Connection connection;
	private Receiver receiver;
	private String bindingKey;

	public MomSubscriber(Connection connection, Receiver receiver, String bindingKey) throws IOException {
		super();
		this.connection = connection;
		this.receiver = receiver;
		this.bindingKey = bindingKey;
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

	public String getBindingKey() {
		return bindingKey;
	}

	public void setBindingKey(String bindingKey) {
		this.bindingKey = bindingKey;
	}

	public void subscribe(String bindingKey) throws Exception {
		this.bindingKey = bindingKey;
		subscribe();
	}

	public void subscribe() throws Exception {

		logger.info("creating a channel...");
		Channel channel = connection.createChannel();
		logger.info("channel created...");

		channel.exchangeDeclare(DIRECT_EXCHANGE_NAME, DIRECT_QUEUE);

		String queueName = channel.queueDeclare().getQueue();

		channel.queueBind(queueName, DIRECT_EXCHANGE_NAME, this.bindingKey);

		logger.info("waiting msgs...");
		channel.basicConsume(queueName, true, new QueueingConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
					byte[] body) throws IOException {
				Message message = deserializeMessage(body);
				logger.info("msg received");
				receiver.receive(message);
			}
		});

	}

	private Message deserializeMessage(byte[] body){
		ByteArrayInputStream bis = new ByteArrayInputStream(body);
		ObjectInput in = null;
		try {
			in = new ObjectInputStream(bis);
			return (Message) in.readObject();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		} finally {
			if (in != null)
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			try {
				bis.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}