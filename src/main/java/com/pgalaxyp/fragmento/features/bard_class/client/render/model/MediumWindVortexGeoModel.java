package com.pgalaxyp.fragmento.features.bard_class.client.render.model;

import com.pgalaxyp.fragmento.features.bard_class.spirit.type.MediumWindVortex;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class MediumWindVortexGeoModel extends DefaultedEntityGeoModel<MediumWindVortex> {

    public MediumWindVortexGeoModel() {
        super(ResourceLocation.fromNamespaceAndPath("fragmento", "wind_vortex"), true);
    }
}
