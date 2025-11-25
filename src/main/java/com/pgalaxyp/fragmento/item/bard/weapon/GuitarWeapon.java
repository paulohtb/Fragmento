//package com.pgalaxyp.fragmento.item.bard.weapon;
//
//import com.pgalaxyp.fragmento.entity.bard.projectile.guitar_projectile.GuitarProjectile;
//import com.pgalaxyp.fragmento.entity.bard.angel.AbstractAngel;
//import net.minecraft.core.particles.ParticleTypes;
//import net.minecraft.server.level.ServerLevel;
//import net.minecraft.server.level.ServerPlayer;
//import net.minecraft.util.RandomSource;
//import net.minecraft.world.effect.MobEffectInstance;
//import net.minecraft.world.effect.MobEffects;
//import net.minecraft.world.entity.EntityType;
//import net.minecraft.world.entity.Mob;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.entity.projectile.Projectile;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.phys.AABB;
//import net.minecraft.world.phys.Vec3;
//import org.joml.Vector3f;
//
//import java.util.HashSet;
//import java.util.Set;
//
//public class GuitarWeapon extends AbstractWeapon {
//
//    private static final int BEAM_SEGMENTS = 8;
//    private static final double HITBOX_INFLATE = 0.3D;
//
//    public GuitarWeapon(Properties properties) {
//        super(properties);
//    }
//
//    @Override
//    protected Projectile createProjectile(ServerLevel server, ServerPlayer player, ItemStack stack, boolean charged) {
//        return new GuitarProjectile(server, player, charged);
//    }
//
//    @Override
//    protected void specialAbility(ServerLevel server, ServerPlayer player, ItemStack stack) {
//        applyBuff(server, player);
//        spawnRandomLightningBeam(server, player);
//    }
//
//    @Override
//    protected EntityType<? extends AbstractAngel> getAngelType() {
//        return null;
//    }
//
//    @Override
//    protected Vector3f getSpecialColor() {
//        return new Vector3f(1.0f, 1.0f, 1.0f);
//    }
//
//    private void applyBuff(ServerLevel server, Player player) {
//        AABB area = getSpecialRangeAABB(player);
//        server.getEntitiesOfClass(Player.class, area).forEach(
//                p -> p.addEffect(new MobEffectInstance(
//                        MobEffects.DAMAGE_BOOST,
//                        10,
//                        0,
//                        false,
//                        false,
//                        false
//                ))
//        );
//    }
//
//    private void spawnRandomLightningBeam(ServerLevel server, ServerPlayer player) {
//        RandomSource random = server.random;
//
//        Vec3 origin = player.getEyePosition();
//
//        double yaw = random.nextDouble() * Math.PI * 2.0D;
//        double pitch = (random.nextDouble() - 0.5D) * (Math.PI / 6.0D);
//
//        double dx = -Math.sin(yaw) * Math.cos(pitch);
//        double dy = -Math.sin(pitch);
//        double dz = Math.cos(yaw) * Math.cos(pitch);
//
//        Vec3 dir = new Vec3(dx, dy, dz).normalize();
//
//        Set<Mob> alreadyHit = new HashSet<>();
//
//        Vec3 previous = origin;
//
//        for (int i = 1; i <= BEAM_SEGMENTS; i++) {
//            double t = (SPECIAL_RANGE / BEAM_SEGMENTS) * i;
//
//            Vec3 base = origin.add(dir.scale(t));
//
//            double offsetX = (random.nextDouble() - 0.5D) * 0.6D;
//            double offsetY = (random.nextDouble() - 0.5D) * 0.6D;
//            double offsetZ = (random.nextDouble() - 0.5D) * 0.6D;
//
//            Vec3 point = base.add(offsetX, offsetY, offsetZ);
//
//            server.sendParticles(
//                    ParticleTypes.ELECTRIC_SPARK,
//                    point.x,
//                    point.y,
//                    point.z,
//                    5,
//                    0.0D,
//                    0.0D,
//                    0.0D,
//                    0.0D
//            );
//
//            AABB segmentBox = new AABB(previous, point).inflate(HITBOX_INFLATE);
//
//            server.getEntitiesOfClass(Mob.class, segmentBox, Mob::isAlive)
//                    .forEach(mob -> {
//                        if (alreadyHit.add(mob)) {
//                            applyBeamHitEffects(mob);
//                        }
//                    });
//
//            previous = point;
//        }
//    }
//
//    private void applyBeamHitEffects(Mob mob) {
//        mob.addEffect(new MobEffectInstance(
//                MobEffects.MOVEMENT_SLOWDOWN,
//                20,
//                0,
//                false,
//                false,
//                false
//        ));
//
//        mob.hurt(mob.damageSources().generic(), 1.0F);
//    }
//}