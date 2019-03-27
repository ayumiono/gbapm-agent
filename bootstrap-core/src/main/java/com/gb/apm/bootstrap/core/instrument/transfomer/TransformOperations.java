package com.gb.apm.bootstrap.core.instrument.transfomer;

import com.gb.apm.bootstrap.core.instrument.matcher.Matcher;

/**
 * @author Woonduk Kang(emeroad)
 */
@Plugin
public interface TransformOperations {

    void transform(String className, TransformCallback transformCallback);
    
    void transform(Matcher matcher, TransformCallback transformCallback);
}
