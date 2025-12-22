package com.pgalaxyp.fragmento.cosmetics.network;

import com.mojang.logging.LogUtils;
import com.pgalaxyp.fragmento.cosmetics.CosmeticsKeys;
import com.pgalaxyp.fragmento.cosmetics.network.c2s.C2SLinkTierKeyPayload;
import com.pgalaxyp.fragmento.cosmetics.network.c2s.C2SRequestClearBaseCosmeticPayload;
import com.pgalaxyp.fragmento.cosmetics.network.c2s.C2SRequestSetBaseCosmeticPayload;
import com.pgalaxyp.fragmento.cosmetics.network.s2c.S2COpenCosmeticsScreenPayload;
import com.pgalaxyp.fragmento.cosmetics.network.s2c.S2CSetBaseCosmeticResultPayload;
import com.pgalaxyp.fragmento.cosmetics.network.s2c.S2CSyncCatalogPayload;
import com.pgalaxyp.fragmento.cosmetics.network.s2c.S2CSyncCosmeticsPayload;
import com.pgalaxyp.fragmento.cosmetics.network.s2c.S2CSyncTierPayload;
import com.pgalaxyp.fragmento.cosmetics.network.s2c.S2CToastPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.slf4j.Logger;

@EventBusSubscriber(modid = CosmeticsKeys.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public final class CosmeticsNetwork {

    private static final Logger LOGGER = LogUtils.getLogger();

    public static final String PROTOCOL_VERSION = "1";

    private static volatile ServerHandlers SERVER_HANDLERS;
    private static volatile ClientHandlers CLIENT_HANDLERS;

    private CosmeticsNetwork() {
    }

    public static void setServerHandlers(ServerHandlers handlers) {
        SERVER_HANDLERS = handlers;
        LOGGER.info("CosmeticsNetwork setServerHandlers {}", Boolean.valueOf(handlers != null));
    }

    public static void setClientHandlers(ClientHandlers handlers) {
        CLIENT_HANDLERS = handlers;
        LOGGER.info("CosmeticsNetwork setClientHandlers {}", Boolean.valueOf(handlers != null));
    }

    @SubscribeEvent
    public static void onRegisterPayloads(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(PROTOCOL_VERSION);

        registrar.playToServer(C2SRequestSetBaseCosmeticPayload.TYPE, C2SRequestSetBaseCosmeticPayload.STREAM_CODEC, CosmeticsNetwork::handleSetBase);
        registrar.playToServer(C2SRequestClearBaseCosmeticPayload.TYPE, C2SRequestClearBaseCosmeticPayload.STREAM_CODEC, CosmeticsNetwork::handleClearBase);
        registrar.playToServer(C2SLinkTierKeyPayload.TYPE, C2SLinkTierKeyPayload.STREAM_CODEC, CosmeticsNetwork::handleLinkKey);

        registrar.playToClient(S2CSyncCosmeticsPayload.TYPE, S2CSyncCosmeticsPayload.STREAM_CODEC, CosmeticsNetwork::handleSyncCosmeticsClient);
        registrar.playToClient(S2CSyncTierPayload.TYPE, S2CSyncTierPayload.STREAM_CODEC, CosmeticsNetwork::handleSyncTierClient);
        registrar.playToClient(S2CSetBaseCosmeticResultPayload.TYPE, S2CSetBaseCosmeticResultPayload.STREAM_CODEC, CosmeticsNetwork::handleSetBaseResultClient);
        registrar.playToClient(S2CSyncCatalogPayload.TYPE, S2CSyncCatalogPayload.STREAM_CODEC, CosmeticsNetwork::handleSyncCatalogClient);
        registrar.playToClient(S2COpenCosmeticsScreenPayload.TYPE, S2COpenCosmeticsScreenPayload.STREAM_CODEC, CosmeticsNetwork::handleOpenScreenClient);
        registrar.playToClient(S2CToastPayload.TYPE, S2CToastPayload.STREAM_CODEC, CosmeticsNetwork::handleToastClient);

        LOGGER.info("CosmeticsNetwork onRegisterPayloads ok");
    }

    private static void handleSetBase(C2SRequestSetBaseCosmeticPayload payload, IPayloadContext context) {
        ServerHandlers handlers = SERVER_HANDLERS;
        if (handlers == null) {
            LOGGER.warn("handleSetBase ignored handlers null");
            return;
        }
        if (!(context.player() instanceof ServerPlayer sp)) {
            LOGGER.warn("handleSetBase ignored not ServerPlayer");
            return;
        }
        handlers.onSetBase(sp, payload);
    }

    private static void handleClearBase(C2SRequestClearBaseCosmeticPayload payload, IPayloadContext context) {
        ServerHandlers handlers = SERVER_HANDLERS;
        if (handlers == null) {
            LOGGER.warn("handleClearBase ignored handlers null");
            return;
        }
        if (!(context.player() instanceof ServerPlayer sp)) {
            LOGGER.warn("handleClearBase ignored not ServerPlayer");
            return;
        }
        handlers.onClearBase(sp, payload);
    }

    private static void handleLinkKey(C2SLinkTierKeyPayload payload, IPayloadContext context) {
        ServerHandlers handlers = SERVER_HANDLERS;
        if (handlers == null) {
            LOGGER.warn("handleLinkKey ignored handlers null");
            return;
        }
        if (!(context.player() instanceof ServerPlayer sp)) {
            LOGGER.warn("handleLinkKey ignored not ServerPlayer");
            return;
        }
        handlers.onLinkKey(sp, payload);
    }

    private static void handleSyncCosmeticsClient(S2CSyncCosmeticsPayload payload, IPayloadContext context) {
        ClientHandlers handlers = CLIENT_HANDLERS;
        if (handlers == null) return;
        handlers.onSyncCosmetics(payload, context);
    }

    private static void handleSyncTierClient(S2CSyncTierPayload payload, IPayloadContext context) {
        ClientHandlers handlers = CLIENT_HANDLERS;
        if (handlers == null) return;
        handlers.onSyncTier(payload, context);
    }

    private static void handleSetBaseResultClient(S2CSetBaseCosmeticResultPayload payload, IPayloadContext context) {
        ClientHandlers handlers = CLIENT_HANDLERS;
        if (handlers == null) return;
        handlers.onSetBaseResult(payload, context);
    }

    private static void handleSyncCatalogClient(S2CSyncCatalogPayload payload, IPayloadContext context) {
        ClientHandlers handlers = CLIENT_HANDLERS;
        if (handlers == null) return;
        handlers.onSyncCatalog(payload, context);
    }

    private static void handleOpenScreenClient(S2COpenCosmeticsScreenPayload payload, IPayloadContext context) {
        ClientHandlers handlers = CLIENT_HANDLERS;
        if (handlers == null) return;
        handlers.onOpenScreen(payload, context);
    }

    private static void handleToastClient(S2CToastPayload payload, IPayloadContext context) {
        ClientHandlers handlers = CLIENT_HANDLERS;
        if (handlers == null) return;
        handlers.onToast(payload, context);
    }

    public interface ServerHandlers {
        void onSetBase(ServerPlayer player, C2SRequestSetBaseCosmeticPayload payload);
        void onClearBase(ServerPlayer player, C2SRequestClearBaseCosmeticPayload payload);
        void onLinkKey(ServerPlayer player, C2SLinkTierKeyPayload payload);
    }

    public interface ClientHandlers {
        void onSyncCosmetics(S2CSyncCosmeticsPayload payload, IPayloadContext context);
        void onSyncTier(S2CSyncTierPayload payload, IPayloadContext context);
        void onSetBaseResult(S2CSetBaseCosmeticResultPayload payload, IPayloadContext context);
        void onSyncCatalog(S2CSyncCatalogPayload payload, IPayloadContext context);
        void onOpenScreen(S2COpenCosmeticsScreenPayload payload, IPayloadContext context);
        void onToast(S2CToastPayload payload, IPayloadContext context);
    }
}