package com.pgalaxyp.fragmento.combat.state.runtime;

import com.pgalaxyp.fragmento.combat.state.snapshot.CombatSnapshot;
import com.pgalaxyp.fragmento.combat.state.snapshot.CombatSnapshotVersion;
import com.pgalaxyp.fragmento.combat.state.snapshot.ComboSnapshot;
import com.pgalaxyp.fragmento.combat.state.snapshot.LoadoutSnapshot;
import com.pgalaxyp.fragmento.combat.state.snapshot.LockSnapshot;

public record ServerCombatState(
        CombatSnapshotVersion version,
        LoadoutRuntimeState loadout,
        EquippedSkillsRuntimeState equippedSkills,
        ComboRuntimeState combo,
        AbilityRuntimeState abilities,
        ActionLockState lock
) {

    public static ServerCombatState initial() {
        return new ServerCombatState(
                CombatSnapshotVersion.initial(),
                LoadoutRuntimeState.empty(),
                EquippedSkillsRuntimeState.empty(),
                ComboRuntimeState.idle(),
                AbilityRuntimeState.initial(),
                ActionLockState.idle()
        );
    }

    public ServerCombatState withLoadout(LoadoutRuntimeState next) {
        return new ServerCombatState(version.next(), next, equippedSkills, combo, abilities, lock);
    }

    public ServerCombatState withEquippedSkills(EquippedSkillsRuntimeState next) {
        return new ServerCombatState(version.next(), loadout, next, combo, abilities, lock);
    }

    public ServerCombatState withCombo(ComboRuntimeState next) {
        return new ServerCombatState(version.next(), loadout, equippedSkills, next, abilities, lock);
    }

    public ServerCombatState withAbilities(AbilityRuntimeState next) {
        return new ServerCombatState(version.next(), loadout, equippedSkills, combo, next, lock);
    }

    public ServerCombatState withLock(ActionLockState next) {
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