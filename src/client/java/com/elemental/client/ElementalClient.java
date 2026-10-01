package com.elemental.client;

import com.elemental.client.renderer.FrostwakePickRenderer;
import com.elemental.client.renderer.SolarArcRenderer;
import com.elemental.client.renderer.SunforgedScimitarRenderer;
import com.elemental.entity.ModEntities;
import com.elemental.item.FrostwakePickItem;
import com.elemental.item.SunforgedScimitarItem;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import software.bernie.geckolib.animatable.client.RenderProvider;

public class ElementalClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// Register GeckoLib custom renderer for the Sunforged Scimitar item
		SunforgedScimitarItem.RENDER_PROVIDER_CONSUMER = (consumer) -> {
			consumer.accept(new RenderProvider() {
				private SunforgedScimitarRenderer renderer;

				@Override
				public BuiltinModelItemRenderer getCustomRenderer() {
					if (this.renderer == null) {
						this.renderer = new SunforgedScimitarRenderer();
					}
					return this.renderer;
				}
			});
		};

		// Register GeckoLib custom renderer for the Frostwake Pick item
		FrostwakePickItem.RENDER_PROVIDER_CONSUMER = (consumer) -> {
			consumer.accept(new RenderProvider() {
				private FrostwakePickRenderer renderer;

				@Override
				public BuiltinModelItemRenderer getCustomRenderer() {
					if (this.renderer == null) {
						this.renderer = new FrostwakePickRenderer();
					}
					return this.renderer;
				}
			});
		};

		// Register GeckoLib animated renderer for the Solar Arc projectile
		EntityRendererRegistry.register(ModEntities.SOLAR_ARC, SolarArcRenderer::new);
	}
}