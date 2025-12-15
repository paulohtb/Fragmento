package com.pgalaxyp.fragmento.content.bard.catalyst;

import com.pgalaxyp.fragmento.gameplay.skill.Skill;
import com.pgalaxyp.fragmento.gameplay.skill.SkillSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public abstract class BardCatalystItem extends Item {

    protected final List<Skill> skills;

    protected BardCatalystItem(Properties props, List<Skill> skills) {
        super(props);
        this.skills = skills;
    }

    public Skill getSkill(SkillSlot slot) {
        int id = slot.id();
        return id >= 0 && id < skills.size() ? skills.get(id) : null;
    }

    public List<Skill> getSkills() {
        return skills;
    }

    public abstract void onSkillExecuted(
            ItemStack stack,
            SkillSlot slot
    );
}
