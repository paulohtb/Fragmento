package com.pgalaxyp.fragmento.item;

import com.pgalaxyp.fragmento.entity.violinProjectile.ViolinProjectile;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

public class ViolinWeapon extends AbstractBardWeapon {

    private static final ResourceLocation PROJECTILE_TYPE =
            ResourceLocation.fromNamespaceAndPath("fragmento", "violin_projectile");

    public ViolinWeapon(Properties properties) {
        super(properties);
    }

    protected void onSpecialAbility(Level level, Player player) {
        if (!(level instanceof ServerLevel server)) return;

        AABB range = player.getBoundingBox().inflate(10);
        level.getEntitiesOfClass(Mob.class, range)
                .forEach(mob ->
                        mob.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 20, 1)));
    }

    protected void onUltimateAbility() {}

    protected Projectile getProjectileInstance(ServerLevel server, ServerPlayer serverPlayer, boolean charged) {
        return new ViolinProjectile(server, serverPlayer, charged);
    }

    public ResourceLocation getProjectileType() {
        return PROJECTILE_TYPE;
    }
}