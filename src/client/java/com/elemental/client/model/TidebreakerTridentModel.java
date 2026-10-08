package com.elemental.client.model;

import com.elemental.Elemental;
import com.elemental.item.TidebreakerTridentItem;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class TidebreakerTridentModel extends GeoModel<TidebreakerTridentItem> {
    private static final Identifier MODEL = Elemental.id("geo/tidebreaker_trident.geo.json");
    private static final Identifier TEXTURE = Elemental.id("textures/item/tidebreaker_trident.png");
    private static final Identifier ANIMATION = Elemental.id("animations/tidebreaker_trident.animation.json");

    @Override
    public Identifier getModelResource(TidebreakerTridentItem animatable) {
        return MODEL;
    }

    @Override
    public Identifier getTextureResource(TidebreakerTridentItem animatable) {
        return TEXTURE;
    }

    @Override
    public Identifier getAnimationResource(TidebreakerTridentItem animatable) {
        return ANIMATION;
    }
}
