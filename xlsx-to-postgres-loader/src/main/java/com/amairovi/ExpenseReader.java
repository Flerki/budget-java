package com.amairovi;

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
public class ExpenseReader {

    public static final String EXPENSES_SHEET_NAME = "Расходы";
    public static final int CREATED_AT_CELL_IDX = 0;
    public static final int SUM_CELL_IDX = 1;
    public static final int CATEGORY_CELL_IDX = 2;
    public static final int COMMENT_AT_CELL_IDX = 3;

    private final Path pathToExpenseFile;

    @NonNull
    public synchronized List<Expense> read() {
        try (final XSSFWorkbook workbook = new XSSFWorkbook(pathToExpenseFile.toFile())) {
            final XSSFSheet expensesSheet = workbook.getSheet(EXPENSES_SHEET_NAME);
            final int lastRowNum = expensesSheet.getLastRowNum();

            final ArrayList<Expense> expenses = new ArrayList<>();
            for (int rowNumber = 1; rowNumber <= lastRowNum; rowNumber++) {
                final XSSFRow row = expensesSheet.getRow(rowNumber);
                final Instant createdAt = row.getCell(CREATED_AT_CELL_IDX).getDateCellValue().toInstant();
                final BigDecimal sum = new BigDecimal(row.getCell(SUM_CELL_IDX).getRawValue());
                final String category = row.getCell(CATEGORY_CELL_IDX).getStringCellValue();
                final String comment = row.getCell(COMMENT_AT_CELL_IDX) == null ? null : row.getCell(COMMENT_AT_CELL_IDX).getStringCellValue();
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
