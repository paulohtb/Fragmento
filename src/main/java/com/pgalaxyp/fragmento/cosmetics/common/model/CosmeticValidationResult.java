package com.pgalaxyp.fragmento.cosmetics.common.model;

public final class CosmeticValidationResult {

    public static final CosmeticValidationResult OK =
            new CosmeticValidationResult(true, null);

    private final boolean success;
    private final String reason;

    private CosmeticValidationResult(boolean success, String reason) {
        this.success = success;
        this.reason = reason;
    }

    public static CosmeticValidationResult fail(String reason) {
        return new CosmeticValidationResult(false, reason);
    }

    public boolean success() {
        return success;
    }

    public String reason() {
        return reason;
    }
}