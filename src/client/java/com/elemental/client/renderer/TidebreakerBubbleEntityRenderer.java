package com.elemental.client.renderer;

import com.elemental.entity.TidebreakerBubbleEntity;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;

public class TidebreakerBubbleEntityRenderer extends EntityRenderer<TidebreakerBubbleEntity> {
    public TidebreakerBubbleEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public Identifier getTexture(TidebreakerBubbleEntity entity) {
        return null;
    }
}
