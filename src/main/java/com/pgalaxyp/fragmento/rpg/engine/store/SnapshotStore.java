package com.pgalaxyp.fragmento.rpg.engine.store;

import com.pgalaxyp.fragmento.rpg.core.rule.command.RuleCommand;
import com.pgalaxyp.fragmento.rpg.core.state.snapshot.CombatSnapshot;

import java.util.List;

public final class SnapshotStore {

    private long version;

    public CombatSnapshot nextSnapshot(CombatStateStore state, List<RuleCommand> commands) {
        version++;
        return new CombatSnapshot(
                version,
                state.copyActors(),
                List.copyOf(commands)
        );
    }
}