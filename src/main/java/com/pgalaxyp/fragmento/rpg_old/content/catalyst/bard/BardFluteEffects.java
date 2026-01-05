package com.pgalaxyp.fragmento.rpg_old.content.catalyst.bard;

import com.pgalaxyp.fragmento.rpg_old.content.entity.CutOrientation;
import com.pgalaxyp.fragmento.rpg_old.content.skill.BardSkills;
import com.pgalaxyp.fragmento.rpg_old.domain.action.ActionKind;
import com.pgalaxyp.fragmento.rpg_old.domain.id.SkillId;
import com.pgalaxyp.fragmento.rpg_old.domain.timing.Time;
import com.pgalaxyp.fragmento.rpg_old.effect.RpgEffect;
import com.pgalaxyp.fragmento.rpg_old.effect.gameplay.SpawnCutEffect;
import com.pgalaxyp.fragmento.rpg_old.effect.gameplay.SpawnInfusedStrikeEffect;
import com.pgalaxyp.fragmento.rpg_old.engine.catalyst.CatalystEffectAdapter;
import com.pgalaxyp.fragmento.rpg_old.targeting.AimResolver;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public final class BardFluteEffects implements CatalystEffectAdapter {

    private static final int CUT_LIFE_TICKS = 15;
    private static final float INFUSED_HIT_DAMAGE = 6.0f;

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

        return List.of();
    }

    private List<RpgEffect> onComboStep(ServerPlayer player, int comboStep) {
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

    private List<RpgEffect> onInfusedStrike(ServerPlayer player) {
        AimResolver.Aim a = aim.resolve(player, 22.0, 7.0);
        LivingEntity target = a.target();

        Vec3 foot = target != null
                ? new Vec3(target.getX(), target.getBoundingBox().minY, target.getZ())
                : a.point();

        Vec3 from = foot.add(0.0, 1.0, 0.0);
        Vec3 to = foot.add(0.0, -12.0, 0.0);

        BlockHitResult hit = player.level().clip(new ClipContext(from, to, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player));
        Vec3 impact = hit != null && hit.getType() == HitResult.Type.BLOCK ? hit.getLocation() : foot.add(0.0, -1.0, 0.0);

        Vec3 start = foot.add(0.0, 5.0, 0.0);

        return List.of(
                new SpawnInfusedStrikeEffect(
                        player.getUUID(),
                        INFUSED_HIT_DAMAGE,
                        start.x, start.y, start.z,
                        impact.x, impact.y, impact.z
                )
        );
    }
}