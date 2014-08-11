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
import com.finaxys.rd.dataextraction.domain.Index;

public class FileIndexGateway implements RefDataGateway<Index> {

	/** The logger. */
	static Logger logger = Logger.getLogger(FileIndexGateway.class);

	/** The index infos file. */
	@Value("${gateway.file.indexesFile:indexes}")
	public String INDEXES_FILE;

	/** The content type. */
	private ContentType contentType;

	private Parser<Index> parser;

	public FileIndexGateway(ContentType contentType, Parser<Index> parser) {
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

	public Parser<Index> getParser() {
		return parser;
	}

	public void setParser(Parser<Index> parser) {
		this.parser = parser;
	}

	@Override
	public List<Index> getRefData() throws Exception {
		File file = FileGatewayHelper.getResourceFile(FileGatewayHelper
				.getPath(FileGatewayHelper.DATA_FOLDER, INDEXES_FILE,
						contentType.getName()));
		if (file != null && file.length() > 0)
			return parser.parse(new Document(contentType, DataType.REF,
					DataClass.IndexInfo, FileGatewayHelper.FILE_PROVIDER_SYMB,
					FileGatewayHelper.toBytes(file)));
		else
			return null;
	}

}