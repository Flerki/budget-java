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
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Slf4j
public class Main {

    private final List<Consumer<ApplicationProperties>> propertiesConfigurator = new ArrayList<>();
    private WebConfiguration webConfiguration;
    @Getter
    private ExpenseRepository expenseRepository;

    public static void main(String[] args) {
        Main app = new Main();
        app.start();
        Runtime.getRuntime().addShutdownHook(new Thread(app::stop));
    }

    public void start() {
        log.info("Starting app");
        try {
            ObjectMapper propertiesParser = new ObjectMapper(new YAMLFactory());
            ApplicationProperties properties = propertiesParser.readValue(
                    Main.class.getClassLoader().getResourceAsStream("application.yaml"),
                    ApplicationProperties.class
            );

            propertiesConfigurator.forEach(configurator -> configurator.accept(properties));

            final BackUpConfiguration backUpConfiguration = new BackUpConfiguration();
            log.info("Properties are loaded: {}", propertiesParser.writeValueAsString(properties));

            final BackUpService backUpService = backUpConfiguration.backUpService(properties.getBackup(), properties.getExpense());
            backUpConfiguration.scheduleDefaultBackUp(backUpService);

            final ObjectMapper objectMapper = new ObjectMapper();

            final CreateBackUpHandler createBackUpHandler = new CreateBackUpHandler(backUpService, objectMapper);
            final ListBackUpHandler listBackUpHandler = new ListBackUpHandler(backUpService, objectMapper);

            final ExpenseConfiguration expenseConfiguration = new ExpenseConfiguration();
            expenseRepository = expenseConfiguration.expenseRepository(properties.getExpense());
            final ExpenseService expenseService = expenseConfiguration.expenseService(expenseRepository);
            final ListExpenseHandler listExpenseHandler = new ListExpenseHandler(expenseService, objectMapper);
            final CreateExpenseHandler createExpenseHandler = new CreateExpenseHandler(expenseService, objectMapper);

            webConfiguration = new WebConfiguration(createBackUpHandler, listBackUpHandler, createExpenseHandler, listExpenseHandler);
            webConfiguration.create();
        } catch (Exception e) {
            log.error("App couldn't be started", e);
        }
        log.info("App started");
    }

    public void stop() {
        log.info("Stopping app");
        if (webConfiguration != null) {
            log.info("Stopping web configuration");
            webConfiguration.stop();
        }
        log.info("App has been successfully stopped");
    }

    public void configure(Consumer<ApplicationProperties> propertiesBuilder) {
        propertiesConfigurator.add(propertiesBuilder);
    }
}
