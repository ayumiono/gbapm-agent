package com.gb.apm.profiler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gb.apm.bootstrap.core.plugin.ApplicationTypeDetector;
import com.gb.apm.bootstrap.core.resolver.ApplicationServerTypePluginResolver;
import com.gb.apm.common.trace.ServiceType;

/**
 * @author emeroad
 * @author netspider
 * @author hyungil.jeong
 */
public class ApplicationServerTypeResolver {
    
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ServiceType defaultType;
    private final ApplicationServerTypePluginResolver resolver;


    public ApplicationServerTypeResolver(List<ApplicationTypeDetector> applicationTypeDetector, ServiceType defaultType, List<String> orderedDetectors) {
        if (applicationTypeDetector == null) {
            throw new NullPointerException("applicationTypeDetector must not be null");
        }

        if (isValidApplicationServerType(defaultType)) {
            this.defaultType = defaultType;
        } else {
            this.defaultType = ServiceType.UNDEFINED;
        }

        List<ApplicationTypeDetector> sortedDetectors = sortByOrder(orderedDetectors, applicationTypeDetector);

        this.resolver = new ApplicationServerTypePluginResolver(sortedDetectors);
    }

    private List<ApplicationTypeDetector> sortByOrder(List<String> orderedDetectors, List<ApplicationTypeDetector> applicationTypeDetectors) {
        final List<ApplicationTypeDetector> detectionOrder = new ArrayList<ApplicationTypeDetector>();

        Map<String, ApplicationTypeDetector> applicationTypeDetectorMap = toMap(applicationTypeDetectors);
        for (String orderedDetector : orderedDetectors) {
            if (applicationTypeDetectorMap.containsKey(orderedDetector)) {
                detectionOrder.add(applicationTypeDetectorMap.remove(orderedDetector));
            }
        }

        detectionOrder.addAll(applicationTypeDetectorMap.values());
        return detectionOrder;
    }

    private Map<String, ApplicationTypeDetector> toMap(List<ApplicationTypeDetector> applicationTypeDetectorList) {

        Map<String, ApplicationTypeDetector> typeDetectorMap = new HashMap<String, ApplicationTypeDetector>();
        for (ApplicationTypeDetector applicationTypeDetector : applicationTypeDetectorList) {
            typeDetectorMap.put(applicationTypeDetector.getClass().getName(), applicationTypeDetector);
        }
        return typeDetectorMap;
    }


    public ServiceType resolve() {
        ServiceType resolvedApplicationServerType;
        if (this.defaultType == ServiceType.UNDEFINED) {
            resolvedApplicationServerType = this.resolver.resolve();
            logger.info("Resolved ApplicationServerType : {}", resolvedApplicationServerType.getName());
        } else {
            resolvedApplicationServerType = this.defaultType;
            logger.info("Configured ApplicationServerType : {}", resolvedApplicationServerType.getName());
        }
        return resolvedApplicationServerType;
    }
    
    private boolean isValidApplicationServerType(ServiceType serviceType) {
        if (serviceType == null) {
            return false;
        }
        return serviceType.isWas();
    }
}
