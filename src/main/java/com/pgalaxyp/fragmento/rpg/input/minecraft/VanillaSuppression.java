package com.pgalaxyp.fragmento.rpg.input.minecraft;

import com.pgalaxyp.fragmento.rpg.input.api.ModInputDecision;
import com.pgalaxyp.fragmento.rpg.input.bridge.SnapshotProvider;
import com.pgalaxyp.fragmento.rpg.input.bridge.SnapshotView;
import com.pgalaxyp.fragmento.rpg.input.system.LocalClientFrameClock;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

public final class VanillaSuppression {

    private final LocalClientFrameClock clock;
    private final SnapshotProvider snapshots;
    private final MinecraftInputHook hook;

    private long suppressedFrame = Long.MIN_VALUE;

    public VanillaSuppression(LocalClientFrameClock clock, SnapshotProvider snapshots, MinecraftInputHook hook) {
        if (clock == null || snapshots == null || hook == null) {
            throw new IllegalArgumentException();
        }
        this.clock = clock;
        this.snapshots = snapshots;
        this.hook = hook;
    }

    public void registerClient(IEventBus bus) {
        if (bus == null) {
            throw new IllegalArgumentException();
        }
        bus.addListener(this::onClientTick);
        bus.addListener(this::onMouseButton);
        bus.addListener(this::onInteractionKey);
        bus.addListener(this::onLeftClickBlock);
        bus.addListener(this::onAttackEntity);
    }

    public void registerServer(IEventBus bus) {
        if (bus == null) {
            throw new IllegalArgumentException();
        }
    }

    private void onClientTick(ClientTickEvent.Post event) {
        clock.tick();
    }

    private boolean isSuppressedNow() {
        return suppressedFrame == clock.now();
    }

    private void suppressThisFrame() {
        suppressedFrame = clock.now();
    }

    private SnapshotView snap() {
        SnapshotView v = snapshots.current();
        return v == null ? SnapshotView.empty() : v;
    }

    private void onMouseButton(InputEvent.MouseButton.Pre event) {
        if (event == null) {
            return;
        }

        SnapshotView snapshot = snap();

        if (event.getAction() != 1) {
            return;
        }

        if (event.getButton() == 0) {
            ModInputDecision d = hook.onMouseLeft(snapshot);
            if (d.consumeVanilla()) {
                suppressThisFrame();
                event.setCanceled(true);
            }
        }
    }

    private void onInteractionKey(InputEvent.InteractionKeyMappingTriggered event) {
        if (event == null) {
            return;
        }

        SnapshotView snapshot = snap();

        if (event.isAttack()) {
            ModInputDecision d = hook.onMouseLeft(snapshot);
            if (d.consumeVanilla()) {
                suppressThisFrame();
                event.setCanceled(true);
                event.setSwingHand(false);
            }
        }
    }

    private void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        if (event == null) {
            return;
        }
        if (isSuppressedNow()) {
            event.setCanceled(true);
        }
    }

    private void onAttackEntity(AttackEntityEvent event) {
        if (event == null) {
            return;
        }
        if (isSuppressedNow()) {
            event.setCanceled(true);
        }
    }
}