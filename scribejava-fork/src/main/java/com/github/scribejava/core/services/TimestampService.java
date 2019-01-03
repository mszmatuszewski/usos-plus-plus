package com.github.scribejava.core.services;

/**
 * Unix epoch timestamp generator.
 * <p>
 * This class is useful for stubbing in tests.
 */
public interface TimestampService {

    /**
     * Returns a nonce (unique value for each request)
     *
     * @return nonce
     */
    String getNonce();

    /**
     * Returns the unix epoch timestamp in seconds
     *
     * @return timestamp
     */
    String getTimestampInSeconds();
}
