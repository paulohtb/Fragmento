//package com.pgalaxyp.fragmento.item.bard.weapon;
//
//import com.pgalaxyp.fragmento.entity.bard.projectile.lute_projectile.LuteProjectile;
//import com.pgalaxyp.fragmento.entity.bard.angel.AbstractAngel;
//import com.pgalaxyp.fragmento.registry.EffectsRegistry;
//import net.minecraft.world.effect.MobEffectInstance;
//import net.minecraft.world.entity.projectile.Projectile;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.server.level.ServerPlayer;
//import net.minecraft.server.level.ServerLevel;
//import net.minecraft.world.entity.EntityType;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.entity.Mob;
//import net.minecraft.world.phys.AABB;
//import org.joml.Vector3f;
//
//public class LuteWeapon extends AbstractWeapon {
//
//    public LuteWeapon(Properties properties) {
//        super(properties);
//    }
//
//    @Override
//    protected Projectile createProjectile(ServerLevel server, ServerPlayer player, ItemStack stack, boolean charged) {
//        return new LuteProjectile(server, player, charged);
//    }
//
//    @Override
//    protected void specialAbility(ServerLevel server, ServerPlayer player, ItemStack stack) {
//        applyBuff(server, player);
//        applyDebuff(server, player);
////        AABB area = getSpecialRangeAABB(player);
////        server.getEntitiesOfClass(Mob.class, area)
////                .forEach(mob -> {
////                    if (mob.isDeadOrDying()) return;
////                    TimerHandler.incrementTimer(mob);
////                    if (TimerHandler.isReadyToTrigger(mob)) {
////                        mob.hurt(mob.damageSources().generic(), 2.0f);
////                        TimerHandler.resetTimer(mob);
////                    }
////                });
//    }
//
//    @Override
//    protected Vector3f getSpecialColor() {
//        return new Vector3f(1.0f, 1.0f, 1.0f);
//    }
//
//    @Override
//    protected EntityType<? extends AbstractAngel> getAngelType() {
//        return null;
//    }
//
//    private void applyBuff(ServerLevel server, Player player) {
//        AABB area = getSpecialRangeAABB(player);
//        server.getEntitiesOfClass(Player.class, area).forEach(
//                p -> p.addEffect(new MobEffectInstance(
//                        EffectsRegistry.INSOMNIA, 10, 0,
//                        false, false, false)));
//    }
//
//    private void applyDebuff(ServerLevel server, Player player) {
//        AABB area = getSpecialRangeAABB(player);
//        server.getEntitiesOfClass(Mob.class, area).forEach(
//                m -> m.addEffect(new MobEffectInstance(
//                        EffectsRegistry.SLEEP, 10, 0,
//                        false, false, false)));
//    }
//}