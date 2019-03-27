package com.gb.apm.bootstrap.core.context;

import com.gb.apm.dapper.context._SpanEventRecorder;

public interface SpanEventRecorder extends _SpanEventRecorder {
	ParsingResult recordSqlInfo(String sql);

	void recordSqlParsingResult(ParsingResult parsingResult);

	void recordSqlParsingResult(ParsingResult parsingResult, String bindValue);
}
