package com.elemental.client.model;

import com.elemental.Elemental;
import com.elemental.entity.EverlivingKnightEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class EverlivingKnightModel extends GeoModel<EverlivingKnightEntity> {

    @Override
    public Identifier getModelResource(EverlivingKnightEntity animatable) {
        return Elemental.id("geo/everliving_knight.geo.json");
    }

    @Override
    public Identifier getTextureResource(EverlivingKnightEntity animatable) {
        return Elemental.id("textures/entity/everliving_knight.png");
    }

    @Override
    public Identifier getAnimationResource(EverlivingKnightEntity animatable) {
        return Elemental.id("animations/everliving_knight.animation.json");
    }
}
