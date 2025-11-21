package com.pgalaxyp.fragmento.client;

import com.pgalaxyp.fragmento.Fragmento;
import com.pgalaxyp.fragmento.registry.EffectsRegistry;
import com.pgalaxyp.fragmento.util.TimerHandler;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

@EventBusSubscriber(modid = Fragmento.MODID)
public class CommonEvents {

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
        for (ServerLevel sLevel : event.getServer().getAllLevels()) {
            TimerHandler.tickAll(sLevel);
        }
    }

    @SubscribeEvent
    public static void onProjectileAttack(LivingIncomingDamageEvent event) {
        LivingEntity target = event.getEntity();
        if (!target.hasEffect(EffectsRegistry.PROJECTILE_REJECTION)) return;

        Entity attacker = event.getSource().getDirectEntity();
        if (!(attacker instanceof Projectile)) return;

        event.setCanceled(true);
        if (!target.level().isClientSide()) {
            attacker.setDeltaMovement(0, -0.4, 0);
        }
    }
}