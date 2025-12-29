package com.pgalaxyp.fragmento.cosmetics.client.render.resources;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public final class CosmeticRenderResources {

    private static final ConcurrentHashMap<ResourceLocation, ResourceLocation> TEXTURES =
            new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<ResourceLocation, RenderType> CUTOUT_NO_CULL =
            new ConcurrentHashMap<>();

    private CosmeticRenderResources() {}

    public static ResourceLocation textureFor(ResourceLocation cosmeticId) {
        if (cosmeticId == null) {
            return null;
        }

        ResourceLocation existing = TEXTURES.get(cosmeticId);
        if (existing != null) {
            return existing;
        }

        ResourceLocation built = buildTexture(cosmeticId);
        ResourceLocation raced = TEXTURES.putIfAbsent(cosmeticId, built);
        return raced == null ? built : raced;
    }

    public static RenderType cutoutNoCull(ResourceLocation texture) {
        if (texture == null) {
            return null;
        }

        RenderType existing = CUTOUT_NO_CULL.get(texture);
        if (existing != null) {
            return existing;
        }

        RenderType built = RenderType.entityCutoutNoCull(texture);
        RenderType raced = CUTOUT_NO_CULL.putIfAbsent(texture, built);
        return raced == null ? built : raced;
    }

    private static ResourceLocation buildTexture(ResourceLocation cosmeticId) {
        Objects.requireNonNull(cosmeticId, "cosmeticId");

        String ns = cosmeticId.getNamespace();
        String p = cosmeticId.getPath();

        if ("fragmento".equals(ns) && "red_cube_head".equals(p)) {
            return ResourceLocation
                    .fromNamespaceAndPath("minecraft", "textures/misc/white.png");
        }

        String path = "textures/cosmetics/" + p + ".png";
        return ResourceLocation
                .fromNamespaceAndPath(ns, path);
    }
}