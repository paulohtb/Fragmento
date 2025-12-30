package com.pgalaxyp.fragmento.cosmetics.client.entitlement;

import com.pgalaxyp.fragmento.cosmetics.common.entitlement.CosmeticEntitlementClientView;
import com.pgalaxyp.fragmento.cosmetics.common.entitlement.ProgressionBasedCosmeticEntitlementCore;
import com.pgalaxyp.fragmento.cosmetics.common.model.CosmeticDefinition;
import com.pgalaxyp.fragmento.common.progression.PlayerProgressionView;
import net.minecraft.client.Minecraft;

public final class ProgressionBasedCosmeticEntitlementClientView
        implements CosmeticEntitlementClientView {

    private final ProgressionBasedCosmeticEntitlementCore core;

    public ProgressionBasedCosmeticEntitlementClientView(PlayerProgressionView progression) {
        this.core = new ProgressionBasedCosmeticEntitlementCore(progression);
    }

    @Override
    public boolean allowed(CosmeticDefinition def) {
        Minecraft mc = Minecraft.getInstance();
        return mc.player != null && core.allowed(mc.player.getUUID(), def);
    }

    @Override
    public long version() {
        Minecraft mc = Minecraft.getInstance();
        return mc.player == null ? 0L : core.version(mc.player.getUUID());
    }

    @Override
    public String label() {
        Minecraft mc = Minecraft.getInstance();
        return mc.player == null ? "Tier: ?" : core.label(mc.player.getUUID());
    }
}