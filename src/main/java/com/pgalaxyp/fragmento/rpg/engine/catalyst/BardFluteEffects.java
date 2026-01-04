package com.pgalaxyp.fragmento.rpg.engine.catalyst;

import com.pgalaxyp.fragmento.rpg.content.entity.CutOrientation;
import com.pgalaxyp.fragmento.rpg.domain.action.ActionKind;
import com.pgalaxyp.fragmento.rpg.domain.id.SkillId;
import com.pgalaxyp.fragmento.rpg.domain.timing.Time;
import com.pgalaxyp.fragmento.rpg.effect.RpgEffect;
import com.pgalaxyp.fragmento.rpg.effect.gameplay.SpawnCutEffect;
import com.pgalaxyp.fragmento.rpg.targeting.AimResolver;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public final class BardFluteEffects implements CatalystEffectAdapter {

    private static final int CUT_LIFE_TICKS = 15;
    private final AimResolver aim = new AimResolver();

    @Override
    public List<RpgEffect> onAction(
            ServerPlayer player,
            ActionKind action,
            int comboStep,
            SkillId skillId,
            Time now
    ) {
        if (action != ActionKind.COMBO_STEP) return List.of();

        AimResolver.Aim a = aim.resolve(player, 20.0, 5.0);
        LivingEntity target = a.target();
        Vec3 targetPoint = target != null
                ? target.getBoundingBox().getCenter()
                : a.point();

        Vec3 forward = player.getLookAngle().normalize();
        Vec3 right = forward.cross(new Vec3(0, 1, 0));
        if (right.lengthSqr() < 1.0E-6) right = new Vec3(1, 0, 0);
        right = right.normalize();

        Vec3 spawn;
        CutOrientation orientation;

        if (comboStep == 1) {
            spawn = targetPoint.add(right.scale(4.0));
            orientation = CutOrientation.HORIZONTAL;
        } else if (comboStep == 2) {
            spawn = targetPoint.subtract(right.scale(4.0));
            orientation = CutOrientation.HORIZONTAL;
        } else {
            spawn = targetPoint.add(0, 4.0, 0);
            orientation = CutOrientation.VERTICAL;
        }

        float dmg = comboStep == 1 ? 3.0f : comboStep == 2 ? 4.0f : 5.0f;

        return List.of(
                new SpawnCutEffect(
                        player.getUUID(),
                        target != null ? target.getUUID() : null,
                        spawn.x,
                        spawn.y,
                        spawn.z,
                        targetPoint.x,
                        targetPoint.y,
                        targetPoint.z,
                        CUT_LIFE_TICKS,
                        dmg,
                        orientation
                )
        );
    }
}