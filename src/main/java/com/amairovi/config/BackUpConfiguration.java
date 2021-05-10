package com.amairovi.config;

import com.amairovi.back_up.BackUpService;
import com.amairovi.back_up.DefaultBackUpService;
import com.amairovi.config.properties.BackUpProperties;
import com.amairovi.config.properties.ExpenseProperties;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static java.util.concurrent.TimeUnit.DAYS;

@Slf4j
public class BackUpConfiguration {
    private final ScheduledExecutorService scheduler =
            Executors.newScheduledThreadPool(1);


    public void scheduleDefaultBackUp(BackUpService backUpService) {
        scheduler.scheduleAtFixedRate(() -> {
            try {
                backUpService.createBackUp();
            } catch (Exception e) {
                log.error("Scheduled back up failed", e);
            }
        }, 0, 1, DAYS);
    }

    public BackUpService backUpService(BackUpProperties backUpProperties, ExpenseProperties expenseProperties) {
        final String pathToBackUpDir = backUpProperties.getPathToBackUpDir();
        final Path path = Paths.get(pathToBackUpDir);
        final Path pathToExpenseFile = Paths.get(expenseProperties.getPathToFile());
        return new DefaultBackUpService(path, pathToExpenseFile);
    }
}
