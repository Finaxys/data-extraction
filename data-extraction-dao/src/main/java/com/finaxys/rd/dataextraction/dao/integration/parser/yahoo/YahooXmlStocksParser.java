/*
 * 
 */
package com.finaxys.rd.dataextraction.dao.integration.parser.yahoo;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;

import com.finaxys.rd.dataextraction.dao.exception.ParserException;
import com.finaxys.rd.dataextraction.dao.integration.parser.Parser;
import com.finaxys.rd.dataextraction.domain.Document;
import com.finaxys.rd.dataextraction.domain.Stock;

// TODO: Auto-generated Javadoc
/**
 * The Class YahooXmlStocksConverter.
 */
public class YahooXmlStocksParser implements Parser<Stock> {

	static Logger logger = Logger
			.getLogger(YahooXmlStocksParser.class);
	
	@Value("${parser.yahoo.stocks.new.stock_el}")
	private String ITEM_EL;
	/* (non-Javadoc)
	 * @see com.finaxys.rd.dataextraction.parser.Parser#parse(com.finaxys.rd.dataextraction.msg.Message)
	 */
	public List<Stock> parse(Document document) throws ParserException {
//		Assert.notNull(document, "Cannot parse Null document");
//		XMLEventFactory eventFactory = XMLEventFactory.newInstance();
//		XMLInputFactory ifactory = XMLInputFactory.newInstance();
//		XMLOutputFactory ofactory = XMLOutputFactory.newInstance();
//		InputStream is = new ByteArrayInputStream(document.getContent());
//		StreamSource source = new StreamSource(is);
//		ByteArrayOutputStream os = new ByteArrayOutputStream();
//		StreamResult result = new StreamResult(os);
//		XMLEventReader reader = ifactory.createXMLEventReader(source);
//		XMLEventWriter writer = ofactory.createXMLEventWriter(result);
//		boolean end = false;
//
//		while (reader.hasNext()) {
//			XMLEvent ev = reader.nextEvent();
//			if (ev.isStartElement() && ((StartElement) ev).getName().getLocalPart().equalsIgnoreCase("results")) {
//
//				writer.add(eventFactory.createStartDocument());
//
//				writer.add(eventFactory.createStartElement("", "", "stocks"));
//				writer.add(eventFactory.createStartElement("", "", "stocksList"));
//				while (reader.hasNext()) {
//					XMLEvent e = reader.nextEvent();
//					if (e.isStartElement() && ((StartElement) e).getName().getLocalPart().equalsIgnoreCase("stock")) {
//
//						writer.add(eventFactory.createStartElement("", "", ITEM_EL));
//						Attribute a = ((StartElement) e).getAttributeByName(new QName("symbol"));
//						writer.add(eventFactory.createStartElement("", "", "Symbol"));
//						writer.add(eventFactory.createCharacters(a.getValue()));
//						writer.add(eventFactory.createEndElement("", "", "Symbol"));
//						writer.add(eventFactory.createStartElement("", "", "Provider"));
//						writer.add(eventFactory.createCharacters(document.getSource() + ""));
//						writer.add(eventFactory.createEndElement("", "", "Provider"));
//						writer.add(eventFactory.createStartElement("", "", "ExchSymb"));
//						String exch = "NY";
//						if (a.getValue().indexOf('.') != -1)
//							exch = a.getValue().substring(a.getValue().indexOf('.') + 1);
//						writer.add(eventFactory.createCharacters(exch));
//						writer.add(eventFactory.createEndElement("", "", "ExchSymb"));
//					} else if (e.isCharacters() && e.asCharacters().getData().equals("N/A"))
//						writer.add(eventFactory.createCharacters("0"));
//
//					else if (e.isEndElement() && e.asEndElement().getName().equals("results")) {
//						writer.add(eventFactory.createEndElement("", "", "stocksList"));
//						writer.add(eventFactory.createEndElement("", "", "stocks"));
//						end = true;
//						break;
//
//					} else
//						writer.add(e);
//				}
//			}
//			if (end)
//				break;
//		}
//		writer.flush();
//		reader.close();
//		writer.close();
//		document.setContent(os.toByteArray());
		return null;
	}
}
