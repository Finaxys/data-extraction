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

import provider.HomeDataProvider;

public class StocksConverter {

	public byte[] convert(File f) throws Exception {
		XMLOutputFactory factory = XMLOutputFactory.newInstance();
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		XMLStreamWriter writer = factory.createXMLStreamWriter(os);

		writer.writeStartDocument();
		writer.writeStartElement("stocks");
		writer.writeStartElement("stocksList");

		FileInputStream file = new FileInputStream(f);
		HSSFWorkbook workbook = new HSSFWorkbook(file);
		// Get first sheet from the workbook
		HSSFSheet sheet = workbook.getSheetAt(0);
		// Iterate through each rows from first sheet
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
			writer.writeCharacters(HomeDataProvider.H_PROVIDER_SYMB + "");
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
		return os.toByteArray();
	}

}
