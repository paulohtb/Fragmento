package com.pgalaxyp.fragmento.combat.input.minecraft;

import com.pgalaxyp.fragmento.combat.input.api.*;
import com.pgalaxyp.fragmento.combat.input.bridge.*;
import com.pgalaxyp.fragmento.combat.input.system.*;
import com.pgalaxyp.fragmento.combat.core.ids.*;
import net.neoforged.bus.api.*;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.event.entity.player.*;

public final class McInputHook {

    private final InputSnapshotProvider snapshots;
    private final ActorInputContextProvider actors;
    private final FrameClock clock;
    private final InputController controller;
    private long cachedFrameId = Long.MIN_VALUE;
    private ActorId cachedActorId;
    private InputDecision cachedDecision = InputDecision.passThrough();

    public McInputHook(InputSnapshotProvider snapshots, ActorInputContextProvider actors, FrameClock clock, InputController controller) {
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
        cachedActorId = null;
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
        ActorId actorId = ctx.actorIdOpt().orElse(null);

        if (frameId == cachedFrameId && actorId == cachedActorId) {
            return cachedDecision;
        }

        InputDecision decision = controller.handle(ctx, SemanticInput.PRIMARY_ACTION);
        cachedFrameId = frameId;
        cachedActorId = actorId;
        cachedDecision = decision;

        return decision;
    }

    private InputContext resolveContext() {
        ActorId actorId = actors.localActorId().orElse(null);
        InputSnapshotView snapshot = snapshots.current();
        WeaponId weaponId = actorId != null ? actors.weaponInHandId(actorId, snapshot).orElse(null) : null;

        return new InputContext(actorId, snapshot, weaponId);
    }
}