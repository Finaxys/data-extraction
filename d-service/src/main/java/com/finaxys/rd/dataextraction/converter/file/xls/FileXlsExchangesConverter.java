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
 * The Class FileXlsExchangesConverter.
 */
public class FileXlsExchangesConverter implements Converter {
	
	/** The exchanges el. */
	@Value("${converter.file.exchanges.exchanges_el}")
	public String EXCHANGES_EL;
	
	/** The exchanges list el. */
	@Value("${converter.file.exchanges.exchanges_list_el}")
	public String EXCHANGES_LIST_EL;
	
	/** The exchange el. */
	@Value("${converter.file.exchanges.exchange_el}")
	public String EXCHANGE_EL;
	
	/** The mic el. */
	@Value("${converter.file.exchanges.mic_el}")
	public String MIC_EL;
	
	/** The suffix el. */
	@Value("${converter.file.exchanges.suffix_el}")
	public String SUFFIX_EL;
	
	/** The provider el. */
	@Value("${converter.file.exchanges.provider_el}")
	public String PROVIDER_EL;
	
	/** The name el. */
	@Value("${converter.file.exchanges.name_el}")
	public String NAME_EL;
	
	/** The type el. */
	@Value("${converter.file.exchanges.type_el}")
	public String TYPE_EL;
	
	/** The continent el. */
	@Value("${converter.file.exchanges.continent_el}")
	public String CONTINENT_EL;
	
	/** The country el. */
	@Value("${converter.file.exchanges.country_el}")
	public String COUNTRY_EL;
	
	/** The currency el. */
	@Value("${converter.file.exchanges.currency_el}")
	public String CURRENCY_EL;
	
	/** The open time el. */
	@Value("${converter.file.exchanges.open_time_el}")
	public String OPEN_TIME_EL;
	
	/** The close time el. */
	@Value("${converter.file.exchanges.close_time_el}")
	public String CLOSE_TIME_EL;
	
	/** The status el. */
	@Value("${converter.file.exchanges.status_el}")
	public String STATUS_EL;


	/* (non-Javadoc)
	 * @see com.finaxys.rd.dataextraction.converter.Converter#convert(com.finaxys.rd.dataextraction.msg.Message)
	 */
	public void convert(Message message) throws Exception {
		XMLOutputFactory factory = XMLOutputFactory.newInstance();
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		XMLStreamWriter writer = factory.createXMLStreamWriter(os);
		writer.writeStartDocument();
		writer.writeStartElement(EXCHANGES_EL);
		writer.writeStartElement(EXCHANGES_LIST_EL);

		InputStream is = new ByteArrayInputStream(message.getBody().getContent());
		HSSFWorkbook workbook = new HSSFWorkbook(is);
		HSSFSheet sheet = workbook.getSheetAt(0);
		Iterator<Row> rowIterator = sheet.iterator();

		Row row = rowIterator.next();
		while (rowIterator.hasNext()) {
			row = rowIterator.next();
			writer.writeStartElement(EXCHANGE_EL);
			Iterator<Cell> cellIterator = row.cellIterator();

		
			writer.writeStartElement(MIC_EL);
			writer.writeCharacters(cellIterator.next().toString());
			writer.writeEndElement();

			writer.writeStartElement("symbol");
			writer.writeCharacters(cellIterator.next().toString());
			writer.writeEndElement();
			
			writer.writeStartElement(SUFFIX_EL);
			writer.writeCharacters(cellIterator.next().toString());
			writer.writeEndElement();

			writer.writeStartElement(PROVIDER_EL);
			writer.writeCharacters(message.getBody().getProvider() + "");
			writer.writeEndElement();

			writer.writeStartElement(NAME_EL);
			writer.writeCharacters(cellIterator.next().toString());
			writer.writeEndElement();

			writer.writeStartElement(TYPE_EL);
			writer.writeCharacters(cellIterator.next().toString());
			writer.writeEndElement();

			writer.writeStartElement(CONTINENT_EL);
			writer.writeCharacters(cellIterator.next().toString());
			writer.writeEndElement();

			writer.writeStartElement(COUNTRY_EL);
			writer.writeCharacters(cellIterator.next().toString());
			writer.writeEndElement();

			writer.writeStartElement(CURRENCY_EL);
			writer.writeCharacters(cellIterator.next().toString());
			writer.writeEndElement();

			writer.writeStartElement(OPEN_TIME_EL);
			writer.writeCharacters(Long.toString(cellIterator.next().getDateCellValue().getTime()));
			writer.writeEndElement();

			writer.writeStartElement(CLOSE_TIME_EL);
			writer.writeCharacters(Long.toString(cellIterator.next().getDateCellValue().getTime()));
			writer.writeEndElement();

			writer.writeStartElement(STATUS_EL);
			writer.writeCharacters(cellIterator.next().toString());
			writer.writeEndElement();

			writer.writeEndElement();

		}

		writer.writeEndElement();
		writer.writeEndElement();
		writer.writeEndDocument();
		message.getBody().setContent(os.toByteArray());

	}
}
