package com.pgalaxyp.fragmento.combat.state.runtime;

import com.pgalaxyp.fragmento.combat.state.snapshot.CombatSnapshot;
import com.pgalaxyp.fragmento.combat.state.snapshot.CombatSnapshotVersion;
import com.pgalaxyp.fragmento.combat.state.snapshot.ComboSnapshot;

public final class ServerCombatState {

    private CombatSnapshotVersion version;
    private final ComboRuntimeState weapon;
    private final InfusionRuntimeState infusion;
    private SkillRuntimeState skills;

    public ServerCombatState() {
        this.version = CombatSnapshotVersion.initial();
        this.weapon = new ComboRuntimeState();
        this.infusion = new InfusionRuntimeState();
        this.skills = SkillRuntimeState.initial();
    }

    public ComboRuntimeState weapon() {
        return weapon;
    }

    public InfusionRuntimeState infusion() {
        return infusion;
    }

    public SkillRuntimeState skills() {
        return skills;
    }

    public void setSkills(SkillRuntimeState next) {
        this.skills = next;
    }

    public CombatSnapshot snapshot() {
        return new CombatSnapshot(
                version,
                new ComboSnapshot(weapon.comboIndex()),
                skills.snapshot()
        );
    }

    public void bumpVersion() {
        version = version.next();
    }
}