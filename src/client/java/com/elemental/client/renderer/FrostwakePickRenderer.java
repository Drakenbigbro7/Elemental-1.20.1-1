package com.elemental.client.renderer;

import com.elemental.client.model.FrostwakePickModel;
import com.elemental.item.FrostwakePickItem;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class FrostwakePickRenderer extends GeoItemRenderer<FrostwakePickItem> {
    public FrostwakePickRenderer() {
        super(new FrostwakePickModel());
    }
}