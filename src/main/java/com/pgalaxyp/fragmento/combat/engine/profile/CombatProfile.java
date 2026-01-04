package com.pgalaxyp.fragmento.combat.engine.profile;

import com.pgalaxyp.fragmento.combat.domain.id.SkillId;
import com.pgalaxyp.fragmento.combat.domain.timing.CombatTime;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;

public interface CombatProfile {

    CombatProfile NOOP = new CombatProfile() {
        @Override
        public List<CombatEffect> onComboStep(ServerPlayer player, int stepIndex, CombatTime now) {
            return List.of();
        }

        @Override
        public List<CombatEffect> onInfusedExecute(ServerPlayer player, SkillId skillId, CombatTime now) {
            return List.of();
        }

        @Override
        public List<CombatEffect> onCastFinish(ServerPlayer player, SkillId skillId, CombatTime now) {
            return List.of();
        }
    };

    List<CombatEffect> onComboStep(ServerPlayer player, int stepIndex, CombatTime now);

    List<CombatEffect> onInfusedExecute(ServerPlayer player, SkillId skillId, CombatTime now);

    List<CombatEffect> onCastFinish(ServerPlayer player, SkillId skillId, CombatTime now);
}