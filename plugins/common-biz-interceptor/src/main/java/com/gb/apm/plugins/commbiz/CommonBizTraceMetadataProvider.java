package com.gb.apm.plugins.commbiz;

import com.gb.apm.common.trace.TraceMetadataProvider;
import com.gb.apm.common.trace.TraceMetadataSetupContext;

public class CommonBizTraceMetadataProvider implements TraceMetadataProvider{
	@Override
	public void setup(TraceMetadataSetupContext context) {
		context.addAnnotationKey(CommBizConstants.RESPONSE_CODE);
	}
}
