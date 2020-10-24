package com.amairovi;

import com.amairovi.back_up.BackUpProperties;
import com.amairovi.back_up.BackUpService;
import com.amairovi.config.BackUpConfiguration;
import com.amairovi.config.WebConfiguration;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Properties;

@Slf4j
public class Main {

    public static void main(String[] args) throws IOException {
        log.info("Starting app");
        final BackUpConfiguration backUpConfiguration = new BackUpConfiguration();

        final Properties properties = new Properties();
        properties.load(Main.class.getClassLoader().getResourceAsStream("application.properties"));
        log.info("Properties are loaded: {}", properties);

        final BackUpProperties backUpProperties = createBackUpProperties(properties);
        final BackUpService backUpService = backUpConfiguration.backUpService(backUpProperties);
        backUpConfiguration.scheduleDefaultBackUp(backUpService);

        new WebConfiguration(backUpService).create();
        log.info("App started");
    }

    private static BackUpProperties createBackUpProperties(final Properties properties) {
        final BackUpProperties backUpProperties = new BackUpProperties();
        final String pathToBackUpDir = properties.getProperty("backup.path-to-back-up-dir");
        backUpProperties.setPathToBackUpDir(pathToBackUpDir);
        final String backupFileDatePattern = properties.getProperty("backup.file.date-pattern");
        backUpProperties.setBackupFileDatePattern(backupFileDatePattern);

        return backUpProperties;
    }
}
