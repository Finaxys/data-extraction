package com.finaxys.rd.dataextraction.dao.integration.file;

import java.io.File;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;

import com.finaxys.rd.dataextraction.dao.helper.FileGatewayHelper;
import com.finaxys.rd.dataextraction.dao.integration.RefDataGateway;
import com.finaxys.rd.dataextraction.dao.integration.parser.Parser;
import com.finaxys.rd.dataextraction.domain.Document;
import com.finaxys.rd.dataextraction.domain.Enum.ContentType;
import com.finaxys.rd.dataextraction.domain.Enum.DataClass;
import com.finaxys.rd.dataextraction.domain.Enum.DataType;
import com.finaxys.rd.dataextraction.domain.Exchange;

public class FileExchangeGateway implements RefDataGateway<Exchange> {

	/** The logger. */
	static Logger logger = Logger.getLogger(FileExchangeGateway.class);

	/** The exchanges file. */
	@Value("${gateway.file.exchangesFile:exchanges}")
	public String EXCHANGES_FILE;

	/** The content type. */
	private ContentType contentType;

	private Parser<Exchange> parser;

	

	public FileExchangeGateway(ContentType contentType, Parser<Exchange> parser) {
		super();
		this.contentType = contentType;
		this.parser = parser;
	}



	public ContentType getContentType() {
		return contentType;
	}



	public void setContentType(ContentType contentType) {
		this.contentType = contentType;
	}



	public Parser<Exchange> getParser() {
		return parser;
	}



	public void setParser(Parser<Exchange> parser) {
		this.parser = parser;
	}



	@Override
	public List<Exchange> getRefData() throws Exception {
		File file = FileGatewayHelper.getResourceFile(FileGatewayHelper
				.getPath(FileGatewayHelper.DATA_FOLDER, EXCHANGES_FILE,
						contentType.getName()));
		if (file != null && file.length() > 0)
			return parser.parse(new Document(contentType, DataType.REF, DataClass.Exchange,
					FileGatewayHelper.FILE_PROVIDER_SYMB,
					FileGatewayHelper.toBytes(file)));
		else
			return null;
	}
}