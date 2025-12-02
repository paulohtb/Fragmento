package com.pgalaxyp.fragmento.NEW;

import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.PartDefinition;

public final class NewModelPartUtil {

    private NewModelPartUtil() {}

    public static void addCube(
            PartDefinition parent,
            String name,
            float x, float y, float z,
            float sx, float sy, float sz,
            int u, int v
    ) {
        parent.addOrReplaceChild(
                name,
                CubeListBuilder.create()
                        .texOffs(u, v)
                        .addBox(x, y, z, sx, sy, sz, new CubeDeformation(0)),
                PartPose.ZERO
        );
    }
}
