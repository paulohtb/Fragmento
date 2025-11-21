package com.pgalaxyp.fragmento.item.bard_weapon;

import com.pgalaxyp.fragmento.entity.bard.angels.AbstractAngel;
import com.pgalaxyp.fragmento.entity.bard.projectiles.flute_projectile.FluteProjectile;
import com.pgalaxyp.fragmento.registry.EntitiesRegistry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.List;

public class FluteWeaponItem extends AbstractBardWeapon {

    private static final int PUSH_COOLDOWN_TICKS = 10;
    private static final String PUSH_COOLDOWN_KEY = "FlutePushCooldown";
    private static final double PUSH_STRENGTH = 1.75D;
    private static final double PUSH_VERTICAL = 0.5D;
    private static final double MIN_HORIZONTAL_EPSILON = 1.0E-4D;

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
        applyPushWithCooldown(server, player);
    }

    @Override
    protected EntityType<? extends AbstractAngel> getAngelType() {
        return EntitiesRegistry.AEOLUS_ANGEL.get();
    }

    @Override
    protected Vector3f getSpecialColor() {
        return new Vector3f(1.0F, 1.0F, 1.0F);
    }

    private void applyBuff(ServerLevel server, Player player) {
        List<Player> players = getEntitiesInCircularRange(server, player, Player.class);
        for (Player p : players) {
            p.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 10, 0, false, false, false));
        }
    }

    private void applyPushWithCooldown(ServerLevel server, Player player) {
        var data = player.getPersistentData();
        int cooldown = data.getInt(PUSH_COOLDOWN_KEY);

        if (cooldown > 0) {
            data.putInt(PUSH_COOLDOWN_KEY, cooldown - 1);
            return;
        }

        pushNearbyMobs(server, player);
        data.putInt(PUSH_COOLDOWN_KEY, PUSH_COOLDOWN_TICKS);
    }

    private void pushNearbyMobs(ServerLevel server, Player player) {
        List<Mob> mobs = getEntitiesInCircularRange(server, player, Mob.class);
        Vec3 playerPos = player.position();

        for (Mob mob : mobs) {
            if (!mob.isAlive() || !mob.isPushable()) continue;

            Vec3 diff = mob.position().subtract(playerPos);
            Vec3 horizontal = new Vec3(diff.x, 0.0D, diff.z);

            if (horizontal.lengthSqr() < MIN_HORIZONTAL_EPSILON) {
                continue;
            }

            Vec3 push = horizontal.normalize().scale(PUSH_STRENGTH);
            Vec3 current = mob.getDeltaMovement();
            mob.setDeltaMovement(current.add(push.x, PUSH_VERTICAL, push.z));
            mob.hurtMarked = true;
        }
    }
}