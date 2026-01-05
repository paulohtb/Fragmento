package com.pgalaxyp.fragmento.rpg.client.vfx;

import com.pgalaxyp.fragmento.rpg.content.entity.CutEffectEntity;
import com.pgalaxyp.fragmento.rpg.content.entity.CutOrientation;
import com.pgalaxyp.fragmento.rpg.content.entity.RpgEntityRegistry;
import com.pgalaxyp.fragmento.rpg.network.payload.s2c.CutFxPayload;
import net.minecraft.client.Minecraft;
import net.minecraft.world.phys.Vec3;

public final class CutFxSpawner {

    private static long lastPredictedClientTick = Long.MIN_VALUE;

    public static void predictFromAttackClick() {
        Minecraft mc = Minecraft.getInstance();
        if (mc == null || mc.player == null || mc.level == null) return;

        lastPredictedClientTick = mc.level.getGameTime();

        Vec3 eye = mc.player.getEyePosition();
        Vec3 look = mc.player.getLookAngle();
        Vec3 end = eye.add(look.scale(4.5));

        Vec3 spawn = mc.player.position().add(0.0, 1.0, 0.0);

        CutEffectEntity.spawnClient(
                mc.level,
                RpgEntityRegistry.CUT.get(),
                spawn,
                end,
                15,
                CutOrientation.HORIZONTAL
        );
    }

    public static void spawnFromServer(CutFxPayload p) {
        Minecraft mc = Minecraft.getInstance();
        if (mc == null || mc.level == null) return;

        long tick = mc.level.getGameTime();
        if (tick == lastPredictedClientTick) {
            return;
        }

        Vec3 spawn = new Vec3(p.spawnX(), p.spawnY(), p.spawnZ());
        Vec3 aim = new Vec3(p.aimX(), p.aimY(), p.aimZ());

        CutOrientation o = CutOrientation.HORIZONTAL;
        int ord = p.orientationOrdinal();
        if (ord >= 0 && ord < CutOrientation.values().length) {
            o = CutOrientation.values()[ord];
        }

        CutEffectEntity.spawnClient(
                mc.level,
                RpgEntityRegistry.CUT.get(),
                spawn,
                aim,
                p.lifeTicks(),
                o
        );
    }

    private CutFxSpawner() {}
}