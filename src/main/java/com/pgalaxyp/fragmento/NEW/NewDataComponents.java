package com.pgalaxyp.fragmento.NEW;

import com.mojang.serialization.Codec;
import com.pgalaxyp.fragmento.Fragmento;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class NewDataComponents {

    public static final DeferredRegister.DataComponents COMPONENTS =
            DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, Fragmento.MODID);

    public static final Supplier<DataComponentType<Integer>> NORMAL_ABILITY_HIT_COUNT =
            COMPONENTS.registerComponentType(
                    "ability_weapon_normal_ability_hit_count",
                    builder -> builder.persistent(Codec.INT)
                            .networkSynchronized(ByteBufCodecs.VAR_INT)
            );
}
