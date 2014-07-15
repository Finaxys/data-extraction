package com.finaxys.rd.dataextraction.splitter;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.finaxys.rd.dataextraction.domain.msg.Document;
import com.finaxys.rd.dataextraction.domain.msg.Message;

public class CsvSplitter implements Splitter {

	private Integer maxSize = 10000;

	public Integer getMaxSize() {
		return maxSize;
	}

	public void setMaxSize(Integer maxSize) {
		this.maxSize = maxSize;
	}

	public List<Message> split(Message message) throws IOException {
		List<Message> l = new ArrayList<Message>();
		BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(message.getBody()
				.getContent()), "UTF-8"));
		String line = "";
		Message m = null;
		String body = "";
		while ((line = reader.readLine()) != null || (line == null && body != null && !body.equals(""))) {
			if ((line != null && (line.getBytes().length + body.getBytes().length) > maxSize )|| (line == null && !body.equals("")) ) {
				m = new Message(new Document(message.getBody().getContentType(), message.getBody().getDataType(),
						message.getBody().getDataClass(), message.getBody().getProvider(), body.getBytes()));
				m.setRoutingKey(message.getRoutingKey());
				l.add(m);
				body = line;
			} else
				body = body + "\n" + line;
		}
		
		reader.close();
		return l;
	}
}
