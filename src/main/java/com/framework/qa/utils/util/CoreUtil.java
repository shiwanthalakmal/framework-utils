package com.framework.qa.utils.util;

import com.framework.qa.utils.exception.ApplicationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static com.framework.qa.utils.util.Constant.SCREENSHOT_DESKTOP_PATH;
/**
 * Created by kaslakmal on 4/26/2016.
 */
public class CoreUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(CoreUtil.class);

    /**
     * Get current date as string
     * @return String
     */
    public static String getFileName() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'_'HH-mm-ssz");
        return dateFormat.format(new Date());
    }


    public static void captureDesktopScreenshot(String fileName) {
        BufferedImage image = null;
        try {
            new File("target/" + SCREENSHOT_DESKTOP_PATH).mkdirs();
            image = new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
            ImageIO.write(image, "png", new File(fileName + "-desktop.png"));
        } catch (AWTException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Use to delete any file which match to given file name and file extension in given location
     *
     * @param strFileName
     * @param strFilePath
     * @param extension
     */
    public static void deleteFileIfExists(final String strFileName, String strFilePath, final String extension){
        final File folder = new File(strFilePath);
        if (!folder.exists()) {
            folder.mkdir();
        }
        final File[] files = folder.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(final File dir,
                                  final String name) {
                return name.matches(strFileName + ".*\\."+extension);
            }
        });
        if (!files.equals(null)) {
            for (final File file : files) {
                if (file.exists()) {
                    file.delete();
                    LOGGER.info("["+file.getAbsolutePath() + " Delete Success]");
                }
            }
        }
    }

    /**
     * Use to delete any given directory
     *
     * @param directory
     */
    public static void deleteDirectory(File directory) {
        if(directory.exists()){
            File[] files = directory.listFiles();
            if(null!=files){
                for(int i=0; i<files.length; i++) {
                    if(files[i].isDirectory()) {
                        deleteDirectory(files[i]);
                    }
                    else {
                        files[i].delete();
                    }
                }
            }
        }
        directory.delete();
    }

    /**
     * Extract given zip file to given location
     *
     * @param zipFilePath
     * @param outputFolder
     * @throws ApplicationException
     */
    public static void extractZipFile(String zipFilePath, String outputFolder) throws ApplicationException {
        int BUFFER = 2048;
        try{
            ZipFile zip = new ZipFile(zipFilePath);
            File directory = new File(outputFolder);
            deleteDirectory(directory); //First delete outputFolder if exists
            new File(outputFolder).mkdir();
            Enumeration zipFileEntries = zip.entries();

            // Process each entry
            while (zipFileEntries.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) zipFileEntries.nextElement(); // grab a zip file entry
                String currentEntry = entry.getName();
                File destFile = new File(outputFolder, currentEntry);
                File destinationParent = destFile.getParentFile();
                destinationParent.mkdirs(); // create the parent directory structure if needed

                if (!entry.isDirectory()) {
                    BufferedInputStream is = new BufferedInputStream(zip.getInputStream(entry));
                    int currentByte;
                    byte data[] = new byte[BUFFER]; // establish buffer for writing file
                    FileOutputStream fos = new FileOutputStream(destFile); // write the current file to disk
                    BufferedOutputStream dest = new BufferedOutputStream(fos, BUFFER);
                    // read and write until last byte is encountered
                    while ((currentByte = is.read(data, 0, BUFFER)) != -1) {
                        dest.write(data, 0, currentByte);
                    }
                    dest.flush();
                    dest.close();
                    is.close();
                }
            }
        }catch (FileNotFoundException e){
            throw new ApplicationException("Destination folder not found");
        }catch (IOException e){
            throw new ApplicationException(".zip file not found");
        }

    }

    /**
     * Check given list is sorted in descending order
     *
     * @param list
     * @return boolean value [list is sorted in descending order]
     */
    public static boolean isSortedDescendingOrder (java.util.List<String> list){
        for (int i = 1; i < list.size(); i++) {
            if (list.get(i).compareTo(list.get(i-1)) > 0){
                return false;
            }
        }
        return true;
    }

    /**
     * Check given list is sorted in ascending order
     *
     * @param list
     * @return boolean value [list is sorted in ascending order]
     */
    public static boolean isSortedAscendingOrder (java.util.List<String> list){
        for (int i = 1; i < list.size(); i++) {
            if (list.get(i-1).compareTo(list.get(i)) > 0){
                return false;
            }
        }
        return true;
    }
}
