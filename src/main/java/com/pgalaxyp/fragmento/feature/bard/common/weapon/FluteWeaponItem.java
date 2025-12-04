package com.pgalaxyp.fragmento.feature.bard.common.weapon;

import com.pgalaxyp.fragmento.core.engine.ability.AbilityRaycastBase;
import com.pgalaxyp.fragmento.feature.bard.common.ability.BardTargetedSpiritAbility;
import com.pgalaxyp.fragmento.feature.bard.common.combat.FluteSpiritCombatProfile;
import com.pgalaxyp.fragmento.feature.bard.common.config.FluteConstants;
import com.pgalaxyp.fragmento.feature.bard.common.init.FluteSpiritTypeRegistry;
import com.pgalaxyp.fragmento.feature.bard.common.spirit.impl.FluteSpirit;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;

public class FluteWeaponItem extends WeaponBase {

    private final AbilityRaycastBase basicAbility;
    private final AbilityRaycastBase chargedAbility;

    public FluteWeaponItem(Properties props) {
        super(props);
        this.basicAbility = new BardTargetedSpiritAbility<>(
                FluteSpiritCombatProfile.INSTANCE,
                false,
                FluteConstants.FLUTE_SPAWN_MIN_FORWARD,
                FluteConstants.FLUTE_SPAWN_MAX_FORWARD,
                FluteConstants.FLUTE_SPAWN_MAX_SIDE_OFFSET,
                FluteConstants.FLUTE_EXTRA_LIFETIME_TICKS,
                level -> {
                    FluteSpirit spirit = new FluteSpirit(FluteSpiritTypeRegistry.FLUTE_SPIRIT.get(), level);
                    spirit.setCharged(false);
                    return spirit;
                }
        );
        this.chargedAbility = new BardTargetedSpiritAbility<>(
                FluteSpiritCombatProfile.INSTANCE,
                true,
                FluteConstants.FLUTE_SPAWN_MIN_FORWARD,
                FluteConstants.FLUTE_SPAWN_MAX_FORWARD,
                FluteConstants.FLUTE_SPAWN_MAX_SIDE_OFFSET,
                FluteConstants.FLUTE_EXTRA_LIFETIME_TICKS,
                level -> {
                    FluteSpirit spirit = new FluteSpirit(FluteSpiritTypeRegistry.FLUTE_SPIRIT.get(), level);
                    spirit.setCharged(true);
                    return spirit;
                }
        );
    }

    @Override
    protected AbilityRaycastBase getNormalBasic() {
        return this.basicAbility;
    }

    @Override
    protected AbilityRaycastBase getNormalCharged() {
        return this.chargedAbility;
    }

    @Override
    protected int getNormalBasicCooldownTicks(ItemStack stack) {
        return FluteSpiritCombatProfile.INSTANCE.getCooldown(false);
    }

    @Override
    protected int getNormalChargedCooldownTicks(ItemStack stack) {
        return FluteSpiritCombatProfile.INSTANCE.getCooldown(true);
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
