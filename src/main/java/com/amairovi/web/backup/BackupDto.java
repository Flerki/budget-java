package com.amairovi.web.backup;

import lombok.Value;

import java.time.Instant;

@Value
public class BackupDto {
    String filename;
    Instant createdAt;
}
