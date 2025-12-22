package com.pgalaxyp.fragmento.cosmetics.api;

import com.mojang.logging.LogUtils;
import java.util.Objects;
import org.slf4j.Logger;

public final class CosmeticValidationResult {

    private static final Logger LOGGER = LogUtils.getLogger();

    public static final CosmeticValidationResult OK = new CosmeticValidationResult(true, "ok");

    private final boolean success;
    private final String errorCode;

    public CosmeticValidationResult(boolean success, String errorCode) {
        this.success = success;
        this.errorCode = Objects.requireNonNull(errorCode, "errorCode");
        if (success) {
            LOGGER.debug("CosmeticValidationResult ok, code {}", this.errorCode);
        } else {
            LOGGER.warn("CosmeticValidationResult fail, code {}", this.errorCode);
        }
    }

    public boolean success() {
        return success;
    }

    public String errorCode() {
        return errorCode;
    }

    public static CosmeticValidationResult fail(String code) {
        return new CosmeticValidationResult(false, Objects.requireNonNull(code, "code"));
    }
}