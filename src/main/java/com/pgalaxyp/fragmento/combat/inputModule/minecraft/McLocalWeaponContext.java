package com.pgalaxyp.fragmento.combat.inputModule.minecraft;

import com.pgalaxyp.fragmento.combat.weaponModule.api.WeaponId;
import com.pgalaxyp.fragmento.combat.inputModule.port.LocalWeaponContext;
import com.pgalaxyp.fragmento.combat.weaponModule.minecraft.ItemWeaponBinding;
import java.util.*;
import net.minecraft.client.Minecraft;

public final class McLocalWeaponContext implements LocalWeaponContext {
    private final ItemWeaponBinding weaponBinding;

    public McLocalWeaponContext(ItemWeaponBinding weaponBinding) {
        this.weaponBinding = Objects.requireNonNull(weaponBinding);
    }

    @Override public Optional<WeaponId> weaponInMainHandId() {
        var p = Minecraft.getInstance().player;
        return p == null ? Optional.empty() : weaponBinding.resolve(p.getMainHandItem());
    }
}