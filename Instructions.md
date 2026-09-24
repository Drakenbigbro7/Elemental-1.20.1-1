You are generating code for a Fabric Loader mod for Minecraft 1.20.1.
The mod already has a working template: initialization class, item registry pattern, data/asset folders, and build config.
Do not rewrite the whole mod. Only add/modify the files needed to implement the item below perfectly within the existing template.

Mod environment (do not change these):

text
minecraft_version=1.20.1
yarn_mappings=1.20.1+build.10
loader_version=0.19.5
loom_version=1.18-SNAPSHOT
fabric_api_version=0.92.12+1.20.1
Assume:

Java 17.

Standard Fabric project layout (src/main/java, src/main/resources).

An existing ModInitializer and an existing item registry class (e.g. ModItems) using Registry.register(Registries.ITEM, id, item) with Item.Settings.

Existing data generators or manual data folders for tags, recipes, etc.

You can create new Java classes under the mod’s main package (e.g. com.example.mod.item or whatever the template uses).

You can add/modify JSONs under resources/assets/<modid> and resources/data/<modid>.

Item to implement: Sunforged Scimitar
Base identity

Item ID: sunforged_scimitar

Display name: Sunforged Scimitar

Type: custom sword-like melee weapon that also functions as a shovel for specific blocks.

Max durability: choose a balanced value (e.g. 250–350) and expose as a constant.

Stack size: 1.

Rarity: common (unless your template uses a rarity system; then pick an appropriate one).

Core behavior requirements

Tool function – sand digging

When used on the following blocks, it should mine them as efficiently as a shovel:

minecraft:sand

minecraft:red_sand

minecraft:suspicious_sand

For all other blocks, it should behave like a sword (no special mining speed).

Implement this by:

Creating a custom ToolMaterial for the scimitar (or reusing an existing one if the template suggests).

Ensuring the item is tagged as a shovel in Fabric tool tags so it works with modded sand-like blocks if applicable: fabric:shovels.

Overriding getMiningSpeedMultiplier (or the 1.20.1 equivalent in Yarn) to give high speed only for sand, red_sand, and suspicious_sand.

Weapon function – fast melee attack

Behave as a sword with:

Slightly higher attack speed than a normal diamond/netherite sword (e.g. attack speed ~1.8–2.0 instead of 1.6).

Damage around diamond-tier or slightly above (e.g. 7–8 base damage), tunable via constants.

Use SwordItem as a base or implement similar logic in a custom item class that extends SwordItem.

Ensure the item is tagged as fabric:swords.

Passive – bonus damage to undead in sunlight

When hitting an entity:

If the entity is undead (zombie, skeleton, wither skeleton, drowned, phantoms, etc. – use the EntityTags or EntityType undead check available in 1.20.1 Yarn),

And the attacker (player) is in direct sunlight (sky-visible, day time, not raining, not under a roof),

Then add bonus magic/fire-like damage (e.g. +2–4 damage).

Implement this by overriding postHit (or the appropriate combat hook in 1.20.1) in your custom item class.

Use the world’s lighting / sky access methods to determine “sunlight” (e.g. World.isDay(), World.isSkyVisible, local light level checks).

Apply the extra damage as magic or fire damage, not plain attack damage, so it interacts correctly with armor/resistance.

Active ability – solar arc projectile

Right-click (use item) to fire a short-range solar arc projectile:

Visual: a small, bright, orange/yellow projectile that travels in a straight line for a short distance/time.

On hit:

Deals moderate magic/fire damage to the entity.

Sets the target on fire for a short duration (e.g. 3–5 seconds).

Cooldown:

Use Minecraft’s built-in item cooldown system (player.getItemCooldownManager().set(this, cooldownTicks)).

Base cooldown: e.g. 200–300 ticks (10–15 seconds), exposed as a constant.

Implementation details:

Create a custom Entity class for the solar arc projectile (e.g. SolarArcEntity) that extends ThrowableProjectile or AbstractArrow-like class appropriate for 1.20.1.

Register the entity in your mod’s entity registry.

On right-click:

Consume 1 durability from the scimitar.

Spawn the projectile at the player’s eye position, with direction from player rotation.

Put the item on cooldown.

Limitations:

If the player is in rain or in water, the projectile:

Deals reduced damage (e.g. 50%).

Has shorter fire duration (e.g. 1–2 seconds instead of 3–5).

Optionally, shorter travel distance/lifetime.

Detect rain via World.isRaining() and whether the entity’s position is exposed to rain; detect water via isInWater().

Biome interaction – desert cooldown reduction

When the player is in a desert biome (minecraft:desert and related desert biome tags if available):

Reduce the active ability cooldown by a fixed percentage or fixed ticks (e.g. 25% less cooldown).

Implement this by checking the player’s current biome when applying the cooldown and adjusting the ticks accordingly.

Upgrade – Solar Core

Design the item so it can be upgraded via a “Solar Core” upgrade item (you do not need to fully implement the upgrade item now, but structure the code to support it).

The upgrade should:

Increase solar arc range (e.g. longer lifetime or higher velocity).

Increase fire burn duration on hit.

Implementation approach:

Add an NBT tag (e.g. SolarCoreLevel int) to the scimitar.

Provide a method getSolarCoreLevel(ItemStack stack) that returns 0 by default and >0 if upgraded.

Use this value to scale projectile speed/lifetime and fire duration.

Leave a clear TODO/comment where the upgrade item would modify this NBT (e.g. via anvil, crafting, or custom upgrade station).

Heat damage (passive environmental effect)

While holding the Sunforged Scimitar:

If the player is in a hot biome (e.g. desert, badlands, savanna – use biome temperature or specific biome tags),

And in direct sunlight during the day,

The item very slowly gains “heat” over time (tracked via NBT or a component).

At high heat levels, the item may:

Deal slight extra damage (optional).

Or cause minor self-damage to the holder if you want a risk/reward mechanic (make this configurable via a constant).

Implement a simple server-side tick handler that:

Runs each tick for players holding this item.

Updates a Heat NBT value.

Applies effects based on thresholds.

Keep this effect subtle and tunable via constants.

Files you must create/modify
Work within the existing template’s package structure. If the template uses com.example.mod, adapt accordingly.

Java – item class

Create: SunforgedScimitarItem.java

Extend SwordItem.

Implement:

Custom mining speed for sand/red_sand/suspicious_sand.

Passive sunlight vs undead bonus damage in postHit.

Right-click behavior to fire solar arc and apply cooldown.

Biome-based cooldown reduction.

Heat NBT logic hooks (you may delegate ticking to a separate class).

Expose all numeric values as private static final constants at the top of the class for easy tuning.

Java – projectile entity

Create: SolarArcEntity.java

Extend an appropriate projectile class for 1.20.1 (e.g. ThrowableProjectile or similar).

Implement:

Movement, lifetime, collision.

Damage on hit (with rain/water reduction).

Fire application (with rain/water reduction).

Use SolarCoreLevel from the shooter’s held item to adjust range and burn duration.

Register this entity in your mod’s entity registry class (create or edit ModEntities.java if needed):

Define EntityType<SolarArcEntity>.

Register under Registries.ENTITY_TYPE.

Provide spawn egg / creative spawn if your template expects it (optional).

Java – heat tick handler

Create: SunforgedHeatHandler.java (or similar)

Implement a server tick event (via Fabric API’s ServerTickEvents or equivalent in 1.20.1).

Each tick:

For each player, if holding SunforgedScimitarItem:

Check biome temperature / type.

Check sunlight & day.

Update Heat NBT on the item.

Apply optional effects at thresholds.

Keep logic efficient and null-safe.

Item registration

In your existing ModItems (or equivalent) class:

Register SUNFORGED_SCIMITAR:

Using SunforgedScimitarItem with appropriate Item.Settings (durability, fire resistant if desired, etc.).

Ensure the item’s RegistryKey is correctly stored in settings if your template requires it.

Add the item to the TOOLS item group via ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS) if your template uses that pattern.

Tags

In fabric:shovels and fabric:swords item tags:

Add sunforged_scimitar so it works properly with other mods and recipes.

If your template uses a tag provider, add entries there; otherwise create/modify JSON tag files under data/fabric/tags/item.

Assets

Create/modify:

assets/<modid>/textures/item/sunforged_scimitar.png – placeholder texture description (you can just describe what the texture should look like if you’re not generating images).

assets/<modid>/models/item/sunforged_scimitar.json – using item/handheld parent with the texture.

assets/<modid>/lang/en_us.json – add translation:

"item.<modid>.sunforged_scimitar": "Sunforged Scimitar"

If your template uses additional languages, add keys accordingly.

Recipes (optional but recommended)

Provide at least one example crafting recipe JSON under data/<modid>/recipes/:

For sunforged_scimitar, using plausible materials (e.g. sun-related items, gold, blaze rods, etc.).

Use shapeless or shaped recipe as fits your mod’s theme.

Ensure the recipe matches 1.20.1 format (data/<modid>/recipes/, not recipe singular).

Configuration hooks

At the top of SunforgedScimitarItem and SolarArcEntity, define constants for:

Base damage, attack speed.

Durability.

Cooldown ticks (base and desert-reduced).

Fire durations (normal, rain/water reduced, solar-core increased).

Projectile speed, lifetime, and solar-core multipliers.

Heat gain rate, thresholds, and effects.

Add comments explaining each constant so designers can tune them without reading logic.

Coding constraints & style
Target Minecraft 1.20.1 with Yarn mappings 1.20.1+build.10.

Use Fabric API 0.92.12+1.20.1 features where helpful (e.g. item groups, tags, events).

Do not use mixins unless absolutely necessary; prefer event hooks and overrides.

Keep code clean, null-safe, and consistent with typical Fabric examples.

Use clear method names and short Javadoc-style comments for public methods.

Do not change existing unrelated items or systems in the template.

Assume the build system (Gradle, Fabric Loom) is already configured correctly.

Output format
Produce:

The full contents of each new/modified Java file, with package declarations matching a generic mod package (e.g. com.example.mod.item, com.example.mod.entity, com.example.mod.util), clearly labeled with file paths as comments at the top.

The JSON contents for:

Item model.

Language file entries.

Tag files (if not using a tag provider).

Example recipe(s).

Short notes on:

Where to register the entity type.

Where to hook the server tick event for heat.

Any assumptions you made about the existing template.

Do not include explanations about how Fabric works; only output the code and minimal notes necessary to integrate this item into an existing 1.20.1 Fabric mod template.

