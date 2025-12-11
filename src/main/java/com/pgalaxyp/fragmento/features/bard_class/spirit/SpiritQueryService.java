package com.pgalaxyp.fragmento.features.bard_class.spirit;

import com.pgalaxyp.fragmento.core.debug.ModLogger;
import com.pgalaxyp.fragmento.features.bard_class.spirit.behavior.FluteSpecialBehavior;
import com.pgalaxyp.fragmento.features.bard_class.spirit.behavior.SpiritBehavior;
import com.pgalaxyp.fragmento.features.bard_class.spirit.type.FluteSpirit;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

public final class SpiritQueryService {

    private SpiritQueryService() {
    }

    public static void interruptSpecialSpirits(ServerPlayer player) {
        if (!(player.level() instanceof ServerLevel level)) {
            return;
        }

        level.getEntities(
                player,
                player.getBoundingBox().inflate(64.0),
                e -> e instanceof FluteSpirit fs && fs.getOwner() == player && isInterruptible(fs)
        ).forEach(e -> {
            ModLogger.state(e, "INTERRUPTED_SPECIAL_SPIRIT");
            e.discard();
        });
    }

    private static boolean isInterruptible(FluteSpirit fs) {
        SpiritBehavior behavior = fs.getBehavior();
        if (behavior instanceof FluteSpecialBehavior special) {
            return special.isInterruptible();
        }
        return false;
    }
}
