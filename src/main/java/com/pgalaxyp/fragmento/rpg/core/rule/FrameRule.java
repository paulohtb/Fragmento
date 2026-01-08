package com.pgalaxyp.fragmento.rpg.core.rule;

import java.util.List;
import com.pgalaxyp.fragmento.rpg.core.domain.WeaponByAction;
import com.pgalaxyp.fragmento.rpg.core.domain.intent.DomainIntent;
import com.pgalaxyp.fragmento.rpg.core.spec.GameSpec;
import com.pgalaxyp.fragmento.rpg.core.state.snapshot.GameSnapshot;

public interface FrameRule {
    RuleResult apply(
            GameSpec spec,
            GameSnapshot snapshot,
            List<DomainIntent> intents,
            WeaponByAction weapons
    );
}