package com.pgalaxyp.fragmento.rpg.damage.system;

import com.pgalaxyp.fragmento.rpg.damage.domain.*;
import com.pgalaxyp.fragmento.rpg.damage.snapshot.*;

public final class DamageResolver {

    public DamageResult resolve(DamageRequest request, DamageSnapshot snapshot) {
        int base = request.spec().baseHearts();
        float resistance = snapshot.targetResistances().resistanceFor(request.spec().element());
        int reduced = Math.round(base * (1.0f - resistance));

        if (reduced < 1) {
            reduced = 1;
        }

        return new DamageResult(reduced, request.spec().type(), request.spec().element());
    }
}