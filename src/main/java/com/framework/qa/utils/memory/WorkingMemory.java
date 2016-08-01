package com.framework.qa.utils.memory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * WorkingMemory.java
 * <p/>
 * Store and maintain temporary working memory
 *
 * @author Shiwantha Lakmal
 * @version 1.0-SNAPSHOT Last modified on 04_25_2016
 * @since 04/25/2016.
 */
public class WorkingMemory {
    private static WorkingMemory instance = null;
    private Map<String, String> memory;
    private Map<String, String[]> memoryArray;

    private static final Logger LOGGER = LoggerFactory.getLogger(WorkingMemory.class);

    private WorkingMemory() {
        memory = new HashMap<String, String>();
        memoryArray = new HashMap<String, String[]>();
    }

    /**
     * Get memory instance as a synchronized way
     * @return WorkingMemory
     */
    public static synchronized WorkingMemory getInstance() {
        if (instance == null) {
            instance = new WorkingMemory();
        }
        return instance;
    }

    /**
     * Store given temporary value under hash-map
     * @param key
     * @param value
     */
    public synchronized void setMemory(String key, String value) {
        memory.put(key, value);
        LOGGER.info("Memorizing the value'" + value + "' for the key: " + key);
    }

    /**
     * Store given temporary value under array-list
     * @param key
     * @param array
     */
    public synchronized void setMemoryArray(String key, String[] array) {
        memoryArray.put(key, array);
        LOGGER.info("Memorizing array:" + key + "with values"+ Arrays.toString(array));
    }

    /**
     * Get stored value according to the given key from hash-map
     * @param key
     * @return String
     */
    public synchronized String getMemory(String key) {
        try {
            LOGGER.info("Using the memorized value for the key " + key + ":"+ memory.get(key).toString());
            return memory.get(key).toString();
        } catch (Exception e) {
            LOGGER.error("exception :", e);
            return null;
        }
    }

    /**
     * Get stored value according to the given key from array-list
     * @param key
     * @return String[]
     */
    public synchronized String[] getMemoryArray(String key) {
        String[] memArray = null;
        try {
            memArray = memoryArray.get(key);
            LOGGER.info("Using the memorized array value for the key " + key+ ":" + memoryArray.get(key));
            return memArray;
        } catch (Exception e) {
            LOGGER.error("exception :", e);
            return memArray;
        }

    }

}
