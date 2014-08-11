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
import com.finaxys.rd.dataextraction.domain.InterbankRate;

// TODO: Auto-generated Javadoc
/**
 * The Class FileXlsStocksConverter.
 */
public class FileXlsInterbankRatesParser implements Parser<InterbankRate> {


	public List<InterbankRate> parse(Document document) throws Exception {

		InputStream is = new ByteArrayInputStream(document.getContent());
		HSSFWorkbook workbook = new HSSFWorkbook(is);
		HSSFSheet sheet = workbook.getSheetAt(0);
		Iterator<Row> rowIterator = sheet.iterator();
		List<InterbankRate> list = new ArrayList<InterbankRate>();
		

		Row row = rowIterator.next();
		InterbankRate interbankRate;
		while (rowIterator.hasNext()) {
			row = rowIterator.next();

			Iterator<Cell> cellIterator = row.cellIterator();

			try {
				interbankRate = new InterbankRate();
				if (cellIterator.hasNext())
					interbankRate.setSymbol(cellIterator.next().toString());
				if (cellIterator.hasNext())
					interbankRate.setCurrency(cellIterator.next().toString());
				interbankRate.setProvider(cellIterator.next().toString().charAt(0));
				interbankRate.setSource(document.getSource());
				interbankRate.setInputDate(new DateTime());
				interbankRate.setDataType(document.getDataType());
				list.add(interbankRate);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return list;
	}

}
