package com.pgalaxyp.fragmento.rpg.gameplay.combat.basic;

import com.pgalaxyp.fragmento.rpg.core.loop.GameTick;
import com.pgalaxyp.fragmento.rpg.core.loop.TickBus;
import com.pgalaxyp.fragmento.rpg.core.loop.Updatable;
import com.pgalaxyp.fragmento.rpg.gameplay.effects.EffectEnded;
import com.pgalaxyp.fragmento.rpg.gameplay.input.InputAction;
import com.pgalaxyp.fragmento.rpg.gameplay.input.InputEvent;
import com.pgalaxyp.fragmento.rpg.gameplay.state.ActorRepository;
import com.pgalaxyp.fragmento.rpg.gameplay.state.CombatDirty;
import com.pgalaxyp.fragmento.rpg.gameplay.state.WeaponRepository;
import java.util.Objects;

public final class ComboResolver implements Updatable {

    private final BasicSequence sequence;
    private final ActorRepository actors;
    private final WeaponRepository weapons;
    private final TickBus bus;

    private long nowNanos;

    public ComboResolver(BasicSequence sequence, ActorRepository actors, WeaponRepository weapons, TickBus bus) {
        this.sequence = Objects.requireNonNull(sequence);
        this.actors = Objects.requireNonNull(actors);
        this.weapons = Objects.requireNonNull(weapons);
        this.bus = Objects.requireNonNull(bus);

        bus.subscribe(InputEvent.class, this::onInput);
        bus.subscribe(EffectEnded.class, this::onEffectEnded);
    }

    private void onInput(InputEvent e) {
        if (e.action() != InputAction.ATTACK_PRIMARY) return;
        if (!weapons.canUse(e.actorId(), nowNanos)) return;

        var combo = actors.combo(e.actorId());
        if (combo.executing()) return;

        var steps = sequence.steps();
        var idx = Math.floorMod(combo.index(), steps.size());
        var step = steps.get(idx);

        combo.beginStep(idx + 1, step.stepId());
        weapons.markUsed(e.actorId(), nowNanos);

        long v = actors.bumpCombatVersion(e.actorId());
        bus.publish(new CombatDirty(e.actorId(), v));
        bus.publish(new ComboStepStarted(e.actorId(), step.stepId()));
    }

    private void onEffectEnded(EffectEnded e) {
        var combo = actors.combo(e.actorId());
        if (!Objects.equals(combo.activeStepId(), e.stepId())) return;

        combo.endStep();
        long v = actors.bumpCombatVersion(e.actorId());
        bus.publish(new CombatDirty(e.actorId(), v));
    }

    @Override
    public void update(GameTick tick, TickBus bus) {
        this.nowNanos = tick.nowNanos();
    }
}