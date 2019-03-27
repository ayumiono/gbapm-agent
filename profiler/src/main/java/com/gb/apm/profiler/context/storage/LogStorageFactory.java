package com.gb.apm.profiler.context.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gb.apm.dapper.context.Span;
import com.gb.apm.dapper.context.Span._SpanEvent;

/**
 * @author emeroad
 */
public class LogStorageFactory implements StorageFactory {

    public final static Storage DEFAULT_STORAGE = new LogStorage();

    @Override
    public Storage createStorage() {
         // reuse because it has no states.
        return DEFAULT_STORAGE;
    }

    public static class LogStorage implements Storage {
        private final Logger logger = LoggerFactory.getLogger(this.getClass());
        @Override
        public void store(_SpanEvent spanEvent) {
            logger.debug("log spanEvent:{}", spanEvent);
        }

        @Override
        public void store(Span span) {
            logger.debug("log span:{}", span);
        }

        @Override
        public void flush() {
        }

        @Override
        public void close() {
        }
    }
}
