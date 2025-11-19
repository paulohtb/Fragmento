package com.pgalaxyp.fragmento.item.bard_weapon;

import com.pgalaxyp.fragmento.entity.bard.angels.*;
import com.pgalaxyp.fragmento.entity.bard.projectiles.flute_projectile.*;
import com.pgalaxyp.fragmento.registry.*;
import net.minecraft.server.level.*;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.entity.projectile.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.phys.*;

import java.util.*;

public class FluteWeaponItem extends AbstractBardWeapon {

    private static final int PUSH_COOLDOWN_TICKS = 10;

    public FluteWeaponItem(Properties properties) {
        super(properties);
    }

    @Override
    protected Projectile createProjectile(ServerLevel server, ServerPlayer player, ItemStack stack, boolean charged) {
        return new FluteProjectile(server, player, charged);
    }

    @Override
    protected void specialAbility(ServerLevel server, ServerPlayer player, ItemStack stack) {
        applyBuff(server, player);
        applyDebuff(server, player);
    }

    @Override
    protected EntityType<? extends AbstractAngel> getAngelType() {
        return EntitiesRegistry.AEOLUS_ANGEL.get();
    }

    private void applyBuff(ServerLevel server, Player player) {
        AABB area = getSpecialRangeAABB(player);
        server.getEntitiesOfClass(Player.class, area).forEach(
                p -> p.addEffect(new MobEffectInstance(
                        MobEffects.MOVEMENT_SPEED, 10, 0,
                        false, false, false)));
    }

    private void applyDebuff(ServerLevel server, Player player) {
        var data = player.getPersistentData();
        int cooldown = data.getInt("FlutePushCooldown");

        if (cooldown > 0) {
            data.putInt("FlutePushCooldown", cooldown - 1);
            return;
        }

        pushNearbyMobs(server, player);
        data.putInt("FlutePushCooldown", PUSH_COOLDOWN_TICKS);
    }

    private void pushNearbyMobs(ServerLevel server, Player player) {
        AABB area = getSpecialRangeAABB(player);
        List<Mob> mobs = server.getEntitiesOfClass(Mob.class, area, mob -> mob.getId() != player.getId());
        for (Mob mob : mobs) {
            Vec3 diff = mob.position().subtract(player.position());
            Vec3 horizontal = new Vec3(diff.x, 0.0D, diff.z);

            if (horizontal.lengthSqr() < 1.0E-4) continue;

            Vec3 push = horizontal.normalize().scale(1.75D);

            Vec3 current = mob.getDeltaMovement();
            mob.setDeltaMovement(current.add(push.x, 0.5D, push.z));
            mob.hurtMarked = true;
        }
    }
}