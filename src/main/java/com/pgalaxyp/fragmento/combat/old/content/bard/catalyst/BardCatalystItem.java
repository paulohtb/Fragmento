package com.pgalaxyp.fragmento.combat.old.content.bard.catalyst;

import com.pgalaxyp.fragmento.combat.old.system.skill.Skill;
import com.pgalaxyp.fragmento.combat.old.system.skill.SkillSlot;
import net.minecraft.world.item.Item;

import java.util.List;

public abstract class BardCatalystItem extends Item {

    protected final List<Skill> skills;

    protected BardCatalystItem(Properties props, List<Skill> skills) {
        super(props);
        this.skills = skills;
    }

    public Skill getSkill(SkillSlot slot) {
        if (slot == null) return null;

        int id = slot.id();
        return id >= 0 && id < skills.size() ? skills.get(id) : null;
    }

    public List<Skill> getSkills() {
        return skills;
    }
}