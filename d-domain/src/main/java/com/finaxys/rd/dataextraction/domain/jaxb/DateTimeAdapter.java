package com.finaxys.rd.dataextraction.domain.jaxb;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
 
public class DateTimeAdapter
    extends XmlAdapter<String, DateTime>{
	DateTimeFormatter dformatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.S");
	
    public DateTime unmarshal(String v) throws Exception {
        return dformatter.parseDateTime(v);
    }
 
    public String marshal(DateTime v) throws Exception {
        return dformatter.print(v);
    }
 
}