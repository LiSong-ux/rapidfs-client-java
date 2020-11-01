package net.industryhive.client.config;

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.FileReader;

/**
 * @Author 未央
 * @Create 2020-10-29 16:11
 */
public class ConfigLoader {
    public static String[] TRACKER_ADDR;
    public static String TRACKER_PORT;
    public static String STORAGE_PORT;

    static {
        ClassPathResource resource = new ClassPathResource("rapidfs.properties.sample");
        try {
            File configFile = resource.getFile();
            FileReader fileReader = new FileReader(configFile);
            PropertiesConfiguration prop = new PropertiesConfiguration();
            prop.read(fileReader);
            TRACKER_ADDR = prop.getStringArray("tracker.addr");
            TRACKER_PORT = prop.getString("tracker.port", "19093");
            STORAGE_PORT = prop.getString("storage.port", "19094");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
