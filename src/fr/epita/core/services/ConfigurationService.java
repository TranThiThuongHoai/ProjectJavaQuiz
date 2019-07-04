package fr.epita.core.services;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * This class is to read the Configuration saved in config.properties.
 * config.properties contains information like: db.username, db.password, db.url
 */
public class ConfigurationService {

    private Properties prop = new Properties();
    private final static Logger LOGGER =  
        Logger.getLogger(Logger.GLOBAL_LOGGER_NAME); 
    public ConfigurationService() {
        // load the properties
        try (InputStream input = new FileInputStream("./config.properties")) {
            this.prop.load(input);
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, ex.toString(), ex);
        }
    }

    /**
     * Get the information stored in the "properties" file by the given key
     * @param key, String, the corresponding key of the wanted value
     * @return the corresponding value
     */
    public String getConfigurationValue(String key){
        return prop.getProperty(key);
    }
    
}