//package com.pgalaxyp.fragmento.combat.old.content.bard.channel;
//
//import com.pgalaxyp.fragmento.combat.old.system.channel.ChannelAdapter;
//import com.pgalaxyp.fragmento.combat.old.system.channel.ChannelEntity;
//import com.pgalaxyp.fragmento.combat.old.system.channel.ChannelFingerprint;
//import com.pgalaxyp.fragmento.combat.old.system.entity.host.NewwSpiritEntityBase;
//import com.pgalaxyp.fragmento.combat.old.content.bard.gameplay.BardCatalystVisualCooldownService;
//import com.pgalaxyp.fragmento.combat.old.system.skill.SkillStateSnapshotDispatch;
//import net.minecraft.server.level.ServerLevel;
//import net.minecraft.server.level.ServerPlayer;
//import net.minecraft.world.InteractionHand;
//import net.minecraft.world.entity.Entity;
//import net.minecraft.world.item.ItemStack;
//
//public final class BardChannelAdapter implements ChannelAdapter {
//
//    @Override
//    public ChannelFingerprint createFingerprint(ItemStack stack) {
//        if (stack == null || stack.isEmpty()) {
//            return null;
//        }
//        return new BardInstrumentFingerprint(stack);
//    }
//
//    @Override
//    public ChannelEntity resolveEntity(ServerLevel level, int entityId) {
//        if (level == null || entityId <= 0) {
//            return null;
//        }
//
//        Entity e = level.getEntity(entityId);
//        if (e instanceof NewwSpiritEntityBase spirit) {
//            return new com.pgalaxyp.fragmento.combat.old.system.channel.BardChannelEntity(spirit);
//        }
//
//        return null;
//    }
//
//    @Override
//    public void applyVisualCooldown(ServerPlayer player, int ticks) {
//        BardCatalystVisualCooldownService.apply(player, ticks);
//        SkillStateSnapshotDispatch.sendForHeldInstrument(player);
//    }
//
//    @Override
//    public boolean isStillHolding(ServerPlayer player, InteractionHand hand, ChannelFingerprint fingerprint) {
//        if (fingerprint == null || player == null) {
//            return false;
//        }
//
//        ItemStack stack = hand == InteractionHand.OFF_HAND
//                ? player.getOffhandItem()
//                : player.getMainHandItem();
//
//        return fingerprint.matches(stack);
//    }
//}