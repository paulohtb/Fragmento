package com.pgalaxyp.fragmento.NEW;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class NewDrumProjectileModel<T extends Entity> extends EntityModel<T> {

    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(
                    ResourceLocation.fromNamespaceAndPath("modid", "drum_projectile"),
                    "main"
            );

    private final ModelPart main;
    private final ModelPart original;
    private final ModelPart past1;
    private final ModelPart past2;
    private final ModelPart e1;
    private final ModelPart e2;
    private final ModelPart e3;
    private final ModelPart e4;
    private final ModelPart e5;
    private final ModelPart e6;
    private final ModelPart e7;
    private final ModelPart e8;
    private final ModelPart e9;
    private final ModelPart e10;
    private final ModelPart e11;
    private final ModelPart e12;

    public NewDrumProjectileModel(ModelPart root) {
        this.main     = root.getChild("main");
        this.original = this.main.getChild("original");
        this.past1    = this.main.getChild("past1");
        this.past2    = this.main.getChild("past2");

        this.e1 = this.original.getChild("orig1");
        this.e2 = this.original.getChild("orig2");
        this.e3 = this.original.getChild("orig3");
        this.e4 = this.original.getChild("orig4");
        this.e5 = this.original.getChild("orig5");
        this.e6 = this.original.getChild("orig6");
        this.e7 = this.original.getChild("orig7");
        this.e8 = this.original.getChild("orig8");
        this.e9 = this.original.getChild("orig9");
        this.e10 = this.original.getChild("orig10");
        this.e11 = this.original.getChild("orig11");
        this.e12 = this.original.getChild("orig12");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        PartDefinition main = root.addOrReplaceChild("main", CubeListBuilder.create(), PartPose.ZERO);

        PartDefinition original = main.addOrReplaceChild("original", CubeListBuilder.create(), PartPose.ZERO);
        NewModelPartUtil.addCube(original, "orig1", 3, -4, 3, 1, 8, 1, 0, 0);
        NewModelPartUtil.addCube(original, "orig2", -4, -4, 3, 1, 8, 1, 0, 0);
        NewModelPartUtil.addCube(original, "orig3", 3, -4, -4, 1, 8, 1, 0, 0);
        NewModelPartUtil.addCube(original, "orig4", -4, -4, -4, 1, 8, 1, 0, 0);
        NewModelPartUtil.addCube(original, "orig5", -3, 3, 3, 6, 1, 1, 0, 0);
        NewModelPartUtil.addCube(original, "orig6", -3, -4, 3, 6, 1, 1, 0, 0);
        NewModelPartUtil.addCube(original, "orig7", 3, 3, -3, 1, 1, 6, 0, 0);
        NewModelPartUtil.addCube(original, "orig8", 3, -4, -3, 1, 1, 6, 0, 0);
        NewModelPartUtil.addCube(original, "orig9", -4, 3, -3, 1, 1, 6, 0, 0);
        NewModelPartUtil.addCube(original, "orig10", -4, -4, -3, 1, 1, 6, 0, 0);
        NewModelPartUtil.addCube(original, "orig11", -3, 3, -4, 6, 1, 1, 0, 0);
        NewModelPartUtil.addCube(original, "orig12", -3, -4, -4, 6, 1, 1, 0, 0);

        NewModelPartUtil.addCube(original, "orig13", -3.001F, -3.001F, 3, 6, 6, 1, 34, 41);
        NewModelPartUtil.addCube(original, "orig14", 3, -3.001F, -3.001F, 1, 6, 6, 34, 36);
        NewModelPartUtil.addCube(original, "orig15", -4, -3.001F, -3.001F, 1, 6, 6, 34, 36);
        NewModelPartUtil.addCube(original, "orig16", -3.001F, 3, -3.001F, 6, 1, 6, 24, 41);
        NewModelPartUtil.addCube(original, "orig17", -3.001F, -4, -3.001F, 6, 1, 6, 24, 41);

        PartDefinition past1 = main.addOrReplaceChild("past1", CubeListBuilder.create(), PartPose.ZERO);
        NewModelPartUtil.addCube(past1, "p1_1", 3, -4, -7, 1, 8, 1, 44, 0);
        NewModelPartUtil.addCube(past1, "p1_2", -4, -4, -7, 1, 8, 1, 44, 0);
        NewModelPartUtil.addCube(past1, "p1_3", 3, 3, -6, 1, 1, 2, 42, 0);
        NewModelPartUtil.addCube(past1, "p1_4", 3, -4, -6, 1, 1, 2, 42, 0);
        NewModelPartUtil.addCube(past1, "p1_5", -4, 3, -6, 1, 1, 2, 42, 0);
        NewModelPartUtil.addCube(past1, "p1_6", -4, -4, -6, 1, 1, 2, 42, 0);
        NewModelPartUtil.addCube(past1, "p1_7", -3, 3, -7, 6, 1, 1, 34, 0);
        NewModelPartUtil.addCube(past1, "p1_8", -3, -4, -7, 6, 1, 1, 34, 0);
        NewModelPartUtil.addCube(past1, "p1_9", 3, -3.001F, -6.001F, 1, 6, 2, 42, 40);
        NewModelPartUtil.addCube(past1, "p1_10", -4, -3.001F, -6.001F, 1, 6, 2, 42, 40);
        NewModelPartUtil.addCube(past1, "p1_11", -3.001F, 3, -6.001F, 6, 1, 2, 32, 45);
        NewModelPartUtil.addCube(past1, "p1_12", -3.001F, -4, -6.001F, 6, 1, 2, 32, 45);

        PartDefinition past2 = main.addOrReplaceChild("past2", CubeListBuilder.create(), PartPose.ZERO);
        NewModelPartUtil.addCube(past2, "p2_1", 3, -4, -10, 1, 8, 1, 0, 39);
        NewModelPartUtil.addCube(past2, "p2_2", -4, -4, -10, 1, 8, 1, 0, 39);
        NewModelPartUtil.addCube(past2, "p2_3", 3, 3, -9, 1, 1, 2, 0, 45);
        NewModelPartUtil.addCube(past2, "p2_4", 3, -4, -9, 1, 1, 2, 0, 45);
        NewModelPartUtil.addCube(past2, "p2_5", -4, 3, -9, 1, 1, 2, 0, 45);
        NewModelPartUtil.addCube(past2, "p2_6", -4, -4, -9, 1, 1, 2, 0, 45);
        NewModelPartUtil.addCube(past2, "p2_7", -3, 3, -10, 6, 1, 1, 0, 46);
        NewModelPartUtil.addCube(past2, "p2_8", -3, -4, -10, 6, 1, 1, 0, 46);
        NewModelPartUtil.addCube(past2, "p2_9", 3, -3.001F, -9.001F, 1, 6, 2, 42, 40);
        NewModelPartUtil.addCube(past2, "p2_10", -4, -3.001F, -9.001F, 1, 6, 2, 42, 40);
        NewModelPartUtil.addCube(past2, "p2_11", -3.001F, 3, -9.001F, 6, 1, 2, 32, 45);
        NewModelPartUtil.addCube(past2, "p2_12", -3.001F, -4, -9.001F, 6, 1, 2, 32, 45);
        NewModelPartUtil.addCube(past2, "p2_13", -3.001F, -3.001F, -10, 6, 6, 1, 34, 41);

        return LayerDefinition.create(mesh, 64, 64);
    }

    public void applyTickVisibility(int tick) {
        this.past1.visible = tick >= 3;
        this.past2.visible = tick >= 4;
    }

    @Override
    public void renderToBuffer(
            PoseStack pose, VertexConsumer consumer,
            int light, int overlay, int color
    ) {
        this.original.render(pose, consumer, light, overlay, color);
        this.past1.render(pose, consumer, light, overlay, color);
        this.past2.render(pose, consumer, light, overlay, color);

        int full = 0xF000F0;

        e1.render(pose, consumer, full, overlay, color);
        e2.render(pose, consumer, full, overlay, color);
        e3.render(pose, consumer, full, overlay, color);
        e4.render(pose, consumer, full, overlay, color);
        e5.render(pose, consumer, full, overlay, color);
        e6.render(pose, consumer, full, overlay, color);
        e7.render(pose, consumer, full, overlay, color);
        e8.render(pose, consumer, full, overlay, color);
        e9.render(pose, consumer, full, overlay, color);
        e10.render(pose, consumer, full, overlay, color);
        e11.render(pose, consumer, full, overlay, color);
        e12.render(pose, consumer, full, overlay, color);
    }

    @Override
    public void setupAnim(
            T ent, float limbSwing, float limbSwingAmount,
            float ageInTicks, float netHeadYaw, float headPitch
    ) {}
}