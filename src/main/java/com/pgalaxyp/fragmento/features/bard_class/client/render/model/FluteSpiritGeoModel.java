package com.pgalaxyp.fragmento.features.bard_class.client.render.model;

import com.pgalaxyp.fragmento.features.bard_class.spirit.type.FluteSpirit;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class FluteSpiritGeoModel extends DefaultedEntityGeoModel<FluteSpirit> {

    public FluteSpiritGeoModel() {
        super(ResourceLocation.fromNamespaceAndPath("fragmento", "flute_spirit"), true);
    }
}
