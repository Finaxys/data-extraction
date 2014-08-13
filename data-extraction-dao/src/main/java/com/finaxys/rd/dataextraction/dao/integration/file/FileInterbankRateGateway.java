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
import com.finaxys.rd.dataextraction.domain.InterbankRate;

public class FileInterbankRateGateway implements RefDataGateway<InterbankRate> {

	/** The logger. */
	static Logger logger = Logger.getLogger(FileInterbankRateGateway.class);

	/** The stocks file. */
	@Value("${gateway.file.interbankRatesFile:interbank_rates}")
	public String RATES_FILE;

	/** The content type. */
	private ContentType contentType;

	private Parser<InterbankRate> parser;

	public FileInterbankRateGateway(ContentType contentType, Parser<InterbankRate> parser) {
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

	public Parser<InterbankRate> getParser() {
		return parser;
	}

	public void setParser(Parser<InterbankRate> parser) {
		this.parser = parser;
	}

	@Override
	public List<InterbankRate> getRefData() throws GatewayException {

		try {
			File file = FileGatewayHelper.getResourceFile(FileGatewayHelper.getPath(FileGatewayHelper.DATA_FOLDER, RATES_FILE, contentType.getName()));
			if (file != null && file.length() > 0)
				return parser.parse(new Document(contentType, DataType.REF, DataClass.InterbankRate, FileGatewayHelper.FILE_PROVIDER_SYMB, FileGatewayHelper.toBytes(file)));
			else
				return null;
		} catch (NullPointerException | IOException | ParserException e) {
			throw new GatewayException(e);
		}
	}

}
