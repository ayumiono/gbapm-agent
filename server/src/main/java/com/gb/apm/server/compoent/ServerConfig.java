package com.gb.apm.server.compoent;

import java.io.Closeable;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Properties;

import com.gb.apm.common.utils.NumberUtils;

public class ServerConfig {
	
	public static final String DEFAULT_ENCODING = "UTF-8";

	private final Properties properties;

	public ServerConfig(Properties properties) {
		if (properties == null) {
			throw new NullPointerException("properties must not be null");
		}
		this.properties = properties;
	}
	
	public static ServerConfig load(String pinpointConfigFileName) throws IOException {
        try {
            Properties properties = loadProperty(pinpointConfigFileName);
            return new ServerConfig(properties);
        } catch (FileNotFoundException fe) {
            throw fe;
        } catch (IOException e) {
            throw e;
        }
    }

	public String readString(String propertyName, String defaultValue) {
		String value = properties.getProperty(propertyName, defaultValue);
		return value;
	}

	public int readInt(String propertyName, int defaultValue) {
		String value = properties.getProperty(propertyName);
		int result = NumberUtils.parseInteger(value, defaultValue);
		return result;
	}
	
	public static Properties loadProperty(final String filePath) throws IOException {
        if (filePath == null) {
            throw new NullPointerException("filePath must not be null");
        }
        final InputStreamFactory inputStreamFactory = new InputStreamFactory() {
            @Override
            public InputStream openInputStream() throws IOException {
                return new FileInputStream(filePath);
            }
        };
        return loadProperty(new Properties(), inputStreamFactory, DEFAULT_ENCODING);
    }
	
	public static Properties loadProperty(Properties properties, InputStreamFactory inputStreamFactory, String encoding) throws IOException {
        if (properties == null) {
            throw new NullPointerException("properties must not be null");
        }
        if (inputStreamFactory == null) {
            throw new NullPointerException("inputStreamFactory must not be null");
        }
        if (encoding == null) {
            throw new NullPointerException("encoding must not be null");
        }
        InputStream in = null;
        Reader reader = null;
        try {
            in = inputStreamFactory.openInputStream();
            reader = new InputStreamReader(in, encoding);
            properties.load(reader);
        } finally {
            close(reader);
            close(in);
        }
        return properties;
    }
	
	private static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException ignore) {
                // skip
            }
        }
    }
	
	
	public interface InputStreamFactory {
        InputStream openInputStream() throws IOException;
    }
}
