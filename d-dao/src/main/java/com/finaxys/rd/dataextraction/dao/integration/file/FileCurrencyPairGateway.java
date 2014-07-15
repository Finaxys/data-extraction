package com.finaxys.rd.dataextraction.dao.integration.file;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;

import com.finaxys.rd.dataextraction.dao.integration.CurrencyPairGateway;
import com.finaxys.rd.dataextraction.domain.msg.Document;
import com.finaxys.rd.dataextraction.domain.msg.Document.ContentType;
import com.finaxys.rd.dataextraction.domain.msg.Document.DataClass;
import com.finaxys.rd.dataextraction.domain.msg.Document.DataType;
import com.finaxys.rd.dataextraction.helper.GatewayHelper;

public class FileCurrencyPairGateway implements CurrencyPairGateway {
	/** The logger. */
	static Logger logger = Logger.getLogger(FileCurrencyPairGateway.class);

	/** The file provider symb. */
	@Value("${gateway.file.symbol:0}")
	private char FILE_PROVIDER_SYMB;

	/** The data folder. */
	@Value("${gateway.file.dataFolder:home_data}")
	private String DATA_FOLDER;

	/** The currency pairs file. */
	@Value("${gateway.file.currencyPairsFile:currency_pairs}")
	private String CURRENCY_PAIRS_FILE;

	/** The content type. */
	private ContentType contentType;
	// private final char FILE_PROVIDER_SYMB;
	// private final String DATA_FOLDER;
	// private final String CURRENCY_PAIRS_FILE;
	//
	//
	//
	//
	// public FileCurrencyPairProvider(final @Value("${gateway.file.symbol:0}")
	// char fILE_PROVIDER_SYMB,final @Value("${gateway.file.symbol:0}") String
	// dATA_FOLDER,final @Value("${gateway.file.symbol:0}") String
	// cURRENCY_PAIRS_FILE) {
	// super();
	// FILE_PROVIDER_SYMB = fILE_PROVIDER_SYMB;
	// DATA_FOLDER = dATA_FOLDER;
	// CURRENCY_PAIRS_FILE = cURRENCY_PAIRS_FILE;
	// }

	

	public FileCurrencyPairGateway(ContentType contentType) {
		super();
		this.contentType = contentType;
	}


	public ContentType getContentType() {
		return contentType;
	}


	public void setContentType(ContentType contentType) {
		this.contentType = contentType;
	}


	public Document getCurrencyPairs() throws IOException {
		File file = GatewayHelper.getResourceFile(GatewayHelper.getPath(DATA_FOLDER, CURRENCY_PAIRS_FILE,
				contentType.getName()));
		if (file != null && file.length() > 0)
			return new Document(contentType, DataType.Ref, DataClass.CurrencyPair, FILE_PROVIDER_SYMB,
					GatewayHelper.toBytes(file));
		else
			return null;
	}

}