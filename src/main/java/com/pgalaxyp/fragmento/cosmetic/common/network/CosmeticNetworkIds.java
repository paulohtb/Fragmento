package com.pgalaxyp.fragmento.cosmetic.common.network;

import net.minecraft.resources.ResourceLocation;

public final class CosmeticNetworkIds {

    public static final ResourceLocation SYNC_LIST =
            ResourceLocation.fromNamespaceAndPath("fragmento", "cosmetic_list");

    public static final ResourceLocation SYNC_DELTA =
            ResourceLocation.fromNamespaceAndPath("fragmento", "cosmetic_delta");

    public static final ResourceLocation EQUIP =
            ResourceLocation.fromNamespaceAndPath("fragmento", "cosmetic_equip");

    public static final ResourceLocation UNEQUIP =
            ResourceLocation.fromNamespaceAndPath("fragmento", "cosmetic_unequip");

    private CosmeticNetworkIds() {}
}