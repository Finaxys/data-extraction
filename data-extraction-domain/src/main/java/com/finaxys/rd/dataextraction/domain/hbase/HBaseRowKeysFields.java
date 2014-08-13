/*
 * 
 */
package com.finaxys.rd.dataextraction.domain.hbase;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface HBaseRowKeysFields {

	HBaseRowKeyField[] rowkeys();

}
