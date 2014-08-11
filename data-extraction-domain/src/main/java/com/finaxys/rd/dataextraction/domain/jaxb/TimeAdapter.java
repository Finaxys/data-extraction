package com.finaxys.rd.dataextraction.domain.jaxb;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
 
public class TimeAdapter
    extends XmlAdapter<String, LocalTime>{
	DateTimeFormatter dformatter = DateTimeFormat.forPattern("HH:mm:ss");
	
    public LocalTime unmarshal(String v) throws Exception {
        return dformatter.parseDateTime(v).toLocalTime();
    }
 
    public String marshal(LocalTime v) throws Exception {
        return dformatter.print(v);
    }
 
}