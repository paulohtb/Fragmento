package com.pgalaxyp.fragmento.rpg.core.content;

import com.pgalaxyp.fragmento.rpg.core.domain.def.ActionDef;
import com.pgalaxyp.fragmento.rpg.core.domain.def.ActionKind;
import com.pgalaxyp.fragmento.rpg.core.domain.def.ClassDef;
import com.pgalaxyp.fragmento.rpg.core.domain.def.EffectDef;
import com.pgalaxyp.fragmento.rpg.core.domain.def.WeaponDef;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.ActionId;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.ClassId;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.EffectId;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.WeaponId;
import com.pgalaxyp.fragmento.rpg.core.domain.spec.ComboSpec;
import com.pgalaxyp.fragmento.rpg.core.domain.spec.CycleSpec;
import com.pgalaxyp.fragmento.rpg.core.domain.spec.DamageSpec;
import com.pgalaxyp.fragmento.rpg.core.domain.spec.FrameWindowSpec;
import com.pgalaxyp.fragmento.rpg.core.domain.spec.TargetingSpec;
import com.pgalaxyp.fragmento.rpg.core.domain.spec.WeaponSpec;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class DefaultRpgContent {

    public static final ClassId BARD = new ClassId("class.bard");

    public static final WeaponId FLUTE = new WeaponId("weapon.flute");
    public static final ActionId FLUTE_BASIC_COMBO = new ActionId("action.bard.flute.basic");
    public static final EffectId HIT_1 = new EffectId("effect.bard.flute.hit1");
    public static final EffectId HIT_2 = new EffectId("effect.bard.flute.hit2");
    public static final EffectId HIT_3 = new EffectId("effect.bard.flute.hit3");

    public static RpgContent create() {
        Map<EffectId, EffectDef> effects = new LinkedHashMap<>();
        effects.put(HIT_1, new EffectDef(HIT_1, new DamageSpec(1)));
        effects.put(HIT_2, new EffectDef(HIT_2, new DamageSpec(1)));
        effects.put(HIT_3, new EffectDef(HIT_3, new DamageSpec(1)));

        CycleSpec cycle = new CycleSpec(
                new ComboSpec(3, 3),
                new TargetingSpec(true),
                new FrameWindowSpec(1)
        );

        ActionDef action = new ActionDef(
                FLUTE_BASIC_COMBO,
                ActionKind.COMBO,
                cycle,
                List.of(HIT_1, HIT_2, HIT_3)
        );

        Map<ActionId, ActionDef> actions = new LinkedHashMap<>();
        actions.put(FLUTE_BASIC_COMBO, action);

        WeaponDef weapon = new WeaponDef(FLUTE, WeaponSpec.empty(), FLUTE_BASIC_COMBO);
        Map<WeaponId, WeaponDef> weapons = new LinkedHashMap<>();
        weapons.put(FLUTE, weapon);

        Map<ClassId, ClassDef> classes = new LinkedHashMap<>();
        classes.put(BARD, new ClassDef(BARD, FLUTE));

        return new RpgContent(classes, actions, weapons, effects);
    }

    private DefaultRpgContent() {}
}