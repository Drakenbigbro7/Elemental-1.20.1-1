package com.elemental.entity;

import com.elemental.Elemental;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModEntities {
    public static final EntityType<SolarArcEntity> SOLAR_ARC = Registry.register(
            Registries.ENTITY_TYPE,
            Elemental.id("solar_arc"),
            FabricEntityTypeBuilder.<SolarArcEntity>create(SpawnGroup.MISC, SolarArcEntity::new)
                    .dimensions(EntityDimensions.fixed(0.5f, 0.5f))
                    .trackRangeBlocks(64)
                    .trackedUpdateRate(10)
                    .build()
    );

    public static void registerModEntities() {
        Elemental.LOGGER.info("Registering mod entities for " + Elemental.MOD_ID);
    }
}
