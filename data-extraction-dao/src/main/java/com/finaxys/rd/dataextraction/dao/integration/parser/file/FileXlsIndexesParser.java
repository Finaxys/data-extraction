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
import com.finaxys.rd.dataextraction.domain.Index;

// TODO: Auto-generated Javadoc
/**
 * The Class FileXlsIndexInfosConverter.
 */
public class FileXlsIndexesParser implements Parser<Index> {

	public List<Index> parse(Document document) throws Exception {

		InputStream is = new ByteArrayInputStream(document.getContent());
		HSSFWorkbook workbook = new HSSFWorkbook(is);
		HSSFSheet sheet = workbook.getSheetAt(0);
		Iterator<Row> rowIterator = sheet.iterator();
		List<Index> list = new ArrayList<Index>();
		

		Row row = rowIterator.next();
		Index index;
		while (rowIterator.hasNext()) {
			row = rowIterator.next();

			Iterator<Cell> cellIterator = row.cellIterator();

			try {
				index = new Index();
				index.setSymbol(cellIterator.next().toString());
				index.setName(cellIterator.next().toString());
				index.setExchSymb(cellIterator.next().toString());
				index.setProvider(cellIterator.next().toString().charAt(0));
				index.setSource(document.getSource());
				index.setInputDate(new DateTime());
				index.setDataType(document.getDataType());
				list.add(index);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return list;
	}

}
