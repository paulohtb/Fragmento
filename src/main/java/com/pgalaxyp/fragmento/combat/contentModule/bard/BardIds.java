package com.pgalaxyp.fragmento.combat.contentModule.bard;

import com.pgalaxyp.fragmento.combat.abilityModule.api.AbilityId;
import com.pgalaxyp.fragmento.combat.classModule.api.ClassId;
import com.pgalaxyp.fragmento.combat.effectModule.api.EffectId;
import com.pgalaxyp.fragmento.combat.weaponModule.WeaponId;

public final class BardIds {
    public static final ClassId BARD = new ClassId("bard");
    public static final WeaponId FLUTE = new WeaponId("flute");
    public static final AbilityId FLUTE_NOTE = new AbilityId("flute.note");
    public static final AbilityId FLUTE_NOTE_2 = new AbilityId("flute.note.2");
    public static final EffectId FLUTE_NOTE_DAMAGE = new EffectId("flute.note.damage");
    public static final EffectId FLUTE_NOTE_2_DAMAGE = new EffectId("flute.note.2.damage");

    private BardIds() {}
}