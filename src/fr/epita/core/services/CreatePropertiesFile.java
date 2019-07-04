package fr.epita.core.services;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;
import java.util.logging.Logger;
import java.util.logging.Level;
/**
 * This class is to create the config.properties file
 * in which the db.username, db.password or db.url are contained.
 * Run this main to create the file.
 */
public class CreatePropertiesFile {
    private static final Logger LOGGER =  
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    public static void main(String[] args) {
        
        try (OutputStream output = new FileOutputStream("./config.properties")) {

            Properties prop = new Properties();

            // set the properties value
            prop.setProperty("db.url", "jdbc:h2:./database/quiz_database");
            prop.setProperty("db.user", "sa");
            prop.setProperty("db.password", "");

            // save properties to project root folder
            prop.store(output, null);

            System.out.println(prop);

        } catch (IOException io) {
            LOGGER.log(Level.SEVERE, io.toString(), io);
        }

    }
}