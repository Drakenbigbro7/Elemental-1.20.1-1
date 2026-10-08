package com.elemental.client.renderer;

import com.elemental.client.model.TidebreakerTridentModel;
import com.elemental.item.TidebreakerTridentItem;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class TidebreakerTridentRenderer extends GeoItemRenderer<TidebreakerTridentItem> {
    public TidebreakerTridentRenderer() {
        super(new TidebreakerTridentModel());
    }
}
