package com.pgalaxyp.fragmento.rpg.state.runtime;

import com.pgalaxyp.fragmento.rpg.domain.timing.Time;
import com.pgalaxyp.fragmento.rpg.state.snapshot.*;

public record ServerCombatState(
        CombatSnapshotVersion version,
        LoadoutState loadout,
        EquippedSkillsState equippedSkills,
        ComboState combo,
        AbilityState abilities,
        ExecutionState execution,
        ActionLockState lock
) {

    public static ServerCombatState initial() {
        return initial(null);
    }

    public static ServerCombatState initial(LoadoutState loadout) {
        return new ServerCombatState(
                CombatSnapshotVersion.initial(),
                loadout != null ? loadout : LoadoutState.empty(),
                EquippedSkillsState.empty(),
                ComboState.initial(),
                AbilityState.initial(),
                ExecutionState.idle(),
                ActionLockState.idle()
        );
    }

    public CombatSnapshot toSnapshot(Time now) {
        LoadoutState lo = loadout != null ? loadout : LoadoutState.empty();
        EquippedSkillsState eq = equippedSkills != null ? equippedSkills : EquippedSkillsState.empty();
        ActionLockState lk = lock != null ? lock : ActionLockState.idle();
        ExecutionState ex = execution != null ? execution : ExecutionState.idle();

        return new CombatSnapshot(
                version,
                now != null ? now : Time.ofTicks(0L),
                new ComboSnapshot(
                        combo.stepIndex(),
                        combo.nextStepAt(),
                        combo.holding(),
                        combo.holdLatched()
                ),
                abilities.toSnapshot(),
                new ExecutionSnapshot(
                        ex.active(),
                        ex.kind(),
                        ex.endsAt()
                ),
                new LockSnapshot(
                        lk.actionKind(),
                        lk.skillId(),
                        lk.endsAt(),
                        lk.itemSwapLockedUntil()
                ),
                new LoadoutSnapshot(
                        lo.equippedCatalyst(),
                        lo.family(),
                        lo.offhandEmpty()
                ),
                new EquippedSkillsSnapshot(eq.bySlot())
        );
    }

    public ServerCombatState withLoadout(LoadoutState next) {
        return new ServerCombatState(
                version,
                next != null ? next : LoadoutState.empty(),
                equippedSkills,
                combo,
                abilities,
                execution,
                lock
        );
    }

    public ServerCombatState withEquippedSkills(EquippedSkillsState next) {
        return new ServerCombatState(
                version,
                loadout,
                next != null ? next : EquippedSkillsState.empty(),
                combo,
                abilities,
                execution,
                lock
        );
    }

    public ServerCombatState withCombo(ComboState next) {
        return new ServerCombatState(
                version,
                loadout,
                equippedSkills,
                next != null ? next : ComboState.initial(),
                abilities,
                execution,
                lock
        );
    }

    public ServerCombatState withAbilities(AbilityState next) {
        return new ServerCombatState(
                version,
                loadout,
                equippedSkills,
                combo,
                next != null ? next : AbilityState.initial(),
                execution,
                lock
        );
    }

    public ServerCombatState withExecution(ExecutionState next) {
        return new ServerCombatState(
                version,
                loadout,
                equippedSkills,
                combo,
                abilities,
                next != null ? next : ExecutionState.idle(),
                lock
        );
    }

    public ServerCombatState withLock(ActionLockState next) {
        return new ServerCombatState(
                version,
                loadout,
                equippedSkills,
                combo,
                abilities,
                execution,
                next != null ? next : ActionLockState.idle()
        );
    }

    public ServerCombatState bumpVersion() {
        return new ServerCombatState(
                version.next(),
                loadout,
                equippedSkills,
                combo,
                abilities,
                execution,
                lock
        );
    }
}