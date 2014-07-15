package com.finaxys.rd.dataextraction.dao.integration;

import com.finaxys.rd.dataextraction.domain.msg.Document;
import com.finaxys.rd.dataextraction.domain.msg.Document.ContentType;

public interface IndexInfoGateway {

/**
* Gets the index infos.
*
* @param format the format
* @return the index infos
* @throws Exception the exception
*/
public Document getIndexInfos() throws Exception;
}