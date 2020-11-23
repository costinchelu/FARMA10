package farma.util.fileIO;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ReadingAndWritiongNIO {

    public static void main(String[] args) {

        try {

            Path dataPath = FileSystems.getDefault().getPath(
                    "src" + File.separator + "farma" + File.separator +
                          "util" + File.separator + "user" + File.separator + "user.dat"
            );

            String timeStamp = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(new Date());
            String userId = String.valueOf(5);
            String userName = "Popescu";
            String entry = "User id: " + userId + " User name: " + userName + " [" + timeStamp + "]\n";

            Files.write(dataPath, userId.getBytes(StandardCharsets.UTF_8), StandardOpenOption.TRUNCATE_EXISTING);

            List<String> lines = Files.readAllLines(dataPath);
            for (String line : lines) {
                System.out.println(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
