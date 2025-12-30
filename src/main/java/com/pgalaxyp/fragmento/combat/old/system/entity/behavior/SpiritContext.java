package com.pgalaxyp.fragmento.combat.old.system.entity.behavior;

import java.util.UUID;

import com.pgalaxyp.fragmento.combat.old.system.entity.event.SpiritSelf;
import com.pgalaxyp.fragmento.combat.old.system.skill.SkillMode;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public final class SpiritContext {

    public final SkillMode mode;
    public final SpiritSelf self;
    public final LivingEntity owner;
    public final LivingEntity target;
    public final Vec3 pos;
    public final int lifetimeTicks;
    public final boolean casted;
    public final Vec3 anchorPos;
    public final UUID sourceInstrumentUuid;

    public SpiritContext(
            SkillMode mode,
            SpiritSelf self,
            LivingEntity owner,
            LivingEntity target,
            Vec3 pos,
            int lifetimeTicks,
            boolean casted,
            Vec3 anchorPos,
            UUID sourceInstrumentUuid
    ) {
        this.mode = mode;
        this.self = self;
        this.owner = owner;
        this.target = target;
        this.pos = pos;
        this.lifetimeTicks = lifetimeTicks;
        this.casted = casted;
        this.anchorPos = anchorPos;
        this.sourceInstrumentUuid = sourceInstrumentUuid;
    }
}