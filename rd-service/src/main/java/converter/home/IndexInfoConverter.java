package converter.home;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import converter.Converter;

public class IndexInfoConverter implements Converter{

	public byte[] convert(File f) throws Exception {
		XMLOutputFactory factory = XMLOutputFactory.newInstance();
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		XMLStreamWriter writer = factory.createXMLStreamWriter(os);

		writer.writeStartDocument();
		writer.writeStartElement("indexInfos");
		writer.writeStartElement("indexInfosList");

		FileInputStream file = new FileInputStream(f);
		HSSFWorkbook workbook = new HSSFWorkbook(file);
		// Get first sheet from the workbook
		HSSFSheet sheet = workbook.getSheetAt(0);
		// Iterate through each rows from first sheet
		Iterator<Row> rowIterator = sheet.iterator();

		Row row = rowIterator.next();
		while (rowIterator.hasNext()) {
			row = rowIterator.next();
			writer.writeStartElement("indexInfo");
			Iterator<Cell> cellIterator = row.cellIterator();
			writer.writeStartElement("symbol");
			writer.writeCharacters(cellIterator.next().toString());
			writer.writeEndElement();

			writer.writeStartElement("name");
			writer.writeCharacters(cellIterator.next().toString());
			writer.writeEndElement();

			writer.writeStartElement("exchSymb");
			writer.writeCharacters(cellIterator.next().toString());
			writer.writeEndElement();
			
			writer.writeStartElement("provider");
			writer.writeCharacters(((Double)cellIterator.next().getNumericCellValue()).intValue()+"");
			writer.writeEndElement();


			writer.writeEndElement();

		}

		writer.writeEndElement();
		writer.writeEndElement();
		writer.writeEndDocument();
		return os.toByteArray();
	}

}
