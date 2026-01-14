package com.pgalaxyp.fragmento.rpg.core.content;

import com.pgalaxyp.fragmento.rpg.combo.api.*;
import com.pgalaxyp.fragmento.rpg.action.key.*;
import com.pgalaxyp.fragmento.rpg.action.type.*;
import com.pgalaxyp.fragmento.rpg.damage.domain.*;
import com.pgalaxyp.fragmento.rpg.core.domain.def.*;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.*;
import com.pgalaxyp.fragmento.rpg.core.domain.spec.*;
import java.util.*;

public final class DefaultRpgContent {

    public static final ClassId BARD = new ClassId("class.bard");
    public static final WeaponId FLUTE = new WeaponId("weapon.flute");
    public static final ActionKey FLUTE_BASIC_COMBO = new ActionKey("action.bard.flute.basic");
    public static final EffectId HIT_1 = new EffectId("effect.bard.flute.hit1");
    public static final EffectId HIT_2 = new EffectId("effect.bard.flute.hit2");
    public static final EffectId HIT_3 = new EffectId("effect.bard.flute.hit3");

    public static RpgContent create() {
        NavigableMap<EffectId, EffectDef> effects = new TreeMap<>();

        HomingMagicSpec homing = HomingMagicSpec.defaultSpec();

        effects.put(HIT_1, EffectDef.withVisual(HIT_1, new DamageSpec(1, DamageType.MAGIC, DamageElement.AIR), homing));
        effects.put(HIT_2, EffectDef.withVisual(HIT_2, new DamageSpec(1, DamageType.MAGIC, DamageElement.AIR), homing));
        effects.put(HIT_3, EffectDef.withVisual(HIT_3, new DamageSpec(1, DamageType.MAGIC, DamageElement.AIR), homing));

        List<ComboActionStep> steps = List.of(
                new ComboActionStep(0, ComboInput.PRIMARY, HIT_1, "anim.none"),
                new ComboActionStep(1, ComboInput.PRIMARY, HIT_2, "anim.none"),
                new ComboActionStep(2, ComboInput.PRIMARY, HIT_3, "anim.none")
        );

        ActionDefinition action = new ActionDefinition(FLUTE_BASIC_COMBO, ActionType.COMBO, new ComboActionPlan(steps, 1));

        NavigableMap<ActionKey, ActionDefinition> actions = new TreeMap<>();
        actions.put(FLUTE_BASIC_COMBO, action);

        WeaponDef weapon = new WeaponDef(FLUTE, WeaponSpec.empty(), FLUTE_BASIC_COMBO);

        NavigableMap<WeaponId, WeaponDef> weapons = new TreeMap<>();
        weapons.put(FLUTE, weapon);

        NavigableMap<ClassId, ClassDef> classes = new TreeMap<>();
        classes.put(BARD, new ClassDef(BARD, FLUTE));

        return new RpgContent(classes, actions, weapons, effects);
    }

    private DefaultRpgContent() {}
}