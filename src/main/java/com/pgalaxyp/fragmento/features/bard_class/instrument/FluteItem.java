package com.pgalaxyp.fragmento.features.bard_class.instrument;

import com.pgalaxyp.fragmento.features.bard_class.ability.NormalAbility;
import com.pgalaxyp.fragmento.features.bard_class.registry.entity.FluteSpiritRegistry;
import com.pgalaxyp.fragmento.features.bard_class.spirit.type.FluteSpirit;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import java.util.List;

public class FluteItem extends InstrumentBase {

    public FluteItem(Properties props) {
        super(props, List.of(
                new NormalAbility<>(
                        level -> new FluteSpirit(FluteSpiritRegistry.FLUTE_SPIRIT.get(), level),
                        level -> new FluteSpirit(FluteSpiritRegistry.FLUTE_SPIRIT.get(), level),
                        InstrumentConstants.BASIC_RANGE,
                        InstrumentConstants.CHARGED_RANGE
                )
        ));
    }

    @Override
    protected int getNormalCooldown(ItemStack stack) {
        return InstrumentConstants.BASIC_COOLDOWN;
    }

    @Override
    protected int getChargedCooldown(ItemStack stack) {
        return InstrumentConstants.CHARGED_COOLDOWN;
    }

    @Override
    public void onAbilityExecuted(ServerLevel level, ServerPlayer player) {
        level.playSound(
                null,
                player.getX(),
                player.getY(),
                player.getZ(),
                SoundEvents.NOTE_BLOCK_FLUTE,
                SoundSource.PLAYERS,
                1.0F,
                1.0F
        );
    }
}
