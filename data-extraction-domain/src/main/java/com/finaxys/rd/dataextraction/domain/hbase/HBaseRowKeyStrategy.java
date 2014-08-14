/*
 * 
 */
package com.finaxys.rd.dataextraction.domain.hbase;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface HBaseRowKeyStrategy {

	Class<? extends RowKeyStrategy> strategy() default HashRowKeyStrategy.class;

}
