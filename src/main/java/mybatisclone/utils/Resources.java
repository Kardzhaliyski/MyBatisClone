package mybatisclone.utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class Resources {
    public static InputStream getResourceAsStream(String path) {
        try {
            return Files.newInputStream(Path.of(path));
        } catch (IOException e) {
            throw new RuntimeException(e); //todo exception
        }
    }
}
