package com.pgalaxyp.fragmento.cosmetics.client;

import com.pgalaxyp.fragmento.cosmetics.network.CosmeticsNetwork;
import com.pgalaxyp.fragmento.cosmetics.network.s2c.S2COpenCosmeticsScreenPayload;
import com.pgalaxyp.fragmento.cosmetics.network.s2c.S2CSetBaseCosmeticResultPayload;
import com.pgalaxyp.fragmento.cosmetics.network.s2c.S2CSyncCatalogPayload;
import com.pgalaxyp.fragmento.cosmetics.network.s2c.S2CSyncCosmeticsPayload;
import com.pgalaxyp.fragmento.cosmetics.network.s2c.S2CSyncTierPayload;
import com.pgalaxyp.fragmento.cosmetics.network.s2c.S2CToastPayload;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class CosmeticsClientHandlersImpl implements CosmeticsNetwork.ClientHandlers {

    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void onSyncCosmetics(final S2CSyncCosmeticsPayload payload, final IPayloadContext context) {
        if (payload == null) return;
        context.enqueueWork(new Runnable() {
            @Override
            public void run() {
                CosmeticsClientState.setEffective(payload.playerId(), payload.effective(), payload.version());
            }
        });
    }

    @Override
    public void onSyncTier(final S2CSyncTierPayload payload, final IPayloadContext context) {
        if (payload == null) return;
        context.enqueueWork(new Runnable() {
            @Override
            public void run() {
                CosmeticsClientState.setLocalTier(payload.tier(), payload.version());
            }
        });
    }

    @Override
    public void onSetBaseResult(final S2CSetBaseCosmeticResultPayload payload, final IPayloadContext context) {
        if (payload == null) return;
        context.enqueueWork(new Runnable() {
            @Override
            public void run() {
                CosmeticsClientState.setLastSetBaseResult(payload.slotOrdinal(), payload.success(), payload.errorCode(), payload.version());
            }
        });
    }

    @Override
    public void onSyncCatalog(final S2CSyncCatalogPayload payload, final IPayloadContext context) {
        if (payload == null) return;
        context.enqueueWork(new Runnable() {
            @Override
            public void run() {
                CosmeticsClientState.setCatalog(payload.playerTierLevel(), payload.dataVersion(), payload.unlocked(), payload.tiers(), payload.slots(), payload.lockedCountsByTierSlot());
            }
        });
    }

    @Override
    public void onOpenScreen(final S2COpenCosmeticsScreenPayload payload, final IPayloadContext context) {
        context.enqueueWork(new Runnable() {
            @Override
            public void run() {
                Minecraft mc = Minecraft.getInstance();
                if (mc == null) return;
                mc.setScreen(new com.pgalaxyp.fragmento.cosmetics.client.screen.CosmeticsScreen());
            }
        });
    }

    @Override
    public void onToast(final S2CToastPayload payload, final IPayloadContext context) {
        if (payload == null) return;
        context.enqueueWork(new Runnable() {
            @Override
            public void run() {
                Minecraft mc = Minecraft.getInstance();
                if (mc == null) return;
                if (mc.getToasts() == null) return;
                SystemToast.add(mc.getToasts(), SystemToast.SystemToastId.PACK_LOAD_FAILURE, Component.literal(payload.title()), Component.literal(payload.message()));
            }
        });
    }
}