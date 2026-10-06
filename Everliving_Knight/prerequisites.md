You are a professional Minecraft Java/Fabric mod developer.

I am developing a Minecraft 1.20.1 Fabric mod and need you to create the prerequisites and foundational architecture for a custom boss entity.

## EXACT PROJECT VERSIONS

* Minecraft: 1.20.1
* Yarn mappings: 1.20.1+build.10
* Fabric Loader: 0.19.5
* Fabric Loom: 1.18-SNAPSHOT
* Fabric API: 0.92.12+1.20.1
* Mod ID: elemental
* Java: use the Java version actually required/compatible with Minecraft 1.20.1 and this project configuration. Do NOT blindly change the project's Java version to Java 25 if the existing 1.20.1 Fabric toolchain requires another version.
* GeckoLib: use a GeckoLib 4 version compatible with Minecraft 1.20.1 and the existing project.

## BOSS

Name:
Sir Solvane, the Everliving Knight

Entity ID:
elemental:everliving_knight

The complete boss mechanics specification is provided in the attached guide:
"EVERLIVING_KNIGHT_GUIDE(1).md"

Treat that guide as the authoritative specification for the boss mechanics. Do not invent replacement mechanics or silently remove mechanics.

## TASK

Create ONLY the prerequisites/foundation required for the boss implementation.

Create or modify the necessary Java files, registration classes, constants/configuration classes, and resources required to support the boss.

The implementation must include:

1. Entity registration

   * Register `elemental:everliving_knight`.
   * Use a proper Fabric 1.20.1 entity registration approach.
   * Configure the entity with the required dimensions suitable for a large armored knight.
   * Set appropriate tracking range and update rate.
   * Make the entity summonable/spawnable through commands for testing.

2. Entity class architecture
   Create a dedicated entity class such as:

   `EverlivingKnightEntity`

   It must be designed so the following systems can be cleanly implemented later:

   * Three combat phases.
   * Boss bar.
   * Core integrity.
   * Regeneration.
   * Attack cooldowns.
   * Attack windups/recovery.
   * Elemental reactions.
   * Arena boundaries.
   * GeckoLib animations.
   * Network-synchronized animation triggers.

3. Boss state system

   Create clearly named state fields/enums/constants for:

   * Phase 1
   * Phase 2
   * Phase 3
   * Current attack
   * Attack cooldowns
   * Attack windup state
   * Recovery state
   * Core integrity
   * Core broken state
   * Staggered state
   * Enrage state
   * Arena center
   * Arena radius

4. Core system foundation

   The boss must have persistent state for:

   * `coreIntegrity`
   * `coreBroken`
   * regeneration state
   * Frostburst healing lock timer
   * Thorn Cage healing modifier
   * stagger state

   Core integrity starts at 100.

5. Persistent data

   Use appropriate Minecraft 1.20.1 entity persistent-data/NBT mechanisms so important boss state survives where appropriate.

6. Boss bar foundation

   Create a server-side boss bar:

   * Name: `Sir Solvane, the Everliving Knight`
   * Red progress bar
   * Progress based on current health.

7. Attribute foundation

   Register/configure the boss attributes from the guide:

   * Max Health: 800 HP
   * Attack Damage: 14.0
   * Armor: 16
   * Armor Toughness: 6.0
   * Knockback Resistance: 0.9
   * Base Movement Speed: 0.25
   * Follow Range: 48 blocks

8. Testing support

   Make the entity usable with:

   `/summon elemental:everliving_knight`

   and ensure it can be spawned during development without requiring a crafting recipe or other item.

## IMPORTANT IMPLEMENTATION RULES

* Do not create crafting recipes.
* Do not create unrelated items.
* Do not change the mod ID.
* Do not change the Minecraft version.
* Do not use Forge/NeoForge APIs.
* Use Fabric/Yarn APIs appropriate for Minecraft 1.20.1.
* Do not use APIs from newer Minecraft versions.
* Keep server logic separate from client rendering/animation logic.
* Avoid deprecated or version-incompatible methods when a 1.20.1-compatible alternative exists.
* Keep the code modular rather than putting the entire boss into one enormous class.

## OUTPUT

Create the actual files required by the existing project.

For every created/modified file:

1. Give the exact path.
2. Give the complete file contents.
3. Explain briefly what the file does.
4. Clearly identify any dependency that must be added to `build.gradle`.

Do not implement the complete combat system yet. Build a clean foundation that the next implementation stage can extend without rewriting the architecture.
