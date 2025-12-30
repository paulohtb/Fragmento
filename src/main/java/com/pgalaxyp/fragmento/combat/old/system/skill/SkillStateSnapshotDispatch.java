package com.pgalaxyp.fragmento.combat.old.system.skill;

import com.pgalaxyp.fragmento.combat.old.content.bard.catalyst.BardCatalystIdService;
import com.pgalaxyp.fragmento.combat.old.content.bard.catalyst.BardCatalystItem;
import com.pgalaxyp.fragmento.combat.old.network.s2c.SkillStateSnapshotPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.UUID;

public final class SkillStateSnapshotDispatch {

    private SkillStateSnapshotDispatch() {
    }

    public static void send(ServerPlayer player, UUID instrumentId) {
        if (player == null || instrumentId == null) return;

        MinecraftServer server = player.getServer();
        if (server == null) return;

        SkillStateSnapshotService service = ServerSkillStateServices.snapshots(server);
        SkillStateSnapshot snapshot = service.build(player, instrumentId);
        SkillStateSnapshotPacket packet = service.toPacket(snapshot);

        PacketDistributor.sendToPlayer(player, packet);
    }

    public static void sendForHeldInstrument(ServerPlayer player) {
        if (player == null) return;

        ItemStack main = player.getMainHandItem();
        if (main.getItem() instanceof BardCatalystItem) {
            UUID id = BardCatalystIdService.getOrCreate(main);
            send(player, id);
            return;
        }

        ItemStack off = player.getOffhandItem();
        if (off.getItem() instanceof BardCatalystItem) {
            UUID id = BardCatalystIdService.getOrCreate(off);
            send(player, id);
        }
    }
}