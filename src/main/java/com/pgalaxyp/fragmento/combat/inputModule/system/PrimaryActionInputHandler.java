package com.pgalaxyp.fragmento.combat.inputModule.system;

import com.pgalaxyp.fragmento.combat.inputModule.api.*;
import com.pgalaxyp.fragmento.combat.inputModule.port.*;
import java.util.Objects;

public final class PrimaryActionInputHandler {
    private final LocalWeaponContext context;
    private final PrimaryActionCommandPort port;

    public PrimaryActionInputHandler(LocalWeaponContext context, PrimaryActionCommandPort port) {
        this.context = Objects.requireNonNull(context);
        this.port = Objects.requireNonNull(port);
    }

    public InputDecision onSemanticInput(SemanticInput input) {
        Objects.requireNonNull(input);
        if (input != SemanticInput.PRIMARY_ACTION) return InputDecision.passThrough();
        if (context.weaponInMainHandId().isEmpty()) return InputDecision.passThrough();
        return port.sendPrimaryAction() ? InputDecision.consume() : InputDecision.passThrough();
    }
}