package com.gb.apm.bootstrap.core.instrument.transfomer;

import com.gb.apm.bootstrap.core.instrument.InstrumentContext;
import com.gb.apm.bootstrap.core.instrument.matcher.Matcher;

/**
 * @author emeroad
 */
public class TransformTemplate implements TransformOperations {

    private final InstrumentContext instrumentContext;

    public TransformTemplate(InstrumentContext instrumentContext) {
        if (instrumentContext == null) {
            throw new NullPointerException("instrumentContext must not be null");
        }
        this.instrumentContext = instrumentContext;
    }

    @Override
    public void transform(String className, TransformCallback transformCallback) {
        if (className == null) {
            throw new NullPointerException("className must not be null");
        }
        if (transformCallback == null) {
            throw new NullPointerException("transformCallback must not be null");
        }
        this.instrumentContext.addClassFileTransformer(className, transformCallback);
    }

	@Override
	public void transform(Matcher matcher, TransformCallback transformCallback) {
		if (matcher == null) {
            throw new NullPointerException("matcher must not be null");
        }
        if (transformCallback == null) {
            throw new NullPointerException("transformCallback must not be null");
        }
        this.instrumentContext.addClassFileTransformer(matcher, transformCallback);
	}

}
