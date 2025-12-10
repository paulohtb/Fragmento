package com.pgalaxyp.fragmento.features.bard_class.instrument;

import com.pgalaxyp.fragmento.features.bard_class.ability.AbilityBase;
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

    public void executeAbility(ServerLevel level,
                               ServerPlayer player,
                               ItemStack stack,
                               int index,
                               LivingEntity target) {

        if (player.getCooldowns().isOnCooldown(this)) {
            return;
        }

        AbilityBase ability = getAbility(index);
        if (ability == null) return;

        int cooldown = ability.execute(level, player, stack, target);
        if (cooldown <= 0) return;

        player.getCooldowns().addCooldown(this, cooldown);
    }
}
