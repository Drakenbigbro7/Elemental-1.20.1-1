package com.elemental.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import com.elemental.Elemental;

public class ModEntityTags {
    public static final TagKey<EntityType<?>> BOSSES = TagKey.of(
            RegistryKeys.ENTITY_TYPE,
            new Identifier(Elemental.MOD_ID, "bosses")
    );
}