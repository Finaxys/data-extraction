package converter.yahoo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import msg.Document;
import msg.Message;
import converter.Converter;

public class StocksConverter implements Converter {

	public void convert(Message message) throws Exception {
		XMLEventFactory eventFactory = XMLEventFactory.newInstance();
		XMLInputFactory ifactory = XMLInputFactory.newInstance();
		XMLOutputFactory ofactory = XMLOutputFactory.newInstance();
		InputStream is = new ByteArrayInputStream(message.getBody().getContent());
		StreamSource source = new StreamSource(is);
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		StreamResult result = new StreamResult(os);
		XMLEventReader reader = ifactory.createXMLEventReader(source);
		XMLEventWriter writer = ofactory.createXMLEventWriter(result);
		boolean end = false;

		while (reader.hasNext()) {
			XMLEvent ev = reader.nextEvent();
			if (ev.isStartElement() && ((StartElement) ev).getName().getLocalPart().equalsIgnoreCase("results")) {

				writer.add(eventFactory.createStartDocument());

				writer.add(eventFactory.createStartElement("", "", "stocks"));
				writer.add(eventFactory.createStartElement("", "", "stocksList"));
				while (reader.hasNext()) {
					XMLEvent e = reader.nextEvent();
					if (e.isStartElement() && ((StartElement) e).getName().getLocalPart().equalsIgnoreCase("stock")) {

						writer.add(eventFactory.createStartElement("", "", "stock"));
						Attribute a = ((StartElement) e).getAttributeByName(new QName("symbol"));
						writer.add(eventFactory.createStartElement("", "", "Symbol"));
						writer.add(eventFactory.createCharacters(a.getValue()));
						writer.add(eventFactory.createEndElement("", "", "Symbol"));
						writer.add(eventFactory.createStartElement("", "", "Provider"));
						writer.add(eventFactory.createCharacters(message.getBody().getProvider() + ""));
						writer.add(eventFactory.createEndElement("", "", "Provider"));
						writer.add(eventFactory.createStartElement("", "", "ExchSymb"));
						String exch = "NY";
						if (a.getValue().indexOf('.') != -1)
							exch = a.getValue().substring(a.getValue().indexOf('.') + 1);
						writer.add(eventFactory.createCharacters(exch));
						writer.add(eventFactory.createEndElement("", "", "ExchSymb"));
					} else if (e.isCharacters() && e.asCharacters().getData().equals("N/A"))
						writer.add(eventFactory.createCharacters("0"));

					else if (e.isEndElement() && e.asEndElement().getName().equals("results")) {
						writer.add(eventFactory.createEndElement("", "", "stocksList"));
						writer.add(eventFactory.createEndElement("", "", "stocks"));
						end = true;
						break;

					} else
						writer.add(e);
				}
			}
			if (end)
				break;
		}
		writer.flush();
		reader.close();
		writer.close();
		message.getBody().setContent(os.toByteArray());
	}
}
