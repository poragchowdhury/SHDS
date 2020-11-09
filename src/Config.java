import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
	public static String OPENHAB_URL = "localhost";
	public static String OPENHAB_PORT = "8080";
	public static int CHECK_TIME = 60;
    
	public Config() {
        Properties prop = new Properties();

        InputStream input = null;

        try {
            input = new FileInputStream("config.properties");
            // load a properties file
            prop.load(input);
            // get the property value and print it out
            OPENHAB_URL = prop.getProperty("openHAB_URL");
            OPENHAB_PORT = prop.getProperty("openHAB_Port");
            CHECK_TIME = Integer.parseInt(prop.getProperty("check_Time"));
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
