package com.pgalaxyp.fragmento.cosmetics.internal;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.logging.LogUtils;
import com.pgalaxyp.fragmento.cosmetics.CosmeticsKeys;
import com.pgalaxyp.fragmento.cosmetics.api.CosmeticDefinition;
import com.pgalaxyp.fragmento.cosmetics.api.CosmeticId;
import com.pgalaxyp.fragmento.cosmetics.api.CosmeticSlot;
import com.pgalaxyp.fragmento.cosmetics.api.CosmeticTier;
import com.pgalaxyp.fragmento.cosmetics.api.CosmeticTypeId;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import org.slf4j.Logger;

public final class CosmeticDataLoader extends SimplePreparableReloadListener<CosmeticDefinitionsSnapshot> {

    private static final Logger LOGGER = LogUtils.getLogger();

    private static final ResourceLocation BUILTIN_TEST_ID = ResourceLocation.fromNamespaceAndPath(CosmeticsKeys.MOD_ID, "test_cube");
    private static final ResourceLocation BUILTIN_TEST_TYPE = ResourceLocation.fromNamespaceAndPath(CosmeticsKeys.MOD_ID, "builtin");

    private final CosmeticRegistryImpl registry;
    private int nextDataVersion;

    public CosmeticDataLoader(CosmeticRegistryImpl registry) {
        this.registry = Objects.requireNonNull(registry, "registry");
        this.nextDataVersion = 1;
        LOGGER.info("CosmeticDataLoader iniciado");
    }

    @Override
    protected CosmeticDefinitionsSnapshot prepare(ResourceManager resourceManager, ProfilerFiller profiler) {
        Objects.requireNonNull(resourceManager, "resourceManager");

        Map<ResourceLocation, Resource> resources = resourceManager.listResources(
            "cosmetics",
            new Predicate<ResourceLocation>() {
                @Override
                public boolean test(ResourceLocation id) {
                    return id != null && id.getPath().endsWith(".json");
                }
            }
        );

        LOGGER.info("CosmeticDataLoader prepare encontrados {}", Integer.valueOf(resources.size()));

        HashMap<CosmeticId, CosmeticDefinition> byId = new HashMap<>(resources.size());
        EnumMap<CosmeticSlot, List<CosmeticDefinition>> bySlot = new EnumMap<>(CosmeticSlot.class);
        for (CosmeticSlot slot : CosmeticSlot.values()) {
            bySlot.put(slot, new ArrayList<>());
        }

        int loaded = 0;
        for (Map.Entry<ResourceLocation, Resource> e : resources.entrySet()) {
            ResourceLocation fileId = e.getKey();
            Resource res = e.getValue();
            JsonObject obj = readJsonObject(fileId, res);
            if (obj == null) continue;

            CosmeticDefinition def = parseDefinition(fileId, obj);
            if (def == null) continue;

            if (byId.containsKey(def.id())) {
                LOGGER.warn("CosmeticDataLoader duplicado id {} arquivo {}", def.id(), fileId);
                continue;
            }

            byId.put(def.id(), def);
            List<CosmeticDefinition> list = bySlot.get(def.slot());
            if (list != null) {
                list.add(def);
            } else {
                LOGGER.warn("CosmeticDataLoader slot invalido na tabela slot {} cosmetic {}", def.slot().name(), def.id());
            }
            loaded++;
        }

        CosmeticDefinition builtin = new CosmeticDefinition(
            CosmeticId.of(BUILTIN_TEST_ID),
            CosmeticTypeId.of(BUILTIN_TEST_TYPE),
            CosmeticSlot.HEAD,
            CosmeticTier.TIER_0,
            0,
            true
        );

        if (!byId.containsKey(builtin.id())) {
            byId.put(builtin.id(), builtin);
            List<CosmeticDefinition> list = bySlot.get(builtin.slot());
            if (list != null) {
                list.add(builtin);
            }
            loaded++;
        }

        int version = nextDataVersion++;
        LOGGER.info("CosmeticDataLoader prepare pronto carregados {} version {}", Integer.valueOf(loaded), Integer.valueOf(version));
        return new CosmeticDefinitionsSnapshot(byId, bySlot, version);
    }

    @Override
    protected void apply(CosmeticDefinitionsSnapshot snapshot, ResourceManager resourceManager, ProfilerFiller profiler) {
        Objects.requireNonNull(snapshot, "snapshot");
        registry.setSnapshot(snapshot);
        LOGGER.info("CosmeticDataLoader apply version {}", Integer.valueOf(snapshot.dataVersion()));
    }

    private static JsonObject readJsonObject(ResourceLocation fileId, Resource res) {
        try (BufferedReader reader = res.openAsReader()) {
            JsonElement element = JsonParser.parseReader(reader);
            if (element == null || !element.isJsonObject()) {
                LOGGER.warn("CosmeticDataLoader json invalido arquivo {}", fileId);
                return null;
            }
            return element.getAsJsonObject();
        } catch (Exception ex) {
            LOGGER.error("CosmeticDataLoader erro lendo json arquivo {}", fileId, ex);
            return null;
        }
    }

    private static CosmeticDefinition parseDefinition(ResourceLocation fileId, JsonObject obj) {
        try {
            String typeStr = getString(obj, "type", null);
            if (typeStr == null) {
                LOGGER.warn("CosmeticDataLoader faltando type arquivo {}", fileId);
                return null;
            }
            ResourceLocation typeRl = ResourceLocation.tryParse(typeStr);
            if (typeRl == null) {
                LOGGER.warn("CosmeticDataLoader type invalido {} arquivo {}", typeStr, fileId);
                return null;
            }

            String slotStr = getString(obj, "slot", null);
            if (slotStr == null) {
                LOGGER.warn("CosmeticDataLoader faltando slot arquivo {}", fileId);
                return null;
            }

            CosmeticSlot slot = parseSlot(slotStr);
            if (slot == null) {
                LOGGER.warn("CosmeticDataLoader slot invalido {} arquivo {}", slotStr, fileId);
                return null;
            }

            int tierLevel = getInt(obj, "tier", getInt(obj, "required_tier", 0));
            CosmeticTier requiredTier = CosmeticTier.fromLevel(tierLevel);

            int priority = getInt(obj, "priority", 0);
            boolean visibleToSelf = getBool(obj, "visible_to_self", false);

            CosmeticId id = CosmeticId.of(toCosmeticId(fileId));
            CosmeticTypeId type = CosmeticTypeId.of(typeRl);

            CosmeticDefinition def = new CosmeticDefinition(id, type, slot, requiredTier, priority, visibleToSelf);
            LOGGER.info("CosmeticDataLoader parse ok id {} slot {} tier {}", def.id(), def.slot().name(), def.requiredTier().name());
            return def;
        } catch (Exception ex) {
            LOGGER.error("CosmeticDataLoader erro parse arquivo {}", fileId, ex);
            return null;
        }
    }

    private static ResourceLocation toCosmeticId(ResourceLocation fileId) {
        String path = fileId.getPath();
        String p = path;

        if (p.startsWith("cosmetics/")) {
            p = p.substring("cosmetics/".length());
        }

        if (p.endsWith(".json")) {
            int end = Math.subtractExact(p.length(), ".json".length());
            p = p.substring(0, end);
        }

        ResourceLocation out = ResourceLocation.fromNamespaceAndPath(fileId.getNamespace(), p);
        LOGGER.debug("CosmeticDataLoader toCosmeticId file {} id {}", fileId, out);
        return out;
    }

    private static CosmeticSlot parseSlot(String slotStr) {
        ResourceLocation rl = ResourceLocation.tryParse(slotStr);
        if (rl != null) {
            CosmeticSlot byId = CosmeticSlot.byId(rl);
            if (byId != null) return byId;
        }
        return CosmeticSlot.byName(slotStr);
    }

    private static String getString(JsonObject obj, String key, String def) {
        if (obj.has(key) && obj.get(key).isJsonPrimitive() && obj.get(key).getAsJsonPrimitive().isString()) {
            return obj.get(key).getAsString();
        }
        return def;
    }

    private static int getInt(JsonObject obj, String key, int def) {
        if (obj.has(key) && obj.get(key).isJsonPrimitive() && obj.get(key).getAsJsonPrimitive().isNumber()) {
            return obj.get(key).getAsInt();
        }
        return def;
    }

    private static boolean getBool(JsonObject obj, String key, boolean def) {
        if (obj.has(key) && obj.get(key).isJsonPrimitive() && obj.get(key).getAsJsonPrimitive().isBoolean()) {
            return obj.get(key).getAsBoolean();
        }
        return def;
    }
}