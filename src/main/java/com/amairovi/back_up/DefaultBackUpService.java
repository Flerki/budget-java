package com.amairovi.back_up;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
public class DefaultBackUpService implements BackUpService {

    private final Path pathToBackUpDir;
    private final DateTimeFormatter formatter;

    public DefaultBackUpService(Path pathToBackUpDir, String pattern) {
        this.pathToBackUpDir = pathToBackUpDir;
        this.formatter = DateTimeFormatter.ofPattern(pattern);
    }


    @Override
    public void createBackUp() {
        final LocalDateTime now = LocalDateTime.now();

        try {
            if (!Files.exists(pathToBackUpDir)) {
                Files.createDirectories(pathToBackUpDir);
            }
            String filename = formatter.format(now) + ".xlsx";
            log.info("Creating back up at {} - {}", now, filename);
            final Path pathToNewBackUpFile = pathToBackUpDir.resolve(filename);
            Files.createFile(pathToNewBackUpFile);
            log.info("Back up was successfully created");
        } catch (IOException e) {
            log.error("Back up wasn't created: ", e);
        }
    }
}
