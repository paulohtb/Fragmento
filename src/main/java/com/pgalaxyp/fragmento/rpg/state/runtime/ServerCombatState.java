package com.pgalaxyp.fragmento.rpg.state.runtime;

import com.pgalaxyp.fragmento.rpg.state.snapshot.CombatSnapshot;
import com.pgalaxyp.fragmento.rpg.state.snapshot.CombatSnapshotVersion;
import com.pgalaxyp.fragmento.rpg.state.snapshot.ComboSnapshot;
import com.pgalaxyp.fragmento.rpg.state.snapshot.LoadoutSnapshot;
import com.pgalaxyp.fragmento.rpg.state.snapshot.LockSnapshot;

public record ServerCombatState(
        CombatSnapshotVersion version,
        LoadoutState loadout,
        EquippedSkillsState equippedSkills,
        ComboState combo,
        AbilityState abilities,
        ActionLockState lock
) {

    public static ServerCombatState initial() {
        return new ServerCombatState(
                CombatSnapshotVersion.initial(),
                LoadoutState.empty(),
                EquippedSkillsState.empty(),
                ComboState.idle(),
                AbilityState.initial(),
                ActionLockState.idle()
        );
    }

    public ServerCombatState withLoadout(LoadoutState next) {
        if (next != null && next.equals(loadout)) {
            return this;
        }
        return new ServerCombatState(version.next(), next, equippedSkills, combo, abilities, lock);
    }

    public ServerCombatState withEquippedSkills(EquippedSkillsState next) {
        if (next != null && next.equals(equippedSkills)) {
            return this;
        }
        return new ServerCombatState(version.next(), loadout, next, combo, abilities, lock);
    }

    public ServerCombatState withCombo(ComboState next) {
        if (next != null && next.equals(combo)) {
            return this;
        }
        return new ServerCombatState(version.next(), loadout, equippedSkills, next, abilities, lock);
    }

    public ServerCombatState withAbilities(AbilityState next) {
        if (next != null && next.equals(abilities)) {
            return this;
        }
        return new ServerCombatState(version.next(), loadout, equippedSkills, combo, next, lock);
    }

    public ServerCombatState withLock(ActionLockState next) {
        if (next != null && next.equals(lock)) {
            return this;
        }
        return new ServerCombatState(version.next(), loadout, equippedSkills, combo, abilities, next);
    }

    public CombatSnapshot snapshot() {
        return new CombatSnapshot(
                version,
                new ComboSnapshot(
                        combo.stepIndex(),
                        combo.nextStepAt(),
                        combo.holding(),
                        combo.holdLatched()
                ),
                abilities.snapshot(),
                new LockSnapshot(
                        lock.actionKind(),
                        lock.skillId(),
                        lock.endsAt(),
                        lock.itemSwapLockedUntil()
                ),
                new LoadoutSnapshot(
                        loadout.equippedCatalyst(),
                        loadout.family(),
                        loadout.offhandEmpty()
                )
        );
    }
}