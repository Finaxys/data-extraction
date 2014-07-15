/*
 * 
 */
package com.finaxys.rd.dataextraction.dao.integration.yahoo;

import java.io.File;
import java.io.FileNotFoundException;

import org.apache.http.HttpResponse;
import org.apache.http.nio.client.methods.ZeroCopyConsumer;

// TODO: Auto-generated Javadoc
/**
 * The Class HttpResponseConsumer.
 */
public class HttpResponseConsumer extends ZeroCopyConsumer<File> {
	
	/**
	 * Instantiates a new http response consumer.
	 *
	 * @param file the file
	 * @throws FileNotFoundException the file not found exception
	 */
	public HttpResponseConsumer(File file) throws FileNotFoundException {
		super(file);
	}

	/* (non-Javadoc)
	 * @see org.apache.http.nio.client.methods.ZeroCopyConsumer#process(org.apache.http.HttpResponse, java.io.File, org.apache.http.entity.ContentType)
	 */
	@Override
	protected File process(HttpResponse response, File file, org.apache.http.entity.ContentType contentType)
			throws Exception {
		return file;
	}
}