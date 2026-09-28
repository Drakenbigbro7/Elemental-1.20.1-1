package com.elemental.client.renderer;

import com.elemental.client.model.SolarArcModel;
import com.elemental.entity.SolarArcEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class SolarArcRenderer extends GeoEntityRenderer<SolarArcEntity> {
    public SolarArcRenderer(EntityRendererFactory.Context context) {
        super(context, new SolarArcModel());
    }
}
