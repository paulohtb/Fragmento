package com.pgalaxyp.fragmento.rpg.core.content;

import com.pgalaxyp.fragmento.rpg.action.model.*;
import com.pgalaxyp.fragmento.rpg.damage.domain.*;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.*;
import com.pgalaxyp.fragmento.rpg.core.domain.def.*;
import com.pgalaxyp.fragmento.rpg.core.domain.spec.*;
import java.util.*;

public final class DefaultContent {

    public static final ClassId BARD = new ClassId("class.bard");
    public static final WeaponId FLUTE = new WeaponId("weapon.flute");
    public static final EffectId HIT_1 = new EffectId("effect.bard.flute.hit1");
    public static final EffectId HIT_2 = new EffectId("effect.bard.flute.hit2");
    public static final EffectId HIT_3 = new EffectId("effect.bard.flute.hit3");

    public static GameContent create() {
        NavigableMap<EffectId, EffectDef> effects = new TreeMap<>();
        HomingMagicSpec homing = HomingMagicSpec.defaultSpec();

        effects.put(HIT_1, EffectDef.withVisual(HIT_1, new DamageSpec(1, DamageType.MAGIC, DamageElement.AIR), homing));
        effects.put(HIT_2, EffectDef.withVisual(HIT_2, new DamageSpec(1, DamageType.MAGIC, DamageElement.AIR), homing));
        effects.put(HIT_3, EffectDef.withVisual(HIT_3, new DamageSpec(1, DamageType.MAGIC, DamageElement.AIR), homing));

        ActionDef action = new ActionDef(FLUTE_BASIC, ActionType.TIMED_SEQUENCE, new TimedSequenceActionPlan(
                        List.of(
                                new EffectStep(0, HIT_1),
                                new EffectStep(1, HIT_2),
                                new EffectStep(2, HIT_3)
                        ), 1));

        NavigableMap<ActionKey, ActionDef> actions = new TreeMap<>();
        actions.put(FLUTE_BASIC, action);

        WeaponDef weapon = new WeaponDef(FLUTE, WeaponSpec.empty(), FLUTE_BASIC);

        NavigableMap<WeaponId, WeaponDef> weapons = new TreeMap<>();
        weapons.put(FLUTE, weapon);

        NavigableMap<ClassId, ClassDef> classes = new TreeMap<>();
        classes.put(BARD, new ClassDef(BARD, FLUTE));

        return new GameContent(classes, actions, weapons, effects);
    }

    private DefaultContent() {}
}