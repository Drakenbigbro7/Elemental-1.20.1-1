You are a professional Minecraft Fabric 1.20.1 + GeckoLib 4 developer.

Implement the complete CLIENT-SIDE rendering and GeckoLib animation system for:

# Sir Solvane, the Everliving Knight

Entity ID:

`elemental:everliving_knight`

## PROJECT VERSIONS

* Minecraft 1.20.1
* Yarn mappings 1.20.1+build.10
* Fabric Loader 0.19.5
* Fabric Loom 1.18-SNAPSHOT
* Fabric API 0.92.12+1.20.1
* Mod ID: elemental
* GeckoLib 4 compatible with Minecraft 1.20.1

Do not use Minecraft 1.21+ APIs.

## IMPORTANT

The attached `EVERLIVING_KNIGHT_GUIDE(1).md` defines the required animation names and trigger identifiers.

The animation system must integrate with the already-created `EverlivingKnightEntity`.

## REQUIRED ANIMATIONS

Implement support for:

### Basic

`animation.everliving_knight.idle`

Trigger:
`idle`

`animation.everliving_knight.walk`

Trigger:
automatic movement

`animation.everliving_knight.run`

Trigger:
automatic high-speed pursuit

### Combat

`animation.everliving_knight.slash_windup`

Trigger:
`slash_windup`

`animation.everliving_knight.slash`

Trigger:
`slash`

`animation.everliving_knight.shield_bash_windup`

Trigger:
`shield_bash_windup`

`animation.everliving_knight.shield_bash`

Trigger:
`shield_bash`

`animation.everliving_knight.charge_windup`

Trigger:
`charge_windup`

`animation.everliving_knight.charge`

Trigger:
`charge`

### Core

`animation.everliving_knight.core_pulse_windup`

Trigger:
`core_pulse_windup`

`animation.everliving_knight.core_pulse`

Trigger:
`core_pulse`

`animation.everliving_knight.core_exposed`

Trigger:
`core_exposed`

### State

`animation.everliving_knight.stagger`

Trigger:
`stagger`

`animation.everliving_knight.phase_two`

Trigger:
`phase_two`

`animation.everliving_knight.phase_three`

Trigger:
`phase_three`

`animation.everliving_knight.enrage`

Trigger:
`enrage`

`animation.everliving_knight.death`

Trigger:
`death`

## ANIMATION ARCHITECTURE

Create:

* GeckoLib entity implementation.
* Model class.
* Renderer class.
* Animation controller.
* Required animation predicates/controllers.
* Resource JSON files.
* Model/texture/animation resource paths.

Use server-to-client synchronized animation triggers.

The server must remain authoritative over:

* attack state
* phase
* core state
* attack timing

The client should only visualize those states.

## ANIMATION PRIORITY

Ensure combat animations override movement animations appropriately.

For example:

idle/walk/run

should not override:

slash
shield bash
charge
core pulse
stagger
phase transition
enrage
death

Windup animations must play before their corresponding attacks.

Do not randomly select animations every tick.

## ENTITY STATE SYNCHRONIZATION

Use appropriate GeckoLib/Fabric 1.20.1-compatible synchronization.

When the server triggers an attack animation, the client must receive the corresponding animation trigger.

Examples:

`slash_windup`

then

`slash`

and:

`core_pulse_windup`

then

`core_pulse`

Do not rely exclusively on client-side distance checks to determine when attacks happen.

## MODEL

Create the GeckoLib model implementation for the knight.

The model should support:

* armored knight body
* sword
* shield
* chest/radiant core
* core exposure state

The model must allow the `core_exposed` animation to visually reveal the shattered radiant core.

## RENDERER

Create the appropriate GeckoLib renderer.

The renderer should:

* render the custom model
* render the correct texture
* support animations
* support the boss's large physical presence
* use appropriate shadow size
* work correctly in multiplayer

## OPTIONAL VISUAL EFFECTS

Where appropriate, support visual effects for:

* radiant core glow
* core pulse
* phase transitions
* enrage
* stagger
* exposed core

Do not make gameplay depend on client-only effects.

## RESOURCE STRUCTURE

Use a clean structure similar to:

`src/main/resources/assets/elemental/geo/`

`src/main/resources/assets/elemental/animations/`

`src/main/resources/assets/elemental/textures/entity/`

Use exact file names consistently.

## IMPORTANT COMPATIBILITY RULES

Do NOT:

* use Forge
* use NeoForge
* use newer GeckoLib APIs incompatible with 1.20.1
* use Minecraft 1.21+ rendering APIs
* invent nonexistent classes
* mix client-only imports into common/server classes

## OUTPUT

Create all required Java and resource files.

For every file provide:

1. Exact path.
2. Complete contents.
3. Purpose.
4. How it connects to the entity.

Also provide:

* GeckoLib dependency required in `build.gradle`.
* Required client initialization changes.
* Required entity renderer registration.
* Required resource directory structure.
* Any Blockbench/GeckoLib model files that must be created manually.

If a `.geo.json`, `.animation.json`, or texture cannot realistically be generated as Java code, clearly identify it as an asset that must be supplied and provide the exact expected schema/file path rather than pretending the Java code creates it.

Finally provide a test procedure confirming that every required animation can be triggered.
