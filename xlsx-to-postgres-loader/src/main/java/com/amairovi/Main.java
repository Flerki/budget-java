package com.amairovi;

import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;

public class Main {

    private final static String PATH_TO_EXPENSES = "";

    public static void main(String[] args) {
        final ExpenseReader expenseReader = new ExpenseReader(Path.of(PATH_TO_EXPENSES));
        final List<Expense> expenses = expenseReader.read();

        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/test",
                            "postgres", "root");
            System.out.println("Opened database successfully");

            stmt = c.createStatement();

            for (Expense expense : expenses) {
                final String comment = expense.getComment() != null ? "E'" + expense.getComment().replaceAll("'", "\\\\'") + "'" : expense.getComment();

                String sql = String.format("INSERT INTO expenses (cost, occured_at, categories, \"comment\") "
                                + "VALUES (%s, to_timestamp(%d), '%s', %s);",
                        expense.getSum(),
                        expense.getCreatedAt().getEpochSecond(),
                        expense.getCategory(),
                        comment);
                System.out.println(sql);
                stmt.executeUpdate(sql);

            }
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Table created successfully");
    }
}
