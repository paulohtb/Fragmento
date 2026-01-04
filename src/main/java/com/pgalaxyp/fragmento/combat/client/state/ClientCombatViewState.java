package com.pgalaxyp.fragmento.combat.client.state;

import com.pgalaxyp.fragmento.bootstrap.logging.FragmentoLog;
import com.pgalaxyp.fragmento.combat.state.snapshot.CombatSnapshot;

public final class ClientCombatViewState {

    private CombatSnapshot last;

    public void apply(CombatSnapshot snapshot) {
        if (snapshot == null || snapshot.version() == null) {
            FragmentoLog.snapshot("client viewState apply ignore, snapshotNull={} versionNull={}", snapshot == null, snapshot != null && snapshot.version() == null);
            return;
        }

        if (last == null) {
            last = snapshot;
            FragmentoLog.snapshot("client viewState apply accepted first, version={}", snapshot.version().value());
            return;
        }

        long inV = snapshot.version().value();
        long curV = last.version() != null ? last.version().value() : -1L;

        if (snapshot.version().isAfter(last.version())) {
            last = snapshot;
            FragmentoLog.snapshot("client viewState apply accepted newer, from={} to={}", curV, inV);
            return;
        }

        if (inV == curV) {
            FragmentoLog.snapshot("client viewState apply ignored equal, version={}", inV);
            return;
        }

        FragmentoLog.snapshot("client viewState apply ignored older, current={} incoming={}", curV, inV);
    }

    public CombatSnapshot current() {
        return last;
    }

    public void clear() {
        if (last != null && last.version() != null) {
            FragmentoLog.snapshot("client viewState cleared, lastVersion={}", last.version().value());
        } else {
            FragmentoLog.snapshot("client viewState cleared, lastWasNull={}", last == null);
        }
        last = null;
    }
}