package com.amairovi.repository;

import com.amairovi.domain.Expense;
import com.amairovi.exception.Error;
import com.amairovi.exception.ServiceException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class DefaultExpenseRepository implements ExpenseRepository {
    public static final String EXPENSES_SHEET_NAME = "Расходы";
    public static final int CREATED_AT_CELL_IDX = 0;
    public static final int SUM_CELL_IDX = 1;
    public static final int CATEGORY_CELL_IDX = 2;
    public static final int COMMENT_AT_CELL_IDX = 3;

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

    @Override
    @NonNull
    public synchronized Expense save(@NonNull Expense expense) {
        final XSSFWorkbook workbook;
        try (final FileInputStream inputStream = new FileInputStream(pathToExpenseFile.toFile())) {
            workbook = new XSSFWorkbook(inputStream);
            addExpenseRow(expense, workbook);

        } catch (IOException e) {
            log.debug("Cannot create expense", e);
            throw new ServiceException(Error.IMPOSSIBLE_TO_CREATE_EXPENSE, "Cannot create expense", e);
        }

        try (final FileOutputStream outFile = new FileOutputStream(pathToExpenseFile.toFile())) {
            workbook.write(outFile);
            outFile.close();
            return expense;
        } catch (IOException e) {
            log.debug("Cannot save updated file", e);
            throw new ServiceException(Error.IMPOSSIBLE_TO_CREATE_EXPENSE, "Cannot save updated file", e);
        }
    }

    private void addExpenseRow(final Expense expense, final XSSFWorkbook workbook) {
        final XSSFSheet expensesSheet = workbook.getSheet(EXPENSES_SHEET_NAME);
        final int lastRowNum = expensesSheet.getLastRowNum();

        final XSSFRow expenseRow = expensesSheet.createRow(lastRowNum + 1);
        final XSSFCell createdAtCell = expenseRow.createCell(CREATED_AT_CELL_IDX);
        CellStyle cellStyle = workbook.createCellStyle();
        CreationHelper createHelper = workbook.getCreationHelper();
        cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd.MM.yyyy"));
        createdAtCell.setCellValue(Date.from(expense.getCreatedAt()));
        createdAtCell.setCellStyle(cellStyle);

        final XSSFCell sumCell = expenseRow.createCell(SUM_CELL_IDX);
        sumCell.setCellValue(expense.getSum().doubleValue());

        final XSSFCell categoryCell = expenseRow.createCell(CATEGORY_CELL_IDX);
        categoryCell.setCellValue(expense.getCategory());

        if (expense.getComment() != null) {
            final XSSFCell commentCell = expenseRow.createCell(COMMENT_AT_CELL_IDX);
            commentCell.setCellValue(expense.getComment());
        }
    }
}
