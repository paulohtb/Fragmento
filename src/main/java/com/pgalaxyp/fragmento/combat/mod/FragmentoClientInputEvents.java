package com.pgalaxyp.fragmento.combat.mod;

import com.pgalaxyp.fragmento.combat.inputModule.api.*;
import com.pgalaxyp.fragmento.combat.inputModule.port.*;
import com.pgalaxyp.fragmento.combat.inputModule.minecraft.*;
import com.pgalaxyp.fragmento.combat.platformModule.FragmentoPlatform;
import com.pgalaxyp.fragmento.combat.weaponModule.minecraft.ItemWeaponBinding;
import com.pgalaxyp.fragmento.combat.inputModule.system.PrimaryActionInputHandler;
import com.pgalaxyp.fragmento.combat.contentModule.minecraft.FragmentoMinecraftContent;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;

@EventBusSubscriber(modid = FragmentoPlatform.MODID, value = Dist.CLIENT)
public final class FragmentoClientInputEvents {
    private static volatile Runtime runtime;

    @SubscribeEvent public static void onKey(InputEvent.InteractionKeyMappingTriggered event) {
        var mc = Minecraft.getInstance();
        if (mc.level == null) return;
        if (event.getKeyMapping() != mc.options.keyAttack) return;
        var r = runtime;
        if (r == null) runtime = r = Runtime.create();
        if (r.primaryHandler.onSemanticInput(SemanticInput.PRIMARY_ACTION).consumeVanilla()) event.setCanceled(true);
    }

    private record Runtime(PrimaryActionInputHandler primaryHandler) {
        static Runtime create() {
            var binding = new ItemWeaponBinding();
            for (var e : FragmentoMinecraftContent.REGISTRY.weaponBindings()) binding.register(e.item(), e.weaponId());
            binding.freeze();
            LocalWeaponContext context = new McLocalWeaponContext(binding);
            PrimaryActionCommandPort port = new McPrimaryActionCommandPort();
            return new Runtime(new PrimaryActionInputHandler(context, port));
        }
    }

    private FragmentoClientInputEvents() {}
}