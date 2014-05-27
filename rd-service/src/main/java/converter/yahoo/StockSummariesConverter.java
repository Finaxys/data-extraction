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
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import provider.YahooDataProvider;
import converter.Converter;

public class StockSummariesConverter implements Converter {

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
		
		while (reader.hasNext()) {
			XMLEvent ev = reader.nextEvent();
			if (ev.isStartElement() && ((StartElement) ev).getName().getLocalPart().equalsIgnoreCase("results")) {

				writer.add(eventFactory.createStartDocument());

				writer.add(eventFactory.createStartElement("", "", "stockSummaries"));
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
						writer.add(eventFactory.createCharacters("" + YahooDataProvider.Y_PROVIDER_SYMB));
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
						writer.add(eventFactory.createEndElement("", "", "stockSummaries"));
						writer.add(eventFactory.createEndElement("", "", "stocksList"));
						writer.add(eventFactory.createStartDocument());
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
		return os.toByteArray();
	}
}
