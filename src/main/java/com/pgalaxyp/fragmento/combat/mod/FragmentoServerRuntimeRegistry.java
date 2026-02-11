package com.pgalaxyp.fragmento.combat.mod;

import com.pgalaxyp.fragmento.combat.actionModule.api.PrimaryActionCommand;
import com.pgalaxyp.fragmento.combat.commandModule.api.CommandSinkPort;
import com.pgalaxyp.fragmento.combat.contentModule.FragmentoDomainContent;
import com.pgalaxyp.fragmento.combat.contentModule.minecraft.FragmentoMinecraftContent;
import com.pgalaxyp.fragmento.combat.damageModule.minecraft.McDamageWorldCommandPort;
import com.pgalaxyp.fragmento.combat.engineModule.api.GameEngine;
import com.pgalaxyp.fragmento.combat.engineModule.port.WorldCommandPort;
import com.pgalaxyp.fragmento.combat.engineModule.system.CompositeWorldCommandPort;
import com.pgalaxyp.fragmento.combat.platformModule.minecraft.McDamageWorldCommandAdapter;
import com.pgalaxyp.fragmento.combat.platformModule.minecraft.McProjectileWorldCommandAdapter;
import com.pgalaxyp.fragmento.combat.projectileModule.minecraft.McProjectileWorldCommandPort;
import com.pgalaxyp.fragmento.combat.weaponModule.minecraft.ItemWeaponBinding;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public final class FragmentoServerRuntimeRegistry {
    private static final Map<MinecraftServer, Runtime> RUNTIMES = new HashMap<>();

    public static Runtime getOrCreate(MinecraftServer server) {
        return RUNTIMES.computeIfAbsent(Objects.requireNonNull(server), FragmentoServerRuntimeRegistry::createRuntime);
    }

    public static void remove(MinecraftServer server) {
        RUNTIMES.remove(Objects.requireNonNull(server));
    }

    public static void onPrimaryAction(ServerPlayer player) {
        Objects.requireNonNull(player);
        var r = getOrCreate(player.server);
        var weaponId = r.weaponBinding.resolve(player.getMainHandItem()).orElse(null);
        if (weaponId == null) return;
        r.commands.enqueue(new PrimaryActionCommand(new com.pgalaxyp.fragmento.combat.actorModule.api.ActorId(player.getUUID()), weaponId));
    }

    private static Runtime createRuntime(MinecraftServer server) {
        var damagePort = new McDamageWorldCommandPort(server);
        var projectilePort = new McProjectileWorldCommandPort(server, FragmentoDomainContent.CATALOG.projectiles());

        WorldCommandPort world = new CompositeWorldCommandPort(List.of(
                new McDamageWorldCommandAdapter(damagePort),
                new McProjectileWorldCommandAdapter(projectilePort)
        ));

        var created = McCombatServerBootstrap.create(server, world, FragmentoDomainContent.CATALOG, FragmentoDomainContent.DEFAULT_CLASS_ID);

        var weaponBinding = new ItemWeaponBinding();
        for (var e : FragmentoMinecraftContent.REGISTRY.weaponBindings()) weaponBinding.register(e.item(), e.weaponId());
        weaponBinding.freeze();

        return new Runtime(created.engine(), created.commands(), weaponBinding, new AtomicInteger());
    }

    public record Runtime(GameEngine engine, CommandSinkPort commands, ItemWeaponBinding weaponBinding, AtomicInteger tickIndex) {}

    private FragmentoServerRuntimeRegistry() {}
}
