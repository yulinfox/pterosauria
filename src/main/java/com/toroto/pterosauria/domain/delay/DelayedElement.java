package com.toroto.pterosauria.domain.delay;

import lombok.Getter;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * @author yuinfu
 * @date 2020/9/18
 */
public class DelayedElement implements Delayed {

    @Getter
    private Object obj;

    @Getter
    private long expire;

    public DelayedElement(Object obj, long expire) {
        this.obj = obj;
        this.expire = System.currentTimeMillis() + expire;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(this.expire - System.currentTimeMillis() , TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        return (int) (this.getDelay(TimeUnit.MILLISECONDS) -o.getDelay(TimeUnit.MILLISECONDS));
    }
}
