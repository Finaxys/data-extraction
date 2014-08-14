package com.finaxys.rd.dataextraction.dao.integration.file;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;

import com.finaxys.rd.dataextraction.dao.exception.GatewayException;
import com.finaxys.rd.dataextraction.dao.exception.ParserException;
import com.finaxys.rd.dataextraction.dao.helper.FileGatewayHelper;
import com.finaxys.rd.dataextraction.dao.integration.RefDataGateway;
import com.finaxys.rd.dataextraction.dao.integration.parser.Parser;
import com.finaxys.rd.dataextraction.domain.Document;
import com.finaxys.rd.dataextraction.domain.Enum.ContentType;
import com.finaxys.rd.dataextraction.domain.Enum.DataClass;
import com.finaxys.rd.dataextraction.domain.Enum.DataType;
import com.finaxys.rd.dataextraction.domain.Stock;

public class FileStockGateway implements RefDataGateway<Stock> {

	/** The logger. */
	private static Logger logger = Logger.getLogger(FileStockGateway.class);

	/** The stocks file. */
	@Value("${gateway.file.stocksFile:stocks}")
	public String STOCKS_FILE;

	/** The content type. */
	private ContentType contentType;

	private Parser<Stock> parser;

	public FileStockGateway(Parser<Stock> parser, ContentType contentType) {
		super();
		this.parser = parser;
		this.contentType = contentType;
	}

	public Parser<Stock> getParser() {
		return parser;
	}

	public void setParser(Parser<Stock> parser) {
		this.parser = parser;
	}

	public ContentType getContentType() {
		return contentType;
	}

	public void setContentType(ContentType contentType) {
		this.contentType = contentType;
	}

	@Override
	public List<Stock> getRefData() throws GatewayException {
		try {
			File file = FileGatewayHelper.getResourceFile(FileGatewayHelper.getPath(FileGatewayHelper.DATA_FOLDER, STOCKS_FILE, contentType.getName()));
			if (file != null && file.length() > 0)
				return parser.parse(new Document(contentType, DataType.REF, DataClass.Stock, FileGatewayHelper.FILE_PROVIDER_SYMB, FileGatewayHelper.toBytes(file)));
			
				return null;
		} catch ( IOException | ParserException e) {
			throw new GatewayException(e);
		}
	}
}
