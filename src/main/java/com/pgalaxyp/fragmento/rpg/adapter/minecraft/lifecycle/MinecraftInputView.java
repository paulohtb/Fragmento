package com.pgalaxyp.fragmento.rpg.adapter.minecraft.lifecycle;

import com.pgalaxyp.fragmento.rpg.platform.api.player.InputView;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

import java.util.concurrent.atomic.AtomicBoolean;

public final class MinecraftInputView implements InputView {

    private final AtomicBoolean attack = new AtomicBoolean(false);

    public MinecraftInputView() {
        NeoForge.EVENT_BUS.addListener(this::onAttack);
    }

    private void onAttack(PlayerInteractEvent.LeftClickEmpty event) {
        attack.set(true);
    }

    @Override
    public boolean pressedThisTick(String actionId) {
        if (!"ATTACK_PRIMARY".equals(actionId)) return false;
        return attack.getAndSet(false);
    }
}