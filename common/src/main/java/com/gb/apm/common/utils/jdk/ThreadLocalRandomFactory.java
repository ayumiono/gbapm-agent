package com.gb.apm.common.utils.jdk;

import java.util.Random;

/**
 * @author HyunGil Jeong
 */
public interface ThreadLocalRandomFactory {
    Random current();
}
