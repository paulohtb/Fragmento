package com.pgalaxyp.fragmento.feature.bard.common.weapon;

import com.pgalaxyp.fragmento.core.engine.RaycastBase;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public abstract class InstrumentBase extends Item {

    protected InstrumentBase(Properties props) {
        super(props.stacksTo(1));
    }

    public final boolean useNormalBasic(ServerPlayer player, ItemStack stack) {
        if (player.getCooldowns().isOnCooldown(this)) {
            return false;
        }
        if (!this.canUseNormalBasic(player, stack)) {
            return false;
        }
        RaycastBase ability = getNormalBasic();
        if (ability != null) {
            boolean hit = ability.execute(player);
            if (!hit) {
                return false;
            }
            this.onNormalBasicUsed(player, stack);
            int cooldown = this.getNormalBasicCooldownTicks(stack);
            if (cooldown > 0) {
                player.getCooldowns().addCooldown(this, cooldown);
            }
            return true;
        }
        return false;
    }

    public final boolean useNormalCharged(ServerPlayer player, ItemStack stack) {
        if (player.getCooldowns().isOnCooldown(this)) {
            return false;
        }
        if (!this.canUseNormalCharged(player, stack)) {
            return false;
        }
        RaycastBase ability = getNormalCharged();
        if (ability != null) {
            boolean hit = ability.execute(player);
            if (!hit) {
                return false;
            }
            this.onNormalChargedUsed(player, stack);
            int cooldown = this.getNormalChargedCooldownTicks(stack);
            if (cooldown > 0) {
                player.getCooldowns().addCooldown(this, cooldown);
            }
            return true;
        }
        return false;
    }

    protected boolean canUseNormalBasic(ServerPlayer player, ItemStack stack) {
        return true;
    }

    protected boolean canUseNormalCharged(ServerPlayer player, ItemStack stack) {
        return true;
    }

    protected void onNormalBasicUsed(ServerPlayer player, ItemStack stack) {
        this.triggerNormalBasicAnimation(player, stack);
    }

    protected void onNormalChargedUsed(ServerPlayer player, ItemStack stack) {
        this.triggerNormalChargedAnimation(player, stack);
    }

    protected void triggerNormalBasicAnimation(ServerPlayer player, ItemStack stack) {
    }

    protected void triggerNormalChargedAnimation(ServerPlayer player, ItemStack stack) {
    }

    protected abstract RaycastBase getNormalBasic();

    protected abstract RaycastBase getNormalCharged();

    protected int getNormalBasicCooldownTicks(ItemStack stack) {
        return 0;
    }

    protected int getNormalChargedCooldownTicks(ItemStack stack) {
        return 0;
    }
}
