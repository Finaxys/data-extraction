/*
 * 
 */
package com.finaxys.rd.dataextraction.converter.file.xls;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Iterator;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.finaxys.rd.dataextraction.converter.Converter;
import com.finaxys.rd.dataextraction.msg.Document;
import com.finaxys.rd.dataextraction.msg.Message;

// TODO: Auto-generated Javadoc
/**
 * The Class FileXlsCurrencyPairsConverter.
 */
public class FileXlsCurrencyPairsConverter implements Converter {

	/** The currency pairs el. */
	@Value("${converter.file.currency_pairs.currency_pairs_el}")
	public String CURRENCY_PAIRS_EL;
	
	/** The currency pairs list el. */
	@Value("${converter.file.currency_pairs.currency_pairs_list_el}")
	public String CURRENCY_PAIRS_LIST_EL;
	
	/** The currency pair el. */
	@Value("${converter.file.currency_pairs.currency_pair_el}")
	public String CURRENCY_PAIR_EL;
	
	/** The symbol el. */
	@Value("${converter.file.currency_pairs.symbol_el}")
	public String SYMBOL_EL;
	
	/** The base el. */
	@Value("${converter.file.currency_pairs.base_el}")
	public String BASE_EL;
	
	/** The quote el. */
	@Value("${converter.file.currency_pairs.quote_el}")
	public String QUOTE_EL;
	
	/* (non-Javadoc)
	 * @see com.finaxys.rd.dataextraction.converter.Converter#convert(com.finaxys.rd.dataextraction.msg.Message)
	 */
	public void convert(Message message) throws Exception {
		XMLOutputFactory factory = XMLOutputFactory.newInstance();
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		XMLStreamWriter writer = factory.createXMLStreamWriter(os);

		writer.writeStartDocument();
		writer.writeStartElement(CURRENCY_PAIRS_EL);
		writer.writeStartElement(CURRENCY_PAIRS_LIST_EL);

		InputStream is = new ByteArrayInputStream(message.getBody().getContent());
		HSSFWorkbook workbook = new HSSFWorkbook(is);
		HSSFSheet sheet = workbook.getSheetAt(0);
		Iterator<Row> rowIterator = sheet.iterator();

		Row row = rowIterator.next();
		while (rowIterator.hasNext()) {
			row = rowIterator.next();
			writer.writeStartElement(CURRENCY_PAIR_EL);
			Iterator<Cell> cellIterator = row.cellIterator();
			String sym = cellIterator.next().toString();
			String[] symbol = (sym.split(" ")[0]).split("/");

			writer.writeStartElement(SYMBOL_EL);
			writer.writeCharacters(symbol[0] + symbol[1]);
			writer.writeEndElement();

			writer.writeStartElement("base");
			writer.writeCharacters(symbol[0]);
			writer.writeEndElement();

			writer.writeStartElement("quote");
			writer.writeCharacters(symbol[1]);
			writer.writeEndElement();

			writer.writeEndElement();

		}

		writer.writeEndElement();
		writer.writeEndElement();
		writer.writeEndDocument();
		message.getBody().setContent(os.toByteArray());

	}

}
