package com.gb.apm.common.utils.jdk;

import java.util.Random;

/**
 * @author HyunGil Jeong
 */
public class PinpointThreadLocalRandomFactory implements ThreadLocalRandomFactory {

    @Override
    public Random current() {
        return ThreadLocalRandom.current();
    }
}
