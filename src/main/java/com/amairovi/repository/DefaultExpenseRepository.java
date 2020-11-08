package com.amairovi.repository;

import com.amairovi.domain.Expense;
import com.amairovi.exception.Error;
import com.amairovi.exception.ServiceException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class DefaultExpenseRepository implements ExpenseRepository {
    public static final String EXPENSES_SHEET_NAME = "Расходы";

    private final Path pathToExpenseFile;

    @Override
    @NonNull
    public synchronized List<Expense> findAll() {
        try (final XSSFWorkbook workbook = new XSSFWorkbook(pathToExpenseFile.toFile())) {
            final XSSFSheet expensesSheet = workbook.getSheet(EXPENSES_SHEET_NAME);
            final int lastRowNum = expensesSheet.getLastRowNum();

            final ArrayList<Expense> expenses = new ArrayList<>();
            for (int rowNumber = 1; rowNumber <= lastRowNum; rowNumber++) {
                final XSSFRow row = expensesSheet.getRow(rowNumber);
                final Instant createdAt = row.getCell(0).getDateCellValue().toInstant();
                final BigDecimal sum = new BigDecimal(row.getCell(1).getRawValue());
                final String category = row.getCell(2).getStringCellValue();
                final String comment = row.getCell(3) == null ? null : row.getCell(3).getStringCellValue();
                final Expense expense = Expense.builder()
                        .createdAt(createdAt)
                        .category(category)
                        .comment(comment)
                        .sum(sum)
                        .build();
                expenses.add(expense);
            }

            log.debug("{} expenses were retrieved: {}", expenses.size(), expenses);
            return expenses;
        } catch (IOException | InvalidFormatException e) {
            log.debug("Cannot read file", e);
            throw new ServiceException(Error.IMPOSSIBLE_TO_LIST_EXPENSES, "Cannot retrieve all expenses", e);
        }
    }
}
