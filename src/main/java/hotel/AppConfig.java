package hotel;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AppConfig {
    private static final String DEFAULT_STATE_FILE = "hotel-state.json";
    private static final boolean DEFAULT_STATUS_CHANGE_ENABLED = true;

    private final String stateFilePath;
    private final boolean statusChangeEnabled;

    public AppConfig(String configFilePath) {
        Properties props = new Properties();

        try (InputStream in = new FileInputStream(configFilePath)) {
            props.load(in);
        } catch (IOException e) {
            System.out.println("Config file not found, using defaults.");
        }

        this.stateFilePath = props.getProperty("state.file.path", DEFAULT_STATE_FILE);
        this.statusChangeEnabled = Boolean.parseBoolean(
                props.getProperty("hotel.status.change.enabled",
                        String.valueOf(DEFAULT_STATUS_CHANGE_ENABLED)));
    }

    public String getStateFilePath() { return stateFilePath; }
    public boolean isStatusChangeEnabled() { return statusChangeEnabled; }
}
