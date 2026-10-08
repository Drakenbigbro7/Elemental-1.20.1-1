package com.elemental.client.model;

import com.elemental.Elemental;
import com.elemental.entity.TidebreakerTridentEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class TidebreakerTridentEntityModel extends GeoModel<TidebreakerTridentEntity> {
    private static final Identifier MODEL = Elemental.id("geo/tidebreaker_trident.geo.json");
    private static final Identifier TEXTURE = Elemental.id("textures/entity/tidebreaker_trident.png");
    private static final Identifier ANIMATION = Elemental.id("animations/tidebreaker_trident.animation.json");

    @Override
    public Identifier getModelResource(TidebreakerTridentEntity animatable) {
        return MODEL;
    }

    @Override
    public Identifier getTextureResource(TidebreakerTridentEntity animatable) {
        return TEXTURE;
    }

    @Override
    public Identifier getAnimationResource(TidebreakerTridentEntity animatable) {
        return ANIMATION;
    }
}
