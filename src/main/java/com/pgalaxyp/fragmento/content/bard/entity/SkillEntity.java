package com.pgalaxyp.fragmento.content.bard.entity;

import com.pgalaxyp.fragmento.core.controller.EntityController;

public abstract class SkillEntity extends EntityController<BardSkillEntityBase> {

    protected SkillEntity(BardSkillEntityBase spirit) {
        super(spirit);
    }

    protected final BardSkillEntityBase spirit() {
        return entity;
    }

    protected void onCasted() {
    }

    protected void onCancelled() {
    }

    @Override
    protected final void onTick() {
        tickSkill();
    }

    protected abstract void tickSkill();
}
