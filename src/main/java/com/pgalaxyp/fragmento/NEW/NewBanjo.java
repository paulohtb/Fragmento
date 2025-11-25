package com.pgalaxyp.fragmento.NEW;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.model.geom.builders.LayerDefinition;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class NewBanjo<T extends Entity> extends EntityModel<T> {

    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(
                    ResourceLocation.fromNamespaceAndPath("fragmento", "banjo"),
                    "main"
            );

    private final ModelPart newBanjo;

    public NewBanjo(ModelPart root) {
        this.newBanjo = root.getChild("NewBanjo");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild("NewBanjo",
                CubeListBuilder.create()
                        .texOffs(12, 5).addBox(-2, -2, -1, 1, 1, 2, new CubeDeformation(0))
                        .texOffs(0, 6).addBox(-2, -1, -1, 1, 2, 2, new CubeDeformation(0))
                        .texOffs(6, 6).addBox(1, -1, -1, 1, 2, 2, new CubeDeformation(0))
                        .texOffs(4, 13).addBox(-1, -1, -2, 2, 2, 1, new CubeDeformation(0))
                        .texOffs(16, 0).addBox(-1, -1, 1, 2, 2, 1, new CubeDeformation(0))
                        .texOffs(16, 3).addBox(-1, 1, 1, 2, 1, 1, new CubeDeformation(0))
                        .texOffs(12, 0).addBox(-2, -2, 1, 1, 4, 1, new CubeDeformation(0))
                        .texOffs(4, 16).addBox(-1, -2, 1, 2, 1, 1, new CubeDeformation(0))
                        .texOffs(0, 0).addBox(-1, -2, -1, 2, 1, 2, new CubeDeformation(0))
                        .texOffs(0, 3).addBox(-1, 1, -1, 2, 1, 2, new CubeDeformation(0))
                        .texOffs(8, 0).addBox(1, -2, 1, 1, 4, 1, new CubeDeformation(0))
                        .texOffs(0, 10).addBox(1, -2, -1, 1, 1, 2, new CubeDeformation(0))
                        .texOffs(6, 10).addBox(1, 1, -1, 1, 1, 2, new CubeDeformation(0))
                        .texOffs(0, 13).addBox(1, -2, -2, 1, 4, 1, new CubeDeformation(0))
                        .texOffs(10, 16).addBox(-1, 1, -2, 2, 1, 1, new CubeDeformation(0))
                        .texOffs(16, 11).addBox(-1, -2, -2, 2, 1, 1, new CubeDeformation(0))
                        .texOffs(12, 8).addBox(-2, 1, -1, 1, 1, 2, new CubeDeformation(0))
                        .texOffs(12, 11).addBox(-2, -2, -2, 1, 4, 1, new CubeDeformation(0)),
                PartPose.offset(0, 2, 0));

        return LayerDefinition.create(mesh, 32, 32);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    @Override
    public void renderToBuffer(PoseStack pose, VertexConsumer consumer, int light, int overlay, int color) {
        newBanjo.render(pose, consumer, light, overlay, color);
    }
}
