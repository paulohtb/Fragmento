package com.pgalaxyp.fragmento.combat.old.system.gameplay.cooldown;

import com.pgalaxyp.fragmento.combat.old.system.skill.SkillSlot;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class PlayerSkillCooldownSavedData extends SavedData {

    private static final String NAME = "fragmento_skill_cooldowns";

    private final Map<UUID, EnumMap<SkillSlot, Long>> data = new HashMap<>();

    public static PlayerSkillCooldownSavedData get(MinecraftServer server) {
        if (server == null) throw new IllegalStateException("Server nulo");
        ServerLevel overworld = server.getLevel(Level.OVERWORLD);
        if (overworld == null) throw new IllegalStateException("Overworld indisponível");

        return overworld.getDataStorage().computeIfAbsent(
                new SavedData.Factory<>(
                        PlayerSkillCooldownSavedData::new,
                        PlayerSkillCooldownSavedData::load
                ),
                NAME
        );
    }

    public boolean isOnCooldown(UUID playerId, SkillSlot slot, long now) {
        EnumMap<SkillSlot, Long> map = data.get(playerId);
        if (map == null) return false;

        Long end = map.get(slot);
        return end != null && end > now;
    }

    public int remainingTicks(UUID playerId, SkillSlot slot, long now) {
        EnumMap<SkillSlot, Long> map = data.get(playerId);
        if (map == null) return 0;

        Long end = map.get(slot);
        if (end == null) return 0;

        long rem = end - now;
        return rem > 0 ? (int) Math.min(Integer.MAX_VALUE, rem) : 0;
    }

    public void apply(UUID playerId, SkillSlot slot, long endGameTime) {
        data.computeIfAbsent(playerId, k -> new EnumMap<>(SkillSlot.class))
                .put(slot, endGameTime);
        setDirty();
    }

    public static PlayerSkillCooldownSavedData load(
            CompoundTag tag,
            HolderLookup.Provider provider
    ) {
        PlayerSkillCooldownSavedData data = new PlayerSkillCooldownSavedData();
        CompoundTag players = tag.getCompound("Players");

        for (String key : players.getAllKeys()) {
            UUID id = UUID.fromString(key);
            CompoundTag slots = players.getCompound(key);

            EnumMap<SkillSlot, Long> map = new EnumMap<>(SkillSlot.class);
            for (SkillSlot s : SkillSlot.values()) {
                if (slots.contains(s.name())) {
                    map.put(s, slots.getLong(s.name()));
                }
            }
            data.data.put(id, map);
        }

        return data;
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider provider) {
        CompoundTag players = new CompoundTag();

        for (var e : data.entrySet()) {
            CompoundTag slots = new CompoundTag();
            for (var s : e.getValue().entrySet()) {
                slots.putLong(s.getKey().name(), s.getValue());
            }
            players.put(e.getKey().toString(), slots);
        }

        tag.put("Players", players);
        return tag;
    }
}