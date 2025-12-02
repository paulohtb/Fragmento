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
import java.util.ArrayList;
import java.util.List;

public class NewSoundWaveModel<T extends Entity> extends EntityModel<T> {

    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(
                    ResourceLocation.fromNamespaceAndPath("modid", "sound_wave"),
                    "main");

    private final ModelPart main;
    private final List<ModelPart> parts;
    private final ModelPart core1;
    private final ModelPart core2;
    private final ModelPart core3;
    private final ModelPart core4;

    public NewSoundWaveModel(ModelPart root) {
        this.main = root.getChild("main");

        this.core1 = main.getChild("p3");
        this.core2 = main.getChild("p6");
        this.core3 = main.getChild("p12");
        this.core4 = main.getChild("p18");

        List<ModelPart> list = new ArrayList<>();

        main.getAllParts().forEach(part -> {
            if (part != main) list.add(part);
        });

        this.parts = list;
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition main = partdefinition.addOrReplaceChild("main", CubeListBuilder.create(), PartPose.ZERO);

        NewModelPartUtil.addCube(main, "p1", 5, -3, 0, 1, 1, 1, 0, 0);
        NewModelPartUtil.addCube(main, "p2", 4, -4, 0, 1, 2, 1, 0, 0);
        NewModelPartUtil.addCube(main, "p3", 5, -2, 0, 1, 4, 1, 0, 0);
        NewModelPartUtil.addCube(main, "p4", 5,  2, 0, 1, 1, 1, 0, 0);
        NewModelPartUtil.addCube(main, "p5", 4,  2, 0, 1, 2, 1, 0, 0);
        NewModelPartUtil.addCube(main, "p6", -6, -2, 0, 1, 4, 1, 0, 0);
        NewModelPartUtil.addCube(main, "p7", -5, -4, 0, 1, 2, 1, 0, 0);
        NewModelPartUtil.addCube(main, "p8", -6, -3, 0, 1, 1, 1, 0, 0);
        NewModelPartUtil.addCube(main, "p9", -5, -5, 0, 3, 1, 1, 0, 0);
        NewModelPartUtil.addCube(main, "p10", -6, 2, 0, 1, 1, 1, 0, 0);
        NewModelPartUtil.addCube(main, "p11", -5, 2, 0, 1, 2, 1, 0, 0);
        NewModelPartUtil.addCube(main, "p12", -2, -6, 0, 4, 1, 1, 0, 0);
        NewModelPartUtil.addCube(main, "p13", -3, -6, 0, 1, 1, 1, 0, 0);
        NewModelPartUtil.addCube(main, "p14",  2, -6, 0, 1, 1, 1, 0, 0);
        NewModelPartUtil.addCube(main, "p15",  2, -5, 0, 3, 1, 1, 0, 0);
        NewModelPartUtil.addCube(main, "p16", -3, 5, 0, 1, 1, 1, 0, 0);
        NewModelPartUtil.addCube(main, "p17", -5, 4, 0, 3, 1, 1, 0, 0);
        NewModelPartUtil.addCube(main, "p18", -2, 5, 0, 4, 1, 1, 0, 0);
        NewModelPartUtil.addCube(main, "p19",  2, 5, 0, 1, 1, 1, 0, 0);
        NewModelPartUtil.addCube(main, "p20",  2, 4, 0, 3, 1, 1, 0, 0);

        return LayerDefinition.create(meshdefinition, 12, 8);
    }

    public void applyStageVisibility(int stage) {
        boolean restrict = stage > 3;

        for (ModelPart part : parts) {
            boolean hide =
                    part == core1 ||
                    part == core2 ||
                    part == core3 ||
                    part == core4;

            part.visible = !restrict || !hide;
        }
    }

    @Override
    public void renderToBuffer(
            PoseStack pose, VertexConsumer consumer,
            int light, int overlay, int color
    ) {
        main.render(pose, consumer, light, overlay, color);
    }

    @Override
    public void setupAnim(
            T entity, float limbSwing, float limbSwingAmount,
            float ageInTicks, float netHeadYaw, float headPitch
    ) {}
}
