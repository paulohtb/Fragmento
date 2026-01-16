package com.pgalaxyp.fragmento.combat.content.defaults;

import com.pgalaxyp.fragmento.combat.action.model.ActionId;
import com.pgalaxyp.fragmento.combat.combo.model.ComboId;
import com.pgalaxyp.fragmento.combat.core.ids.*;

public final class DefaultIds {
    public static final ClassId CLASS_DEFAULT = new ClassId("class.default");
    public static final WeaponId WEAPON_FLUTE = new WeaponId("weapon.flute");
    public static final ComboId COMBO_FLUTE = new ComboId("combo.flute.basic");
    public static final ActionId ACTION_FLUTE_CAST = new ActionId("action.flute.cast");
    public static final EffectId EFFECT_FLUTE_MAGIC = new EffectId("effect.flute.magic");

    private DefaultIds() {}
}