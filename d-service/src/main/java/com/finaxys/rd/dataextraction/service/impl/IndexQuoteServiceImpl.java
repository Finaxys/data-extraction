/*
 * 
 */
package com.finaxys.rd.dataextraction.service.impl;

import java.util.List;

import com.finaxys.rd.dataextraction.converter.Converter;
import com.finaxys.rd.dataextraction.msg.Document;
import com.finaxys.rd.dataextraction.msg.Message;
import com.finaxys.rd.dataextraction.msg.Document.ContentType;
import com.finaxys.rd.dataextraction.msg.Document.DataType;
import com.finaxys.rd.dataextraction.provider.IndexQuoteProvider;
import com.finaxys.rd.dataextraction.publisher.Publisher;
import com.finaxys.rd.dataextraction.service.IndexQuoteService;

// TODO: Auto-generated Javadoc
/**
 * The Class IndexQuoteServiceImpl.
 */
public class IndexQuoteServiceImpl implements IndexQuoteService {

	/** The provider. */
	private IndexQuoteProvider provider;

	/** The converter. */
	private Converter converter;

	/** The publisher. */
	private Publisher publisher;

	/** The routing key. */
	private String routingKey;

	/** The content type. */
	private ContentType contentType;

	/** The data type. */
	private DataType dataType;

	/**
	 * Instantiates a new index quote service impl.
	 */
	public IndexQuoteServiceImpl() {
		super();
	}

	/**
	 * Instantiates a new index quote service impl.
	 *
	 * @param provider the provider
	 * @param converter the converter
	 * @param publisher the publisher
	 * @param routingKey the routing key
	 * @param contentType the content type
	 * @param dataType the data type
	 */
	public IndexQuoteServiceImpl(IndexQuoteProvider provider, Converter converter, Publisher publisher, String routingKey,
			ContentType contentType, DataType dataType) {
		super();
		this.provider = provider;
		this.converter = converter;
		this.publisher = publisher;
		this.routingKey = routingKey;
		this.contentType = contentType;
		this.dataType = dataType;
	}

	/**
	 * Gets the provider.
	 *
	 * @return the provider
	 */
	public IndexQuoteProvider getProvider() {
		return provider;
	}

	/**
	 * Sets the provider.
	 *
	 * @param provider the new provider
	 */
	public void setProvider(IndexQuoteProvider provider) {
		this.provider = provider;
	}

	/**
	 * Gets the converter.
	 *
	 * @return the converter
	 */
	public Converter getConverter() {
		return converter;
	}

	/**
	 * Sets the converter.
	 *
	 * @param converter the new converter
	 */
	public void setConverter(Converter converter) {
		this.converter = converter;
	}

	/**
	 * Gets the publisher.
	 *
	 * @return the publisher
	 */
	public Publisher getPublisher() {
		return publisher;
	}

	/**
	 * Sets the publisher.
	 *
	 * @param publisher the new publisher
	 */
	public void setPublisher(Publisher publisher) {
		this.publisher = publisher;
	}

	/**
	 * Gets the routing key.
	 *
	 * @return the routing key
	 */
	public String getRoutingKey() {
		return routingKey;
	}

	/**
	 * Sets the routing key.
	 *
	 * @param routingKey the new routing key
	 */
	public void setRoutingKey(String routingKey) {
		this.routingKey = routingKey;
	}

	/**
	 * Gets the content type.
	 *
	 * @return the content type
	 */
	public ContentType getContentType() {
		return contentType;
	}

	/**
	 * Sets the content type.
	 *
	 * @param contentType the new content type
	 */
	public void setContentType(ContentType contentType) {
		this.contentType = contentType;
	}

	/**
	 * Gets the data type.
	 *
	 * @return the data type
	 */
	public DataType getDataType() {
		return dataType;
	}

	/**
	 * Sets the data type.
	 *
	 * @param dataType the new data type
	 */
	public void setDataType(DataType dataType) {
		this.dataType = dataType;
	}

	/* (non-Javadoc)
	 * @see com.finaxys.rd.dataextraction.service.IndexQuoteService#publishIndexQuotes()
	 */
	public void publishIndexQuotes() {

		List<Document> l;
		try {
			l = this.provider.getCurrentIndexQuotes(this.contentType, this.dataType);
			if (l != null)
			for (Document d : l) {
				Message m = new Message(d, this.routingKey);
				this.converter.convert(m);
				this.publisher.publish(m);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

