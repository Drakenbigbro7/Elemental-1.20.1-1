package com.elemental.client.renderer;

import com.elemental.Elemental;
import com.elemental.entity.EverlivingKnightEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class EverlivingKnightRenderer extends GeoEntityRenderer<EverlivingKnightEntity> {
    public EverlivingKnightRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new EverlivingKnightModel());
        this.addRenderLayer(new EverlivingKnightArmorLayer(this));
    }

    @Override
    public Identifier getTextureLocation(EverlivingKnightEntity entity) {
        return Elemental.id("textures/entity/everliving_knight.png");
    }

    @Override
    public void render(EverlivingKnightEntity entity, float yaw, float partialTick, MatrixStack poseStack, VertexConsumerProvider bufferSource, int packedLight) {
        poseStack.push();
        // Apply scale for the boss (larger than normal entities)
        poseStack.scale(1.3f, 1.3f, 1.3f);
        super.render(entity, yaw, partialTick, poseStack, bufferSource, packedLight);
        poseStack.pop();
    }

    // Placeholder model class - will be replaced with actual Blockbench model in Prompt 3
    public static class EverlivingKnightModel extends GeoModel<EverlivingKnightEntity> {
        @Override
        public Identifier getModelResource(EverlivingKnightEntity entity) {
            return Elemental.id("geo/everliving_knight.geo.json");
        }

        @Override
        public Identifier getTextureResource(EverlivingKnightEntity entity) {
            return Elemental.id("textures/entity/everliving_knight.png");
        }

        @Override
        public Identifier getAnimationResource(EverlivingKnightEntity entity) {
            return Elemental.id("animations/everliving_knight.animation.json");
        }
    }

    // Placeholder armor layer - will be expanded in Prompt 3
    public static class EverlivingKnightArmorLayer extends GeoRenderLayer<EverlivingKnightEntity> {
        public EverlivingKnightArmorLayer(EverlivingKnightRenderer renderer) {
            super(renderer);
        }

        @Override
        public void render(MatrixStack poseStack, EverlivingKnightEntity entity, BakedGeoModel bakedModel, RenderLayer renderType, VertexConsumerProvider bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
            // Armor layer rendering will be implemented in Prompt 3
        }
    }
}