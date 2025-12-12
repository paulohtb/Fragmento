package com.pgalaxyp.fragmento.features.bard_class.client.render.model;

import com.pgalaxyp.fragmento.features.bard_class.spirit.type.MinorWindVortex;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class MinorWindVortexGeoModel extends DefaultedEntityGeoModel<MinorWindVortex> {

    public MinorWindVortexGeoModel() {
        super(ResourceLocation.fromNamespaceAndPath("fragmento", "wind_vortex"), true);
    }
}
