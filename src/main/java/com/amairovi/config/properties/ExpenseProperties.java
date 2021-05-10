package com.amairovi.config.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ExpenseProperties {
    @JsonProperty("path-to-file")
    private String pathToFile;
}
