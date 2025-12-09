package com.pgalaxyp.fragmento.features.bard_class.client.render.model;

import com.pgalaxyp.fragmento.features.bard_class.spirit.type.WindVortex;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class WindVortexGeoModel extends DefaultedEntityGeoModel<WindVortex> {

    public WindVortexGeoModel() {
        super(ResourceLocation.fromNamespaceAndPath("fragmento", "wind_vortex"), true);
    }
}
