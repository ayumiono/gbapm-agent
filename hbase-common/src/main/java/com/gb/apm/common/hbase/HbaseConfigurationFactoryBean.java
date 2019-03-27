package com.gb.apm.common.hbase;

import java.util.Enumeration;
import java.util.Properties;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;

/**
 * Factory for creating HBase specific configuration. By default cleans up any connection associated with the current configuration.
 *
 * @author Costin Leau
 */
public class HbaseConfigurationFactoryBean {

    private Configuration configuration;
    private Configuration hadoopConfig;
    private Properties properties;

    /**
     * Sets the Hadoop configuration to use.
     *
     * @param configuration The configuration to set.
     */
    public void setConfiguration(Configuration configuration) {
        this.hadoopConfig = configuration;
    }

    /**
     * Sets the configuration properties.
     *
     * @param properties The properties to set.
     */
    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public void afterPropertiesSet() {
        configuration = (hadoopConfig != null ? HBaseConfiguration.create(hadoopConfig) : HBaseConfiguration.create());
        addProperties(configuration, properties);
    }
    
    /**
     * Adds the specified properties to the given {@link Configuration} object.  
     * 
     * @param configuration configuration to manipulate. Should not be null.
     * @param properties properties to add to the configuration. May be null.
     */
    private void addProperties(Configuration configuration, Properties properties) {
        Assert.notNull(configuration, "A non-null configuration is required");
        if (properties != null) {
            Enumeration<?> props = properties.propertyNames();
            while (props.hasMoreElements()) {
                String key = props.nextElement().toString();
                configuration.set(key, properties.getProperty(key));
            }
        }
    }

    public Configuration getObject() {
        return configuration;
    }

    public Class<? extends Configuration> getObjectType() {
        return (configuration != null ? configuration.getClass() : Configuration.class);
    }

    public boolean isSingleton() {
        return true;
    }
}