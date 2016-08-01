package com.framework.qa.utils.util;

/**
 * Created by kaslakmal on 4/26/2016.
 */
public class Constant {
    public static final String NUMBER_FORMAT_EXCEPTION = "";
    public static final String CONTEXT_MISSING_EXCEPTION = "Context Missing";
    public static final String NETWORK_FAILURE_EXCEPTION = "Network Connection Failure";
    //    private static final String DBURL_KEY = "dburl";
//    private static final String DBUSERNAME_KEY = "dbusername";
//    private static final String DBPASSWORD_KEY = "dbpassword";
    public static final String SCREENSHOT_DESKTOP_PATH = "screenshots/desktop/";
//    public static String DBURL;
//    public static String DBUSERNAME;
//    public static String DBPASSWORD;
//    private static HashMap<String, String> hmLoadDataFromXml;
//    private static String strClassName;

/*    static {

        try {
            loadXmlProperties();
        } catch (FrameworkException e) {
            e.printStackTrace();
        }
    }*/

    /**
     * Loading xml values to global variables.
     */

    /*public static void loadXmlProperties() throws FrameworkException {
        FrameworkProperties loadProperties = new FrameworkProperties();
        hmLoadDataFromXml = new HashMap<String, String>();
        URL inputStream = null;
        inputStream = Constant.class.getClassLoader().getResource("config/environment.xml");
        hmLoadDataFromXml = loadProperties.readProjEnvConfig(inputStream.getPath());


        try {
            // Display elements and assign xml values to public variables so it could be accessed globally.
            DBURL = (System.getProperty(DBURL_KEY) != null ? System.getProperty(DBURL_KEY) : hmLoadDataFromXml.get(DBURL_KEY));
            DBUSERNAME = (System.getProperty(DBUSERNAME_KEY) != null ? System.getProperty(DBUSERNAME_KEY) : hmLoadDataFromXml.get(DBUSERNAME_KEY));
            DBPASSWORD = (System.getProperty(DBPASSWORD_KEY) != null ? System.getProperty(DBPASSWORD_KEY) : hmLoadDataFromXml.get(DBPASSWORD_KEY));
        } catch (Exception e) {
            strClassName = Constant.class.getSimpleName();
            throw new FrameworkException("Configuration not found error on Class:" + strClassName + " line #:" + Thread.currentThread().getStackTrace()[1].getLineNumber(), e);
        }
    }*/
}
