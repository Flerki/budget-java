package com.amairovi;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws IOException {
        String fileLocation = "<path-to-file>";

        FileInputStream file = new FileInputStream(fileLocation);
        Workbook workbook = new XSSFWorkbook(file);

        Sheet sheet = workbook.getSheetAt(0);

        int[][] configs = {
                {7, 8, 26},
                {28, 29, 32},
        };

        for (int[] config : configs) {
            System.out.println();
            int titleRowPos = config[0];
            int startRow = config[1];
            int endRow = config[2];
            Map<ColumnPos, Integer> model = getModel(sheet, titleRowPos);

            for (int rowIdx = startRow; rowIdx <= endRow; rowIdx++) {
                Row row = sheet.getRow(rowIdx);
                String id = row.getCell(model.get(ColumnPos.ID)).getStringCellValue();
                String date = row.getCell(model.get(ColumnPos.DATE)).getStringCellValue();
                String kind = row.getCell(model.get(ColumnPos.KIND)).getStringCellValue();
                String shortenedName = row.getCell(model.get(ColumnPos.SHORTENED_NAME)).getStringCellValue();
                String ticker = row.getCell(model.get(ColumnPos.TICKER)).getStringCellValue();
                String currency = row.getCell(model.get(ColumnPos.CURRENCY)).getStringCellValue();
                String amount = row.getCell(model.get(ColumnPos.AMOUNT)).getStringCellValue();
                String totalPrice = row.getCell(model.get(ColumnPos.TOTAL_PRICE)).getStringCellValue();
                String bondAccruedInterest = row.getCell(model.get(ColumnPos.BOND_ACCRUED_INTEREST)).getStringCellValue();
                String commission = row.getCell(model.get(ColumnPos.COMMISSION)).getStringCellValue();

                String result = String.join("\t",
                        id, date, kind, ticker, shortenedName, currency,
                        amount, totalPrice, commission, bondAccruedInterest);
                System.out.println(result);
            }
        }
    }

    private static Map<ColumnPos, Integer> getModel(Sheet sheet, int titleRow) {
        int pos = 0;
        Map<ColumnPos, Integer>  columnToItsPos = new HashMap<>();

        Row rowTest = sheet.getRow(titleRow);

        for (Iterator<Cell> it = rowTest.cellIterator(); it.hasNext(); ) {
            Cell cell = it.next();

            if (!cell.getStringCellValue().equals("")) {
                ColumnPos.getByPos(pos)
                        .ifPresent(colPos -> columnToItsPos.put(colPos, cell.getColumnIndex()));
                pos++;
            }
        }

        return columnToItsPos;
    }
}
