package com.pgalaxyp.fragmento.rpg.content.sequences;

import com.pgalaxyp.fragmento.rpg.gameplay.combat.basic.BasicSequence;
import com.pgalaxyp.fragmento.rpg.gameplay.combat.basic.ComboStep;
import com.pgalaxyp.fragmento.rpg.gameplay.zone.SpawnRule;
import java.util.List;

public final class DemoContent {

    private DemoContent() {}

    public static BasicSequence fluteSequence() {
        return new BasicSequence(List.of(
                new ComboStep("flute_1", "MISSILE", SpawnRule.AUTO),
                new ComboStep("flute_2", "MISSILE", SpawnRule.OPPOSITE),
                new ComboStep("flute_3", "MISSILE", SpawnRule.TOP)
        ));
    }
}