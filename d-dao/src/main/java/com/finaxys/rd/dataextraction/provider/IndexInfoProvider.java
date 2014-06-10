/*
 * 
 */
package com.finaxys.rd.dataextraction.provider;

import java.util.List;

import com.finaxys.rd.dataextraction.msg.Document;
import com.finaxys.rd.dataextraction.msg.Document.ContentType;

// TODO: Auto-generated Javadoc
/**
 * The Interface IndexInfoProvider.
 */
public interface IndexInfoProvider {
	
	/**
	 * Gets the index infos.
	 *
	 * @param format the format
	 * @return the index infos
	 * @throws Exception the exception
	 */
	public List<Document> getIndexInfos(ContentType format) throws Exception;
}
