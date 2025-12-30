package com.pgalaxyp.fragmento.tier.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.pgalaxyp.fragmento.tier.common.service.TierService;
import com.pgalaxyp.fragmento.tier.server.http.TierRedeemApiClient;
import com.pgalaxyp.fragmento.tier.server.service.TierServices;
import com.pgalaxyp.fragmento.tier.server.sync.TierSyncRuntime;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import java.util.UUID;

public final class RedeemCommand {

    private static final TierRedeemApiClient API = new TierRedeemApiClient();

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

        int level = API.redeem(uuid, code);
        if (level <= 0) {
            src.sendFailure(Component.literal("Código inválido ou já usado."));
            return 0;
        }

        TierService service = TierServices.service();
        if (service == null) {
            src.sendFailure(Component.literal("Serviço indisponível."));
            return 0;
        }

        long now = System.currentTimeMillis();
        service.applyRedeem(uuid, level, now);

        TierSyncRuntime.syncNow(uuid);

        src.sendSuccess(() -> Component.literal("Código resgatado com sucesso."), false);
        return 1;
    }
}