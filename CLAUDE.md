# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a **Fabric mod for Minecraft 1.20.1** called "Elemental" that adds three custom elemental weapons with unique abilities, animations, and mechanics using **GeckoLib** for animated 3D models.

## Build & Development Commands

| Command | Description |
|---------|-------------|
| `./gradlew build` | Build the mod JAR (outputs to `build/libs/`) |
| `./gradlew runClient` | Run Minecraft client with the mod for testing |
| `./gradlew runServer` | Run a dedicated server with the mod |
| `./gradlew jar` | Build just the mod JAR |
| `./gradlew clean` | Clean build artifacts |
| `./gradlew genSources` | Generate sources for IDE |

### IDE Setup
- Uses Fabric Loom Gradle plugin (version 1.18-SNAPSHOT)
- Java 17 required (configured in build.gradle)
- Run `./gradlew genIntellijRuns` or `./gradlew genEclipseRuns` for IDE run configurations

## Architecture

### Mod Entry Points
- **Main**: `com.elemental.Elemental` - ModInitializer, registers items, entities, and heat handler
- **Client**: `com.elemental.client.ElementalClient` - ClientModInitializer, registers GeckoLib renderers
- **Data Gen**: `com.elemental.client.ElementalDataGenerator` - DataGeneratorEntrypoint (currently empty)

### Core Components

#### 1. Three Custom Weapons (Items)

**Sunforged Scimitar** (`SunforgedScimitarItem.java`)
- Sword with heat mechanic that builds up in sunlight/desert biomes
- Active ability: Charge and fire Solar Arc projectile (configurable damage/speed)
- Passive: Bonus damage vs undead in sunlight, fast sand mining, heat bonus fire damage
- Upgradeable via "Solar Core" NBT data (increases projectile speed, lifetime, fire duration)

**Frostwake Pick** (`FrostwakePickItem.java`)
- Pickaxe with ice/snow mining speed bonuses
- Active: Frostburst - freezes water to ice in radius, applies Slowness to nearby entities
- Passive: Applies stacking Slowness on hit (chill stacks)
- Upgradeable via "Glacial Core" NBT (increases frostburst radius/effect)

**Rootbound Axe** (`RootboundAxeItem.java`)
- Axe with fast wood/log mining
- Active: Thorn Cage - damages and slows enemies in radius around player
- Passive: Wide sweep attack on hit (larger angle than vanilla), chance to spawn healing roots (stronger in forests/jungles)
- Biome-aware bonuses (forest/jungle)

#### 2. Custom Entity
**SolarArcEntity** (`SolarArcEntity.java`) - Projectile fired by Sunforged Scimitar
- Extends `PersistentProjectileEntity`, implements `GeoEntity` for GeckoLib animations
- No gravity, configurable lifetime, damage, fire duration
- Weakened by rain/water (50% damage reduction)
- Solar Core level affects speed, lifetime, and fire duration

#### 3. Server-Side Systems
**SunforgedHeatHandler** (`SunforgedHeatHandler.java`)
- Runs on `ServerTickEvents.END_SERVER_TICK` every 20 ticks (1 second)
- Tracks heat level per player per Sunforged Scimitar in inventory
- Heat increases in direct sunlight, decays otherwise
- Optional self-damage at high heat (currently disabled via `SELF_DAMAGE_ENABLED = false`)

#### 4. Tool Materials
Each weapon has a custom `ToolMaterial` implementation:
- `ScimitarToolMaterial` - Gold ingot repair, mining level 2
- `FrostwakePickToolMaterial` - Blue ice repair, mining level 3
- `RootboundAxeToolMaterial` - Netherite scrap repair, mining level 3

#### 5. GeckoLib Integration
All items and the Solar Arc entity use GeckoLib for animated 3D models:
- **Model**: `GeoModel` subclass pointing to `.geo.json` (Blockbench format)
- **Renderer**: `GeoItemRenderer` / `GeoEntityRenderer` subclass
- **Animations**: Defined in `.animation.json` files, triggered via `AnimationController`
- **Registration**: In `ElementalClient.onInitializeClient()` via `RENDER_PROVIDER_CONSUMER` pattern

### Resource Structure
```
src/main/resources/
├── assets/elemental/
│   ├── geo/              # Blockbench geometry files (.geo.json)
│   ├── animations/       # Animation definitions (.animation.json)
│   ├── textures/
│   │   ├── item/         # Item textures
│   │   └── entity/       # Entity textures
│   ├── models/item/      # Item model JSONs (fallback)
│   └── lang/en_us.json   # Translations
├── data/elemental/recipes/   # Crafting recipes
├── data/fabric/tags/       # Item tags (swords, axes, shovels)
├── fabric.mod.json         # Mod metadata
└── elemental.mixins.json   # Mixin configs
```

### Key Configuration Files
- `gradle.properties` - Versions (MC 1.20.1, Yarn mappings, Loader 0.19.5, Fabric API 0.92.12, GeckoLib 4.4.7)
- `build.gradle` - Loom config, dependencies (Fabric API, GeckoLib), Java 17
- `fabric.mod.json` - Mod entrypoints, dependencies, mixins
- `settings.gradle` - Root project name = "elemental"

## Development Notes

### Adding New Items
1. Create item class extending appropriate base (SwordItem, PickaxeItem, AxeItem) + `GeoItem`
2. Create ToolMaterial implementation
3. Register in `ModItems.java`
4. Create GeoModel, GeoItemRenderer, and register in `ElementalClient`
5. Add Blockbench model (.geo.json), animation (.animation.json), texture
6. Add recipe and item tags in data/

### Animation System
- Animations defined in `.animation.json` (Bedrock/GeckoLib format)
- `AnimationController` in item/entity class maps animation names to triggers
- Client-side renderer triggered via `RENDER_PROVIDER_CONSUMER` static field
- Use `triggerAnim(entity, id, controllerName, animName)` to play animations

### NBT Data Pattern
Items store upgrade/state data in NBT:
- `SunforgedScimitarItem`: `SolarCoreLevel`, `Heat`
- `FrostwakePickItem`: `GlacialCoreInstalled`, `ChillStacks`, `LastChillTime`
- `RootboundAxeItem`: Root data, `ActiveThornCage`, `LastThornCageTime`

### Testing
- Run `./gradlew runClient` for manual testing
- No automated tests currently configured

## Common Tasks

### Build for Distribution
```bash
./gradlew build
# Output: build/libs/elemental-1.0.0.jar
```

### Update Dependencies
Edit versions in `gradle.properties`:
- `minecraft_version`, `yarn_mappings`, `loader_version`, `loom_version`
- `fabric_api_version`
- GeckoLib version in `build.gradle` dependencies block

### Debug Logging
Mod uses SLF4J logger: `Elemental.LOGGER.info/debug/warn/error`
