package converter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class YahooStockSummariesConverter implements Converter {



	public byte[] convert(File f) throws Exception {
		SAXBuilder builder = new SAXBuilder();
		Document doc = (Document) builder.build(f);

		Element rootNode = doc.getRootElement();
		Element stocksList = rootNode.getChild("results");
		stocksList.setName("stocksList");
		stocksList.detach();
		Element stockSummaries = new Element("stockSummaries");
		stockSummaries.addContent(stocksList);
		doc.detachRootElement();
		doc.setRootElement(stockSummaries);

		XMLOutputter xmlOutput = new XMLOutputter();
		xmlOutput.setFormat(Format.getPrettyFormat());
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		xmlOutput.output(doc, os);

		return os.toByteArray();
	}

}
