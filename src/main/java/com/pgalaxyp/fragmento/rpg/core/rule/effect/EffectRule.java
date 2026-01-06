package com.pgalaxyp.fragmento.rpg.core.rule.effect;

import com.pgalaxyp.fragmento.rpg.core.domain.action.ActionDef;
import com.pgalaxyp.fragmento.rpg.core.domain.event.RpgEvent;
import java.util.List;

public interface EffectRule {
    List<RpgEvent> onComboStep(long actorId, ActionDef action, int comboIndex, long now);
}