package com.pgalaxyp.fragmento.rpg.client.input;

import com.pgalaxyp.fragmento.bootstrap.logging.FragmentoLog;
import com.pgalaxyp.fragmento.bootstrap.logging.LogChannel;
import com.pgalaxyp.fragmento.rpg.client.ClientContext;
import com.pgalaxyp.fragmento.rpg.client.network.ClientIntentSender;
import com.pgalaxyp.fragmento.rpg.domain.input.AbilityIntent;
import com.pgalaxyp.fragmento.rpg.domain.input.AbilityIntentKind;
import com.pgalaxyp.fragmento.rpg.domain.input.AttackIntent;
import com.pgalaxyp.fragmento.rpg.domain.input.SkillSlotId;
import net.minecraft.client.Minecraft;
import net.minecraft.client.KeyMapping;

public final class ClientInputController {

    private static final SkillSlotId NORMAL_SLOT = new SkillSlotId(1);
    private static final SkillSlotId SPECIAL_SLOT = new SkillSlotId(2);

    private final ClientIntentSender sender;
    private boolean wasAttackDown;

    public ClientInputController(ClientIntentSender sender) {
        this.sender = sender;
    }

    public void clientTick() {
        Minecraft mc = Minecraft.getInstance();
        if (!ClientContext.inGame(mc)) {
            wasAttackDown = false;
            return;
        }

        boolean active = ClientContext.catalystActive(mc);

        KeyMapping attack = mc.options.keyAttack;
        if (attack != null) {
            boolean down = attack.isDown();

            if (active) {
                if (down && !wasAttackDown) sender.sendAttack(AttackIntent.HOLD_START);
                if (!down && wasAttackDown) sender.sendAttack(AttackIntent.HOLD_STOP);
            }

            wasAttackDown = down;
        } else {
            wasAttackDown = false;
        }

        KeyMapping normal = ClientKeyBindings.NORMAL_SKILL;
        if (normal != null) {
            while (normal.consumeClick()) {
                if (active) {
                    sender.sendAbility(new AbilityIntent(NORMAL_SLOT, AbilityIntentKind.TOGGLE));
                }
            }
        }
    }

    public void onAttackClick() {
        try {
            sender.sendAttack(AttackIntent.CLICK);
        } catch (Throwable t) {
            FragmentoLog.logEx(LogChannel.INTENT, t, "client onAttackClick crashed");
        }
    }

    public void onUseItemClick() {
        try {
            sender.sendAbility(new AbilityIntent(SPECIAL_SLOT, AbilityIntentKind.PRESS));
        } catch (Throwable t) {
            FragmentoLog.logEx(LogChannel.INTENT, t, "client onUseItemClick crashed");
        }
    }
}