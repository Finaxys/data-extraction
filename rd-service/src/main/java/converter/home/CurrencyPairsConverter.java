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

public class CurrencyPairsConverter implements Converter {

	public byte[] convert(File f) throws Exception {
		XMLOutputFactory factory = XMLOutputFactory.newInstance();
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		XMLStreamWriter writer = factory.createXMLStreamWriter(os);

		writer.writeStartDocument();
		writer.writeStartElement("currencyPairs");
		writer.writeStartElement("currencyPairsList");

		FileInputStream file = new FileInputStream(f);
		HSSFWorkbook workbook = new HSSFWorkbook(file);
		// Get first sheet from the workbook
		HSSFSheet sheet = workbook.getSheetAt(0);
		// Iterate through each rows from first sheet
		Iterator<Row> rowIterator = sheet.iterator();

		Row row = rowIterator.next();
		while (rowIterator.hasNext()) {
			row = rowIterator.next();
			writer.writeStartElement("currencyPair");
			Iterator<Cell> cellIterator = row.cellIterator();
			String sym = cellIterator.next().toString();
			String[] symbol =( sym.split(" ")[0]).split("/");
		
			
			writer.writeStartElement("symbol");
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
		return os.toByteArray();

	}
}
