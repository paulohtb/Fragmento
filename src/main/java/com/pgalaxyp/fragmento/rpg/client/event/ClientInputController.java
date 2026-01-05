package com.pgalaxyp.fragmento.rpg.client.event;

import com.pgalaxyp.fragmento.rpg.client.input.ClientKeyBindings;
import com.pgalaxyp.fragmento.rpg.client.network.ClientIntentSender;
import com.pgalaxyp.fragmento.rpg.domain.input.AbilityIntent;
import com.pgalaxyp.fragmento.rpg.domain.input.AbilityIntentKind;
import com.pgalaxyp.fragmento.rpg.domain.input.AttackIntent;
import com.pgalaxyp.fragmento.rpg.domain.input.SkillSlotId;
import net.minecraft.client.Minecraft;

public final class ClientInputController {

    private static final SkillSlotId NORMAL_SLOT = new SkillSlotId(1);
    private static final SkillSlotId SPECIAL_SLOT = new SkillSlotId(2);

    private final ClientIntentSender sender;
    private boolean using;

    public ClientInputController(ClientIntentSender sender) {
        this.sender = sender;
    }

    public void clientTick() {
        Minecraft mc = Minecraft.getInstance();
        if (mc == null || mc.options == null) return;

        while (ClientKeyBindings.NORMAL_SKILL.consumeClick()) {
            sender.sendAbility(new AbilityIntent(NORMAL_SLOT, AbilityIntentKind.TOGGLE));
        }

        while (ClientKeyBindings.SPECIAL_SKILL.consumeClick()) {
            sender.sendAbility(new AbilityIntent(SPECIAL_SLOT, AbilityIntentKind.PRESS));
        }

        if (using && !mc.options.keyUse.isDown()) {
            using = false;
            sender.sendAttack(AttackIntent.HOLD_STOP);
        }
    }

    public void onAttackClick() {
        sender.sendAttack(AttackIntent.CLICK);
    }

    public void onUseItemClick() {
        if (using) return;
        using = true;
        sender.sendAttack(AttackIntent.HOLD_START);
    }
}