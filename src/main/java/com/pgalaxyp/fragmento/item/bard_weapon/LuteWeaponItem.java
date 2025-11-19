package com.pgalaxyp.fragmento.item.bard_weapon;

import com.pgalaxyp.fragmento.entity.bard.projectiles.lute_projectile.LuteProjectile;
import com.pgalaxyp.fragmento.entity.bard.angels.AbstractAngel;
import net.minecraft.world.entity.projectile.Projectile;
import com.pgalaxyp.fragmento.util.TimerHandler;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.AABB;

public class LuteWeaponItem extends AbstractBardWeapon {

    public LuteWeaponItem(Properties properties) {
        super(properties);
    }

    @Override
    protected Projectile createProjectile(ServerLevel server, ServerPlayer player, ItemStack stack, boolean charged) {
        return new LuteProjectile(server, player, charged);
    }

    @Override
    protected void specialAbility(ServerLevel server, ServerPlayer player, ItemStack stack) {
        AABB area = getSpecialRangeAABB(player);
        server.getEntitiesOfClass(Mob.class, area)
                .forEach(mob -> {
                    if (mob.isDeadOrDying()) return;
                    TimerHandler.incrementTimer(mob);
                    if (TimerHandler.isReadyToTrigger(mob)) {
                        mob.hurt(mob.damageSources().generic(), 2.0f);
                        TimerHandler.resetTimer(mob);
                    }
                });
    }

    @Override
    protected EntityType<? extends AbstractAngel> getAngelType() {
        return null;
    }
}