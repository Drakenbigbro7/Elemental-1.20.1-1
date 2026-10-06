package com.elemental.client.renderer;

import com.elemental.client.model.RootboundAxeModel;
import com.elemental.item.RootboundAxeItem;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class RootboundAxeRenderer extends GeoItemRenderer<RootboundAxeItem> {
    public RootboundAxeRenderer() {
        super(new RootboundAxeModel());
    }
}
