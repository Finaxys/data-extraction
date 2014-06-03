package provider.impl;

import java.io.File;
import java.io.FileNotFoundException;

import org.apache.http.HttpResponse;
import org.apache.http.nio.client.methods.ZeroCopyConsumer;

public class HttpResponseConsumer extends ZeroCopyConsumer<File> {
	public HttpResponseConsumer(File file) throws FileNotFoundException {
		super(file);
	}

	@Override
	protected File process(HttpResponse response, File file, org.apache.http.entity.ContentType contentType)
			throws Exception {
		return file;
	}
}