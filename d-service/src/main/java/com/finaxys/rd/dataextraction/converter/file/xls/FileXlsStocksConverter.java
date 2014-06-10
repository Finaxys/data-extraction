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
public class FileXlsStocksConverter implements Converter{

	/** The stocks el. */
	@Value("${converter.file.stocks.stocks_el}")
	public String STOCKS_EL;
	
	/** The stocks list el. */
	@Value("${converter.file.stocks.stocks_list_el}")
	public String STOCKS_LIST_EL;
	
	/** The stock el. */
	@Value("${converter.file.stocks.stock_el}")
	public String STOCK_EL;
	
	/** The symbol el. */
	@Value("${converter.file.stocks.symbol_el}")
	public String SYMBOL_EL;
	
	/** The company name el. */
	@Value("${converter.file.stocks.company_name_el}")
	public String COMPANY_NAME_EL;
	
	/** The provider el. */
	@Value("${converter.file.stocks.provider_el}")
	public String PROVIDER_EL;
	
	/** The exch symb el. */
	@Value("${converter.file.stocks.exch_symb_el}")
	public String EXCH_SYMB_EL;
	
	/** The default exchange el. */
	@Value("${converter.file.stocks.default_exchange_el}")
	public String DEFAULT_EXCHANGE_EL;
	
	/* (non-Javadoc)
	 * @see com.finaxys.rd.dataextraction.converter.Converter#convert(com.finaxys.rd.dataextraction.msg.Message)
	 */
	public void convert(Message message) throws Exception {
		XMLOutputFactory factory = XMLOutputFactory.newInstance();
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		XMLStreamWriter writer = factory.createXMLStreamWriter(os);
		writer.writeStartDocument();
		writer.writeStartElement(STOCKS_EL);
		writer.writeStartElement(STOCKS_LIST_EL);

		InputStream is = new ByteArrayInputStream(message.getBody().getContent());
		HSSFWorkbook workbook = new HSSFWorkbook(is);
		HSSFSheet sheet = workbook.getSheetAt(0);
		Iterator<Row> rowIterator = sheet.iterator();

		Row row = rowIterator.next();
		while (rowIterator.hasNext()) {
			row = rowIterator.next();
			writer.writeStartElement(STOCK_EL);
			Iterator<Cell> cellIterator = row.cellIterator();
			writer.writeStartElement(SYMBOL_EL);
			String symbol = cellIterator.next().toString();
			writer.writeCharacters(symbol);
			writer.writeEndElement();

			writer.writeStartElement(COMPANY_NAME_EL);
			writer.writeCharacters(cellIterator.next().toString());
			writer.writeEndElement();

			writer.writeStartElement(PROVIDER_EL);
			writer.writeCharacters(message.getBody().getProvider()+ "");
			writer.writeEndElement();

			writer.writeStartElement(EXCH_SYMB_EL);
			String exch = DEFAULT_EXCHANGE_EL;
			if (symbol.indexOf('.') != -1)
				exch = symbol.substring(symbol.indexOf('.') + 1);
			writer.writeCharacters(exch);
			writer.writeEndElement();

			writer.writeEndElement();

		}

		writer.writeEndElement();
		writer.writeEndElement();
		writer.writeEndDocument();
		message.getBody().setContent(os.toByteArray());
	}

}
