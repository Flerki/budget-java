package com.amairovi.config;

import com.amairovi.config.properties.BackUpProperties;
import com.amairovi.config.properties.ExpenseProperties;
import lombok.Data;

@Data
public class ApplicationProperties {

    private BackUpProperties backup = new BackUpProperties();
    private ExpenseProperties expense = new ExpenseProperties();

}
