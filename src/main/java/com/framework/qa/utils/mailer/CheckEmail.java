package com.framework.qa.utils.mailer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.search.FlagTerm;
import javax.mail.search.OrTerm;
import javax.mail.search.SearchTerm;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

/**
 * CheckEmail.java
 * <p/>
 * Email checking functionality automation
 *
 * @author Shiwantha Lakmal
 * @version 1.0-SNAPSHOT Last modified on 04_25_2016
 * @since 04/25/2016.
 */
public class CheckEmail {
    private static final Logger LOGGER = LoggerFactory.getLogger(CheckEmail.class);

    @SuppressWarnings("unused")
    private boolean textIsHtml = false;

    /**
     * Return the primary text content of the message.
     *
     * @param p
     * @return String
     * @throws MessagingException
     * @throws IOException
     */
    private String getText(Part p) throws MessagingException, IOException {
        if (p.isMimeType("text/*")) {
            String s = (String) p.getContent();
            textIsHtml = p.isMimeType("text/html");
            return s;
        }
        if (p.isMimeType("multipart/alternative")) {
            // prefer html text over plain text
            Multipart mp = (Multipart) p.getContent();
            String text = null;
            for (int i = 0; i < mp.getCount(); i++) {
                Part bp = mp.getBodyPart(i);
                if (bp.isMimeType("text/plain")) {
                    if (text == null)
                        text = getText(bp);
                    continue;
                } else if (bp.isMimeType("text/html")) {
                    String s = getText(bp);
                    if (s != null)
                        return s;
                } else {
                    return getText(bp);
                }
            }
            return text;
        } else if (p.isMimeType("multipart/*")) {
            Multipart mp = (Multipart) p.getContent();
            for (int i = 0; i < mp.getCount(); i++) {
                String s = getText(mp.getBodyPart(i));
                if (s != null)
                    return s;
            }
        }
        return null;
    }

    /**
     * Verify the availability of the searched mail
     *
     * @param userName
     * @param password
     * @param subjectKeyword
     * @param fromEmail
     * @param bodySearchText
     * @return boolean
     * @throws IOException
     */
    public boolean searchEmail(String userName, String password, final String subjectKeyword, final String fromEmail, final String bodySearchText) throws IOException {
        Properties properties = new Properties();
        boolean val = false;
        // server setting
        properties.put("mail.imap.host", "imap.gmail.com");
        properties.put("mail.imap.port", 993);
        // SSL setting
        properties.setProperty("mail.imap.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.setProperty("mail.imap.socketFactory.fallback", "false");
        properties.setProperty("mail.imap.socketFactory.port", String.valueOf(993));
        Session session = Session.getDefaultInstance(properties);
        try {
            // connects to the message store
            Store store = session.getStore("imap");
            store.connect(userName, password);
            System.out.println("Connected to Email server�.");
            // opens the inbox folder
            Folder folderInbox = store.getFolder("INBOX");
            folderInbox.open(Folder.READ_ONLY);
            //create a search term for all "unseen" messages
            Flags seen = new Flags(Flags.Flag.SEEN);
            FlagTerm unseenFlagTerm = new FlagTerm(seen, true);
            //create a search term for all recent messages
            Flags recent = new Flags(Flags.Flag.RECENT);
            FlagTerm recentFlagTerm = new FlagTerm(recent, false);
            SearchTerm searchTerm = new OrTerm(unseenFlagTerm, recentFlagTerm);
            // performs search through the folder
            Message[] foundMessages = folderInbox.search(searchTerm);
            LOGGER.info("Total Messages Found :" + foundMessages.length);
            for (int i = foundMessages.length - 1; i >= foundMessages.length - 10; i--) {
                Message message = foundMessages[i];
                Address[] froms = message.getFrom();
                String email = froms == null ? null : ((InternetAddress) froms[0]).getAddress();
                if (message.getSubject() == null) {
                    continue;
                }
                Date date = new Date();//Getting Present date from the system
                long diff = date.getTime() - message.getReceivedDate().getTime();//Get The difference between two dates
                long diffMinutes = diff / (60 * 1000) % 60; //Fetching the difference of minute
                LOGGER.info("Difference in Minutes b/w present time & Email Recieved time :" + diffMinutes);
                try {
                    if (message.getSubject().contains(subjectKeyword) && email.equals(fromEmail) && getText(message).contains(bodySearchText)/* && diffMinutes <= 3*/) {
                        String subject = message.getSubject();
                        LOGGER.info("Found message #" + i + ": ");
                        LOGGER.info("At " + i + " :" + "Subject:" + subject);
                        LOGGER.info("From: " + email + " on : " + message.getReceivedDate());
                        if (getText(message).contains(bodySearchText) == true) {
                            LOGGER.info("Message contains the search text " + bodySearchText);
                            val = true;
                        } else {
                            val = false;
                        }
                        break;
                    }
                } catch (NullPointerException expected) {
                    LOGGER.error("Some error ! ",expected.getMessage());
                    expected.printStackTrace();
                }
                LOGGER.info("Searching.�" + "At " + i);
            }
            folderInbox.close(false);
            store.close();
        } catch (NoSuchProviderException ex) {
            LOGGER.error("No provider. ", ex.getMessage());
            ex.printStackTrace();
        } catch (MessagingException ex) {
            LOGGER.error("Could not connect to the message store. ", ex.getMessage());
            ex.printStackTrace();
        }
        return val;
    }

    /**
     * Use to get email body text by giving start and end strings
     *
     * @param userName
     * @param password
     * @param subjectKeyword
     * @param fromEmail
     * @param startsWith
     * @param endsWith
     * @return string
     * @throws IOException
     */
    public String getEmailBodyMatchingText(String userName, String password, String subjectKeyword, String fromEmail, String startsWith, String endsWith) throws IOException {
        Properties properties = new Properties();
        // server setting
        properties.put("mail.imap.host", "imap.gmail.com");
        properties.put("mail.imap.port", 993);
        // SSL setting
        properties.setProperty("mail.imap.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.setProperty("mail.imap.socketFactory.fallback", "false");
        properties.setProperty("mail.imap.socketFactory.port", String.valueOf(993));
        Session session = Session.getDefaultInstance(properties);
        try {
            // connects to the message store
            Store store = session.getStore("imap");
            store.connect(userName, password);
            LOGGER.info("Connected to Email server�.");
            // opens the inbox folder
            Folder folderInbox = store.getFolder("INBOX");
            folderInbox.open(Folder.READ_ONLY);
            //create a search term for all "unseen" messages
            Flags seen = new Flags(Flags.Flag.SEEN);
            FlagTerm unseenFlagTerm = new FlagTerm(seen, true);
            //create a search term for all recent messages
            Flags recent = new Flags(Flags.Flag.RECENT);
            FlagTerm recentFlagTerm = new FlagTerm(recent, false);
            SearchTerm searchTerm = new OrTerm(unseenFlagTerm, recentFlagTerm);
            // performs search through the folder
            Message[] foundMessages = folderInbox.search(searchTerm);
            LOGGER.info("Total Messages Found :" + foundMessages.length);
            for (int i = foundMessages.length - 1; i >= foundMessages.length - 10; i--) {
                Message message = foundMessages[i];
                Address[] froms = message.getFrom();
                String email = froms == null ? null : ((InternetAddress) froms[0]).getAddress();
                if (message.getSubject() == null) {
                    continue;
                }
                Date date = new Date();//Getting Present date from the system
                long diff = date.getTime() - message.getReceivedDate().getTime();//Get The difference between two dates
                long diffMinutes = diff / (60 * 1000) % 60; //Fetching the difference of minute

                LOGGER.info("Difference in Minutes b/w present time & Email Recieved time :" + diffMinutes);
                try {
                    if (message.getSubject().contains(subjectKeyword) && email.equals(fromEmail) /* && diffMinutes <= 3*/) {
                        String subject = message.getSubject();
                        LOGGER.info("Found message #" + i + ": ");
                        LOGGER.info("At " + i + " :" + "Subject:" + subject);
                        LOGGER.info("From: " + email + " on : " + message.getReceivedDate());
                        String bodyFullText = this.getText(message), extractedText = null;
                        extractedText = bodyFullText.substring(bodyFullText.indexOf(startsWith), bodyFullText.indexOf(endsWith));
                        if (extractedText.isEmpty()) {
                            LOGGER.info("No matching text found");
                            return null;
                        } else {
                            return extractedText;
                        }
                    }
                } catch (NullPointerException expected) {
                    LOGGER.error("Some error ! ", expected.getMessage());
                    expected.printStackTrace();
                }
                LOGGER.info("Searching.�" + "At " + i);
            }
            folderInbox.close(false);
            store.close();
        } catch (NoSuchProviderException ex) {
            LOGGER.error("No provider. ", ex.getMessage());
            ex.printStackTrace();
        } catch (MessagingException ex) {
            LOGGER.error("Could not connect to the message store. ", ex.getMessage());
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * Test this program with a Gmail�s account
     *
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        String userName = "testautomation@gmail.com";
        String password = "passwordpwd";
        CheckEmail searcher = new CheckEmail();
        String subjectKeyword = "test email";
        String fromEmail = "shiwantha@gmail.com";
        String bodySearchText = "test message";
        searcher.searchEmail(userName, password, subjectKeyword, fromEmail, bodySearchText);
    }
}
