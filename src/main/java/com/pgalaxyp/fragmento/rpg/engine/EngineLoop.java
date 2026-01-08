package com.pgalaxyp.fragmento.rpg.engine;

import java.util.ArrayList;
import java.util.List;
import com.pgalaxyp.fragmento.rpg.core.domain.WeaponByAction;
import com.pgalaxyp.fragmento.rpg.core.domain.event.DomainEvent;
import com.pgalaxyp.fragmento.rpg.core.domain.intent.DomainIntent;
import com.pgalaxyp.fragmento.rpg.core.rule.FrameRule;
import com.pgalaxyp.fragmento.rpg.core.rule.RuleResult;
import com.pgalaxyp.fragmento.rpg.core.spec.GameSpec;
import com.pgalaxyp.fragmento.rpg.core.state.delta.StateDelta;
import com.pgalaxyp.fragmento.rpg.core.state.snapshot.GameSnapshot;

public final class EngineLoop {

    private final GameSpec spec;
    private final WeaponByAction weapons;
    private final IntentSource intents;
    private final List<FrameRule> rules;

    public EngineLoop(
            GameSpec spec,
            WeaponByAction weapons,
            IntentSource intents,
            List<FrameRule> rules
    ) {
        this.spec = spec;
        this.weapons = weapons;
        this.intents = intents;
        this.rules = List.copyOf(rules);
    }

    public EngineTickResult tick(GameSnapshot snapshot) {

        List<DomainIntent> frameIntents = intents.drain();

        List<StateDelta> allDeltas = new ArrayList<>();
        List<DomainEvent> allEvents = new ArrayList<>();

        for (FrameRule rule : rules) {
            RuleResult result = rule.apply(spec, snapshot, frameIntents, weapons);
            allDeltas.addAll(result.deltas());
            allEvents.addAll(result.events());
        }

        GameSnapshot next = SnapshotCommit.commit(snapshot, allDeltas);

        return new EngineTickResult(next, List.copyOf(allEvents));
    }
}