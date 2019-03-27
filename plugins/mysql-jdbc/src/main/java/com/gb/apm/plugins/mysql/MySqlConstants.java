package com.gb.apm.plugins.mysql;

import com.gb.apm.common.trace.ServiceType;
import com.gb.apm.common.trace.ServiceTypeFactory;
import static com.gb.apm.common.trace.ServiceTypeProperty.*;

import com.gb.apm.common.trace.AnnotationKey;
import com.gb.apm.common.trace.AnnotationKeyFactory;

/**
 * @author Jongho Moon
 *
 */
public final class MySqlConstants {
    private MySqlConstants() {
    }

    public static final String MYSQL_SCOPE = "MYSQL_JDBC";
    
    public static final ServiceType MYSQL = ServiceTypeFactory.of(2100, "MYSQL", TERMINAL, INCLUDE_DESTINATION_ID);
    public static final ServiceType MYSQL_EXECUTE_QUERY = ServiceTypeFactory.of(2101, "MYSQL_EXECUTE_QUERY", "MYSQL", TERMINAL, RECORD_STATISTICS, INCLUDE_DESTINATION_ID);

    //add by xuelong.chen
    public static final AnnotationKey SQL_SOURCE = AnnotationKeyFactory.of(80,"sql.source");
}