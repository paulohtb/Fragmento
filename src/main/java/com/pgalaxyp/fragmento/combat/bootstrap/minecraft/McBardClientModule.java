package com.pgalaxyp.fragmento.combat.bootstrap.minecraft;

import com.pgalaxyp.fragmento.combat.content.bard.BardMinecraftBindings;
import com.pgalaxyp.fragmento.combat.input.bridge.ActorInputContextProvider;
import com.pgalaxyp.fragmento.combat.input.bridge.InputIntentSink;
import com.pgalaxyp.fragmento.combat.input.bridge.InputSnapshotProvider;
import com.pgalaxyp.fragmento.combat.input.platform.ItemWeaponBinding;
import com.pgalaxyp.fragmento.combat.input.system.InputConsumptionPolicy;
import com.pgalaxyp.fragmento.combat.input.system.PrimaryActionInputHandler;
import com.pgalaxyp.fragmento.combat.content.bard.BardInputBindings;
import java.util.Objects;
import net.minecraft.world.item.Item;

public final class McBardClientModule {
    private final ItemWeaponBinding weaponBinding;
    private final PrimaryActionInputHandler primaryHandler;

    public McBardClientModule(Item fluteItem, ActorInputContextProvider actorContext, InputSnapshotProvider snapshots, InputIntentSink sink) {
        Objects.requireNonNull(fluteItem);
        Objects.requireNonNull(actorContext);
        Objects.requireNonNull(snapshots);
        Objects.requireNonNull(sink);

        this.weaponBinding = new ItemWeaponBinding();
        BardMinecraftBindings.registerFlute(weaponBinding, fluteItem);
        this.primaryHandler = new PrimaryActionInputHandler(actorContext, snapshots, sink, new InputConsumptionPolicy(), BardInputBindings.primaryAbilityByWeapon());
    }

    public ItemWeaponBinding weaponBinding() {
        return weaponBinding;
    }

    public PrimaryActionInputHandler primaryHandler() {
        return primaryHandler;
    }
}