package com.pgalaxyp.fragmento.combat.state.action;

import com.pgalaxyp.fragmento.combat.domain.action.ActionDefinition;
import com.pgalaxyp.fragmento.combat.domain.action.ActionLock;
import com.pgalaxyp.fragmento.combat.domain.action.ActionOutcome;
import com.pgalaxyp.fragmento.combat.domain.timing.CombatTime;

public final class ActiveActionState {

    private ActionPhase phase = ActionPhase.IDLE;

    private ActionDefinition definition;
    private ActionProgress progress;
    private final ActionResultState result = new ActionResultState();

    public ActionPhase phase() {
        return phase;
    }

    public ActionDefinition definition() {
        return definition;
    }

    public ActionProgress progress() {
        return progress;
    }

    public ActionResultState result() {
        return result;
    }

    public boolean isIdle() {
        return phase == ActionPhase.IDLE;
    }

    public boolean isActive() {
        return phase == ActionPhase.ACTIVE;
    }

    public void start(ActionDefinition def, CombatTime now) {
        this.definition = def;
        this.progress = ActionProgress.create(now, def.timing().totalDuration(), def.timing().comboWindow());
        this.result.clear();
        this.phase = ActionPhase.ACTIVE;
    }

    public void tick(CombatTime now) {
        if (phase != ActionPhase.ACTIVE) {
            return;
        }
        if (progress == null) {
            phase = ActionPhase.IDLE;
            return;
        }
        if (!progress.isActive(now)) {
            phase = ActionPhase.RECOVERY;
        }
    }

    public void endNow(ActionOutcome outcome) {
        if (phase == ActionPhase.IDLE) {
            return;
        }
        if (!result.resolved()) {
            result.resolve(outcome);
        }
        phase = ActionPhase.RECOVERY;
    }

    public void clear() {
        phase = ActionPhase.IDLE;
        definition = null;
        progress = null;
        result.clear();
    }

    public boolean blocksWeaponActions() {
        if (definition == null) {
            return false;
        }
        ActionLock lock = definition.lock();
        return lock != null && lock.blocksWeaponActions();
    }

    public boolean blocksSkills() {
        if (definition == null) {
            return false;
        }
        ActionLock lock = definition.lock();
        return lock != null && lock.blocksSkills();
    }

    public boolean canAcceptWeaponAction() {
        return phase == ActionPhase.IDLE;
    }

    public boolean canAcceptSkillAction() {
        if (phase == ActionPhase.IDLE) {
            return true;
        }
        return !blocksSkills();
    }
}