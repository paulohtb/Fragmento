package com.pgalaxyp.fragmento.rpg.adapter.minecraft.lifecycle;

import com.pgalaxyp.fragmento.rpg.gameplay.combat.basic.BasicSequence;
import com.pgalaxyp.fragmento.rpg.gameplay.combat.basic.ComboStep;
import com.pgalaxyp.fragmento.rpg.gameplay.combat.basic.SpawnRule;

import java.util.List;

public final class DemoContent {

    public static BasicSequence fluteSequence() {
        return new BasicSequence(List.of(
                new ComboStep("step1", "MISSILE", SpawnRule.AUTO),
                new ComboStep("step2", "MISSILE", SpawnRule.OPPOSITE),
                new ComboStep("step3", "MISSILE", SpawnRule.TOP)
        ));
    }
}