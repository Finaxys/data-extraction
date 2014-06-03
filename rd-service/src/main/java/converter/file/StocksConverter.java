package converter.file;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Iterator;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;

import msg.Document;
import msg.Message;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import converter.Converter;


public class StocksConverter implements Converter{


	public void convert(Message message) throws Exception {
		XMLOutputFactory factory = XMLOutputFactory.newInstance();
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		XMLStreamWriter writer = factory.createXMLStreamWriter(os);
		writer.writeStartDocument();
		writer.writeStartElement("stocks");
		writer.writeStartElement("stocksList");

		InputStream is = new ByteArrayInputStream(message.getBody().getContent());
		HSSFWorkbook workbook = new HSSFWorkbook(is);
		HSSFSheet sheet = workbook.getSheetAt(0);
		Iterator<Row> rowIterator = sheet.iterator();

		Row row = rowIterator.next();
		while (rowIterator.hasNext()) {
			row = rowIterator.next();
			writer.writeStartElement("stock");
			Iterator<Cell> cellIterator = row.cellIterator();
			writer.writeStartElement("Symbol");
			String symbol = cellIterator.next().toString();
			writer.writeCharacters(symbol);
			writer.writeEndElement();

			writer.writeStartElement("CompanyName");
			writer.writeCharacters(cellIterator.next().toString());
			writer.writeEndElement();

			writer.writeStartElement("Provider");
			writer.writeCharacters(message.getBody().getProvider()+ "");
			writer.writeEndElement();

			writer.writeStartElement("ExchSymb");
			String exch = "NY";
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
