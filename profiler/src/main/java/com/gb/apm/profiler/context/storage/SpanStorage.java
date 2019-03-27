package com.gb.apm.profiler.context.storage;

import java.util.ArrayList;
import java.util.List;

import com.gb.apm.collector.DataSender;
import com.gb.apm.dapper.context.Span;
import com.gb.apm.model.TSpan;

/**
 * @author emeroad
 */
public class SpanStorage implements Storage {

	protected List<Span._SpanEvent> spanEventList = new ArrayList<Span._SpanEvent>(10);
    private final DataSender dataSender;

    public SpanStorage(DataSender dataSender) {
        if (dataSender == null) {
            throw new NullPointerException("dataSender must not be null");
        }
        this.dataSender = dataSender;
    }

    @Override
    public void store(Span._SpanEvent spanEvent) {
        if (spanEvent == null) {
            throw new NullPointerException("spanEvent must not be null");
        }
        final List<Span._SpanEvent> spanEventList = this.spanEventList;
        if (spanEventList != null) {
            spanEventList.add(spanEvent);
        } else {
            throw new IllegalStateException("spanEventList is null");
        }
    }

    @Override
    public void store(Span span) {
        if (span == null) {
            throw new NullPointerException("span must not be null");
        }
        span.setSpanEventList(spanEventList);
        spanEventList = null;
        if(span instanceof TSpan) {
        	this.dataSender.send((TSpan)span);
        }
    }

    @Override
    public void flush() {
    }

    @Override
    public void close() {
    }
}
