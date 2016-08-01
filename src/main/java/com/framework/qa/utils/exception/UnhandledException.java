package com.framework.qa.utils.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * UnhandledException.java
 * <p/>
 * Unknown exceptions
 *
 * @author Shiwantha Lakmal
 * @version 1.0-SNAPSHOT Last modified on 04_25_2016
 * @since 04/25/2016.
 */
public class UnhandledException extends FrameworkException {
    private static final Logger LOGGER = LoggerFactory.getLogger(UnhandledException.class);

    /**
     * Constructor passing message only
     *
     * @param message error message
     */
    public UnhandledException(String message) {
        super(message);
        LOGGER.error(message);
    }

    /**
     * Constructor passing message and exception object
     *
     * @param message error message
     * @param e exception object
     */
    public UnhandledException(String message, Exception e) {
        super(message, e);
        LOGGER.error(message, e);
    }
}
