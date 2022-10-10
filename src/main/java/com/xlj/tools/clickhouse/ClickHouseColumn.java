package com.xlj.tools.clickhouse;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author legend xu
 * @date 2022/9/22
 */
@Inherited
@Target(FIELD)
@Retention(RUNTIME)
public @interface ClickHouseColumn {
    public String columnName() default "";
    public Class<?> wrapperType() default Void.class;
    public boolean isTimeStamp() default false;
}
