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
            public List<CombatEffect> onInfusedExecute(ServerPlayer player, SkillId skillId, CombatTime now) {
                if (skillId == null || skillId.value() != FragmentoSkills.BARDO_NORMAL_INFUSED.value()) {
                    return List.of();
                }

                AimResolver.Aim a = aim.resolve(player, 6.0, 4.0);

                Vec3 eye = player.getEyePosition();
                Vec3 spawn = eye.add(player.getLookAngle().scale(0.8));

                SpawnCutEffect cut = new SpawnCutEffect(
                        player.getUUID(),
                        a.target() != null ? a.target().getUUID() : null,
                        spawn.x, spawn.y, spawn.z,
                        a.aimPoint().x, a.aimPoint().y, a.aimPoint().z,
                        8,
                        4.0f,
                        false
                );

                FragmentoLog.combat(
                        "bardo infused execute, player.uuid={} target={} aimKind={}",
                        player.getUUID(),
                        a.target() != null ? a.target().getUUID() : null,
                        a.kind()
                );

                return List.of(cut);
            }

            @Override
            public List<CombatEffect> onComboStep(ServerPlayer player, int stepIndex, CombatTime now) {
                return List.of();
            }

            @Override
            public List<CombatEffect> onCastFinish(ServerPlayer player, SkillId skillId, CombatTime now) {
                return List.of();
            }
        };

        CatalystDefinition def = new CatalystDefinition(
                new CatalystFamilyId("bardo"),
                FluteItem::isFlute,
                skills,
                profile
        );

        FragmentoCombatRegistries.catalysts().register(def);
    }

    private static void registerDefaultTag() {
        CatalystDefinition def = new CatalystDefinition(
                new CatalystFamilyId("default"),
                stack -> stack != null && !stack.isEmpty() && stack.is(CATALYSTS_TAG),
                Map.of(),
                CombatProfile.NOOP
        );

        FragmentoCombatRegistries.catalysts().register(def);
    }

    private FragmentoCatalystDefinitions() {}
}