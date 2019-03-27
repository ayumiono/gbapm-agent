package com.gb.apm.common.hbase;

import java.nio.charset.Charset;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.HTableInterfaceFactory;

import com.gb.apm.common.utils.spring.StringUtils;

/**
 * Base class for {@link HbaseTemplate} and {@link HbaseInterceptor}, defining commons properties such as {@link HTableInterfaceFactory} and {@link Configuration}.
 * 
 * Not intended to be used directly.
 * 
 * @author Costin Leau
 */
public abstract class HbaseAccessor {

    private String encoding;
    private static final Charset CHARSET = Charset.forName("UTF-8");

    private TableFactory tableFactory;
    private Configuration configuration;

    /**
     * Sets the table factory.
     *
     * @param tableFactory The tableFactory to set.
     */
    public void setTableFactory(TableFactory tableFactory) {
        this.tableFactory = tableFactory;
    }

    /**
     * Sets the encoding.
     *
     * @param encoding The encoding to set.
     */
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    /**
     * Sets the configuration.
     *
     * @param configuration The configuration to set.
     */
    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public TableFactory getTableFactory() {
        return tableFactory;
    }

    public Configuration getConfiguration() {
        return configuration;
    }
    
    public Charset getCharset() {
        return (StringUtils.hasText(encoding) ? Charset.forName(encoding) : CHARSET);
    }
}