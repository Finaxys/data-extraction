package com.finaxys.rd.dataextraction.dao.integration.file;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;

import com.finaxys.rd.dataextraction.dao.integration.HistInterbankRateDataGateway;
import com.finaxys.rd.dataextraction.domain.msg.Document;
import com.finaxys.rd.dataextraction.domain.msg.Document.ContentType;
import com.finaxys.rd.dataextraction.domain.msg.Document.DataClass;
import com.finaxys.rd.dataextraction.domain.msg.Document.DataType;
import com.finaxys.rd.dataextraction.helper.GatewayHelper;

public class FileHistInterbankRateDataGateway implements HistInterbankRateDataGateway{

	/** The logger. */
	static Logger logger = Logger.getLogger(FileHistInterbankRateDataGateway.class);

	/** The file provider symb. */
	@Value("${gateway.file.symbol:0}")
	public char FILE_PROVIDER_SYMB;

	/** The data folder. */
	@Value("${gateway.file.dataFolder:home_data}")
	public String DATA_FOLDER;

	/** The exchanges file. */
	@Value("${gateway.file.histInterbankRateDataFile:hist_interbank_rate_data_}")
	public String RATES_FILE;

	/** The content type. */
	private ContentType contentType;


	public FileHistInterbankRateDataGateway(ContentType contentType) {
		super();
		this.contentType = contentType;
	}

	public ContentType getContentType() {
		return contentType;
	}

	public void setContentType(ContentType contentType) {
		this.contentType = contentType;
	}

	public Document getHistRatesData(Integer year) throws IOException {
		File file = GatewayHelper
				.getResourceFile(GatewayHelper.getPath(DATA_FOLDER, RATES_FILE + year, contentType.getName()));
		if (file != null && file.length() > 0)
			return new Document(contentType, DataType.HIST, DataClass.InterbankRatesData, FILE_PROVIDER_SYMB,
					GatewayHelper.toBytes(file));
		else
			return null;

	}

}
