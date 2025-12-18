package com.pgalaxyp.fragmento.content.bard.module;

import com.pgalaxyp.fragmento.client.render.SpiritClientSetup;
import com.pgalaxyp.fragmento.content.bard.catalyst.BardCatalystItem;
import com.pgalaxyp.fragmento.content.bard.channel.BardChannelAdapter;
import com.pgalaxyp.fragmento.content.bard.registry.BardRegistries;
import com.pgalaxyp.fragmento.content.bard.registry.FluteSkillEntityRegistry;
import com.pgalaxyp.fragmento.content.bard.skill.BardSkillActionHandler;
import com.pgalaxyp.fragmento.system.channel.ChannelingService;
import com.pgalaxyp.fragmento.system.skill.SkillActionRegistry;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.loading.FMLEnvironment;

public final class BardModule {

    private BardModule() {
    }

    public static void init(IEventBus modBus) {
        FluteSkillEntityRegistry.ENTITIES.register(modBus);

        BardRegistries.registerAll();
        BardRegistries.ITEMS.register(modBus);

        ChannelingService.registerAdapter(new BardChannelAdapter());

        SkillActionRegistry.registerForItemClass(
                BardCatalystItem.class,
                new BardSkillActionHandler()
        );

        if (FMLEnvironment.dist == Dist.CLIENT) {
            SpiritClientSetup.init(modBus);
        }
    }
}