package com.pgalaxyp.fragmento.combat.client.input;

import com.pgalaxyp.fragmento.bootstrap.logging.FragmentoLog;
import com.pgalaxyp.fragmento.combat.client.network.CombatIntentSender;
import com.pgalaxyp.fragmento.combat.content.catalyst.FluteItem;
import com.pgalaxyp.fragmento.combat.domain.input.AbilityIntent;
import com.pgalaxyp.fragmento.combat.domain.input.AbilityIntentKind;
import com.pgalaxyp.fragmento.combat.domain.input.AttackIntent;
import com.pgalaxyp.fragmento.combat.domain.input.SkillSlotId;
import net.minecraft.client.Minecraft;
import net.minecraft.client.KeyMapping;
import net.minecraft.world.item.ItemStack;

public final class ClientCombatInputController {

    private static final SkillSlotId NORMAL_SLOT = new SkillSlotId(1);
    private static final SkillSlotId SPECIAL_SLOT = new SkillSlotId(2);

    private final CombatIntentSender sender;
    private boolean wasAttackDown;

    public ClientCombatInputController(CombatIntentSender sender) {
        this.sender = sender;
    }

    private boolean catalystActiveLocal(Minecraft mc) {
        ItemStack main = mc.player.getMainHandItem();
        ItemStack off = mc.player.getOffhandItem();
        return FluteItem.isFlute(main) && off.isEmpty();
    }

    public void clientTick() {
        Minecraft mc = Minecraft.getInstance();
        if (mc == null || mc.player == null || mc.options == null || mc.screen != null) {
            wasAttackDown = false;
            return;
        }

        boolean active = catalystActiveLocal(mc);

        KeyMapping attack = mc.options.keyAttack;
        if (attack != null) {
            boolean down = attack.isDown();

            if (active) {
                if (down && !wasAttackDown) {
                    sender.sendAttack(AttackIntent.HOLD_START);
                }
                if (!down && wasAttackDown) {
                    sender.sendAttack(AttackIntent.HOLD_STOP);
                }
            }

            wasAttackDown = down;
        } else {
            wasAttackDown = false;
        }

        KeyMapping normal = FragmentoClientKeys.NORMAL_SKILL;
        if (normal != null) {
            while (normal.consumeClick()) {
                if (active) {
                    sender.sendAbility(
                            new AbilityIntent(NORMAL_SLOT, AbilityIntentKind.TOGGLE)
                    );
                }
            }
        }
    }

    public void onAttackClick() {
        try {
            sender.sendAttack(AttackIntent.CLICK);
        } catch (Throwable t) {
            FragmentoLog.intentEx(t, "client onAttackClick crashed");
        }
    }

    public void onUseItemClick() {
        try {
            sender.sendAbility(
                    new AbilityIntent(SPECIAL_SLOT, AbilityIntentKind.PRESS)
            );
        } catch (Throwable t) {
            FragmentoLog.intentEx(t, "client onUseItemClick crashed");
        }
    }
}