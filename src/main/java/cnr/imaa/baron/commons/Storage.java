package cnr.imaa.baron.commons;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Storage {
    private static final Logger log = LoggerFactory.getLogger(Storage.class);

    public static synchronized void checkDirectory(String path) {
        Path pathDir = Paths.get(path);

        if (!Files.exists(pathDir)) {
            if (!Files.exists(pathDir.getParent())) {
                checkDirectory(pathDir.getParent().toString());
            }

            try {
                Files.createDirectory(pathDir);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}