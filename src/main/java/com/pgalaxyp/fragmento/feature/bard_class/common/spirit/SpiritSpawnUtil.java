package com.pgalaxyp.fragmento.feature.bard_class.common.spirit;

import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public final class SpiritSpawnUtil {

    private static final Logger LOGGER = LogUtils.getLogger();

    private SpiritSpawnUtil() {
    }

    public static Vec3 area3x3Front(LivingEntity caster, Level level) {

        LOGGER.info("Calculating area3x3Front for caster {}", caster.getName().getString());

        Vec3 look = caster.getLookAngle().normalize();
        Vec3 top = new Vec3(caster.getX(), caster.getY() + 1.0, caster.getZ());
        Vec3 pivot = top.add(look.scale(1.5));

        LOGGER.info("Pivot = {}", pivot);

        double minX = pivot.x - 2.0;
        double maxX = pivot.x + 2.0;
        double minY = pivot.y;
        double maxY = pivot.y + 2.0;
        double minZ = pivot.z - 2.0;
        double maxZ = pivot.z + 2.0;

        double bx1 = pivot.x - 1.0;
        double bx2 = pivot.x + 1.0;
        double by1 = pivot.y - 1.0;
        double by2 = pivot.y + 1.0;
        double bz1 = pivot.z - 1.0;
        double bz2 = pivot.z + 1.0;

        List<Vec3> samples = new ArrayList<>();

        for (int i = 0; i < 200; i++) {
            double x = minX + level.random.nextDouble() * (maxX - minX);
            double y = minY + level.random.nextDouble() * (maxY - minY);
            double z = minZ + level.random.nextDouble() * (maxZ - minZ);

            if (x >= bx1 && x <= bx2 && y >= by1 && y <= by2 && z >= bz1 && z <= bz2)
                continue;

            Vec3 p = new Vec3(x, y, z);
            Vec3 d = p.subtract(pivot);
            if (d.dot(look) > 0)
                continue;

            samples.add(p);
        }

        if (samples.isEmpty()) {
            LOGGER.info("No valid spawn points found, using pivot");
            return pivot;
        }

        Vec3 chosen = samples.get(level.random.nextInt(samples.size()));
        LOGGER.info("Chosen spawn position {}", chosen);
        return chosen;
    }
}
