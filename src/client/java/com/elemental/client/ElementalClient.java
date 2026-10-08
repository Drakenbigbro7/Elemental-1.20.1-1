package com.elemental.client;

import com.elemental.client.renderer.EverlivingKnightRenderer;
import com.elemental.client.renderer.FrostwakePickRenderer;
import com.elemental.client.renderer.RootboundAxeRenderer;
import com.elemental.client.renderer.SolarArcRenderer;
import com.elemental.client.renderer.SunforgedScimitarRenderer;
import com.elemental.client.renderer.TidebreakerBubbleEntityRenderer;
import com.elemental.client.renderer.TidebreakerTridentEntityRenderer;
import com.elemental.client.renderer.TidebreakerTridentRenderer;
import com.elemental.client.screen.WeaponerScreen;
import com.elemental.entity.ModEntities;
import com.elemental.item.FrostwakePickItem;
import com.elemental.item.ModItems;
import com.elemental.item.RootboundAxeItem;
import com.elemental.item.SunforgedScimitarItem;
import com.elemental.item.TidebreakerTridentItem;
import com.elemental.network.TidebreakerNetworking;
import com.elemental.screen.ModScreenHandlers;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import org.lwjgl.glfw.GLFW;
import software.bernie.geckolib.animatable.client.RenderProvider;

public class ElementalClient implements ClientModInitializer {
	private static KeyBinding dashKey;

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

		// Register GeckoLib custom renderer for the Rootbound Axe item
		RootboundAxeItem.RENDER_PROVIDER_CONSUMER = (consumer) -> {
			consumer.accept(new RenderProvider() {
				private RootboundAxeRenderer renderer;

				@Override
				public BuiltinModelItemRenderer getCustomRenderer() {
					if (this.renderer == null) {
						this.renderer = new RootboundAxeRenderer();
					}
					return this.renderer;
				}
			});
		};

		// Register GeckoLib custom renderer for the Tidebreaker Trident item
		TidebreakerTridentItem.RENDER_PROVIDER_CONSUMER = (consumer) -> {
			consumer.accept(new RenderProvider() {
				private TidebreakerTridentRenderer renderer;

				@Override
				public BuiltinModelItemRenderer getCustomRenderer() {
					if (this.renderer == null) {
						this.renderer = new TidebreakerTridentRenderer();
					}
					return this.renderer;
				}
			});
		};

		// Register GeckoLib animated renderer for the Solar Arc projectile
		EntityRendererRegistry.register(ModEntities.SOLAR_ARC, SolarArcRenderer::new);

		// Register GeckoLib animated renderer for Sir Solvane, the Everliving Knight
		EntityRendererRegistry.register(ModEntities.EVERLIVING_KNIGHT, EverlivingKnightRenderer::new);

		// Register Tidebreaker projectile and bubble entity renderers
		EntityRendererRegistry.register(ModEntities.TIDEBREAKER_TRIDENT_PROJECTILE, TidebreakerTridentEntityRenderer::new);
		EntityRendererRegistry.register(ModEntities.TIDEBREAKER_BUBBLE, TidebreakerBubbleEntityRenderer::new);

		// Register Weaponer screen GUI handler
		HandledScreens.register(ModScreenHandlers.WEAPONER_SCREEN_HANDLER, WeaponerScreen::new);

		// Register Tidebreaker Dash keybinding
		dashKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				"key.elemental.tidebreaker_dash",
				InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_R,
				"category.elemental"
		));

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (dashKey.wasPressed()) {
				if (client.player != null) {
					ItemStack main = client.player.getMainHandStack();
					ItemStack off = client.player.getOffHandStack();
					if (main.isOf(ModItems.TIDEBREAKER_TRIDENT) || off.isOf(ModItems.TIDEBREAKER_TRIDENT)) {
						if (!client.player.getItemCooldownManager().isCoolingDown(ModItems.TIDEBREAKER_TRIDENT)) {
							PacketByteBuf buf = PacketByteBufs.create();
							ClientPlayNetworking.send(TidebreakerNetworking.DASH_PACKET_ID, buf);
						}
					}
				}
			}
		});
	}
}