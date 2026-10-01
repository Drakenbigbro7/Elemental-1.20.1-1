package com.elemental.client.model;

import com.elemental.item.FrostwakePickItem;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class FrostwakePickModel extends GeoModel<FrostwakePickItem> {
    private static final Identifier MODEL = new Identifier("elemental", "geo/frostwake_pick.geo.json");
    private static final Identifier TEXTURE = new Identifier("elemental", "textures/item/frostwake_pick.png");
    private static final Identifier ANIMATION = new Identifier("elemental", "animations/frostwake_pick.animation.json");

    @Override
    public Identifier getModelResource(FrostwakePickItem animatable) {
        return MODEL;
    }

    @Override
    public Identifier getTextureResource(FrostwakePickItem animatable) {
        return TEXTURE;
    }

    @Override
    public Identifier getAnimationResource(FrostwakePickItem animatable) {
        return ANIMATION;
    }
}