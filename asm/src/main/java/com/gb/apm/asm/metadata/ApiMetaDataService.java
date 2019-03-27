package com.gb.apm.asm.metadata;

import com.gb.apm.dapper.context.MethodDescriptor;

/**
 * @author Woonduk Kang(emeroad)
 */
public interface ApiMetaDataService {

    int cacheApi(final MethodDescriptor methodDescriptor);
}
