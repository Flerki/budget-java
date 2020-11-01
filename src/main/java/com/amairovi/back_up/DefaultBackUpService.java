package com.amairovi.back_up;

import com.amairovi.domain.Backup;
import com.amairovi.exception.ServiceException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import static com.amairovi.exception.Error.BACK_UP_WAS_NOT_CREATED;
import static com.amairovi.exception.Error.IMPOSSIBLE_TO_LIST_BACK_UP;
import static java.time.ZoneOffset.UTC;

@Slf4j
public class DefaultBackUpService implements BackUpService {

    private static final String PATTERN = "YYYY_MM_dd__HH_mm_ss";
    public static final String FILE_SUFFIX = ".xlsx";
    private final Path pathToBackUpDir;
    private final Path pathToFileForBackUpDir;
    private final DateTimeFormatter formatter;

    public DefaultBackUpService(Path pathToBackUpDir, Path pathToFileForBackUpDir) {
        this.pathToBackUpDir = pathToBackUpDir;
        this.pathToFileForBackUpDir = pathToFileForBackUpDir;
        this.formatter = DateTimeFormatter.ofPattern(PATTERN);
    }


    @Override
    @NonNull
    public Backup createBackUp() {
        final LocalDateTime now = LocalDateTime.now();

        try {
            if (!Files.exists(pathToBackUpDir)) {
                Files.createDirectories(pathToBackUpDir);
            }
            String filename = formatter.format(now) + FILE_SUFFIX;
            log.info("Creating back up at {} - {}", now, filename);
            final Path pathToNewBackUpFile = pathToBackUpDir.resolve(filename);
            Files.copy(pathToFileForBackUpDir, pathToNewBackUpFile);
            log.info("Back up was successfully created. Filename - {}", filename);
            return Backup.builder()
                    .filename(filename)
                    .createdAt(now.toInstant(UTC))
                    .build();
        } catch (IOException e) {
            log.error("Back up wasn't created: ", e);
            throw new ServiceException(BACK_UP_WAS_NOT_CREATED, "Backup wasn't created", e);
        }
    }

    @Override
    @NonNull
    public List<Backup> findAll() {
        try {
            return Files.list(pathToBackUpDir)
                    .filter(Files::isRegularFile)
                    .map(p ->
                            Backup.builder()
                                    .filename(p.getFileName().toString())
                                    .build()
                    )
                    .collect(Collectors.toList());
        } catch (IOException e) {
            log.error("Can't list backups: ", e);
            throw new ServiceException(IMPOSSIBLE_TO_LIST_BACK_UP, "Can't list backups", e);
        }
    }
}
