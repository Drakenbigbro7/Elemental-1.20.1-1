package com.elemental;

import com.elemental.block.ModBlocks;
import com.elemental.block.entity.ModBlockEntities;
import com.elemental.entity.ModEntities;
import com.elemental.item.ModItemGroup;
import com.elemental.item.ModItems;
import com.elemental.recipe.WeaponerRecipes;
import com.elemental.screen.ModScreenHandlers;
import com.elemental.util.SunforgedHeatHandler;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Elemental implements ModInitializer {
	public static final String MOD_ID = "elemental";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Initializing Elemental mod...");
		ModBlocks.registerModBlocks();
		ModBlockEntities.registerBlockEntities();
		ModScreenHandlers.registerScreenHandlers();
		WeaponerRecipes.init();
		ModItems.registerModItems();
		ModItemGroup.registerItemGroups();
		ModEntities.registerModEntities();
		SunforgedHeatHandler.register();
	}

	public static Identifier id(String path) {
		return new Identifier(MOD_ID, path);
	}
}
