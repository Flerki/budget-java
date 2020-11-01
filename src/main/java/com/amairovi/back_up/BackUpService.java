package com.amairovi.back_up;

import com.amairovi.domain.Backup;
import lombok.NonNull;

import java.util.List;

public interface BackUpService {
    @NonNull
    Backup createBackUp();

    @NonNull
    List<Backup> findAll();
}
