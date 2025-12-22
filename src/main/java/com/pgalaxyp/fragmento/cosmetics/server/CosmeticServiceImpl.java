package com.pgalaxyp.fragmento.cosmetics.server;

import com.pgalaxyp.fragmento.cosmetics.api.CosmeticCatalogEntry;
import com.pgalaxyp.fragmento.cosmetics.api.CosmeticDefinition;
import com.pgalaxyp.fragmento.cosmetics.api.CosmeticId;
import com.pgalaxyp.fragmento.cosmetics.api.CosmeticLoadout;
import com.pgalaxyp.fragmento.cosmetics.api.CosmeticService;
import com.pgalaxyp.fragmento.cosmetics.api.CosmeticSlot;
import com.pgalaxyp.fragmento.cosmetics.api.CosmeticTier;
import com.pgalaxyp.fragmento.cosmetics.api.CosmeticValidationResult;
import com.pgalaxyp.fragmento.cosmetics.internal.CosmeticRegistry;
import com.pgalaxyp.fragmento.cosmetics.internal.CosmeticValidator;
import com.pgalaxyp.fragmento.cosmetics.network.CosmeticsNetwork;
import com.pgalaxyp.fragmento.cosmetics.network.c2s.C2SLinkTierKeyPayload;
import com.pgalaxyp.fragmento.cosmetics.network.c2s.C2SRequestClearBaseCosmeticPayload;
import com.pgalaxyp.fragmento.cosmetics.network.c2s.C2SRequestSetBaseCosmeticPayload;
import com.pgalaxyp.fragmento.cosmetics.network.s2c.S2CSetBaseCosmeticResultPayload;
import com.pgalaxyp.fragmento.cosmetics.network.s2c.S2CSyncCatalogPayload;
import com.pgalaxyp.fragmento.cosmetics.network.s2c.S2CSyncCosmeticsPayload;
import com.pgalaxyp.fragmento.cosmetics.network.s2c.S2CToastPayload;
import com.pgalaxyp.fragmento.cosmetics.player.PlayerCosmeticsAttachment;
import com.pgalaxyp.fragmento.cosmetics.player.PlayerCosmeticsStorage;
import com.pgalaxyp.fragmento.cosmetics.server.tier.PlayerTierAttachment;
import com.pgalaxyp.fragmento.cosmetics.server.tier.PlayerTierService;
import com.pgalaxyp.fragmento.cosmetics.server.tier.PlayerTierStorage;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public final class CosmeticServiceImpl implements CosmeticService, CosmeticsNetwork.ServerHandlers {

    private static final Logger LOGGER = LogManager.getLogger();

    private final MinecraftServer server;
    private final CosmeticRegistry registry;
    private final CosmeticValidator validator;
    private final PlayerTierService tierService;

    public CosmeticServiceImpl(MinecraftServer server, CosmeticRegistry registry, CosmeticValidator validator, PlayerTierService tierService) {
        this.server = Objects.requireNonNull(server, "server");
        this.registry = Objects.requireNonNull(registry, "registry");
        this.validator = Objects.requireNonNull(validator, "validator");
        this.tierService = Objects.requireNonNull(tierService, "tierService");
    }

    @Override
    public CosmeticLoadout getEffective(UUID playerId) {
        ServerPlayer player = server.getPlayerList().getPlayer(playerId);
        if (player == null) return CosmeticLoadout.EMPTY;
        PlayerCosmeticsAttachment att = PlayerCosmeticsStorage.get(player);
        return att.state().resolveEffectiveLoadout();
    }

    @Override
    public CosmeticValidationResult setBase(UUID playerId, CosmeticSlot slot, CosmeticId cosmeticId) {
        ServerPlayer player = server.getPlayerList().getPlayer(playerId);
        if (player == null) return CosmeticValidationResult.fail("player_missing");
        if (slot == null) return CosmeticValidationResult.fail("invalid");
        if (cosmeticId == null) return CosmeticValidationResult.fail("invalid");

        CosmeticValidationResult vr = validator.validateBase(playerId, slot, cosmeticId);
        if (!vr.success()) return vr;

        PlayerCosmeticsAttachment att = PlayerCosmeticsStorage.get(player);
        com.pgalaxyp.fragmento.cosmetics.api.PlayerCosmeticState next = att.state().withBaseSet(slot, cosmeticId);
        att.setState(next);
        syncTrackingAndSelf(player);
        return CosmeticValidationResult.OK;
    }

    @Override
    public void clearBase(UUID playerId, CosmeticSlot slot) {
        ServerPlayer player = server.getPlayerList().getPlayer(playerId);
        if (player == null) return;
        if (slot == null) return;

        PlayerCosmeticsAttachment att = PlayerCosmeticsStorage.get(player);
        com.pgalaxyp.fragmento.cosmetics.api.PlayerCosmeticState next = att.state().withBaseCleared(slot);
        att.setState(next);
        syncTrackingAndSelf(player);
    }

    @Override
    public void setForced(UUID playerId, CosmeticSlot slot, CosmeticId cosmeticId) {
        ServerPlayer player = server.getPlayerList().getPlayer(playerId);
        if (player == null) return;
        if (slot == null) return;
        if (cosmeticId == null) return;

        CosmeticValidationResult vr = validator.validateForced(slot, cosmeticId);
        if (!vr.success()) return;

        PlayerCosmeticsAttachment att = PlayerCosmeticsStorage.get(player);
        com.pgalaxyp.fragmento.cosmetics.api.PlayerCosmeticState next = att.state().withForcedSet(slot, cosmeticId);
        att.setState(next);
        syncTrackingAndSelf(player);
    }

    @Override
    public void clearForced(UUID playerId, CosmeticSlot slot) {
        ServerPlayer player = server.getPlayerList().getPlayer(playerId);
        if (player == null) return;
        if (slot == null) return;

        PlayerCosmeticsAttachment att = PlayerCosmeticsStorage.get(player);
        com.pgalaxyp.fragmento.cosmetics.api.PlayerCosmeticState next = att.state().withForcedCleared(slot);
        att.setState(next);
        syncTrackingAndSelf(player);
    }

    @Override
    public void clearAllForced(UUID playerId) {
        ServerPlayer player = server.getPlayerList().getPlayer(playerId);
        if (player == null) return;

        PlayerCosmeticsAttachment att = PlayerCosmeticsStorage.get(player);
        com.pgalaxyp.fragmento.cosmetics.api.PlayerCosmeticState next = att.state().withForcedClearedAll();
        att.setState(next);
        syncTrackingAndSelf(player);
    }

    @Override
    public void onSetBase(ServerPlayer player, C2SRequestSetBaseCosmeticPayload payload) {
        if (player == null) return;
        if (payload == null) return;

        CosmeticSlot slot = payload.slot();
        CosmeticId id = payload.cosmetic();

        CosmeticValidationResult vr = setBase(player.getUUID(), slot, id);

        PlayerCosmeticsAttachment att = PlayerCosmeticsStorage.get(player);
        long version = att.state().version();
        PacketDistributor.sendToPlayer(player, S2CSetBaseCosmeticResultPayload.of(slot, vr.success(), vr.errorCode(), version));
    }

    @Override
    public void onClearBase(ServerPlayer player, C2SRequestClearBaseCosmeticPayload payload) {
        if (player == null) return;
        if (payload == null) return;
        clearBase(player.getUUID(), payload.slot());
    }

    @Override
    public void onLinkKey(ServerPlayer player, C2SLinkTierKeyPayload payload) {
        if (player == null) return;
        if (payload == null) return;

        long nowMillis = System.currentTimeMillis();
        if (!tierService.consumeLinkCooldown(player, nowMillis)) return;
        tierService.linkKey(server, player, payload.key(), nowMillis);
    }

    public void syncPlayer(ServerPlayer player) {
        syncTrackingAndSelf(player);
    }

    public void syncToViewer(ServerPlayer target, ServerPlayer viewer) {
        PlayerCosmeticsAttachment att = PlayerCosmeticsStorage.get(target);
        CosmeticLoadout effective = att.state().resolveEffectiveLoadout();
        long version = att.state().version();
        PacketDistributor.sendToPlayer(viewer, new S2CSyncCosmeticsPayload(target.getUUID(), effective, version));
    }

    public void syncCatalogToPlayer(ServerPlayer player) {
        PlayerTierAttachment tierAtt = PlayerTierStorage.get(player);
        CosmeticTier tier = tierAtt != null ? tierAtt.tier() : CosmeticTier.TIER_0;

        List<CosmeticCatalogEntry> unlocked = new ArrayList<>();
        int tiers = 4;
        int slots = CosmeticSlot.values().length;
        int[] counts = new int[Math.multiplyExact(tiers, slots)];

        Map<CosmeticId, CosmeticDefinition> defs = registry.snapshot().byIdView();
        for (CosmeticDefinition def : defs.values()) {
            int required = def.requiredTier().level();
            int slotOrdinal = def.slot().ordinal();

            if (tier.allows(def.requiredTier())) {
                unlocked.add(new CosmeticCatalogEntry(
                        def.id().value(),
                        def.type().value(),
                        slotOrdinal,
                        required,
                        def.priority(),
                        def.visibleToSelf()
                ));
            } else {
                if (required < 0) continue;
                if (required >= tiers) continue;
                if (slotOrdinal < 0) continue;
                if (slotOrdinal >= slots) continue;
                int idx = Math.addExact(Math.multiplyExact(required, slots), slotOrdinal);
                if (idx < 0) continue;
                if (idx >= counts.length) continue;
                counts[idx] = Math.addExact(counts[idx], 1);
            }
        }

        int dataVersion = registry.snapshot().dataVersion();
        PacketDistributor.sendToPlayer(player, new S2CSyncCatalogPayload(tier.level(), dataVersion, unlocked, tiers, slots, counts));
    }

    public void onTierUpdated(ServerPlayer player, CosmeticTier tier) {
        sanitizeBaseForTier(player, tier);
        syncCatalogToPlayer(player);
        syncTrackingAndSelf(player);
    }

    private void sanitizeBaseForTier(ServerPlayer player, CosmeticTier tier) {
        if (player == null) return;
        if (tier == null) tier = CosmeticTier.TIER_0;

        PlayerCosmeticsAttachment att = PlayerCosmeticsStorage.get(player);
        com.pgalaxyp.fragmento.cosmetics.api.PlayerCosmeticState state = att.state();
        CosmeticLoadout base = state.baseLoadout();

        EnumMap<CosmeticSlot, CosmeticId> nextMap = new EnumMap<>(CosmeticSlot.class);
        boolean changed = false;

        for (Map.Entry<CosmeticSlot, CosmeticId> e : base.equippedView().entrySet()) {
            CosmeticSlot slot = e.getKey();
            CosmeticId id = e.getValue();
            CosmeticDefinition def = registry.getDefinition(id);
            if (def == null) {
                changed = true;
                continue;
            }
            if (!tier.allows(def.requiredTier())) {
                changed = true;
                continue;
            }
            nextMap.put(slot, id);
        }

        if (!changed) return;

        com.pgalaxyp.fragmento.cosmetics.api.PlayerCosmeticState nextState =
                new com.pgalaxyp.fragmento.cosmetics.api.PlayerCosmeticState(new CosmeticLoadout(nextMap), state.forcedLoadout(), state.version() + 1L);
        att.setState(nextState);
        PacketDistributor.sendToPlayer(player, new S2CToastPayload("Cosmetics", "Some cosmetics were removed"));
    }

    private void syncTrackingAndSelf(ServerPlayer target) {
        PlayerCosmeticsAttachment att = PlayerCosmeticsStorage.get(target);
        CosmeticLoadout effective = att.state().resolveEffectiveLoadout();
        long version = att.state().version();
        PacketDistributor.sendToPlayersTrackingEntityAndSelf(target, new S2CSyncCosmeticsPayload(target.getUUID(), effective, version));
    }
}