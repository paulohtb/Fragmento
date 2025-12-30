package com.pgalaxyp.fragmento.combat.engine.adapter.skill;

public interface WeaponSkillListener {

    void onSkillPressed(int skillId);

    void onSkillCancelled(int skillId);
}