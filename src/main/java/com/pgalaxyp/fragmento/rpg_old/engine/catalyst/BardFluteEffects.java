package com.pgalaxyp.fragmento.rpg_old.engine.catalyst;

import com.pgalaxyp.fragmento.rpg_old.content.entity.CutOrientation;
import com.pgalaxyp.fragmento.rpg_old.content.skill.BardSkills;
import com.pgalaxyp.fragmento.rpg_old.domain.action.ActionKind;
import com.pgalaxyp.fragmento.rpg_old.domain.id.SkillId;
import com.pgalaxyp.fragmento.rpg_old.domain.timing.Time;
import com.pgalaxyp.fragmento.rpg_old.effect.RpgEffect;
import com.pgalaxyp.fragmento.rpg_old.effect.gameplay.SpawnCutEffect;
import com.pgalaxyp.fragmento.rpg_old.effect.gameplay.SpawnInfusedStrikeEffect;
import com.pgalaxyp.fragmento.rpg_old.effect.gameplay.SpawnSpeedZoneEffect;
import com.pgalaxyp.fragmento.rpg_old.targeting.AimResolver;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public final class BardFluteEffects implements CatalystEffectAdapter {

    private static final int CUT_LIFE_TICKS = 15;
    private static final float INFUSED_HIT_DAMAGE = 6.0f;
    private static final int SPEED_ZONE_LIFE_TICKS = 100;

    private final AimResolver aim = new AimResolver();

    @Override
    public List<RpgEffect> onAction(
            ServerPlayer player,
            ActionKind action,
            int comboStep,
            SkillId skillId,
            Time now
    ) {
        if (action == ActionKind.COMBO_STEP) {
            return onComboStep(player, comboStep);
        }

        if (action == ActionKind.INFUSED_EXECUTE && skillId != null && skillId.value() == BardSkills.BARDO_NORMAL_INFUSED.value()) {
            return onInfusedStrike(player);
        }

        if (action == ActionKind.CAST_FINISH && skillId != null && skillId.value() == BardSkills.BARDO_SPECIAL_CASTED.value()) {
            return onSpecialCasted(player);
        }

        return List.of();
    }

    private List<RpgEffect> onComboStep(ServerPlayer player, int comboStep) {
        AimResolver.Aim a = aim.resolve(player, 20.0, 5.0);
        LivingEntity target = a.target();
        Vec3 targetPoint = target != null ? target.getBoundingBox().getCenter() : a.point();

        Vec3 forward = player.getLookAngle().normalize();
        Vec3 right = forward.cross(new Vec3(0, 1, 0)).normalize();

        double side = (comboStep % 2 == 0) ? 1.0 : -1.0;
        Vec3 spawn = player.position().add(0.0, 0.9, 0.0).add(right.scale(0.7 * side)).add(forward.scale(1.1));

        return List.of(
                new SpawnCutEffect(
                        player.getUUID(),
                        target != null ? target.getUUID() : null,
                        spawn.x, spawn.y, spawn.z,
                        targetPoint.x, targetPoint.y, targetPoint.z,
                        CUT_LIFE_TICKS,
                        3.0f,
                        CutOrientation.HORIZONTAL
                )
        );
    }

    private List<RpgEffect> onInfusedStrike(ServerPlayer player) {
        AimResolver.Aim a = aim.resolve(player, 24.0, 6.0);
        Vec3 impact = a.point();

        Vec3 start = player.position().add(0.0, 8.0, 0.0);

        return List.of(
                new SpawnInfusedStrikeEffect(
                        player.getUUID(),
                        INFUSED_HIT_DAMAGE,
                        start.x, start.y, start.z,
                        impact.x, impact.y, impact.z
                )
        );
    }

    private List<RpgEffect> onSpecialCasted(ServerPlayer player) {
        Vec3 pos = player.position();
        return List.of(
                new SpawnSpeedZoneEffect(
                        player.getUUID(),
                        pos.x,
                        pos.y,
                        pos.z,
                        SPEED_ZONE_LIFE_TICKS
                )
        );
    }
}