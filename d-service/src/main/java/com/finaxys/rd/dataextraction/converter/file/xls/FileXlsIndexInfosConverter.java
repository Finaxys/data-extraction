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
 * The Class FileXlsIndexInfosConverter.
 */
public class FileXlsIndexInfosConverter implements Converter{


	/** The indexes el. */
	@Value("${converter.file.indexes.indexes_el}")
	public String INDEXES_EL;
	
	/** The indexes list el. */
	@Value("${converter.file.indexes.indexes_list_el}")
	public String INDEXES_LIST_EL;
	
	/** The index el. */
	@Value("${converter.file.indexes.index_el}")
	public String INDEX_EL;
	
	/** The symbol el. */
	@Value("${converter.file.indexes.symbol_el}")
	public String SYMBOL_EL;
	
	/** The name el. */
	@Value("${converter.file.indexes.name_el}")
	public String NAME_EL;
	
	/** The exch symb el. */
	@Value("${converter.file.indexes.exch_symb_el}")
	public String EXCH_SYMB_EL;
	
	/** The provider el. */
	@Value("${converter.file.indexes.provider_el}")
	public String PROVIDER_EL;

	/* (non-Javadoc)
	 * @see com.finaxys.rd.dataextraction.converter.Converter#convert(com.finaxys.rd.dataextraction.msg.Message)
	 */
	public void convert(Message message) throws Exception {
		XMLOutputFactory factory = XMLOutputFactory.newInstance();
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		XMLStreamWriter writer = factory.createXMLStreamWriter(os);
		writer.writeStartDocument();
		writer.writeStartElement(INDEXES_EL);
		writer.writeStartElement(INDEXES_LIST_EL);

		InputStream is = new ByteArrayInputStream(message.getBody().getContent());
		HSSFWorkbook workbook = new HSSFWorkbook(is);
		HSSFSheet sheet = workbook.getSheetAt(0);
		Iterator<Row> rowIterator = sheet.iterator();

		Row row = rowIterator.next();
		while (rowIterator.hasNext()) {
			row = rowIterator.next();
			writer.writeStartElement(INDEX_EL);
			Iterator<Cell> cellIterator = row.cellIterator();
			writer.writeStartElement(SYMBOL_EL);
			writer.writeCharacters(cellIterator.next().toString());
			writer.writeEndElement();

			writer.writeStartElement(NAME_EL);
			writer.writeCharacters(cellIterator.next().toString());
			writer.writeEndElement();

			writer.writeStartElement(EXCH_SYMB_EL);
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
