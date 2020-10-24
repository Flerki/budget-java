package com.amairovi.back_up;

import com.amairovi.domain.Backup;
import com.amairovi.exception.ServiceException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import static com.amairovi.exception.Error.BACK_UP_WAS_NOT_CREATED;

@Slf4j
public class DefaultBackUpService implements BackUpService {

    private final Path pathToBackUpDir;
    private final Path pathToFileForBackUpDir;
    private final DateTimeFormatter formatter;

    public DefaultBackUpService(Path pathToBackUpDir, Path pathToFileForBackUpDir, String pattern) {
        this.pathToBackUpDir = pathToBackUpDir;
        this.pathToFileForBackUpDir = pathToFileForBackUpDir;
        this.formatter = DateTimeFormatter.ofPattern(pattern);
    }


    @Override
    @NonNull
    public Backup createBackUp() {
        final LocalDateTime now = LocalDateTime.now();

        try {
            if (!Files.exists(pathToBackUpDir)) {
                Files.createDirectories(pathToBackUpDir);
            }
            String filename = formatter.format(now) + ".xlsx";
            log.info("Creating back up at {} - {}", now, filename);
            final Path pathToNewBackUpFile = pathToBackUpDir.resolve(filename);
            Files.copy(pathToFileForBackUpDir, pathToNewBackUpFile);
            log.info("Back up was successfully created. Filename - {}", filename);
            return Backup.builder()
                    .filename(filename)
                    .createdAt(now.toInstant(ZoneOffset.UTC))
                    .build();
        } catch (IOException e) {
            log.error("Back up wasn't created: ", e);
            throw new ServiceException(BACK_UP_WAS_NOT_CREATED,
                    "Backup wasn't created",  e);
        }
    }
}
