package com.gb.apm.profiler.plugin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Woonduk Kang(emeroad)
 */
public class PinpointProfilerPackageSkipFilter implements ClassNameFilter {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final List<String> packageList;

    public PinpointProfilerPackageSkipFilter() {
        this(getPinpointPackageList());
    }

    public PinpointProfilerPackageSkipFilter(List<String> packageList) {
        if (packageList == null) {
            throw new NullPointerException("packageList must not be null");
        }
        this.packageList = new ArrayList<String>(packageList);
    }



    @Override
    public boolean accept(String className) {
        if (className == null) {
            throw new NullPointerException("className must not be null");
        }

        for (String packageName : packageList) {
            if (className.startsWith(packageName)) {
                if (logger.isDebugEnabled()) {
                    logger.info("skip ProfilerPackage:{} Class:{}", packageName, className);
                }
                return REJECT;
            }
        }
        return ACCEPT;
    }

    private static List<String> getPinpointPackageList() {
        List<String> pinpointPackageList = new ArrayList<String>();
        pinpointPackageList.add("com.navercorp.pinpoint.bootstrap");
        pinpointPackageList.add("com.navercorp.pinpoint.profiler");
        pinpointPackageList.add("com.navercorp.pinpoint.common");
        pinpointPackageList.add("com.navercorp.pinpoint.exception");
        // TODO move test package
        pinpointPackageList.add("com.navercorp.pinpoint.test");
        return pinpointPackageList;
    }
}
