package com.finaxys.rd.dataextraction.dao.integration.parser;
/*
 * 
 */


import java.util.List;

import com.finaxys.rd.dataextraction.domain.Document;

// TODO: Auto-generated Javadoc
/**
 * The Interface Parser.
 */
public interface Parser<T> {
	
	/**
	 * Convert.
	 *
	 * @param document the document
	 * @throws Exception the exception
	 */
	public List<T> parse(Document document) throws Exception;
}
