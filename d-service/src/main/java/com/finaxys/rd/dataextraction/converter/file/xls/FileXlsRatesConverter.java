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
 * The Class FileXlsStocksConverter.
 */
public class FileXlsRatesConverter {
	/** The rates el. */
	@Value("${converter.file.rates.rates_el}")
	public String RATES_EL;
	
	/** The rates list el. */
	@Value("${converter.file.rates.rates_list_el}")
	public String RATES_LIST_EL;
	
	/** The rate el. */
	@Value("${converter.file.rates.rate_el}")
	public String RATE_EL;
	
	/** The provider el. */
	@Value("${converter.file.rates.provider_el}")
	public String PROVIDER_EL;
	
	/** The currency el. */
	@Value("${converter.file.rates.currency_el}")
	public String CURRENCY_EL;
	
	/** The name el. */
	@Value("${converter.file.rates.name_el}")
	public String NAME_EL;

	/* (non-Javadoc)
	 * @see com.finaxys.rd.dataextraction.converter.Converter#convert(com.finaxys.rd.dataextraction.msg.Message)
	 */
	public void convert(Message message) throws Exception {
		XMLOutputFactory factory = XMLOutputFactory.newInstance();
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		XMLStreamWriter writer = factory.createXMLStreamWriter(os);
		writer.writeStartDocument();
		writer.writeStartElement(RATES_EL);
		writer.writeStartElement(RATES_LIST_EL);

		InputStream is = new ByteArrayInputStream(message.getBody().getContent());
		HSSFWorkbook workbook = new HSSFWorkbook(is);
		HSSFSheet sheet = workbook.getSheetAt(0);
		Iterator<Row> rowIterator = sheet.iterator();

		Row row = rowIterator.next();
		while (rowIterator.hasNext()) {
			row = rowIterator.next();
			writer.writeStartElement(RATE_EL);
			
			Iterator<Cell> cellIterator = row.cellIterator();
			writer.writeStartElement(NAME_EL);
			writer.writeCharacters(cellIterator.next().toString());
			writer.writeEndElement();

			writer.writeStartElement(CURRENCY_EL);
			writer.writeCharacters(cellIterator.next().toString());
			writer.writeEndElement();
			
			writer.writeStartElement(PROVIDER_EL);
			writer.writeCharacters(message.getBody().getProvider() + "");
			writer.writeEndElement();

			writer.writeEndElement();

		}

		writer.writeEndElement();
		writer.writeEndElement();
		writer.writeEndDocument();
		message.getBody().setContent(os.toByteArray());
	}
}
