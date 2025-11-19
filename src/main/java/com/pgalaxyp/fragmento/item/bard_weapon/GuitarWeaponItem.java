package com.pgalaxyp.fragmento.item.bard_weapon;

import com.pgalaxyp.fragmento.entity.bard.projectiles.guitar_projectile.GuitarProjectile;
import com.pgalaxyp.fragmento.entity.bard.angels.AbstractAngel;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.AABB;

public class GuitarWeaponItem extends AbstractBardWeapon {

    public GuitarWeaponItem(Properties properties) {
        super(properties);
    }

    @Override
    protected Projectile createProjectile(ServerLevel server, ServerPlayer player, ItemStack stack, boolean charged) {
        return new GuitarProjectile(server, player, charged);
    }

    @Override
    protected void specialAbility(ServerLevel server, ServerPlayer player, ItemStack stack) {
        AABB area = getSpecialRangeAABB(player);
        server.getEntitiesOfClass(Mob.class, area)
                .forEach(mobOnPlayerRange -> mobOnPlayerRange.addEffect(
                        new MobEffectInstance(MobEffects.DAMAGE_BOOST, 20, 1)));
    }

    @Override
    protected EntityType<? extends AbstractAngel> getAngelType() {
        return null;
    }
}