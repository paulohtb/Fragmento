package com.pgalaxyp.fragmento.cosmetics.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.pgalaxyp.fragmento.cosmetics.api.CosmeticId;
import com.pgalaxyp.fragmento.cosmetics.api.CosmeticLoadout;
import com.pgalaxyp.fragmento.cosmetics.api.CosmeticSlot;
import com.pgalaxyp.fragmento.cosmetics.client.screen.CosmeticsScreen;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.RenderPlayerEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import com.mojang.math.Axis;

@EventBusSubscriber(modid = com.pgalaxyp.fragmento.cosmetics.CosmeticsKeys.MOD_ID, value = Dist.CLIENT)
public final class CosmeticsClientInventoryButtonInit {

    private static final ResourceLocation TEST_COSMETIC = ResourceLocation.fromNamespaceAndPath(com.pgalaxyp.fragmento.cosmetics.CosmeticsKeys.MOD_ID, "test_cube");

    private CosmeticsClientInventoryButtonInit() {
    }

    @SubscribeEvent
    public static void onInit(ScreenEvent.Init.Post event) {
        Screen screen = event.getScreen();
        if (screen == null) return;

        boolean ok = screen instanceof InventoryScreen;
        if (!ok && screen instanceof CreativeModeInventoryScreen) ok = true;
        if (!ok) return;

        int x = Math.addExact(event.getScreen().width / 2, 104);
        int y = Math.subtractExact(event.getScreen().height / 2, 84);

        event.addListener(
            Button.builder(Component.literal("Cosmetics"), new Button.OnPress() {
                @Override
                public void onPress(Button b) {
                    Minecraft mc = Minecraft.getInstance();
                    if (mc == null) return;
                    mc.setScreen(new CosmeticsScreen());
                }
            }).bounds(x, y, 80, 20).build()
        );
    }

    @SubscribeEvent
    public static void onLogout(ClientPlayerNetworkEvent.LoggingOut event) {
        CosmeticsClientState.clearAll();
    }

    @SubscribeEvent
    public static void onRenderPlayer(RenderPlayerEvent.Post event) {
        if (!(event.getEntity() instanceof AbstractClientPlayer player)) return;

        UUID id = player.getUUID();
        Minecraft mc = Minecraft.getInstance();
        UUID self = mc != null && mc.player != null ? mc.player.getUUID() : null;

        if (self != null && self.equals(id)) {
            if (mc != null && mc.options != null && mc.options.getCameraType().isFirstPerson()) {
                return;
            }
        } else {
            if (!CosmeticsClientState.showOthers()) {
                return;
            }
        }

        CosmeticLoadout loadout = CosmeticsClientState.getEffective(id);
        if (loadout == null) return;

        CosmeticId head = loadout.get(CosmeticSlot.HEAD);
        if (head == null) return;

        if (!TEST_COSMETIC.equals(head.value())) return;

        if (!(event.getRenderer() instanceof PlayerRenderer pr)) return;

        PoseStack pose = event.getPoseStack();
        if (pose == null) return;

        MultiBufferSource buffers = event.getMultiBufferSource();
        if (buffers == null) return;

        PlayerModel<?> model = pr.getModel();

        int packedLight = event.getPackedLight();

        pose.pushPose();
        model.head.translateAndRotate(pose);

        pose.translate(0.0F, 0.25F, 0.0F);
        pose.mulPose(Axis.XP.rotationDegrees(180.0F));
        pose.scale(0.6F, 0.6F, 0.6F);

        ItemStack stack = new ItemStack(Blocks.DIAMOND_BLOCK);
        mc.getItemRenderer().renderStatic(stack, ItemDisplayContext.FIXED, packedLight, OverlayTexture.NO_OVERLAY, pose, buffers, player.level(), 0);

        pose.popPose();
    }
}