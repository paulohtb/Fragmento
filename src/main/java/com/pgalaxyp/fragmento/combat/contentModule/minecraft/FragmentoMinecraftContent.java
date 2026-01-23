package com.pgalaxyp.fragmento.combat.contentModule.minecraft;

import com.pgalaxyp.fragmento.combat.contentModule.bard.BardMinecraftPack;
import java.util.List;

public final class FragmentoMinecraftContent {
    public static final MinecraftContentRegistry REGISTRY = new MinecraftContentRegistry(List.of(BardMinecraftPack.INSTANCE));
    private FragmentoMinecraftContent() {}
}