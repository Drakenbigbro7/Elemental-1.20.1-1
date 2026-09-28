package com.elemental.client.model;

import com.elemental.item.SunforgedScimitarItem;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class SunforgedScimitarModel extends GeoModel<SunforgedScimitarItem> {
    private static final Identifier MODEL = new Identifier("elemental", "geo/sunforged_scimitar.geo.json");
    private static final Identifier TEXTURE = new Identifier("elemental", "textures/item/sunforged_scimitar.png");
    private static final Identifier ANIMATION = new Identifier("elemental", "animations/sunforged_scimitar.animation.json");

    @Override
    public Identifier getModelResource(SunforgedScimitarItem animatable) {
        return MODEL;
    }

    @Override
    public Identifier getTextureResource(SunforgedScimitarItem animatable) {
        return TEXTURE;
    }

    @Override
    public Identifier getAnimationResource(SunforgedScimitarItem animatable) {
        return ANIMATION;
    }
}
