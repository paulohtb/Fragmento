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
import com.pgalaxyp.fragmento.rpg.core.domain.spec.ActionCycleSpec;
import com.pgalaxyp.fragmento.rpg.core.domain.spec.ComboSpec;
import com.pgalaxyp.fragmento.rpg.core.domain.spec.HomingMagicSpec;
import com.pgalaxyp.fragmento.rpg.core.domain.spec.StepWindowSpec;
import com.pgalaxyp.fragmento.rpg.core.domain.spec.WeaponSpec;
import com.pgalaxyp.fragmento.rpg.damage.domain.DamageElement;
import com.pgalaxyp.fragmento.rpg.damage.domain.DamageSpec;
import com.pgalaxyp.fragmento.rpg.damage.domain.DamageType;
import com.pgalaxyp.fragmento.rpg.targeting.api.TargetingFallback;
import com.pgalaxyp.fragmento.rpg.targeting.api.TargetingMode;
import com.pgalaxyp.fragmento.rpg.targeting.api.TargetingSpec;
import java.util.List;
import java.util.NavigableMap;
import java.util.TreeMap;

public final class DefaultRpgContent {

    public static final ClassId BARD = new ClassId("class.bard");

    public static final WeaponId FLUTE = new WeaponId("weapon.flute");
    public static final ActionId FLUTE_BASIC_COMBO = new ActionId("action.bard.flute.basic");

    public static final EffectId HIT_1 = new EffectId("effect.bard.flute.hit1");
    public static final EffectId HIT_2 = new EffectId("effect.bard.flute.hit2");
    public static final EffectId HIT_3 = new EffectId("effect.bard.flute.hit3");

    public static RpgContent create() {
        NavigableMap<EffectId, EffectDef> effects = new TreeMap<>();

        HomingMagicSpec homing = HomingMagicSpec.defaultSpec();
        effects.put(HIT_1, EffectDef.withVisual(HIT_1, new DamageSpec(1, DamageType.MAGIC, DamageElement.AIR), homing));
        effects.put(HIT_2, EffectDef.withVisual(HIT_2, new DamageSpec(1, DamageType.MAGIC, DamageElement.AIR), homing));
        effects.put(HIT_3, EffectDef.withVisual(HIT_3, new DamageSpec(1, DamageType.MAGIC, DamageElement.AIR), homing));

        ActionCycleSpec cycle = new ActionCycleSpec(
                new ComboSpec(3, 3),
                new TargetingSpec(TargetingMode.RAYCAST_SINGLE, 20, TargetingFallback.IMAGINARY_POINT),
                new StepWindowSpec(1)
        );

        ActionDef action = new ActionDef(
                FLUTE_BASIC_COMBO,
                ActionKind.COMBO,
                cycle,
                List.of(HIT_1, HIT_2, HIT_3)
        );

        NavigableMap<ActionId, ActionDef> actions = new TreeMap<>();
        actions.put(FLUTE_BASIC_COMBO, action);

        WeaponDef weapon = new WeaponDef(
                FLUTE,
                WeaponSpec.empty(),
                FLUTE_BASIC_COMBO
        );

        NavigableMap<WeaponId, WeaponDef> weapons = new TreeMap<>();
        weapons.put(FLUTE, weapon);

        NavigableMap<ClassId, ClassDef> classes = new TreeMap<>();
        classes.put(BARD, new ClassDef(BARD, FLUTE));

        return new RpgContent(classes, actions, weapons, effects);
    }

    private DefaultRpgContent() {}
}