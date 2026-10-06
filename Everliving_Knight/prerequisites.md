You are a senior Minecraft Java mod developer specializing in Fabric 1.20.1, Yarn mappings, Fabric API, GeckoLib 4.x, entity AI, boss systems, and multiplayer-safe server-side gameplay.

I am working on an existing Minecraft Fabric mod project.

PROJECT TARGET:

* Minecraft version: 1.20.1
* Yarn mappings: 1.20.1+build.10
* Fabric Loader: 0.19.5
* Fabric Loom: 1.18-SNAPSHOT
* Fabric API: 0.92.12+1.20.1
* Java: use the Java version required by Minecraft 1.20.1, normally Java 17
* Mod ID: elemental
* Boss name: Sir Solvane, the Everliving Knight

IMPORTANT:
This is an EXISTING project. Do not replace the project's architecture, package structure, Gradle configuration, entrypoints, or registration system unnecessarily.

FIRST inspect the existing project and determine:

1. The current base Java package.
2. The existing main Fabric mod initializer.
3. Existing item registration classes.
4. Existing entity registration classes, if any.
5. Existing client initializer.
6. Existing GeckoLib setup, if any.
7. Existing weapon implementations:

   * elemental:sunforged_scimitar
   * elemental:frostwake_pick
   * elemental:rootbound_axe
8. Existing resource folder conventions.
9. Existing mixins and whether any are already required.
10. Existing naming conventions.

Your task in this prompt is ONLY to prepare the prerequisites and architecture for the boss.

DO NOT implement the complete boss AI yet.
DO NOT create the full boss entity yet.
DO NOT create the model yet.
DO NOT create animations yet.
DO NOT create the final texture yet.
DO NOT create crafting recipes.

GECKOLIB:
Add GeckoLib 4.x compatible with Fabric 1.20.1 using the appropriate Maven repository and dependency.
Prefer the currently established 1.20.1 GeckoLib 4.x dependency version compatible with this project; do not blindly use GeckoLib 5 APIs.
Do not mix GeckoLib 5 APIs/classes with GeckoLib 4 APIs/classes.

If GeckoLib is already present:

* Reuse the existing dependency.
* Do not add a duplicate dependency.
* Verify that the existing version is compatible with Minecraft 1.20.1.

PREPARE THE ARCHITECTURE FOR THESE FUTURE FILES:

Entity:

* SirSolvaneEntity.java
* SolvaneState.java
* SolvaneEntityAttributes.java if needed
* SolvaneBossBar.java if needed

Renderer/model infrastructure:

* SirSolvaneModel.java
* SirSolvaneRenderer.java

Registration:

* ModEntities.java or the project's existing entity registry
* Client-side renderer registration

Resources to prepare:

* assets/elemental/geo/
* assets/elemental/animations/
* assets/elemental/textures/entity/
* appropriate entity-related JSON/resource locations

The future boss must use GeckoLib's entity animation architecture.

DEFINE THESE CORE CONSTANTS FOR THE FUTURE IMPLEMENTATION:

Boss:

* Max health: 800 HP
* Attack damage: 14
* Armor: 16
* Armor toughness: 6
* Knockback resistance: 0.9
* Movement speed: 0.25
* Recommended arena boundary: 29 × 29 blocks
* Phases: 3

Regeneration:

* Base regeneration: 2% of maximum health per second
* At 800 HP: 16 HP per second
* Normal regeneration multiplier: 1.0
* Thorn Cage regeneration multiplier: 0.40
* Frostburst regeneration: 0
* Core broken regeneration: 0

Core:

* Initial integrity: 100
* Thorn Cage damage: 20 integrity
* Required successful Thorn Cage uses: 5
* Core broken when integrity reaches 0

Timers:

* Frostburst freeze duration: 300 ticks
* Solar Arc wind-up: 15 ticks
* Solar Arc active window: 3 ticks
* Solar Arc range: 8 blocks
* Frostburst wind-up: 20 ticks
* Frostburst radius: 5 blocks
* Thorn Cage wind-up: 25 ticks
* Thorn Cage root duration on boss: 100 ticks

DEFINE THESE ENUMS/STATES FOR THE FUTURE BOSS IMPLEMENTATION:

RegenerationState:

* NORMAL
* FROZEN
* SLOWED
* FROZEN_AND_SLOWED
* DISABLED

SolvaneState:

* INTRO
* PATROL
* CHASE
* ATTACK_WINDUP
* ATTACK_ACTIVE
* ATTACK_RECOVERY
* REGENERATING
* FROST_FROZEN
* THORN_CAGED
* CORE_EXPOSED
* STAGGERED
* PHASE_TRANSITION
* ENRAGED
* DEATH

STATE PRIORITY:

1. DEATH
2. PHASE_TRANSITION
3. CORE_EXPOSED
4. STAGGERED
5. FROST_FROZEN
6. THORN_CAGED
7. ATTACK
8. CHASE
9. PATROL

SERVER/CLIENT RULE:
All gameplay authority must remain server-side.
The client must never be trusted for:

* damage amount
* core damage
* cooldown validation
* regeneration state
* phase state
* boss death condition
* Frostburst duration
* Solar Arc 5% health removal
* Thorn Cage core damage

Prepare clean interfaces/methods so the next prompt can implement these mechanics without rewriting the project.

WEAPON IDENTIFICATION:
The future boss code must identify the custom weapons by registered Item identity, not by display name, lore, NBT text, or client-provided strings.

Expected registered items:

* ModItems.SUNFORGED_SCIMITAR
* ModItems.FROSTWAKE_PICK
* ModItems.ROOTBOUND_AXE

Before finishing:

1. Run/validate Gradle configuration.
2. Make sure imports use the correct 1.20.1 Yarn mappings.
3. Ensure there are no GeckoLib 5 imports.
4. Ensure the entity architecture is ready for server/client separation.
5. Ensure all created files compile.
6. Do not create placeholder code that requires nonexistent classes without clearly creating those prerequisite classes.

OUTPUT:
Provide the exact files you created or modified.
For every file, explain its purpose briefly.
Then provide the exact Gradle/resource changes required.
Finally provide a checklist showing that the project is ready for Prompt 2.
