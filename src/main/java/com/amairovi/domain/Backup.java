package com.amairovi.domain;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class Backup {
    private String filename;
    private Instant createdAt;
}
