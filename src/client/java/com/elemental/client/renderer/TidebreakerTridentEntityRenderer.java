package com.elemental.client.renderer;

import com.elemental.client.model.TidebreakerTridentEntityModel;
import com.elemental.entity.TidebreakerTridentEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class TidebreakerTridentEntityRenderer extends GeoEntityRenderer<TidebreakerTridentEntity> {

    public TidebreakerTridentEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new TidebreakerTridentEntityModel());
    }

    @Override
    protected void applyRotations(TidebreakerTridentEntity entity, MatrixStack matrices, float ageInTicks, float rotationYaw, float partialTick) {
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(MathHelper.lerp(partialTick, entity.prevYaw, entity.getYaw()) - 90.0f));
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(MathHelper.lerp(partialTick, entity.prevPitch, entity.getPitch()) + 90.0f));
    }
}
