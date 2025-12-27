package com.pgalaxyp.fragmento.cosmetics.network;

import net.minecraft.resources.ResourceLocation;

public final class CosmeticNetworkIds {

    public static final ResourceLocation SYNC =
            ResourceLocation.fromNamespaceAndPath("fragmento", "cosmetic_sync");

    public static final ResourceLocation SYNC_REQUEST =
            ResourceLocation.fromNamespaceAndPath("fragmento", "cosmetic_sync_request");

    private CosmeticNetworkIds() {
    }
}