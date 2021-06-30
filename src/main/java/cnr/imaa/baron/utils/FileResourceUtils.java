package cnr.imaa.baron.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class FileResourceUtils {
    private final static String resourceFileDefault = "application.properties";

    private HashMap<String, String> config = new HashMap<>();

    public FileResourceUtils() {
        init(resourceFileDefault);
    }

    public FileResourceUtils(String resourceFile) {
        if (resourceFile == null)
            resourceFile = resourceFileDefault;

        init(resourceFile);
    }

    private void init(String resourceFile) {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(resourceFile);

        if (inputStream == null) {
            throw new IllegalArgumentException("file not found! " + resourceFile);
        } else {
            try (InputStreamReader streamReader =
                         new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                 BufferedReader reader = new BufferedReader(streamReader)) {

                String line;
                while ((line = reader.readLine()) != null) {
                    String value;

                    if (line.split("=").length < 2)
                        value = " ";
                    else
                        value = line.split("=")[1];

                    config.put(line.split("=")[0], value);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public HashMap<String, String> getConfig() {
        return config;
    }
}
