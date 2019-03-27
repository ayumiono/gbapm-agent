package com.gb.apm.common.trace;

/**
 * @author Jongho Moon <jongho.moon@navercorp.com>
 *
 */
public enum ServiceTypeCategory {
    UNDEFINED_CATEGORY((short)-1, (short)-1),
    PINPOINT_INTERNAL((short)0, (short)999),
    SERVER((short)1000, (short)1999),
    DATABASE((short)2000, (short)2999),
    LIBRARY((short)5000, (short)7999),
    CACHE_LIBRARY((short)8000, (short)8999, BaseHistogramSchema.FAST_SCHEMA),
    RPC((short)9000, (short)9999);
   
    
    private final short minCode;
    private final short maxCode;
    private HistogramSchema histogramSchema;

    ServiceTypeCategory(short minCode, short maxCode) {
        this(minCode, maxCode, BaseHistogramSchema.NORMAL_SCHEMA);
    }
    
    ServiceTypeCategory(short minCode, short maxCode, HistogramSchema histogramSchema) {
        this.minCode = minCode;
        this.maxCode = maxCode;
        if (histogramSchema == null) {
            throw new NullPointerException("histogramSchema must not be null");
        }
        this.histogramSchema = histogramSchema;
    }
    
    public boolean contains(short code) {
        return minCode <= code && code <= maxCode; 
    }
    
    public boolean contains(ServiceType type) {
        return contains(type.getCode());
    }

    public HistogramSchema getHistogramSchema() {
        return histogramSchema;
    }

    public static ServiceTypeCategory findCategory(short code) {
        for (ServiceTypeCategory serviceTypeCategory : ServiceTypeCategory.values()) {
            if (serviceTypeCategory.contains(code)) {
                return serviceTypeCategory;
            }
        }
        throw new IllegalStateException("Unknown Category code:" + code);
    }
}
