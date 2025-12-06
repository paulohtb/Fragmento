package com.pgalaxyp.fragmento.feature.bard_class.common.weapon;

import com.pgalaxyp.fragmento.core.engine.AbilityBase;
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

        AbilityBase ability = this.getNormalBasicAbility(stack);
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

        AbilityBase ability = this.getNormalChargedAbility(stack);
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

    protected abstract AbilityBase getNormalBasicAbility(ItemStack stack);

    protected abstract AbilityBase getNormalChargedAbility(ItemStack stack);

    protected int getNormalBasicCooldownTicks(ItemStack stack) {
        return 0;
    }

    protected int getNormalChargedCooldownTicks(ItemStack stack) {
        return 0;
    }

    public double getBasicAbilityRange(ItemStack stack) {
        AbilityBase ability = this.getNormalBasicAbility(stack);
        return ability != null ? ability.getRange() : 0.0D;
    }

    public double getChargedAbilityRange(ItemStack stack) {
        AbilityBase ability = this.getNormalChargedAbility(stack);
        return ability != null ? ability.getRange() : 0.0D;
    }
}
