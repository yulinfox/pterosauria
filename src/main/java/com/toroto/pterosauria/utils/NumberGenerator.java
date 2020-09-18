package com.toroto.pterosauria.utils;

/**
 * @author yuinfu
 * @date 2020/9/18
 */
public class NumberGenerator {

    private static long CODE = 0;

    public static synchronized String generate() {
        CODE ++;
        return String.format("%d%d", System.currentTimeMillis(), CODE);
    }

}
