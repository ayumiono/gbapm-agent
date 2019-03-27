package com.gb.apm.dapper.context;

import java.util.Random;

import com.gb.apm.common.utils.jdk.ThreadLocalRandomUtils;

/**
 * @author emeroad
 */
public class SpanId {

	public static final long NULL = -1;

	public static long newSpanId() {
		final Random random = getRandom();

		return createSpanId(random);
	}

	private static Random getRandom() {
		return ThreadLocalRandomUtils.current();
	}

	private static long createSpanId(Random seed) {
		long id = seed.nextLong();
		while (id == NULL) {
			id = seed.nextLong();
		}
		return id;
	}

	public static long nextSpanID(long spanId, long parentSpanId) {
		final Random seed = getRandom();

		long newId = createSpanId(seed);
		while (newId == spanId || newId == parentSpanId) {
			newId = createSpanId(seed);
		}
		return newId;
	}
}
