package com.pgalaxyp.fragmento.combat.bootstrap.minecraft;

import com.pgalaxyp.fragmento.combat.input.bridge.*;
import com.pgalaxyp.fragmento.combat.input.system.*;
import com.pgalaxyp.fragmento.combat.content.bard.*;
import com.pgalaxyp.fragmento.combat.input.platform.ItemWeaponBinding;
import java.util.Objects;
import java.util.function.Supplier;
import net.minecraft.world.item.Item;

public final class McBardClientModule {
    private final ItemWeaponBinding weaponBinding;
    private final PrimaryActionInputHandler primaryHandler;

    public McBardClientModule(Supplier<Item> fluteItem, ActorInputContextProvider actorContext, InputSnapshotProvider snapshots, InputIntentSink sink) {
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