package com.gb.apm.plugins.mysql;

import com.gb.apm.common.trace.AnnotationKey;
import com.gb.apm.common.trace.AnnotationKeyMatchers;
import com.gb.apm.common.trace.TraceMetadataProvider;
import com.gb.apm.common.trace.TraceMetadataSetupContext;

/**
 * @author Jongho Moon
 *
 */
public class MySqlTypeProvider implements TraceMetadataProvider {

    @Override
    public void setup(TraceMetadataSetupContext context) {
        context.addServiceType(MySqlConstants.MYSQL, AnnotationKeyMatchers.exact(AnnotationKey.ARGS0));
        context.addServiceType(MySqlConstants.MYSQL_EXECUTE_QUERY, AnnotationKeyMatchers.exact(AnnotationKey.ARGS0));
    }

}
