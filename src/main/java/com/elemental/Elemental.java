package com.elemental;

import com.elemental.item.ModItemGroup;
import net.fabricmc.api.ModInitializer;

import net.minecraft.util.Identifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.elemental.item.ModItems;
import com.elemental.entity.ModEntities;
import com.elemental.util.SunforgedHeatHandler;

public class Elemental implements ModInitializer {
	public static final String MOD_ID = "elemental";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {

		ModItemGroup.registerItemGroups();
//		ModItems.registerModItems();
		ModItems.registerModItems();
		ModEntities.registerModEntities();
		SunforgedHeatHandler.register();
	}

	public static Identifier id(String path) {
		return new Identifier(MOD_ID, path);
	}
}

