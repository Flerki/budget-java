package com.amairovi.back_up;

import com.amairovi.domain.Backup;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

public interface BackUpService {
    @NonNull
    Backup createBackUp();
}
