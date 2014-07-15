package com.finaxys.rd.dataextraction.dao.integration.file;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;

import com.finaxys.rd.dataextraction.dao.integration.IndexInfoGateway;
import com.finaxys.rd.dataextraction.domain.msg.Document;
import com.finaxys.rd.dataextraction.domain.msg.Document.ContentType;
import com.finaxys.rd.dataextraction.domain.msg.Document.DataClass;
import com.finaxys.rd.dataextraction.domain.msg.Document.DataType;
import com.finaxys.rd.dataextraction.helper.GatewayHelper;

public class FileIndexInfoGateway implements IndexInfoGateway {

	/** The logger. */
	static Logger logger = Logger.getLogger(FileIndexInfoGateway.class);

	/** The file provider symb. */
	@Value("${gateway.file.symbol:0}")
	public char FILE_PROVIDER_SYMB;

	/** The data folder. */
	@Value("${gateway.file.dataFolder:home_data}")
	public String DATA_FOLDER;

	/** The index infos file. */
	@Value("${gateway.file.indexInfosFile:index_infos}")
	public String INDEX_INFOS_FILE;

	/** The content type. */
	private ContentType contentType;

	

	public FileIndexInfoGateway(ContentType contentType) {
		super();
		this.contentType = contentType;
	}



	public ContentType getContentType() {
		return contentType;
	}



	public void setContentType(ContentType contentType) {
		this.contentType = contentType;
	}



	public Document getIndexInfos() throws IOException {
		File file = GatewayHelper.getResourceFile(GatewayHelper.getPath(DATA_FOLDER, INDEX_INFOS_FILE,
				contentType.getName()));
		if (file != null && file.length() > 0)
			return new Document(contentType, DataType.Ref, DataClass.IndexInfo, FILE_PROVIDER_SYMB,
					GatewayHelper.toBytes(file));
		else
			return null;
	}

}