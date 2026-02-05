package com.pgalaxyp.fragmento.combat.mod;

import com.pgalaxyp.fragmento.combat.actorModule.api.ActorId;
import com.pgalaxyp.fragmento.combat.contentModule.minecraft.FragmentoMinecraftContent;
import com.pgalaxyp.fragmento.combat.inputModule.api.*;
import com.pgalaxyp.fragmento.combat.inputModule.port.ActorInputContextProvider;
import com.pgalaxyp.fragmento.combat.inputModule.system.PrimaryActionInputHandler;
import com.pgalaxyp.fragmento.combat.intentModule.api.IntentSinkPort;
import com.pgalaxyp.fragmento.combat.intentModule.minecraft.*;
import com.pgalaxyp.fragmento.combat.platformModule.FragmentoPlatform;
import com.pgalaxyp.fragmento.combat.weaponModule.minecraft.ItemWeaponBinding;
import java.util.Optional;
import java.util.function.Supplier;
import net.minecraft.client.*;
import net.minecraft.client.player.LocalPlayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;

@EventBusSubscriber(modid = FragmentoPlatform.MODID, value = Dist.CLIENT)
public final class FragmentoClientInputEvents {
    private static volatile Runtime runtime;

    @SubscribeEvent public static void onKey(InputEvent.InteractionKeyMappingTriggered event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;
        if (event.getKeyMapping() != mc.options.keyAttack) return;
        Runtime r = runtime;
        if (r == null) runtime = r = Runtime.create();
        if (r.primaryHandler.onSemanticInput(SemanticInput.PRIMARY_ACTION).consumeVanilla()) event.setCanceled(true);
    }

    private record Runtime(PrimaryActionInputHandler primaryHandler) {
        static Runtime create() {
            var weaponBinding = new ItemWeaponBinding();
            for (var e : FragmentoMinecraftContent.REGISTRY.clientWeaponBindings()) weaponBinding.register(e.item(), e.weaponId());
            weaponBinding.freeze();
            Supplier<Optional<ActorId>> localId = () -> {
                LocalPlayer p = Minecraft.getInstance().player;
                return p == null ? Optional.empty() : Optional.of(new ActorId(p.getUUID()));
            };
            ActorInputContextProvider actorContext = new com.pgalaxyp.fragmento.combat.inputModule.minecraft.McActorContext(weaponBinding, localId);
            IntentSinkPort sink = new IntegratedServerIntentSink(IntegratedServerIntentBridge::get);
            return new Runtime(new PrimaryActionInputHandler(actorContext, sink));
        }
    }

    private FragmentoClientInputEvents() {}
}
