package com.pgalaxyp.fragmento.util;

import com.mojang.serialization.Codec;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.core.component.DataComponentType;
import com.pgalaxyp.fragmento.Fragmento;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class BardComponents {

    public static final DeferredRegister.DataComponents COMPONENTS =
            DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, Fragmento.MODID);

    public static final Supplier<DataComponentType<Integer>> CHARGED_ATTACKS_FIRED =
            COMPONENTS.registerComponentType("charged_attacks_fired",
                    builder -> builder.persistent(Codec.INT)
                            .networkSynchronized(ByteBufCodecs.VAR_INT));

    public static void bootstrap() {}
}