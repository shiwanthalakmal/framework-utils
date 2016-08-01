package com.framework.qa.utils.exception;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * FrameworkException.java
 * <p/>
 * Parent Framework exception class which is extended by exception class
 *
 * @author Shiwantha Lakmal
 * @version 1.0-SNAPSHOT Last modified on 04_25_2016
 * @since 04/25/2016.
 */
public class FrameworkException extends Exception {
    private static final Logger LOGGER = LoggerFactory.getLogger(FrameworkException.class);

    /**
     * Constructor passing message only
     *
     * @param message error message
     */
    public FrameworkException(String message) {
        super(message);
        LOGGER.error(message);
    }

    /**
     * Constructor passing message and exception object
     *
     * @param message error message
     * @param e exception object
     */
    public FrameworkException(String message, Exception e) {
        super(message, e);
        LOGGER.error(message, e);
    }


}
