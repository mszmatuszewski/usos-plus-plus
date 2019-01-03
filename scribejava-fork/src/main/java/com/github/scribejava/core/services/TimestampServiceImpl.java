package com.github.scribejava.core.services;

import java.util.Random;

/**
 * Implementation of {@link TimestampService} using plain java classes.
 */
public class TimestampServiceImpl implements TimestampService {

    private Timer timer;

    /**
     * Default constructor.
     */
    public TimestampServiceImpl() {
        timer = new Timer();
    }

    /**
     * Inner class that uses {@link System} for generating the timestamps.
     */
    static class Timer {

        private final Random rand = new Random();

        Long getMilis() {
            return System.currentTimeMillis();
        }

        Integer getRandomInteger() {
            return rand.nextInt();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getNonce() {
        final Long ts = getTs();
        return String.valueOf(ts + timer.getRandomInteger());
    }

    private Long getTs() {
        return timer.getMilis() / 1000;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTimestampInSeconds() {
        return String.valueOf(getTs());
    }

    void setTimer(Timer timer) {
        this.timer = timer;
    }
}
