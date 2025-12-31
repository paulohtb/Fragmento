package com.pgalaxyp.fragmento.combat.rule.skill;

import com.pgalaxyp.fragmento.combat.domain.id.SkillId;
import com.pgalaxyp.fragmento.combat.domain.timing.CombatTime;
import com.pgalaxyp.fragmento.combat.state.runtime.ServerCombatState;
import com.pgalaxyp.fragmento.combat.state.runtime.SkillRuntimeState;

public final class CastingSkillRule {

    private final int castDurationTicks;

    public CastingSkillRule(int castDurationTicks) {
        this.castDurationTicks = Math.max(1, castDurationTicks);
    }

    public void startCasting(
            ServerCombatState state,
            SkillId skillId,
            long targetEntityId,
            CombatTime now
    ) {
        if (state == null || skillId == null || now == null) {
            return;
        }

        SkillRuntimeState skills = state.skills();

        if (skills.channelActive()) {
            return;
        }

        CombatTime endsAt = CombatTime.ofTicks(
                now.ticks() + castDurationTicks
        );

        SkillRuntimeState next = new SkillRuntimeState(
                skills.cooldownEndsAt(),
                Math.max(0L, targetEntityId),
                endsAt,
                true
        );

        state.setSkills(next);
        state.bumpVersion();
    }

    public boolean isCastComplete(
            ServerCombatState state,
            CombatTime now
    ) {
        if (state == null || now == null) {
            return false;
        }

        SkillRuntimeState skills = state.skills();

        if (!skills.channelActive()) {
            return false;
        }

        return now.isAfterOrEqual(skills.channelEndsAt());
    }

    public long finishCasting(
            ServerCombatState state
    ) {
        if (state == null) {
            return 0L;
        }

        SkillRuntimeState skills = state.skills();

        if (!skills.channelActive()) {
            return 0L;
        }

        long target = skills.channelTargetEntityId();

        SkillRuntimeState next = new SkillRuntimeState(
                skills.cooldownEndsAt(),
                0L,
                CombatTime.ofTicks(0L),
                false
        );

        state.setSkills(next);
        state.bumpVersion();

        return target;
    }

    public void cancelCasting(
            ServerCombatState state
    ) {
        if (state == null) {
            return;
        }

        SkillRuntimeState skills = state.skills();

        if (!skills.channelActive()) {
            return;
        }

        SkillRuntimeState next = new SkillRuntimeState(
                skills.cooldownEndsAt(),
                0L,
                CombatTime.ofTicks(0L),
                false
        );

        state.setSkills(next);
        state.bumpVersion();
    }
}