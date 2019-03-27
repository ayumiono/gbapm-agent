package com.gb.apm.profiler.instrument.transformer;

import java.lang.instrument.ClassFileTransformer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.gb.apm.bootstrap.core.instrument.matcher.ClassNameMatcher;
import com.gb.apm.bootstrap.core.instrument.matcher.CustomMatcher;
import com.gb.apm.bootstrap.core.instrument.matcher.Matcher;
import com.gb.apm.bootstrap.core.instrument.matcher.MultiClassNameMatcher;

/**
 * @author emeroad
 * @author netspider
 * @author hyungil.jeong
 * @author Minwoo Jung
 * @author jaehong.kim
 */
public class DefaultTransformerRegistry implements TransformerRegistry {

    // No concurrent issue because only one thread put entries to the map and get operations are started AFTER the map is completely build.
    // Set the map size big intentionally to keep hash collision low.
    private final Map<Matcher, ClassFileTransformer> registry = new HashMap<Matcher, ClassFileTransformer>(512);

    @Override
    public ClassFileTransformer findTransformer(String className) {
    	for(Entry<Matcher, ClassFileTransformer> reg : registry.entrySet()) {
    		Matcher matcher = reg.getKey();
    		if(matcher instanceof ClassNameMatcher) {
    			final ClassNameMatcher classNameMatcher = (ClassNameMatcher)matcher;
    			String _className = classNameMatcher.getClassName();
    			if(_className.equals(className)) {
    				return reg.getValue();
    			}
    		} else if (matcher instanceof MultiClassNameMatcher) {
                final MultiClassNameMatcher classNameMatcher = (MultiClassNameMatcher)matcher;
                List<String> classNameList = classNameMatcher.getClassNames();
                for (String _className : classNameList) {
                	if(_className.equals(className)) {
        				return reg.getValue();
        			}
                }
            } else if (matcher instanceof CustomMatcher) {
            	if(((CustomMatcher) matcher).match(className)) {
            		return reg.getValue();
            	}
            } 
    	}
    	return null;
    }
    
    public void addTransformer(Matcher matcher, ClassFileTransformer transformer) {
        if (matcher instanceof ClassNameMatcher) {
        } else if (matcher instanceof MultiClassNameMatcher) {
        } else if(matcher instanceof CustomMatcher) {
        }else {
            throw new IllegalArgumentException("unsupported matcher :" + matcher);
        }
        addModifier0(transformer,matcher);
    }

    private void addModifier0(ClassFileTransformer transformer, Matcher matcher) {
        ClassFileTransformer old = registry.put(matcher, transformer);
        if (old != null) {
            throw new IllegalStateException("Transformer already exist. className:" + matcher + " new:" + transformer.getClass() + " old:" + old.getClass());
        }
    }
}
