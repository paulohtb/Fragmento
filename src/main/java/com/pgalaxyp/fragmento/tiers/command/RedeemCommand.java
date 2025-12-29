package com.pgalaxyp.fragmento.tiers.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import com.pgalaxyp.fragmento.tiers.common.model.Tier;
import com.pgalaxyp.fragmento.tiers.common.model.TierLevel;
import com.pgalaxyp.fragmento.tiers.common.model.TierStatus;
import com.pgalaxyp.fragmento.tiers.common.network.TierSyncPacket;
import com.pgalaxyp.fragmento.tiers.server.http.TierRedeemApiClient;
import com.pgalaxyp.fragmento.tiers.server.service.TierServices;
import com.pgalaxyp.fragmento.tiers.common.service.TierService;
import java.util.UUID;

public final class RedeemCommand {

    private RedeemCommand() {}

    public static void register(CommandDispatcher<CommandSourceStack> d) {
        d.register(
                Commands.literal("redeem")
                        .then(
                                Commands.argument("code", StringArgumentType.word())
                                        .executes(RedeemCommand::execute)
                        )
        );
    }

    private static int execute(CommandContext<CommandSourceStack> ctx) {
        CommandSourceStack src = ctx.getSource();

        if (!(src.getEntity() instanceof ServerPlayer player)) {
            src.sendFailure(Component.literal("Este comando só pode ser usado por jogadores."));
            return 0;
        }

        String code = StringArgumentType.getString(ctx, "code");
        UUID uuid = player.getUUID();

        TierRedeemApiClient api = new TierRedeemApiClient();
        int level = api.redeem(uuid, code);

        if (level <= 0) {
            src.sendFailure(Component.literal("Código inválido ou já usado."));
            return 0;
        }

        Tier tier = new Tier(TierLevel.of(level), TierStatus.ACTIVE);
        TierService service = TierServices.service();

        long now = System.currentTimeMillis();
        service.applyLocal(uuid, tier, now);

        long version = service.snapshot(uuid, now).version();
        PacketDistributor.sendToPlayer(player, new TierSyncPacket(tier, version));

        src.sendSuccess(() -> Component.literal("Código resgatado com sucesso."), false);
        return 1;
    }
}