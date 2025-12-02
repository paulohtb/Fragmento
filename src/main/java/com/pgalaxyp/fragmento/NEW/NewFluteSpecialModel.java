package com.pgalaxyp.fragmento.NEW;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class NewFluteSpecialModel<T extends Entity> extends EntityModel<T> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath("modid", "new_flute_special"), "main");
    private final ModelPart main;
    private final ModelPart animation;
    private final ModelPart air1;
    private final ModelPart air2;
    private final ModelPart air3;
    private final ModelPart air4;
    private final ModelPart air5;
    private final ModelPart air6;
    private final ModelPart air7;
    private final ModelPart air8;

    public NewFluteSpecialModel(ModelPart root) {
        this.main = root.getChild("main");
        this.animation = root.getChild("animation");
        this.air1 = this.animation.getChild("air1");
        this.air2 = this.animation.getChild("air2");
        this.air3 = this.animation.getChild("air3");
        this.air4 = this.animation.getChild("air4");
        this.air5 = this.animation.getChild("air5");
        this.air6 = this.animation.getChild("air6");
        this.air7 = this.animation.getChild("air7");
        this.air8 = this.animation.getChild("air8");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition main = partdefinition.addOrReplaceChild("main", CubeListBuilder.create().texOffs(-16, 0).addBox(-8.0F, 0.0F, -8.0F, 16.0F, 0.1F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition animation = partdefinition.addOrReplaceChild("animation", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition air1 = animation.addOrReplaceChild("air1", CubeListBuilder.create().texOffs(-1, 31).addBox(-0.5F, -2.0F, -2.0F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition air2 = animation.addOrReplaceChild("air2", CubeListBuilder.create().texOffs(-1, 31).addBox(-0.5F, -2.0F, -2.0F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition air3 = animation.addOrReplaceChild("air3", CubeListBuilder.create().texOffs(-1, 31).addBox(-0.5F, -2.0F, -2.0F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

        PartDefinition air4 = animation.addOrReplaceChild("air4", CubeListBuilder.create().texOffs(-1, 31).addBox(1.0F, -2.0F, -0.5F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition air5 = animation.addOrReplaceChild("air5", CubeListBuilder.create().texOffs(-1, 31).addBox(-2.0F, -2.0F, -0.5F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition air6 = animation.addOrReplaceChild("air6", CubeListBuilder.create().texOffs(-1, 31).addBox(-0.5F, -2.0F, 1.0F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

        PartDefinition air7 = animation.addOrReplaceChild("air7", CubeListBuilder.create().texOffs(-1, 31).addBox(-0.5F, -2.0F, 1.0F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition air8 = animation.addOrReplaceChild("air8", CubeListBuilder.create().texOffs(-1, 31).addBox(-0.5F, -2.0F, 1.0F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        main.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
//        animation.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
    }
}
