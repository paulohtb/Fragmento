//package com.pgalaxyp.fragmento.item.bard.weapon;
//
//import com.pgalaxyp.fragmento.entity.bard.angel.AbstractAngel;
//import com.pgalaxyp.fragmento.entity.bard.projectile.drum_projectile.DrumProjectile;
//import net.minecraft.world.entity.projectile.Projectile;
//import net.minecraft.world.effect.MobEffectInstance;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.server.level.ServerPlayer;
//import net.minecraft.server.level.ServerLevel;
//import net.minecraft.world.entity.EntityType;
//import net.minecraft.world.effect.MobEffects;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.entity.Mob;
//import net.minecraft.world.phys.AABB;
//import org.joml.Vector3f;
//
//public class DrumWeapon extends AbstractWeapon {
//
//    public DrumWeapon(Properties properties) {
//        super(properties);
//    }
//
//    @Override
//    protected Projectile createProjectile(ServerLevel server, ServerPlayer player, ItemStack stack, boolean charged) {
//        return new DrumProjectile(server, player, charged);
//    }
//
//    @Override
//    protected void specialAbility(ServerLevel server, ServerPlayer player, ItemStack stack) {
//        applyBuff(server, player);
//        applyDebuff(server, player);
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
//                        MobEffects.DAMAGE_RESISTANCE, 10, 0,
//                        false, false, false)));
//    }
//
//    private void applyDebuff(ServerLevel server, Player player) {
//        AABB area = getSpecialRangeAABB(player);
//        server.getEntitiesOfClass(Mob.class, area).forEach(
//                m -> m.addEffect(new MobEffectInstance(
//                        MobEffects.MOVEMENT_SLOWDOWN, 10, 1,
//                        false, false, false)));
//    }
//}