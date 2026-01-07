package com.pgalaxyp.fragmento.rpg.host.minecraft.items;

import com.pgalaxyp.fragmento.bootstrap.FragmentoMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public final class RpgItemTags {

    public static final TagKey<Item> RPG_WEAPON = TagKey.create(
            Registries.ITEM,
            ResourceLocation.fromNamespaceAndPath(FragmentoMod.MODID, "rpg_weapon")
    );

    private RpgItemTags() {}
}