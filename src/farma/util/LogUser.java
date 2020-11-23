package farma.util;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogUser {

    public static void logUser(long userId, String userName, String operation) throws IOException {

        Path dataPath = FileSystems.getDefault().getPath(
                "src" + File.separator + "farma" + File.separator +
                        "util" + File.separator + "user" + File.separator + "log.txt"
        );

        String timeStamp = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(new Date());
        String entry = "[" + operation + "] id: " + userId + " User name: " + userName
                + " [" + timeStamp + "]\n";

        Files.write(dataPath, entry.getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
    }
}
