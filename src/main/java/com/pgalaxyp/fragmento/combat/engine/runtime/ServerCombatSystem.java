package com.pgalaxyp.fragmento.combat.engine.runtime;

import com.pgalaxyp.fragmento.combat.domain.input.AbilityIntent;
import com.pgalaxyp.fragmento.combat.domain.input.AttackIntent;
import com.pgalaxyp.fragmento.combat.domain.timing.CombatTime;
import com.pgalaxyp.fragmento.combat.engine.network.CombatSnapshotSender;
import com.pgalaxyp.fragmento.combat.engine.restriction.ItemSwapBlocker;
import com.pgalaxyp.fragmento.combat.engine.time.TickClock;
import com.pgalaxyp.fragmento.combat.rule.ability.CastedRule;
import com.pgalaxyp.fragmento.combat.rule.ability.InfusedRule;
import com.pgalaxyp.fragmento.combat.rule.combat.AbilityEngine;
import com.pgalaxyp.fragmento.combat.rule.combat.ActionLockRule;
import com.pgalaxyp.fragmento.combat.rule.combat.CombatEngine;
import com.pgalaxyp.fragmento.combat.rule.combat.ComboResetRule;
import com.pgalaxyp.fragmento.combat.rule.combat.ComboRule;
import com.pgalaxyp.fragmento.combat.rule.combat.ComboTimingRule;
import com.pgalaxyp.fragmento.combat.rule.combat.HoldLatchRule;
import com.pgalaxyp.fragmento.combat.rule.cooldown.CooldownRule;
import com.pgalaxyp.fragmento.combat.rule.gate.EquipGateRule;
import com.pgalaxyp.fragmento.combat.rule.gate.LockGateRule;
import com.pgalaxyp.fragmento.combat.rule.port.AbilityConfig;
import com.pgalaxyp.fragmento.combat.rule.port.ComboConfig;
import com.pgalaxyp.fragmento.combat.rule.port.CombatClock;
import com.pgalaxyp.fragmento.combat.state.runtime.ServerCombatState;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class ServerCombatSystem {

    private static final ServerCombatSystem INSTANCE = new ServerCombatSystem();

    private final Map<UUID, CombatRuntime> runtimes = new HashMap<>();
    private final Map<UUID, Long> lastSentVersion = new HashMap<>();

    private final CombatSnapshotSender snapshotSender = new CombatSnapshotSender();

    private final EquipGateRule equipGate = new EquipGateRule();
    private final LockGateRule lockGate = new LockGateRule();
    private final CooldownRule cooldownRule = new CooldownRule();
    private final ActionLockRule actionLockRule = new ActionLockRule();

    private final ComboConfig comboConfig = new ComboConfig() {
        public int maxSteps() { return 3; }
        public com.pgalaxyp.fragmento.combat.domain.timing.Duration stepDuration() { return com.pgalaxyp.fragmento.combat.domain.timing.Duration.ofTicks(10); }
        public com.pgalaxyp.fragmento.combat.domain.timing.Duration actionLockDuration() { return com.pgalaxyp.fragmento.combat.domain.timing.Duration.ofTicks(10); }
    };

    private final AbilityConfig abilityConfig = new AbilityConfig() {
        public com.pgalaxyp.fragmento.combat.domain.timing.Duration castDuration(com.pgalaxyp.fragmento.combat.domain.id.SkillId id) { return com.pgalaxyp.fragmento.combat.domain.timing.Duration.ofTicks(20); }
        public com.pgalaxyp.fragmento.combat.domain.timing.Duration cooldownDuration(com.pgalaxyp.fragmento.combat.domain.id.SkillId id) { return com.pgalaxyp.fragmento.combat.domain.timing.Duration.ofTicks(40); }
        public com.pgalaxyp.fragmento.combat.domain.timing.Duration actionLockDuration(com.pgalaxyp.fragmento.combat.domain.id.SkillId id) { return com.pgalaxyp.fragmento.combat.domain.timing.Duration.ofTicks(10); }
    };

    private final InfusedRule infusedRule = new InfusedRule(cooldownRule, abilityConfig);
    private final CastedRule castedRule = new CastedRule(cooldownRule, abilityConfig);

    private final CombatEngine combatEngine =
            new CombatEngine(
                    equipGate,
                    lockGate,
                    comboConfig,
                    actionLockRule,
                    new ComboRule(),
                    new ComboTimingRule(),
                    new ComboResetRule(),
                    new HoldLatchRule(),
                    infusedRule
            );

    private final AbilityEngine abilityEngine =
            new AbilityEngine(
                    equipGate,
                    lockGate,
                    abilityConfig,
                    actionLockRule,
                    infusedRule,
                    castedRule
            );

    private ServerCombatSystem() {
        NeoForge.EVENT_BUS.register(this);
        NeoForge.EVENT_BUS.register(new ItemSwapBlocker());
    }

    public static ServerCombatSystem get() {
        return INSTANCE;
    }

    public void onAttackIntent(ServerPlayer player, AttackIntent intent) {
        CombatRuntime rt = runtimeFor(player);
        rt.onAttackIntent(intent);
        sendIfChanged(player, rt.state());
    }

    public void onAbilityIntent(ServerPlayer player, AbilityIntent intent) {
        CombatRuntime rt = runtimeFor(player);
        rt.onAbilityIntent(intent);
        sendIfChanged(player, rt.state());
    }

    @SubscribeEvent
    public void onPlayerTick(PlayerTickEvent.Post event) {
        if (!(event.getEntity() instanceof ServerPlayer sp)) {
            return;
        }

        CombatRuntime rt = runtimeFor(sp);
        rt.tick(sp);
        sendIfChanged(sp, rt.state());
    }

    public ServerCombatState stateFor(ServerPlayer player) {
        CombatRuntime rt = runtimes.get(player.getUUID());
        return rt != null ? rt.state() : null;
    }

    public CombatTime now(ServerPlayer player) {
        return CombatTime.ofTicks(player.level().getGameTime());
    }

    private CombatRuntime runtimeFor(ServerPlayer player) {
        return runtimes.computeIfAbsent(
                player.getUUID(),
                id -> {
                    CombatClock clock = new TickClock(() -> player.level().getGameTime());
                    return new CombatRuntime(
                            clock,
                            combatEngine,
                            abilityEngine,
                            ServerCombatState.initial()
                    );
                }
        );
    }

    private void sendIfChanged(ServerPlayer player, ServerCombatState state) {
        long v = state.version().value();
        long last = lastSentVersion.getOrDefault(player.getUUID(), Long.MIN_VALUE);
        if (v == last) {
            return;
        }
        lastSentVersion.put(player.getUUID(), v);
        snapshotSender.send(player, state.snapshot());
    }
}