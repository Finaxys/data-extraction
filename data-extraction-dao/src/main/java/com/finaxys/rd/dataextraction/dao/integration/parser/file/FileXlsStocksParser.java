/*
 * 
 */
package com.finaxys.rd.dataextraction.dao.integration.parser.file;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.joda.time.DateTime;

import com.finaxys.rd.dataextraction.dao.integration.parser.Parser;
import com.finaxys.rd.dataextraction.domain.Document;
import com.finaxys.rd.dataextraction.domain.Stock;

// TODO: Auto-generated Javadoc
/**
 * The Class FileXlsStocksConverter.
 */
public class FileXlsStocksParser implements Parser<Stock> {


	
	public List<Stock> parse(Document document) throws Exception {

		InputStream is = new ByteArrayInputStream(document.getContent());
		HSSFWorkbook workbook = new HSSFWorkbook(is);
		HSSFSheet sheet = workbook.getSheetAt(0);
		Iterator<Row> rowIterator = sheet.iterator();
		List<Stock> list = new ArrayList<Stock>();
		

		Row row = rowIterator.next();
		Stock stock;
		while (rowIterator.hasNext()) {
			row = rowIterator.next();
			Iterator<Cell> cellIterator = row.cellIterator();

			try {
				stock = new Stock();

				String symbol = cellIterator.next().toString();
				stock.setSymbol(symbol);
				stock.setCompanyName(cellIterator.next().toString());
				stock.setExchSymb(cellIterator.next().toString());
				stock.setProvider(cellIterator.next().toString().charAt(0));
				stock.setSource(document.getSource());
				stock.setInputDate(new DateTime());
				stock.setDataType(document.getDataType());
				list.add(stock);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return list;
	}

}