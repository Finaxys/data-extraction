package converter.yahoo;

import java.io.ByteArrayOutputStream;
import java.io.File;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import provider.YahooDataProvider;
import converter.Converter;

public class FXRatesConverter implements Converter {

	public byte[] convert(File f) throws Exception {
		XMLInputFactory ifactory = XMLInputFactory.newInstance();
		XMLOutputFactory ofactory = XMLOutputFactory.newInstance();
		StreamSource source = new StreamSource(f);
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		StreamResult result = new StreamResult(os);
		XMLEventFactory eventFactory = XMLEventFactory.newInstance();

		XMLEventReader reader = ifactory.createXMLEventReader(source);
		XMLEventWriter writer = ofactory.createXMLEventWriter(result);
		boolean end = false;

		Long ts = null;
		String date = null;
		String time = null;
		DateTimeFormatter formatter = DateTimeFormat.forPattern("MM/dd/yyyy h:mmaa");

		while (reader.hasNext()) {
			XMLEvent ev = reader.nextEvent();

			if (ev.isStartElement() && ((StartElement) ev).getName().getLocalPart().equals("results")) {

				writer.add(eventFactory.createStartDocument());

				writer.add(eventFactory.createStartElement("", "", "rates"));
				writer.add(eventFactory.createStartElement("", "", "ratesList"));
				while (reader.hasNext()) {
					XMLEvent e = reader.nextEvent();
					if (e.isStartElement() && ((StartElement) e).getName().getLocalPart().equals("rate")) {

						writer.add(eventFactory.createStartElement("", "", "rate"));
						Attribute a = ((StartElement) e).getAttributeByName(new QName("id"));
						writer.add(eventFactory.createStartElement("", "", "Symbol"));
						writer.add(eventFactory.createCharacters(a.getValue()));
						writer.add(eventFactory.createEndElement("", "", "Symbol"));
						writer.add(eventFactory.createStartElement("", "", "Provider"));
						writer.add(eventFactory.createCharacters("" + YahooDataProvider.Y_PROVIDER_SYMB));
						writer.add(eventFactory.createEndElement("", "", "Provider"));

					} else if ((e.isStartElement()
							&& (((StartElement) e).getName().getLocalPart().equals("Date") || ((StartElement) e)
									.getName().getLocalPart().equals("Time") || ((StartElement) e)
									.getName().getLocalPart().equals("Name"))) || (e.isEndElement()
											&& (((EndElement) e).getName().getLocalPart().equals("Date") || ((EndElement) e)
													.getName().getLocalPart().equals("Time") || ((EndElement) e)
													.getName().getLocalPart().equals("Name"))))
						continue;
					else if (e.isCharacters()) {

						if (e.asCharacters().getData().equals("N/A"))
							writer.add(eventFactory.createCharacters("0"));
						else if (e.asCharacters().getData().matches("(\\d{1,2})/(\\d{2})/(\\d{4})"))
							date = e.asCharacters().getData();
						else if (e.asCharacters().getData().matches("(\\d{1,2}):(\\d{2})(am|pm)"))
							time = e.asCharacters().getData();
						else if (e.asCharacters().getData().matches("(\\w{3}) to (\\w{3})"))
							continue;
						else
							writer.add(e);
					} 
					else if (e.isEndElement() && e.asEndElement().getName().getLocalPart().equals("rate")) {
						DateTime dt = formatter.parseDateTime(date + " " + time);
						ts = dt.getMillis();

						writer.add(eventFactory.createStartElement("", "", "ts"));
						writer.add(eventFactory.createCharacters(ts + ""));
						writer.add(eventFactory.createEndElement("", "", "ts"));
						writer.add(e);
					}else if (e.isEndElement() && e.asEndElement().getName().getLocalPart().equals("results")) {
					
						writer.add(eventFactory.createEndElement("", "", "ratesList"));
						writer.add(eventFactory.createEndElement("", "", "rates"));
						end = true;
						break;
					} 
					else
						writer.add(e);
				}
			}
			if (end)
				break;
		}
		writer.flush();
		reader.close();
		writer.close();
		System.out.println(os);
		return os.toByteArray();
	}
}
