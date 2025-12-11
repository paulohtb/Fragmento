package com.pgalaxyp.fragmento.features.bard_class.instrument;

import com.pgalaxyp.fragmento.features.bard_class.ability.AbilityBase;
import com.pgalaxyp.fragmento.features.bard_class.ability.AbilitySlot;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public abstract class InstrumentBase extends Item {

    protected final List<AbilityBase> abilities;

    public InstrumentBase(Properties props, List<AbilityBase> abilities) {
        super(props);
        this.abilities = abilities;
    }

    public AbilityBase getAbility(int index) {
        return index >= 0 && index < abilities.size() ? abilities.get(index) : null;
    }

    public AbilityBase getAbility(AbilitySlot slot) {
        return getAbility(slot.id());
    }

    public void executeAbility(ServerLevel level,
                               ServerPlayer player,
                               ItemStack stack,
                               int index,
                               LivingEntity target) {

        AbilitySlot slot = AbilitySlot.fromId(index);
        if (slot == null) {
            return;
        }

        InstrumentAbilityService.executeInstrumentAbility(player, stack, slot, target);
    }
}
