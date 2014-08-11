package com.finaxys.rd.dataextraction.dao.integration.parser.yahoo;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.stream.StreamSource;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Value;

import com.finaxys.rd.dataextraction.dao.integration.parser.Parser;
import com.finaxys.rd.dataextraction.domain.Document;
import com.finaxys.rd.dataextraction.domain.OptionChain;

public class YahooXmlOptionChainsParser implements Parser<OptionChain> {
	/** The date format. */
	@Value("${parser.yahoo.option_chains.date_format}")
	private String DATE_FORMAT;

	@Value("${parser.yahoo.option_chains.old.no_data}")
	private String NO_DATA;

	@Value("${parser.yahoo.option_chains.old.main_option_el}")
	private String MAIN_OPTION_EL;

	@Value("${parser.yahoo.option_chains.old.symbol_att}")
	private String SYMBOL_ATT;

	@Value("${parser.yahoo.option_chains.old.contract_el}")
	private String CONTRACT_EL;

	public List<OptionChain> parse(Document document) throws Exception {
		System.out.println(new String(document.getContent()));
		XMLInputFactory ifactory = XMLInputFactory.newInstance();
		InputStream is = new ByteArrayInputStream(document.getContent());
		StreamSource source = new StreamSource(is);
		XMLEventReader reader = ifactory.createXMLEventReader(source);
		
		List<OptionChain> list = new ArrayList<OptionChain>();
		DateTimeFormatter formatter = DateTimeFormat.forPattern(DATE_FORMAT);

		String symbol = null;
		OptionChain optionChain = null;
		while (reader.hasNext()) {
			try {
				XMLEvent ev = reader.nextEvent();

				if (ev.isStartElement() && ((StartElement) ev).getName().getLocalPart().equals(MAIN_OPTION_EL)) {
					Attribute a = ((StartElement) ev).getAttributeByName(new QName(SYMBOL_ATT));
					if (a != null && !a.getValue().equals(""))
						symbol = a.getValue();
				}
				if (symbol != null && ev.isStartElement() && ((StartElement) ev).getName().getLocalPart().equals(CONTRACT_EL) && document.getDataType() != null && document.getSource() != Character.UNASSIGNED) {
					XMLEvent evt = reader.nextEvent();
					if (evt.isCharacters() && !evt.asCharacters().getData().equals(NO_DATA)) {
						optionChain = new  OptionChain(document.getSource(),new DateTime(), symbol,
								document.getDataType(), document.getSource(), formatter.parseDateTime(evt.asCharacters().getData()).toLocalDate());
						list.add(optionChain);
					}else {
						optionChain = null;
					}
				}
				if (ev.isEndElement() && ((EndElement) ev).getName().getLocalPart().equals(MAIN_OPTION_EL)) {
					
						symbol = null;
				}

			} catch (NullPointerException e) {
				e.printStackTrace();
				break;
			} catch (XMLStreamException e) {
				e.printStackTrace();
				break;

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return list;

	}

}
