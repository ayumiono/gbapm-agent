package com.gb.apm.plugins.commbiz;

import com.gb.apm.bootstrap.core.instrument.InstrumentMethod;
import com.gb.apm.bootstrap.core.instrument.MethodFilter;

/**
 * 
 * 所有方法
 * @author xuelong.chen
 *
 */
public class CommBizMethodFilter implements MethodFilter {

	@Override
	public boolean accept(InstrumentMethod method) {
		if(method.isConstructor()) return false;
		return true;
	}

}
