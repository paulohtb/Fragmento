package com.pgalaxyp.fragmento.features.bard_class.instrument;

import com.pgalaxyp.fragmento.features.bard_class.ability.NormalAbility;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import java.util.List;

public abstract class InstrumentBase extends Item {

    private final List<NormalAbility<?>> abilities;

    protected InstrumentBase(Properties props, List<NormalAbility<?>> abilities) {
        super(props.stacksTo(1));
        this.abilities = abilities;
    }

    public NormalAbility<?> getAbility(int index) {
        if (index < 0 || index >= abilities.size()) return null;
        return abilities.get(index);
    }

    public void executeAbility(ServerLevel level, ServerPlayer player, ItemStack stack, int index, LivingEntity target) {
        if (player.getCooldowns().isOnCooldown(this)) return;

        NormalAbility<?> ability = getAbility(index);
        if (ability == null) return;

        boolean charged = InstrumentChargeData.isCharged(stack);

        if (!ability.execute(level, player, stack, target, charged)) return;

        onAbilityExecuted(level, player);

        int cd = charged ? getChargedCooldown(stack) : getNormalCooldown(stack);
        if (cd > 0) player.getCooldowns().addCooldown(this, cd);

        if (charged) InstrumentChargeData.reset(stack);
    }

    protected int getNormalCooldown(ItemStack stack) {
        return 0;
    }

    protected int getChargedCooldown(ItemStack stack) {
        return 0;
    }

    public void onAbilityExecuted(ServerLevel level, ServerPlayer player) {
    }
}
