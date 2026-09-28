package com.elemental.client.model;

import com.elemental.entity.SolarArcEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class SolarArcModel extends GeoModel<SolarArcEntity> {
    private static final Identifier MODEL = new Identifier("elemental", "geo/solar_arc.geo.json");
    private static final Identifier TEXTURE = new Identifier("elemental", "textures/entity/solar_arc.png");
    private static final Identifier ANIMATION = new Identifier("elemental", "animations/solar_arc.animation.json");

    @Override
    public Identifier getModelResource(SolarArcEntity animatable) {
        return MODEL;
    }

    @Override
    public Identifier getTextureResource(SolarArcEntity animatable) {
        return TEXTURE;
    }

    @Override
    public Identifier getAnimationResource(SolarArcEntity animatable) {
        return ANIMATION;
    }
}
