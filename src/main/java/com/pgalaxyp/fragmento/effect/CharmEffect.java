package com.pgalaxyp.fragmento.effect;

import com.pgalaxyp.fragmento.Fragmento;
import com.pgalaxyp.fragmento.registry.EffectsRegistry;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingChangeTargetEvent;
import java.util.Comparator;
import java.util.List;

@EventBusSubscriber(modid = Fragmento.MODID)
public class CharmEffect extends MobEffect {

    public CharmEffect() {
        super(MobEffectCategory.NEUTRAL, 0xFF88CC);
    }

    @SubscribeEvent
    public static void onLivingChangeTarget(LivingChangeTargetEvent event) {
        if (event.getEntity().level().isClientSide()) return;
        if (!(event.getEntity() instanceof Mob mob)) return;
        if (!mob.hasEffect(EffectsRegistry.CHARMED)) return;

        LivingEntity newTarget = event.getNewAboutToBeSetTarget();

        if (newTarget instanceof Player) {
            LivingEntity substituteTarget = findHostileTargetNearby(mob);
            event.setNewAboutToBeSetTarget(substituteTarget);
        }
    }

    private static LivingEntity findHostileTargetNearby(Mob mob) {
        AABB box = mob.getBoundingBox().inflate(8);
        List<Mob> mobs = mob.level().getEntitiesOfClass(Mob.class, box,
                e -> e != mob && e instanceof Monster && e.isAlive());

        if (mobs.isEmpty()) return null;

        mobs.sort(Comparator.comparingDouble(mob::distanceToSqr));
        return mobs.getFirst();
    }
}