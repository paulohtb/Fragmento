package com.pgalaxyp.fragmento.item;

import com.pgalaxyp.fragmento.util.FinalComboAware;
import com.pgalaxyp.fragmento.util.NormalAbilityComboHandler;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public abstract class AbstractBardWeapon extends Item {

    public AbstractBardWeapon(Properties properties) {
        super(properties);
    }

    protected abstract void onSpecialAbility(Level level, Player player);

    protected abstract void onUltimateAbility();

    public static void onAbstractNormalAbility(ServerPlayer player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!(stack.getItem() instanceof AbstractBardWeapon weapon)) return;

        if (player.getCooldowns().isOnCooldown(weapon)) return;
        player.getCooldowns().addCooldown(weapon, 15);

        var combo = NormalAbilityComboHandler.fromItemStack(stack);
        combo.advanceCombo();
        combo.writeToItemStack(stack);

        Level level = player.level();
        if (!(level instanceof ServerLevel server)) return;

        if (combo.isFinalComboHit()) {
            server.addFreshEntity(weapon.buildProjectile(server, player, true));
        } else {
            server.addFreshEntity(weapon.buildProjectile(server, player, false));
        }
    }

    //onAbstractSpecialAbility
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide()) {
            onSpecialAbility(level, player);
        }
        return InteractionResultHolder.pass(player.getItemInHand(hand));
    }

    public static void onAbstractUltimateAbility() {}

    protected abstract Projectile getProjectileInstance(ServerLevel server, ServerPlayer player, boolean charged);

    protected Projectile buildProjectile(ServerLevel server, ServerPlayer player, boolean charged) {
        Projectile projectile = getProjectileInstance(server, player, charged);
        if (projectile instanceof FinalComboAware fc) fc.setFinalCombo(charged);
        projectile.setPos(player.getEyePosition());
        return projectile;
    }
}