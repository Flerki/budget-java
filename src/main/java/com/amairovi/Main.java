package com.amairovi;

import com.amairovi.back_up.BackUpService;
import com.amairovi.config.ApplicationProperties;
import com.amairovi.config.BackUpConfiguration;
import com.amairovi.config.ExpenseConfiguration;
import com.amairovi.config.WebConfiguration;
import com.amairovi.expense.ExpenseService;
import com.amairovi.repository.ExpenseRepository;
import com.amairovi.web.backup.CreateBackUpHandler;
import com.amairovi.web.backup.ListBackUpHandler;
import com.amairovi.web.expenses.CreateExpenseHandler;
import com.amairovi.web.expenses.ListExpenseHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class Main {

    public static void main(String[] args) throws IOException {
        log.info("Starting app");
        ObjectMapper propertiesParser = new ObjectMapper(new YAMLFactory());
        ApplicationProperties properties = propertiesParser.readValue(
                Main.class.getClassLoader().getResourceAsStream("application.yaml"),
                ApplicationProperties.class
        );

        final BackUpConfiguration backUpConfiguration = new BackUpConfiguration();
        log.info("Properties are loaded: {}", propertiesParser.writeValueAsString(properties));

        final BackUpService backUpService = backUpConfiguration.backUpService(properties.getBackup(), properties.getExpense());
        backUpConfiguration.scheduleDefaultBackUp(backUpService);

        final ObjectMapper objectMapper = new ObjectMapper();

        final CreateBackUpHandler createBackUpHandler = new CreateBackUpHandler(backUpService, objectMapper);
        final ListBackUpHandler listBackUpHandler = new ListBackUpHandler(backUpService, objectMapper);

        final ExpenseConfiguration expenseConfiguration = new ExpenseConfiguration();
        final ExpenseRepository expenseRepository = expenseConfiguration.expenseRepository(properties.getExpense());
        final ExpenseService expenseService = expenseConfiguration.expenseService(expenseRepository);
        final ListExpenseHandler listExpenseHandler = new ListExpenseHandler(expenseService, objectMapper);
        final CreateExpenseHandler createExpenseHandler = new CreateExpenseHandler(expenseService, objectMapper);

        new WebConfiguration(createBackUpHandler, listBackUpHandler, createExpenseHandler, listExpenseHandler).create();
        log.info("App started");
    }
}
