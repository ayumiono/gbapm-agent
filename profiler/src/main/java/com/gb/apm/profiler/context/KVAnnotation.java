package com.gb.apm.profiler.context;

import com.gb.apm.dapper.context.IAnnotation;
import com.gb.apm.model.Annotation;

public class KVAnnotation extends Annotation implements IAnnotation {
	
	public KVAnnotation(int key) {
		super(key);
	}

	public KVAnnotation(int key, String annotation) {
		super(key, annotation);
	}
}
