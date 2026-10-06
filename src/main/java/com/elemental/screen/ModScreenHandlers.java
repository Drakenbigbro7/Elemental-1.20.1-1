package com.elemental.screen;

import com.elemental.Elemental;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

/**
 * ScreenHandlerType registry for the mod.
 */
public class ModScreenHandlers {
    public static final ScreenHandlerType<WeaponerScreenHandler> WEAPONER_SCREEN_HANDLER = Registry.register(
            Registries.SCREEN_HANDLER,
            new Identifier(Elemental.MOD_ID, "weaponer"),
            new ScreenHandlerType<>(WeaponerScreenHandler::new, FeatureSet.empty())
    );

    public static void registerScreenHandlers() {
        Elemental.LOGGER.info("Registering Screen Handlers for " + Elemental.MOD_ID);
    }
}
