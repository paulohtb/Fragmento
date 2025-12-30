package com.pgalaxyp.fragmento.combat.engine.adapter.skill;

import com.pgalaxyp.fragmento.combat.engine.runtime.WeaponSkillEmitter;

public final class SimpleWeaponSkillEmitter implements WeaponSkillEmitter {

    private final WeaponSkillListener listener;

    public SimpleWeaponSkillEmitter(WeaponSkillListener listener) {
        this.listener = listener;
    }

    @Override
    public void pressSkill(int skillId) {
        if (listener != null) {
            listener.onSkillPressed(skillId);
        }
    }

    @Override
    public void cancelSkill(int skillId) {
        if (listener != null) {
            listener.onSkillCancelled(skillId);
        }
    }
}