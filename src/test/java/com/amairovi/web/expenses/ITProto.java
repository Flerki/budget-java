package com.amairovi.web.expenses;

import com.amairovi.Main;
import com.amairovi.repository.ExpenseRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.io.File;
import java.io.FileOutputStream;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class ITProto {

    private static final AtomicInteger testId = new AtomicInteger(1);
    private static Main main;

    protected static final String HOST = "http://localhost:7000";
    protected static final OkHttpClient CLIENT = new OkHttpClient();
    protected static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    protected ExpenseRepository expenseRepository;

    @BeforeAll
    static void initializeApplication() throws Exception {
        main = new Main();
        try {
            File tempFile = File.createTempFile("excel-", "-test-" + testId.getAndIncrement() + ".xlsx");
            tempFile.deleteOnExit();

            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet();
            XSSFRow header = sheet.createRow(0);

            workbook.write(new FileOutputStream(tempFile));
            workbook.close();

            String pathToTempFile = tempFile.toPath().toString();
            log.info("Temp file for test is created: {}", pathToTempFile);
            main.configure(properties -> properties.getExpense().setPathToFile(pathToTempFile));
            main.start();
        } catch (Exception exception) {
            log.error("There is an exception during initialization of the app", exception);
            throw exception;
        }
    }

    @AfterAll
    static void stopApplication() {
        main.stop();
    }

    @BeforeEach
    void setup() {
        expenseRepository = main.getExpenseRepository();
        expenseRepository.removeAll();
    }
}
