package com.pgalaxyp.fragmento.combat.combo.api;

import com.pgalaxyp.fragmento.combat.core.ids.ActorId;
import java.util.Objects;

public sealed interface ComboEvent permits
        ComboEvent.Started,
        ComboEvent.Advanced,
        ComboEvent.Completed,
        ComboEvent.Reset,
        ComboEvent.Rejected {

    record Started(ComboSnapshot snapshot, ComboInput input) implements ComboEvent {
        public Started {
            Objects.requireNonNull(snapshot);
            Objects.requireNonNull(input);
        }
    }

    record Advanced(ComboSnapshot snapshot, ComboInput input) implements ComboEvent {
        public Advanced {
            Objects.requireNonNull(snapshot);
            Objects.requireNonNull(input);
        }
    }

    record Completed(ComboSnapshot snapshot, ComboInput input) implements ComboEvent {
        public Completed {
            Objects.requireNonNull(snapshot);
            Objects.requireNonNull(input);
        }
    }

    record Reset(ActorId actorId, ComboId comboId, ComboResetReason reason) implements ComboEvent {
        public Reset {
            Objects.requireNonNull(actorId);
            Objects.requireNonNull(comboId);
            Objects.requireNonNull(reason);
        }
    }

    record Rejected(ActorId actorId, ComboId comboId, ComboInput input, ComboRejectReason reason) implements ComboEvent {
        public Rejected {
            Objects.requireNonNull(actorId);
            Objects.requireNonNull(comboId);
            Objects.requireNonNull(input);
            Objects.requireNonNull(reason);
        }
    }
}