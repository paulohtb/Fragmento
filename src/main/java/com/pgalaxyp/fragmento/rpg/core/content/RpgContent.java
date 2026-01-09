package com.pgalaxyp.fragmento.rpg.core.content;

import com.pgalaxyp.fragmento.rpg.core.domain.def.ActionDef;
import com.pgalaxyp.fragmento.rpg.core.domain.def.ClassDef;
import com.pgalaxyp.fragmento.rpg.core.domain.def.EffectDef;
import com.pgalaxyp.fragmento.rpg.core.domain.def.WeaponDef;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.ActionId;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.ClassId;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.EffectId;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.WeaponId;
import java.util.Map;

public record RpgContent(
        Map<ClassId, ClassDef> classes,
        Map<ActionId, ActionDef> actions,
        Map<WeaponId, WeaponDef> weapons,
        Map<EffectId, EffectDef> effects
) {
    public RpgContent {
        if (classes == null || actions == null || weapons == null || effects == null) {
            throw new IllegalArgumentException();
        }

        classes = Map.copyOf(classes);
        actions = Map.copyOf(actions);
        weapons = Map.copyOf(weapons);
        effects = Map.copyOf(effects);

        validate(classes, actions, weapons, effects);
    }

    private static void validate(
            Map<ClassId, ClassDef> classes,
            Map<ActionId, ActionDef> actions,
            Map<WeaponId, WeaponDef> weapons,
            Map<EffectId, EffectDef> effects
    ) {
        for (var e : classes.entrySet()) {
            if (e.getKey() == null || e.getValue() == null) {
                throw new IllegalArgumentException();
            }
            if (!e.getKey().equals(e.getValue().id())) {
                throw new IllegalArgumentException();
            }
            if (!weapons.containsKey(e.getValue().startingWeaponId())) {
                throw new IllegalArgumentException();
            }
        }

        for (var e : weapons.entrySet()) {
            if (e.getKey() == null || e.getValue() == null) {
                throw new IllegalArgumentException();
            }
            if (!e.getKey().equals(e.getValue().id())) {
                throw new IllegalArgumentException();
            }
            if (!actions.containsKey(e.getValue().actionId())) {
                throw new IllegalArgumentException();
            }
        }

        for (var e : actions.entrySet()) {
            if (e.getKey() == null || e.getValue() == null) {
                throw new IllegalArgumentException();
            }
            if (!e.getKey().equals(e.getValue().id())) {
                throw new IllegalArgumentException();
            }
            for (EffectId effectId : e.getValue().effectSequence()) {
                if (effectId == null) {
                    throw new IllegalArgumentException();
                }
                if (!effects.containsKey(effectId)) {
                    throw new IllegalArgumentException();
                }
            }
        }

        for (var e : effects.entrySet()) {
            if (e.getKey() == null || e.getValue() == null) {
                throw new IllegalArgumentException();
            }
            if (!e.getKey().equals(e.getValue().id())) {
                throw new IllegalArgumentException();
            }
        }
    }

    public ClassDef clazz(ClassId id) {
        ClassDef def = classes.get(id);
        if (def == null) {
            throw new IllegalArgumentException();
        }
        return def;
    }

    public WeaponDef weapon(WeaponId id) {
        WeaponDef def = weapons.get(id);
        if (def == null) {
            throw new IllegalArgumentException();
        }
        return def;
    }

    public ActionDef action(ActionId id) {
        ActionDef def = actions.get(id);
        if (def == null) {
            throw new IllegalArgumentException();
        }
        return def;
    }

    public EffectDef effect(EffectId id) {
        EffectDef def = effects.get(id);
        if (def == null) {
            throw new IllegalArgumentException();
        }
        return def;
    }
}