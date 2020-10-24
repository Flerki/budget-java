package com.amairovi;

import com.amairovi.back_up.BackUpProperties;
import com.amairovi.back_up.BackUpService;
import com.amairovi.config.BackUpConfiguration;
import com.amairovi.config.ExpenseProperties;
import com.amairovi.config.WebConfiguration;
import com.amairovi.web.backup.CreateBackUpHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
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
        final ExpenseProperties expenseProperties = createExpenseProperties(properties);
        final BackUpService backUpService = backUpConfiguration.backUpService(backUpProperties, expenseProperties);
        backUpConfiguration.scheduleDefaultBackUp(backUpService);

        final ObjectMapper objectMapper = new ObjectMapper();

        final CreateBackUpHandler createBackUpHandler = new CreateBackUpHandler(backUpService, objectMapper);
        new WebConfiguration(createBackUpHandler).create();
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

    private static ExpenseProperties createExpenseProperties(Properties properties) {
        final String pathToFile = properties.getProperty("expense.path-to-file");

        final ExpenseProperties expenseProperties = new ExpenseProperties();
        expenseProperties.setPathToExpenseFile(pathToFile);

        return expenseProperties;
    }
}
