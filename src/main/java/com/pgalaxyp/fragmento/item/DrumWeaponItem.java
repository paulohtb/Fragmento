package com.pgalaxyp.fragmento.item;

import com.pgalaxyp.fragmento.entity.drum_projectile.DrumProjectile;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

public class DrumWeaponItem extends AbstractBardWeapon {

    public DrumWeaponItem(Properties properties) {
        super(properties);
    }

    @Override
    protected void onSpecialAbility(Level level, Player player) {

    }

    @Override
    protected void onUltimateAbility() {

    }

    private static final ResourceLocation PROJECTILE_TYPE = ResourceLocation
            .fromNamespaceAndPath("fragmento", "drum_projectile");
    public ResourceLocation getProjectileType() { return PROJECTILE_TYPE; }

    protected void bardWeaponHoldAbility(Level level, Player player) {
        if (!level.isClientSide()) {
            AABB playerRangeArea = player.getBoundingBox().inflate(10);

            level.getEntitiesOfClass(Mob.class, playerRangeArea)
                    .forEach(mobOnPlayerRange -> mobOnPlayerRange.addEffect(
                            new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 20, 1)));
        }
    }

    protected Projectile getProjectileInstance(ServerLevel server, ServerPlayer serverPlayer, boolean charged) {
        return new DrumProjectile(server, serverPlayer, charged);
    }
}