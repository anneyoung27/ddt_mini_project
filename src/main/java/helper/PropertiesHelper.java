package helper;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Properties;

public class PropertiesHelper {
    private static FileInputStream file;

    public static Properties loadFile(){
        LinkedList<String> files = new LinkedList<>();

        files.add("\\src\\main\\resources\\config.properties");
        files.add("\\src\\main\\resources\\log4j2.properties");

        try {
            Properties properties = new Properties();

            for (String f : files){
                Properties tempProp = new Properties();
                String linkFile = SystemHelper.getCurrentDir() + f;
                file = new FileInputStream(linkFile);
                tempProp.load(file);
                properties.putAll(tempProp);
            }
            file.close();
            return properties;
        } catch (IOException e) {
            return new Properties();
        }
    }
}
