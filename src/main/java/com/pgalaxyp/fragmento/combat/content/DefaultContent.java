package com.pgalaxyp.fragmento.combat.content;

import com.pgalaxyp.fragmento.combat.action.model.*;
import com.pgalaxyp.fragmento.combat.effect.model.*;
import com.pgalaxyp.fragmento.combat.damage.domain.*;
import com.pgalaxyp.fragmento.combat.core.domain.ids.*;
import com.pgalaxyp.fragmento.combat.core.domain.def.*;
import com.pgalaxyp.fragmento.combat.core.domain.spec.*;
import java.util.*;

public final class DefaultContent {

    public static final WeaponId FLUTE = new WeaponId("weapon.flute");

    public static GameContent create() {
        NavigableMap<ActionId, ActionDef> actions = new TreeMap<>();
        NavigableMap<WeaponId, WeaponDef> weapons = new TreeMap<>();
        NavigableMap<EffectId, EffectDef> effects = new TreeMap<>();

        EffectId magic = new EffectId("effect.magic.basic");
        effects.put(
                magic,
                EffectDef.withVisual(
                        magic,
                        new DamageSpec(2, DamageType.MAGIC, DamageElement.AIR),
                        HomingMagicSpec.defaultSpec()
                )
        );

        ActionId action = new ActionId("action.magic.basic");
        actions.put(action, new ActionDef(action, new InstantActionPlan(EffectIntent.of(magic))));

        weapons.put(FLUTE, WeaponDef.empty(FLUTE));

        return new GameContent(actions, weapons, effects);
    }

    private DefaultContent() {}
}