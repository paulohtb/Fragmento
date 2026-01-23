package com.pgalaxyp.fragmento.combat.content.bard;

import com.pgalaxyp.fragmento.combat.ability.api.AbilityDef;
import com.pgalaxyp.fragmento.combat.content.api.ContentPack;
import com.pgalaxyp.fragmento.combat.core.ids.WeaponId;
import com.pgalaxyp.fragmento.combat.effect.api.EffectDef;
import com.pgalaxyp.fragmento.combat.skill.api.SkillId;
import com.pgalaxyp.fragmento.combat.skill.api.SkillRule;
import com.pgalaxyp.fragmento.combat.targeting.api.TargetingFallback;
import com.pgalaxyp.fragmento.combat.targeting.api.TargetingMode;
import com.pgalaxyp.fragmento.combat.targeting.api.TargetingSpec;
import com.pgalaxyp.fragmento.combat.ability.api.AbilityId;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public enum BardContentPack implements ContentPack {
    INSTANCE;

    private static final TargetingSpec SPEC = new TargetingSpec(TargetingMode.RAYCAST_SINGLE, 16.0, TargetingFallback.IMAGINARY_POINT);

    @Override
    public Collection<AbilityDef> abilities() {
        return List.of(
                new AbilityDef(BardIds.FLUTE_NOTE, 10, 20, BardIds.FLUTE_NOTE_DAMAGE, SPEC),
                new AbilityDef(BardIds.FLUTE_NOTE_2, 10, 20, BardIds.FLUTE_NOTE_2_DAMAGE, SPEC)
        );
    }

    @Override
    public Collection<EffectDef> effects() {
        return List.of(
                EffectDef.of(BardIds.FLUTE_NOTE_DAMAGE, BardEffects.effects().get(BardIds.FLUTE_NOTE_DAMAGE).damage()),
                EffectDef.of(BardIds.FLUTE_NOTE_2_DAMAGE, BardEffects.effects().get(BardIds.FLUTE_NOTE_2_DAMAGE).damage())
        );
    }

    @Override
    public List<SkillRule> skills() {
        return List.of(
                new SkillRule(
                        new SkillId("bard.flute.combo"),
                        BardIds.BARD,
                        BardIds.FLUTE,
                        1,
                        BardIds.FLUTE_NOTE,
                        BardIds.FLUTE_NOTE_2
                )
        );
    }

    @Override
    public Map<WeaponId, AbilityId> primaryBindings() {
        return Map.of(BardIds.FLUTE, BardIds.FLUTE_NOTE);
    }
}