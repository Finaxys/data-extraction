package com.finaxys.rd.dataextraction.splitter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import com.finaxys.rd.dataextraction.domain.msg.Document;
import com.finaxys.rd.dataextraction.domain.msg.Message;

public class XmlSplitter implements Splitter {

	private String element;
	private Integer maxSize = 10000;

	public XmlSplitter(String element) {
		super();
		this.element = element;
	}

	public Integer getMaxSize() {
		return maxSize;
	}

	public void setMaxSize(Integer maxSize) {
		this.maxSize = maxSize;
	}

	private XMLEventWriter getWriter(ByteArrayOutputStream os) throws XMLStreamException {
		XMLOutputFactory ofactory = XMLOutputFactory.newInstance();
		StreamResult result = new StreamResult(os);
		return ofactory.createXMLEventWriter(result);
	}

	public List<Message> split(Message message) throws XMLStreamException {
		System.out.println(new String(message.getBody().getContent()));
		XMLEventFactory eventFactory = XMLEventFactory.newInstance();
		XMLInputFactory ifactory = XMLInputFactory.newInstance();
		InputStream is = new ByteArrayInputStream(message.getBody().getContent());
		StreamSource source = new StreamSource(is);

		XMLEventReader reader = ifactory.createXMLEventReader(source);
		boolean end = false;
		boolean start = false;

		boolean head = false;
		List<Message> l = new ArrayList<Message>();

		ByteArrayOutputStream os = new ByteArrayOutputStream();
		XMLEventWriter writer = getWriter(os);
		byte[] body = new byte[maxSize];
		List<XMLEvent> hevs = new ArrayList<XMLEvent>();
		int offset = 0;
		while (reader.hasNext()) {
			XMLEvent e = reader.nextEvent();
			if (e.isStartElement() && ((StartElement) e).getName().getLocalPart().equals(element)) {
				
				if(!start) {
					start = true;head = true;
					for(XMLEvent hev : hevs)
						writer.add(hev);
				}
			}

			if(!start && !head && e.isStartElement())
				hevs.add(e);
			
			if (e.isEndElement() && e.asEndElement().getName().getLocalPart().equals(element))
				end = true;


			if(start && !end)
			{
				writer.add(e);
			}
			
			if (end || !reader.hasNext()) {

				writer.add(e);
				writer.flush();
				byte[] item = os.toByteArray();
				if ((item.length + offset) > maxSize || !reader.hasNext()) {
					if(reader.hasNext()) for(int i = 0;i<hevs.size(); i++) writer.add(eventFactory.createEndElement("", "",""));
					writer.flush();
					body = os.toByteArray();
					Message m = new Message(new Document(message.getBody().getContentType(), message.getBody()
							.getDataType(), message.getBody().getDataClass(), message.getBody().getProvider(), body));
					l.add(m);
					body = new byte[maxSize];
					offset = 0;
					os = new ByteArrayOutputStream();
					writer = getWriter(os);
					start = false;
					end = false;
				} else {
					end = false;
				}

			}

	
		}
		return l;
	}
}
