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

public class NewFluteProjectileModel<T extends Entity> extends EntityModel<T> {

    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(
                    ResourceLocation.fromNamespaceAndPath("fragmento", "flute_projectile"),
                    "main"
            );

    private final ModelPart main;
    private final ModelPart original;
    private final ModelPart past1;
    private final ModelPart past2;

    public NewFluteProjectileModel(ModelPart root) {
        this.main = root.getChild("main");
        this.original = this.main.getChild("original");
        this.past1 = this.main.getChild("past1");
        this.past2 = this.main.getChild("past2");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        PartDefinition main = root.addOrReplaceChild(
                "main",
                CubeListBuilder.create(),
                PartPose.offset(0, 24, -3)
        );

        PartDefinition original = main.addOrReplaceChild(
                "original",
                CubeListBuilder.create()
                        .texOffs(0, 0).addBox(-4, -8, -4, 1, 8, 1, new CubeDeformation(0))
                        .texOffs(0, 0).addBox(3, -8, -4, 1, 8, 1, new CubeDeformation(0))
                        .texOffs(0, 0).addBox(-3, -8, -4, 6, 1, 1, new CubeDeformation(0))
                        .texOffs(0, 5).addBox(-3, -1, -4, 6, 1, 1, new CubeDeformation(0))
                        .texOffs(0, 0).addBox(3, -8, 3, 1, 8, 1, new CubeDeformation(0))
                        .texOffs(0, 0).addBox(-4, -8, 3, 1, 8, 1, new CubeDeformation(0))
                        .texOffs(0, 5).addBox(-3, -8, 3, 6, 1, 1, new CubeDeformation(0))
                        .texOffs(0, 5).addBox(-3, -1, 3, 6, 1, 1, new CubeDeformation(0))
                        .texOffs(0, 0).addBox(-4, -8, -3, 1, 1, 6, new CubeDeformation(0))
                        .texOffs(0, 0).addBox(-4, -1, -3, 1, 1, 6, new CubeDeformation(0))
                        .texOffs(0, 0).addBox(3, -8, -3, 1, 1, 6, new CubeDeformation(0))
                        .texOffs(0, 0).addBox(3, -1, -3, 1, 1, 6, new CubeDeformation(0))
                        .texOffs(17, 20).addBox(-3, -7, -4, 6, 6, 1, new CubeDeformation(0)),
                PartPose.offset(0, 0, 0)
        );

        original.addOrReplaceChild(
                "cube_r1",
                CubeListBuilder.create()
                        .texOffs(17, 20).addBox(-3, 0, -0.5f, 6, 6, 1, new CubeDeformation(0)),
                PartPose.offsetAndRotation(0, -0.5f, -3, 1.5708f, 0, 0)
        );

        original.addOrReplaceChild(
                "cube_r2",
                CubeListBuilder.create()
                        .texOffs(17, 20).addBox(-3, 0, -0.5f, 6, 6, 1, new CubeDeformation(0)),
                PartPose.offsetAndRotation(0, -7.5f, -3, 1.5708f, 0, 0)
        );

        original.addOrReplaceChild(
                "cube_r3",
                CubeListBuilder.create()
                        .texOffs(17, 20).addBox(0, -3, -0.5f, 6, 6, 1, new CubeDeformation(0)),
                PartPose.offsetAndRotation(-3.5f, -4, -3, 0, -1.5708f, 0)
        );

        original.addOrReplaceChild(
                "cube_r4",
                CubeListBuilder.create()
                        .texOffs(17, 20).addBox(0, -3, -0.5f, 6, 6, 1, new CubeDeformation(0)),
                PartPose.offsetAndRotation(3.5f, -4, -3, 0, -1.5708f, 0)
        );

        main.addOrReplaceChild(
                "past1",
                CubeListBuilder.create()
                        .texOffs(22, 4).addBox(3, -8, 3, 1, 8, 1, new CubeDeformation(0))
                        .texOffs(22, 4).addBox(-4, -8, 3, 1, 8, 1, new CubeDeformation(0))
                        .texOffs(17, 7).addBox(-3, -8, 3, 6, 1, 1, new CubeDeformation(0))
                        .texOffs(17, 7).addBox(-3, -1, 3, 6, 1, 1, new CubeDeformation(0))
                        .texOffs(21, 6).addBox(-4, -8, 1, 1, 1, 2, new CubeDeformation(0))
                        .texOffs(21, 6).addBox(-4, -1, 1, 1, 1, 2, new CubeDeformation(0))
                        .texOffs(20, 5).addBox(3, -8, 1, 1, 1, 2, new CubeDeformation(0))
                        .texOffs(21, 6).addBox(3, -1, 1, 1, 1, 2, new CubeDeformation(0))
                        .texOffs(22, 20).addBox(-4, -7, 1, 1, 6, 2, new CubeDeformation(0))
                        .texOffs(22, 20).addBox(3, -7, 1, 1, 6, 2, new CubeDeformation(0))
                        .texOffs(16, 22).addBox(-3, -8, 1, 6, 1, 2, new CubeDeformation(0))
                        .texOffs(16, 22).addBox(-3, -1, 1, 6, 1, 2, new CubeDeformation(0)),
                PartPose.offset(0, 0, 3)
        );

        main.addOrReplaceChild(
                "past2",
                CubeListBuilder.create()
                        .texOffs(6, 20).addBox(3, -8, 3, 1, 8, 1, new CubeDeformation(0))
                        .texOffs(6, 20).addBox(-4, -8, 3, 1, 8, 1, new CubeDeformation(0))
                        .texOffs(1, 23).addBox(-3, -8, 3, 6, 1, 1, new CubeDeformation(0))
                        .texOffs(1, 23).addBox(-3, -1, 3, 6, 1, 1, new CubeDeformation(0))
                        .texOffs(5, 22).addBox(-4, -8, 1, 1, 1, 2, new CubeDeformation(0))
                        .texOffs(5, 22).addBox(-4, -1, 1, 1, 1, 2, new CubeDeformation(0))
                        .texOffs(4, 21).addBox(3, -8, 1, 1, 1, 2, new CubeDeformation(0))
                        .texOffs(5, 22).addBox(3, -1, 1, 1, 1, 2, new CubeDeformation(0))
                        .texOffs(22, 20).addBox(-4, -7, 1, 1, 6, 2, new CubeDeformation(0))
                        .texOffs(22, 20).addBox(3, -7, 1, 1, 6, 2, new CubeDeformation(0))
                        .texOffs(16, 22).addBox(-3, -8, 1, 6, 1, 2, new CubeDeformation(0))
                        .texOffs(16, 22).addBox(-3, -1, 1, 6, 1, 2, new CubeDeformation(0))
                        .texOffs(17, 21).addBox(-3, -7, 3, 6, 6, 1, new CubeDeformation(0)),
                PartPose.offset(0, 0, 6)
        );

        return LayerDefinition.create(mesh, 32, 32);
    }

    @Override
    public void setupAnim(
            T entity,
            float limbSwing,
            float limbSwingAmount,
            float ageInTicks,
            float netHeadYaw,
            float headPitch
    ) {
    }

    @Override
    public void renderToBuffer(
            PoseStack pose,
            VertexConsumer consumer,
            int light,
            int overlay,
            int color
    ) {
        main.render(pose, consumer, light, overlay, color);
    }
}