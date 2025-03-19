package risinget.commander.utils;

import org.slf4j.Logger;
import risinget.commander.Commander;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileUtils {

    private static final Logger LOGGER = Commander.getLogger(FileUtils.class);

    public static synchronized void saveText(String msg, File file){
        createDirectories(file);
        saveMessage(msg, file);
    }

    private static void createDirectories(File file) {
        try {
            Files.createDirectories(file.toPath().getParent());
        } catch (IOException e) {
            LOGGER.error("Error al crear directorios: ", e);
        }
    }


    public static synchronized void saveMessage(String msg, File file) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            writer.write("[" + timestamp + "] " + msg);
            writer.newLine();
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
    }
}
