package com.pgalaxyp.fragmento.combat.content.minecraft;

import com.pgalaxyp.fragmento.combat.content.bard.BardMinecraftPack;
import java.util.List;

public final class FragmentoMinecraftContent {
    public static final MinecraftContentRegistry REGISTRY = new MinecraftContentRegistry(List.of(BardMinecraftPack.INSTANCE));
    private FragmentoMinecraftContent() {}
}