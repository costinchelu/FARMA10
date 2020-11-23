package farma.util;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class UserLogged {

    public static void writeUser(long userId) throws IOException {

        Path dataPath = FileSystems.getDefault().getPath(
                "src" + File.separator + "farma" + File.separator +
                        "util" + File.separator + "user" + File.separator + "user.dat"
        );

        Files.write(dataPath,  String.valueOf(userId).getBytes(StandardCharsets.UTF_8), StandardOpenOption.TRUNCATE_EXISTING);
    }

    public static long readUser() throws IOException {
        long userId = 0;

        Path dataPath = FileSystems.getDefault().getPath(
                "src" + File.separator + "farma" + File.separator +
                        "util" + File.separator + "user" + File.separator + "user.dat"
        );

        List<String> lines = Files.readAllLines(dataPath);
        for (String line : lines) {
            userId = Long.parseLong(line);
        }

        return userId;
    }
}
