package com.amairovi.config.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class BackUpProperties {
    @JsonProperty("path-to-back-up-dir")
    private String pathToBackUpDir;
    private FileProperties file = new FileProperties();
}

