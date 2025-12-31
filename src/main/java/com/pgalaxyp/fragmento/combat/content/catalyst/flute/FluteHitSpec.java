package com.pgalaxyp.fragmento.combat.content.catalyst.flute;

public record FluteHitSpec(
        int comboIndex,
        float damage,
        double range
) {
    public static FluteHitSpec forComboIndex(int comboIndex) {
        return switch (comboIndex) {
            case 1 -> new FluteHitSpec(1, 4.0f, 2.6);
            case 2 -> new FluteHitSpec(2, 5.0f, 2.9);
            default -> new FluteHitSpec(0, 3.0f, 2.3);
        };
    }
}