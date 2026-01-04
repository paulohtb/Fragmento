package com.pgalaxyp.fragmento.combat.content.catalyst;

import com.pgalaxyp.fragmento.bootstrap.logging.FragmentoLog;
import com.pgalaxyp.fragmento.combat.content.skill.FragmentoSkills;
import com.pgalaxyp.fragmento.combat.domain.id.CatalystFamilyId;
import com.pgalaxyp.fragmento.combat.domain.id.SkillId;
import com.pgalaxyp.fragmento.combat.domain.input.SkillSlotId;
import com.pgalaxyp.fragmento.combat.domain.timing.CombatTime;
import com.pgalaxyp.fragmento.combat.engine.profile.CombatEffect;
import com.pgalaxyp.fragmento.combat.engine.profile.CombatProfile;
import com.pgalaxyp.fragmento.combat.engine.profile.SpawnCutEffect;
import com.pgalaxyp.fragmento.combat.engine.registry.CatalystDefinition;
import com.pgalaxyp.fragmento.combat.engine.registry.FragmentoCombatRegistries;
import com.pgalaxyp.fragmento.combat.engine.targeting.AimResolver;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class FragmentoCatalystDefinitions {

    private static final SkillSlotId NORMAL_SLOT = new SkillSlotId(1);
    private static final SkillSlotId SPECIAL_SLOT = new SkillSlotId(2);

    private static final TagKey<Item> CATALYSTS_TAG =
            TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("fragmento", "catalysts"));

    public static void registerAll() {
        registerBardoFlute();
        registerDefaultTag();
    }

    private static void registerBardoFlute() {
        Map<SkillSlotId, SkillId> skills = new HashMap<>();
        skills.put(NORMAL_SLOT, FragmentoSkills.BARDO_NORMAL_INFUSED);
        skills.put(SPECIAL_SLOT, FragmentoSkills.BARDO_SPECIAL_CASTED);

        CombatProfile profile = new CombatProfile() {

            private final AimResolver aim = new AimResolver();

            @Override
            public List<CombatEffect> onComboStep(ServerPlayer player, int stepIndex, CombatTime now) {
                AimResolver.Aim a = aim.resolve(player, 20.0, 5.0);

                LivingEntity target = a.target();
                Vec3 targetPoint = target != null ? target.getBoundingBox().getCenter() : a.point();

                Vec3 forward = player.getLookAngle().normalize();
                Vec3 right = forward.cross(new Vec3(0.0, 1.0, 0.0)).normalize();

                Vec3 spawn;

                if (stepIndex == 1) {
                    double d = target != null ? target.getBbWidth() * 0.5 + 4.0 : 4.0;
                    spawn = targetPoint.add(right.scale(d));
                } else if (stepIndex == 2) {
                    double d = target != null ? target.getBbWidth() * 0.5 + 4.0 : 4.0;
                    spawn = targetPoint.subtract(right.scale(d));
                } else {
                    double y = target != null ? target.getBoundingBox().maxY + 4.0 : targetPoint.y + 4.0;
                    spawn = new Vec3(targetPoint.x, y, targetPoint.z);
                }

                SpawnCutEffect cut = new SpawnCutEffect(
                        player.getUUID(),
                        target != null ? target.getUUID() : null,
                        spawn.x,
                        spawn.y,
                        spawn.z,
                        targetPoint.x,
                        targetPoint.y,
                        targetPoint.z,
                        15,
                        switch (stepIndex) {
                            case 1 -> 3.0f;
                            case 2 -> 4.0f;
                            default -> 5.0f;
                        },
                        false
                );

                FragmentoLog.combat(
                        "bardo combo cut spawn, player.uuid={} step={} target={}",
                        player.getUUID(),
                        stepIndex,
                        target != null ? target.getUUID() : null
                );

                return List.of(cut);
            }

            @Override
            public List<CombatEffect> onInfusedExecute(ServerPlayer player, SkillId skillId, CombatTime now) {
                return List.of();
            }

            @Override
            public List<CombatEffect> onCastFinish(ServerPlayer player, SkillId skillId, CombatTime now) {
                return List.of();
            }
        };

        FragmentoCombatRegistries.catalysts().register(
                new CatalystDefinition(
                        new CatalystFamilyId("bardo"),
                        FluteItem::isFlute,
                        skills,
                        profile
                )
        );
    }

    private static void registerDefaultTag() {
        FragmentoCombatRegistries.catalysts().register(
                new CatalystDefinition(
                        new CatalystFamilyId("default"),
                        stack -> stack != null && !stack.isEmpty() && stack.is(CATALYSTS_TAG),
                        Map.of(),
                        CombatProfile.NOOP
                )
        );
    }

    private FragmentoCatalystDefinitions() {}
}