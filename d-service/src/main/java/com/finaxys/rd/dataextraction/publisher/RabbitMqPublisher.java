/*
 * 
 */
package com.finaxys.rd.dataextraction.publisher;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.finaxys.rd.dataextraction.converter.Converter;
import com.finaxys.rd.dataextraction.msg.Document;
import com.finaxys.rd.dataextraction.msg.Message;
import com.finaxys.rd.dataextraction.msg.Document.ContentType;
import com.finaxys.rd.dataextraction.msg.Document.DataType;
import com.finaxys.rd.dataextraction.provider.IndexQuoteProvider;
import com.finaxys.rd.dataextraction.provider.impl.yahoo.YahooIndexQuoteProvider;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

// TODO: Auto-generated Javadoc
/**
 * The Class RabbitMqPublisher.
 */
public class RabbitMqPublisher implements Publisher {
	
	/** The logger. */
	static Logger logger = Logger.getLogger(RabbitMqPublisher.class);

	/** The connection. */
	private Connection connection;
	
	/** The queue. */
	private String queue;

	/**
	 * Instantiates a new rabbit mq publisher.
	 *
	 * @param connection the connection
	 * @param queue the queue
	 * @param exchange the exchange
	 */
	public RabbitMqPublisher(Connection connection, String queue, String exchange) {
		super();
		this.connection = connection;
		this.queue = queue;
		this.exchange = exchange;
	}

	/** The exchange. */
	private String exchange;

	/**
	 * Instantiates a new rabbit mq publisher.
	 */
	public RabbitMqPublisher() {
		super();
	}

	/**
	 * Gets the connection.
	 *
	 * @return the connection
	 */
	public Connection getConnection() {
		return connection;
	}

	/**
	 * Sets the connection.
	 *
	 * @param connection the new connection
	 */
	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	/**
	 * Gets the queue.
	 *
	 * @return the queue
	 */
	public String getQueue() {
		return queue;
	}

	/**
	 * Sets the queue.
	 *
	 * @param queue the new queue
	 */
	public void setQueue(String queue) {
		this.queue = queue;
	}

	/**
	 * Gets the exchange.
	 *
	 * @return the exchange
	 */
	public String getExchange() {
		return exchange;
	}

	/**
	 * Sets the exchange.
	 *
	 * @param exchange the new exchange
	 */
	public void setExchange(String exchange) {
		this.exchange = exchange;
	}

	/* (non-Javadoc)
	 * @see com.finaxys.rd.dataextraction.publisher.Publisher#publish(com.finaxys.rd.dataextraction.msg.Message)
	 */
	public void publish(Message message) throws Exception {

		logger.info("creating channel...");
		Channel channel = connection.createChannel();
		channel.exchangeDeclare(exchange, queue);
		logger.info("channel created...");
	

		logger.info("publishing message " + message.getId() + "...");
		channel.basicPublish(exchange, message.getRoutingKey(), MessageProperties.PERSISTENT_TEXT_PLAIN, serializeMessage(message));

		logger.info("msg published");
		channel.close();
		logger.info("channel closed");

	}

	/**
	 * Serialize message.
	 *
	 * @param message the message
	 * @return the byte[]
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
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
