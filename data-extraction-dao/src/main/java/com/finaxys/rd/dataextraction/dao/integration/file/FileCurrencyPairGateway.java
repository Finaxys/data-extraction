package com.finaxys.rd.dataextraction.dao.integration.file;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;

import com.finaxys.rd.dataextraction.dao.exception.GatewayCommunicationException;
import com.finaxys.rd.dataextraction.dao.exception.GatewayException;
import com.finaxys.rd.dataextraction.dao.exception.GatewaySecurityException;
import com.finaxys.rd.dataextraction.dao.exception.ParserException;
import com.finaxys.rd.dataextraction.dao.helper.FileGatewayHelper;
import com.finaxys.rd.dataextraction.dao.integration.RefDataGateway;
import com.finaxys.rd.dataextraction.dao.integration.parser.Parser;
import com.finaxys.rd.dataextraction.domain.CurrencyPair;
import com.finaxys.rd.dataextraction.domain.Document;
import com.finaxys.rd.dataextraction.domain.Enum.ContentType;
import com.finaxys.rd.dataextraction.domain.Enum.DataClass;
import com.finaxys.rd.dataextraction.domain.Enum.DataType;

public class FileCurrencyPairGateway implements RefDataGateway<CurrencyPair> {
	/** The logger. */
	static Logger logger = Logger.getLogger(FileCurrencyPairGateway.class);

	/** The currency pairs file. */
	@Value("${gateway.file.currencyPairsFile:currency_pairs}")
	private String CURRENCY_PAIRS_FILE;

	/** The content type. */
	private ContentType contentType;

	private Parser<CurrencyPair> parser;

	public FileCurrencyPairGateway(ContentType contentType,
			Parser<CurrencyPair> parser) {
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

	public Parser<CurrencyPair> getParser() {
		return parser;
	}

	public void setParser(Parser<CurrencyPair> parser) {
		this.parser = parser;
	}

	@Override
	public List<CurrencyPair> getRefData() throws GatewayException {
		try{
		File file = FileGatewayHelper.getResourceFile(FileGatewayHelper
				.getPath(FileGatewayHelper.DATA_FOLDER, CURRENCY_PAIRS_FILE,
						contentType.getName()));
		if (file != null && file.length() > 0)
			return parser.parse(new Document(contentType, DataType.REF,
					DataClass.CurrencyPair,
					FileGatewayHelper.FILE_PROVIDER_SYMB,
					FileGatewayHelper.toBytes(file)));
		else
			return null;
		} catch (NullPointerException  | IOException | ParserException e) {
			throw new GatewayException(e);
		}
	}

}