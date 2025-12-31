package com.pgalaxyp.fragmento.combat.state.runtime;

import com.pgalaxyp.fragmento.combat.state.snapshot.CombatSnapshot;
import com.pgalaxyp.fragmento.combat.state.snapshot.CombatSnapshotVersion;
import com.pgalaxyp.fragmento.combat.state.snapshot.SkillSnapshot;
import com.pgalaxyp.fragmento.combat.state.snapshot.WeaponSnapshot;

public final class ServerCombatState {

    private CombatSnapshotVersion version;
    private final WeaponRuntimeState weapon;
    private final InfusionRuntimeState infusion;

    public ServerCombatState() {
        this.version = CombatSnapshotVersion.initial();
        this.weapon = new WeaponRuntimeState();
        this.infusion = new InfusionRuntimeState();
    }

    public WeaponRuntimeState weapon() {
        return weapon;
    }

    public InfusionRuntimeState infusion() {
        return infusion;
    }

    public CombatSnapshot snapshot() {
        return new CombatSnapshot(
                version,
                new WeaponSnapshot(weapon.comboIndex()),
                SkillSnapshot.empty()
        );
    }

    public void bumpVersion() {
        version = version.next();
    }
}