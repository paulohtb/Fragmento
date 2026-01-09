package com.pgalaxyp.fragmento.rpg.core.content;

import com.pgalaxyp.fragmento.rpg.core.domain.def.ActionDef;
import com.pgalaxyp.fragmento.rpg.core.domain.def.ClassDef;
import com.pgalaxyp.fragmento.rpg.core.domain.def.EffectDef;
import com.pgalaxyp.fragmento.rpg.core.domain.def.WeaponDef;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.ActionId;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.ClassId;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.EffectId;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.WeaponId;
import java.util.Collections;
import java.util.NavigableMap;
import java.util.Optional;
import java.util.TreeMap;

public record RpgContent(
        NavigableMap<ClassId, ClassDef> classes,
        NavigableMap<ActionId, ActionDef> actions,
        NavigableMap<WeaponId, WeaponDef> weapons,
        NavigableMap<EffectId, EffectDef> effects
) {
    public RpgContent {
        if (classes == null || actions == null || weapons == null || effects == null) {
            throw new IllegalArgumentException();
        }

        classes = Collections.unmodifiableNavigableMap(new TreeMap<>(classes));
        actions = Collections.unmodifiableNavigableMap(new TreeMap<>(actions));
        weapons = Collections.unmodifiableNavigableMap(new TreeMap<>(weapons));
        effects = Collections.unmodifiableNavigableMap(new TreeMap<>(effects));

        validate(classes, actions, weapons, effects);
    }

    private static void validate(
            NavigableMap<ClassId, ClassDef> classes,
            NavigableMap<ActionId, ActionDef> actions,
            NavigableMap<WeaponId, WeaponDef> weapons,
            NavigableMap<EffectId, EffectDef> effects
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

    public Optional<ClassDef> findClazz(ClassId id) {
        if (id == null) {
            throw new IllegalArgumentException();
        }
        return Optional.ofNullable(classes.get(id));
    }

    public Optional<WeaponDef> findWeapon(WeaponId id) {
        if (id == null) {
            throw new IllegalArgumentException();
        }
        return Optional.ofNullable(weapons.get(id));
    }

    public Optional<ActionDef> findAction(ActionId id) {
        if (id == null) {
            throw new IllegalArgumentException();
        }
        return Optional.ofNullable(actions.get(id));
    }

    public Optional<EffectDef> findEffect(EffectId id) {
        if (id == null) {
            throw new IllegalArgumentException();
        }
        return Optional.ofNullable(effects.get(id));
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