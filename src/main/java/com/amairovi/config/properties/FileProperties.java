package com.amairovi.config.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FileProperties {
    @JsonProperty("date-pattern")
    private String datePattern;
}
