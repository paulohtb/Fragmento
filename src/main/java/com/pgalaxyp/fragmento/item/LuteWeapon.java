package com.pgalaxyp.fragmento.item;

import com.pgalaxyp.fragmento.entity.lira_projectile.LiraProjectile;
import com.pgalaxyp.fragmento.entity.luteProjectile.LuteProjectile;
import com.pgalaxyp.fragmento.registry.EffectsRegistry;
import com.pgalaxyp.fragmento.util.TimerHandler;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

public class LuteWeapon extends AbstractBardWeapon {

    private static final ResourceLocation PROJECTILE_TYPE =
            ResourceLocation.fromNamespaceAndPath("fragmento", "lute_projectile");

    public LuteWeapon(Properties properties) {
        super(properties);
    }

    protected void onSpecialAbility(Level level, Player player) {
        if (!(level instanceof ServerLevel server)) return;

        AABB range = player.getBoundingBox().inflate(10);
        level.getEntitiesOfClass(Mob.class, range)
                .forEach(mob -> {
                    if (mob.isDeadOrDying()) return;
                    TimerHandler.incrementTimer(mob);
                    if (TimerHandler.isReadyToTrigger(mob)) {
                        mob.hurt(mob.damageSources().generic(), 2.0f);
                        mob.addEffect(new MobEffectInstance(EffectsRegistry.EXPOSURE, 500, 0));
                        TimerHandler.resetTimer(mob);
                    }
                });
    }

    protected void onUltimateAbility() {}

    protected Projectile getProjectileInstance(ServerLevel server, ServerPlayer serverPlayer, boolean charged) {
        if(charged) {
            return new LiraProjectile(server, serverPlayer, charged);
        } else {
            return new LuteProjectile(server, serverPlayer, charged);
        }
    }

    public ResourceLocation getProjectileType() {
        return PROJECTILE_TYPE;
    }
}