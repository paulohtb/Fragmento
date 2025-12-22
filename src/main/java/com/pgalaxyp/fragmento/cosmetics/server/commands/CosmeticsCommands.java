package com.pgalaxyp.fragmento.cosmetics.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.pgalaxyp.fragmento.cosmetics.api.CosmeticId;
import com.pgalaxyp.fragmento.cosmetics.api.CosmeticSlot;
import com.pgalaxyp.fragmento.cosmetics.network.s2c.S2COpenCosmeticsScreenPayload;
import com.pgalaxyp.fragmento.cosmetics.server.CosmeticsServerRuntime;
import java.util.function.Predicate;
import java.util.function.Supplier;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;

public final class CosmeticsCommands {

    private CosmeticsCommands() {
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("cosmetics")
                        .then(Commands.literal("open")
                                .executes(CosmeticsCommands::execOpen))
                        .then(Commands.literal("link")
                                .then(Commands.argument("key", StringArgumentType.string())
                                        .executes(CosmeticsCommands::execLink)))
                        .then(Commands.literal("force")
                                .requires(new Predicate<CommandSourceStack>() {
                                    @Override
                                    public boolean test(CommandSourceStack src) {
                                        return src.hasPermission(2);
                                    }
                                })
                                .then(Commands.argument("target", EntityArgument.player())
                                        .then(Commands.argument("slot", StringArgumentType.word())
                                                .then(Commands.argument("cosmetic", ResourceLocationArgument.id())
                                                        .executes(CosmeticsCommands::execForce)))))
                        .then(Commands.literal("unforce")
                                .requires(new Predicate<CommandSourceStack>() {
                                    @Override
                                    public boolean test(CommandSourceStack src) {
                                        return src.hasPermission(2);
                                    }
                                })
                                .then(Commands.argument("target", EntityArgument.player())
                                        .then(Commands.argument("slot", StringArgumentType.word())
                                                .executes(CosmeticsCommands::execUnforce))))
                        .then(Commands.literal("clearforced")
                                .requires(new Predicate<CommandSourceStack>() {
                                    @Override
                                    public boolean test(CommandSourceStack src) {
                                        return src.hasPermission(2);
                                    }
                                })
                                .then(Commands.argument("target", EntityArgument.player())
                                        .executes(CosmeticsCommands::execClearForced)))
        );
    }

    private static int execOpen(CommandContext<CommandSourceStack> ctx) {
        CommandSourceStack src = ctx.getSource();
        if (!(src.getEntity() instanceof ServerPlayer player)) {
            src.sendFailure(Component.literal("Player only"));
            return 0;
        }
        PacketDistributor.sendToPlayer(player, new S2COpenCosmeticsScreenPayload());
        src.sendSuccess(new Supplier<Component>() {
            @Override
            public Component get() {
                return Component.literal("Opened");
            }
        }, false);
        return 1;
    }

    private static int execLink(CommandContext<CommandSourceStack> ctx) {
        CommandSourceStack src = ctx.getSource();
        if (!(src.getEntity() instanceof ServerPlayer player)) {
            src.sendFailure(Component.literal("Player only"));
            return 0;
        }
        String key = StringArgumentType.getString(ctx, "key");
        long nowMillis = System.currentTimeMillis();
        boolean ok = CosmeticsServerRuntime.tierService().consumeLinkCooldown(player, nowMillis);
        if (!ok) {
            src.sendFailure(Component.literal("Please wait"));
            return 0;
        }
        CosmeticsServerRuntime.tierService().linkKey(player.getServer(), player, key, nowMillis);
        src.sendSuccess(new Supplier<Component>() {
            @Override
            public Component get() {
                return Component.literal("Requested");
            }
        }, false);
        return 1;
    }

    private static int execForce(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        ServerPlayer target = EntityArgument.getPlayer(ctx, "target");
        String slotStr = StringArgumentType.getString(ctx, "slot");
        CosmeticSlot slot = CosmeticSlot.byName(slotStr);
        if (slot == null) {
            ctx.getSource().sendFailure(Component.literal("Invalid slot"));
            return 0;
        }
        ResourceLocation rl = ResourceLocationArgument.getId(ctx, "cosmetic");
        CosmeticId id = CosmeticId.of(rl);
        CosmeticsServerRuntime.cosmeticsService().setForced(target.getUUID(), slot, id);
        ctx.getSource().sendSuccess(new Supplier<Component>() {
            @Override
            public Component get() {
                return Component.literal("Applied");
            }
        }, true);
        return 1;
    }

    private static int execUnforce(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        ServerPlayer target = EntityArgument.getPlayer(ctx, "target");
        String slotStr = StringArgumentType.getString(ctx, "slot");
        CosmeticSlot slot = CosmeticSlot.byName(slotStr);
        if (slot == null) {
            ctx.getSource().sendFailure(Component.literal("Invalid slot"));
            return 0;
        }
        CosmeticsServerRuntime.cosmeticsService().clearForced(target.getUUID(), slot);
        ctx.getSource().sendSuccess(new Supplier<Component>() {
            @Override
            public Component get() {
                return Component.literal("Removed");
            }
        }, true);
        return 1;
    }

    private static int execClearForced(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        ServerPlayer target = EntityArgument.getPlayer(ctx, "target");
        CosmeticsServerRuntime.cosmeticsService().clearAllForced(target.getUUID());
        ctx.getSource().sendSuccess(new Supplier<Component>() {
            @Override
            public Component get() {
                return Component.literal("Cleared");
            }
        }, true);
        return 1;
    }
}