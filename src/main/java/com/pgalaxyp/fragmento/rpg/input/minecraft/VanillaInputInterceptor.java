package com.pgalaxyp.fragmento.rpg.input.minecraft;

import com.pgalaxyp.fragmento.rpg.core.domain.ids.ActorId;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.WeaponId;
import com.pgalaxyp.fragmento.rpg.input.api.InputContext;
import com.pgalaxyp.fragmento.rpg.input.api.InputController;
import com.pgalaxyp.fragmento.rpg.input.api.InputDecision;
import com.pgalaxyp.fragmento.rpg.input.api.SemanticInput;
import com.pgalaxyp.fragmento.rpg.input.bridge.ActorInputContextProvider;
import com.pgalaxyp.fragmento.rpg.input.bridge.InputSnapshotProvider;
import com.pgalaxyp.fragmento.rpg.input.bridge.InputSnapshotView;
import com.pgalaxyp.fragmento.rpg.input.system.FrameClock;
import java.util.Optional;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

public final class VanillaInputInterceptor {

    private final InputSnapshotProvider snapshots;
    private final ActorInputContextProvider actors;
    private final FrameClock clock;
    private final InputController controller;

    private long cachedFrameId = Long.MIN_VALUE;
    private Optional<ActorId> cachedActorId = Optional.empty();
    private InputDecision cachedDecision = InputDecision.passThrough();

    public VanillaInputInterceptor(
            InputSnapshotProvider snapshots,
            ActorInputContextProvider actors,
            FrameClock clock,
            InputController controller
    ) {
        if (snapshots == null || actors == null || clock == null || controller == null) {
            throw new IllegalArgumentException();
        }
        this.snapshots = snapshots;
        this.actors = actors;
        this.clock = clock;
        this.controller = controller;
    }

    public void registerClient(IEventBus bus) {
        if (bus == null) {
            throw new IllegalArgumentException();
        }
        bus.register(this);
    }

    @SubscribeEvent
    public void onClientTick(ClientTickEvent.Post event) {
        clock.tick();
        cachedFrameId = Long.MIN_VALUE;
        cachedActorId = Optional.empty();
        cachedDecision = InputDecision.passThrough();
    }

    @SubscribeEvent
    public void onAttackKey(InputEvent.InteractionKeyMappingTriggered event) {
        if (!event.isAttack()) {
            return;
        }

        InputContext ctx = resolveContext();
        InputDecision decision = decisionForCurrentFrame(ctx);

        if (decision.consumeVanilla()) {
            event.setCanceled(true);
            event.setSwingHand(false);
        }
    }

    @SubscribeEvent
    public void onAttackEntity(AttackEntityEvent event) {
        InputContext ctx = resolveContext();
        InputDecision decision = decisionForCurrentFrame(ctx);

        if (decision.consumeVanilla()) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        InputContext ctx = resolveContext();
        InputDecision decision = decisionForCurrentFrame(ctx);

        if (decision.consumeVanilla()) {
            event.setCanceled(true);
        }
    }

    private InputDecision decisionForCurrentFrame(InputContext ctx) {
        long frameId = clock.frameId(ctx);
        Optional<ActorId> actorId = ctx.actorId();

        if (frameId == cachedFrameId && actorId.equals(cachedActorId)) {
            return cachedDecision;
        }

        InputDecision decision = controller.handle(ctx, SemanticInput.PRIMARY_ACTION);

        cachedFrameId = frameId;
        cachedActorId = actorId;
        cachedDecision = decision;
        return decision;
    }

    private InputContext resolveContext() {
        Optional<ActorId> actorId = actors.localActorId();
        InputSnapshotView snap = snapshots.current();
        Optional<WeaponId> weapon = actorId.flatMap(id -> actors.weaponInHandId(id, snap));
        return new InputContext(actorId, snap, weapon);
    }
}