package com.pgalaxyp.fragmento.item.bard.weapon;

import com.pgalaxyp.fragmento.entity.bard.angel.AbstractAngel;
import com.pgalaxyp.fragmento.entity.bard.projectile.flute_projectile.FluteProjectile;
import com.pgalaxyp.fragmento.NEW.EntitiesRegistry;
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

public class FluteWeapon extends AbstractWeapon {

    private static final double PUSH_STRENGTH = 1.75D;
    private static final double PUSH_VERTICAL = 0.5D;
    private static final double MIN_HORIZONTAL_EPSILON = 1.0E-4D;

    public FluteWeapon(Properties properties) {
        super(properties);
    }

    @Override
    protected Projectile createProjectile(ServerLevel server, ServerPlayer player, ItemStack stack, boolean charged) {
        return new FluteProjectile(server, player, charged);
    }

    @Override
    protected void applyBuffs(ServerLevel server, Player player) {
        List<Player> players = getEntitiesInCircularRange(server, player, Player.class);
        for (Player p : players) {
            p.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 10, 0, false, false, false));
        }
    }

    @Override
    protected void applyDebuffs(ServerLevel server, Player player) {
        List<Mob> mobs = getEntitiesInCircularRange(server, player, Mob.class);
        Vec3 playerPos = player.position();
        for (Mob mob : mobs) {
            if (!mob.isAlive() || !mob.isPushable()) continue;
            Vec3 diff = mob.position().subtract(playerPos);
            Vec3 horizontal = new Vec3(diff.x, 0.0D, diff.z);
            if (horizontal.lengthSqr() < MIN_HORIZONTAL_EPSILON) continue;
            Vec3 dir = horizontal.normalize();
            Vec3 push = dir.scale(PUSH_STRENGTH);
            Vec3 current = mob.getDeltaMovement();
            mob.setDeltaMovement(current.add(push.x, PUSH_VERTICAL, push.z));
            mob.hurtMarked = true;
        }
    }

    @Override
    protected EntityType<? extends AbstractAngel> getAngelType() {
        return EntitiesRegistry.AEOLUS_ANGEL.get();
    }

    @Override
    protected Vector3f getSpecialColor() {
        return new Vector3f(1.0F, 1.0F, 1.0F);
    }
}