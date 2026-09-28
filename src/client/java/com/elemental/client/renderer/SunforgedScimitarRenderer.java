package com.elemental.client.renderer;

import com.elemental.client.model.SunforgedScimitarModel;
import com.elemental.item.SunforgedScimitarItem;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class SunforgedScimitarRenderer extends GeoItemRenderer<SunforgedScimitarItem> {
    public SunforgedScimitarRenderer() {
        super(new SunforgedScimitarModel());
    }
}
