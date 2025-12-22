package com.pgalaxyp.fragmento.cosmetics.internal;

import com.mojang.logging.LogUtils;
import com.pgalaxyp.fragmento.cosmetics.CosmeticsKeys;
import com.pgalaxyp.fragmento.cosmetics.player.PlayerCosmeticsAttachment;
import com.pgalaxyp.fragmento.cosmetics.server.tier.PlayerTierAttachment;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.slf4j.Logger;

public final class CosmeticsRegistries {

    private static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister<AttachmentType<?>> ATTACHMENTS =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, CosmeticsKeys.MOD_ID);

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<PlayerCosmeticsAttachment>> PLAYER_COSMETICS =
            ATTACHMENTS.register(
                    CosmeticsKeys.ATT_PLAYER_COSMETICS.getPath(),
                    () -> AttachmentType.serializable(PlayerCosmeticsAttachment::new).build()
            );

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<PlayerTierAttachment>> PLAYER_TIER =
            ATTACHMENTS.register(
                    CosmeticsKeys.ATT_PLAYER_TIER.getPath(),
                    () -> AttachmentType.serializable(PlayerTierAttachment::new).build()
            );

    private CosmeticsRegistries() {
    }

    public static void register(IEventBus modBus) {
        ATTACHMENTS.register(modBus);
        LOGGER.info("CosmeticsRegistries registered");
    }
}