package com.elemental.client;

import com.elemental.entity.ModEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;

public class ElementalClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// Register the solar arc entity renderer using the built-in projectile renderer
		EntityRendererRegistry.register(ModEntities.SOLAR_ARC, (context) ->
				new FlyingItemEntityRenderer<>(context));
	}
}