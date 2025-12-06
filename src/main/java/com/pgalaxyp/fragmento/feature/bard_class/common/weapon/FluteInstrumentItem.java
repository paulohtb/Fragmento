package com.pgalaxyp.fragmento.feature.bard_class.common.weapon;

import com.pgalaxyp.fragmento.core.engine.AbilityBase;
import com.pgalaxyp.fragmento.feature.bard_class.common.ability.SpiritAbility;
import com.pgalaxyp.fragmento.feature.bard_class.common.config.FluteConstants;
import com.pgalaxyp.fragmento.feature.bard_class.common.init.FluteSpiritRegistry;
import com.pgalaxyp.fragmento.feature.bard_class.common.spirit.impl.FluteSpirit;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;

public class FluteInstrumentItem extends InstrumentBase {

    private final AbilityBase basicAbility;
    private final AbilityBase chargedAbility;

    public FluteInstrumentItem(Properties props) {
        super(props);

        this.basicAbility = new SpiritAbility<>(
                false,
                FluteConstants.BASIC_RANGE,
                FluteConstants.FLUTE_SPAWN_MIN_FORWARD,
                FluteConstants.FLUTE_SPAWN_MAX_FORWARD,
                FluteConstants.FLUTE_SPAWN_MAX_SIDE_OFFSET,
                FluteConstants.BASIC_IDLE_TICKS,
                FluteConstants.BASIC_TRAVEL_TICKS,
                FluteConstants.FLUTE_COLLISION_RADIUS,
                FluteConstants.FLUTE_EXTRA_LIFETIME_TICKS,
                level -> {
                    FluteSpirit spirit = new FluteSpirit(FluteSpiritRegistry.FLUTE_SPIRIT.get(), level);
                    spirit.setCharged(false);
                    return spirit;
                }
        );

        this.chargedAbility = new SpiritAbility<>(
                true,
                FluteConstants.CHARGED_RANGE,
                FluteConstants.FLUTE_SPAWN_MIN_FORWARD,
                FluteConstants.FLUTE_SPAWN_MAX_FORWARD,
                FluteConstants.FLUTE_SPAWN_MAX_SIDE_OFFSET,
                FluteConstants.CHARGED_IDLE_TICKS,
                FluteConstants.CHARGED_TRAVEL_TICKS,
                FluteConstants.FLUTE_COLLISION_RADIUS,
                FluteConstants.FLUTE_EXTRA_LIFETIME_TICKS,
                level -> {
                    FluteSpirit spirit = new FluteSpirit(FluteSpiritRegistry.FLUTE_SPIRIT.get(), level);
                    spirit.setCharged(true);
                    return spirit;
                }
        );
    }

    @Override
    protected AbilityBase getNormalBasicAbility(ItemStack stack) {
        return this.basicAbility;
    }

    @Override
    protected AbilityBase getNormalChargedAbility(ItemStack stack) {
        return this.chargedAbility;
    }

    @Override
    protected int getNormalBasicCooldownTicks(ItemStack stack) {
        return FluteConstants.BASIC_COOLDOWN;
    }

    @Override
    protected int getNormalChargedCooldownTicks(ItemStack stack) {
        return FluteConstants.CHARGED_COOLDOWN;
    }

    @Override
    protected void onNormalBasicUsed(ServerPlayer player, ItemStack stack) {
        player.level().playSound(
                null,
                player.getX(),
                player.getY(),
                player.getZ(),
                SoundEvents.NOTE_BLOCK_FLUTE,
                SoundSource.PLAYERS,
                1.0F,
                1.0F
        );
        this.triggerNormalBasicAnimation(player, stack);
    }

    @Override
    protected void onNormalChargedUsed(ServerPlayer player, ItemStack stack) {
        player.level().playSound(
                null,
                player.getX(),
                player.getY(),
                player.getZ(),
                SoundEvents.NOTE_BLOCK_FLUTE,
                SoundSource.PLAYERS,
                1.0F,
                0.8F
        );
        this.triggerNormalChargedAnimation(player, stack);
    }
}
