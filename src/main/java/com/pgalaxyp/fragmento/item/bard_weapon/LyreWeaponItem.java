package com.pgalaxyp.fragmento.item.bard_weapon;

import com.pgalaxyp.fragmento.entity.bard.projectiles.lyre_projectile.LyreProjectile;
import com.pgalaxyp.fragmento.entity.bard.angels.AbstractAngel;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.AABB;

public class LyreWeaponItem extends AbstractBardWeapon {

    public LyreWeaponItem(Properties properties) {
        super(properties);
    }

    @Override
    protected Projectile createProjectile(ServerLevel server, ServerPlayer player, ItemStack stack, boolean charged) {
        return new LyreProjectile(server, player, charged);
    }

    @Override
    protected void specialAbility(ServerLevel server, ServerPlayer player, ItemStack stack) {
        AABB area = getSpecialRangeAABB(player);
        server.getEntitiesOfClass(Mob.class, area)
                .forEach(mob ->
                        mob.heal(0.5f));
    }

    @Override
    protected EntityType<? extends AbstractAngel> getAngelType() {
        return null;
    }
}