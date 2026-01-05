package com.pgalaxyp.fragmento.rpg.client.input;

import com.pgalaxyp.fragmento.rpg.client.ClientContext;
import com.pgalaxyp.fragmento.rpg.client.ClientNetworkProxy;
import com.pgalaxyp.fragmento.rpg.domain.input.AttackIntent;
import com.pgalaxyp.fragmento.rpg.domain.input.SkillSlotId;
import net.minecraft.client.Minecraft;

public final class ClientInputController {

    private static final SkillSlotId SPECIAL_SLOT = new SkillSlotId(2);

    private boolean lastSpecialDown;

    public void clientTick(Minecraft mc) {
        if (!ClientContext.inGame(mc)) return;

        if (!ClientContext.catalystActive(mc)) {
            if (lastSpecialDown) {
                ClientNetworkProxy.sendSkillRelease(SPECIAL_SLOT);
                lastSpecialDown = false;
            }
            return;
        }

        while (ClientKeyBindings.NORMAL_SKILL.consumeClick()) {
            ClientNetworkProxy.sendAttackIntent(AttackIntent.CLICK);
        }

        boolean specialDown = ClientKeyBindings.SPECIAL_SKILL.isDown();

        if (specialDown && !lastSpecialDown) {
            ClientNetworkProxy.sendSkillPress(SPECIAL_SLOT);
        } else if (!specialDown && lastSpecialDown) {
            ClientNetworkProxy.sendSkillRelease(SPECIAL_SLOT);
        }

        lastSpecialDown = specialDown;
    }
}