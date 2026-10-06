You are a professional Minecraft 1.20.1 GeckoLib 4.x model/animation developer and Blockbench-style asset creator.

The project uses:

* Minecraft 1.20.1
* Yarn mappings 1.20.1+build.10
* Fabric Loader 0.19.5
* Fabric Loom 1.18-SNAPSHOT
* Fabric API 0.92.12+1.20.1
* GeckoLib 4.x compatible with Fabric 1.20.1
* Mod ID: elemental
* Boss: Sir Solvane, the Everliving Knight

Prompt 1 established the prerequisites.
Prompt 2 established the complete boss gameplay entity.

YOUR TASK:
Create the complete visual implementation of Sir Solvane:

* GeckoLib model
* model JSON
* animation JSON
* texture
* renderer
* animation controller
* state-to-animation mapping
* visual effects required by the boss design

Do not modify gameplay mechanics unless an animation hook is required.

BOSS VISUAL IDENTITY:

Sir Solvane is:

* tall
* heavily armored
* a cursed knight
* broken gold-and-black plate armor
* glowing regenerative core visible through armor cracks
* large knight helmet
* narrow solar visor
* tattered white or dark-red cloak
* regeneration energy flowing from the chest core
* visually more damaged as phases progress

The model must clearly communicate:

1. knight identity
2. radiant core
3. damaged/corrupted armor
4. phase changes
5. Frostburst reaction
6. Thorn Cage reaction
7. enrage state

MODEL STRUCTURE:

Create a proper hierarchical GeckoLib model with sensible bones.

Suggested hierarchy:

root
├── body
│   ├── chest
│   │   ├── armor_front
│   │   ├── armor_back
│   │   ├── core
│   │   ├── core_cracks
│   │   └── cloak
│   ├── head
│   │   ├── helmet
│   │   ├── visor
│   │   └── crown/helmet_details
│   ├── left_arm
│   │   ├── upper_arm
│   │   ├── forearm
│   │   └── hand
│   ├── right_arm
│   │   ├── upper_arm
│   │   ├── forearm
│   │   └── hand
│   ├── left_leg
│   │   ├── thigh
│   │   ├── shin
│   │   └── foot
│   └── right_leg
│       ├── thigh
│       ├── shin
│       └── foot

Make the core a separate bone so that it can:

* pulse
* rotate slightly
* move/scale subtly
* glow visually
* become more exposed in later phases

The armor cracks should be represented by separate geometry or texture details so the core can become visually stronger without replacing the entire model.

MODEL PROPORTIONS:
Make the boss noticeably larger and more imposing than a standard player model.

It should feel like a large Minecraft knight boss, but remain readable in combat and not become absurdly oversized.

TEXTURE:

Create the main texture at an appropriate resolution such as 128×128 or higher if the model complexity requires it.

Texture requirements:

* black/dark iron armor
* aged/broken gold trim
* bright radiant core
* dark red or white cloth
* visible cracks
* corrupted wear
* subtle gradients/highlights
* readable silhouette at normal Minecraft gameplay distance

Do not use copyrighted external textures.
Create an original texture design.

Core colors should visually communicate:

* normal: radiant gold/white
* regenerating: green-gold
* Frostburst: blue-white
* Thorn Cage: dark green
* enrage: red-gold

The model should not depend on emissive textures unless the project already has an appropriate emissive rendering solution.
Where a true emissive layer is not available, simulate glow with:

* bright texture values
* particles
* lighting/particles
* core scaling/pulsing
* renderer effects

ANIMATIONS:

Create these animation names exactly:

animation.solvane.intro
animation.solvane.idle
animation.solvane.walk
animation.solvane.run
animation.solvane.slash
animation.solvane.combo
animation.solvane.shield_bash
animation.solvane.charge
animation.solvane.solar_arc_reaction
animation.solvane.frostburst_reaction
animation.solvane.thorn_cage_reaction
animation.solvane.core_pulse
animation.solvane.core_exposed
animation.solvane.stagger
animation.solvane.phase_two
animation.solvane.phase_three
animation.solvane.enrage
animation.solvane.death

ANIMATION DESIGN:

IDLE:

* subtle breathing
* slight armor movement
* core slowly pulsing

WALK:

* heavy knight movement
* controlled arm swing
* cloak movement

RUN:

* aggressive heavier movement
* stronger cloak motion

SLASH:

* clear wind-up
* rapid sword attack motion
* readable recovery

COMBO:

* multiple distinct sword movements
* enough spacing between hits for gameplay readability

SHIELD BASH:

* shoulder/body rotation
* short preparation
* explosive forward movement

CHARGE:

* aggressive forward posture
* lowered shoulder
* strong anticipation
* impact/recovery

SOLAR ARC REACTION:

* boss briefly raises/guards
* chest core flashes gold
* armor reacts to the impact
* short stagger/reaction

FROSTBURST REACTION:

* body briefly stiffens
* core shifts toward blue-white
* armor joints appear frozen
* subtle shaking/shattering effect

THORN CAGE REACTION:

* body becomes constrained
* chest/core reacts
* roots visually surround lower body
* slight struggle animation

CORE PULSE:

* core scales/pulses
* chest armor subtly reacts
* use repeating but controlled motion

CORE EXPOSED:

* chest armor opens/cracks
* core moves slightly forward
* brighter pulse
* dramatic visual readability

STAGGER:

* strong temporary loss of balance
* knees/body move slightly
* head and shoulders react

PHASE TWO:

* armor becomes visibly more damaged
* core becomes more exposed
* more aggressive idle

PHASE THREE:

* major armor cracks
* core remains visibly exposed
* stronger movement

ENRAGE:

* sharp body motion
* aggressive stance
* rapid core pulsing
* red-gold visual response

DEATH:

* dramatic collapse
* core destabilizes
* armor settles
* no endless looping

ANIMATION CONTROLLERS:

Create clean GeckoLib controllers for:

1. locomotion
2. combat
3. reactions
4. phase/core visuals

Do not play attack animations continuously.
Attack animations should trigger from actual boss state/animation calls.

Idle/walk/run should be controlled from movement velocity/state.

Special animations should have priority over locomotion when necessary.

The following state order should be respected visually:

DEATH
→ PHASE_TRANSITION
→ CORE_EXPOSED
→ STAGGERED
→ FROST_FROZEN
→ THORN_CAGED
→ ATTACK
→ MOVEMENT
→ IDLE

Use non-looping animations for attacks, reactions, transitions, and death.
Use looping animations for idle, movement, regeneration visual pulses, and appropriate phase states.

KEYFRAME EFFECTS:

Use GeckoLib keyframes where appropriate for:

* Solar Arc impact sound
* Frostburst impact sound
* ice-crack particles
* Thorn Cage/root particles
* core pulse effects
* armor crack visual timing
* phase transition effects
* enrage effects

Gameplay effects must still be server-controlled.
Animation keyframes should only trigger presentation/audio/particle effects and must not independently apply boss damage or core damage.

CORE VISUAL STATES:

NORMAL:

* gold/white pulse

REGENERATING:

* green-gold healing streams from surrounding armor toward the core

FROSTBURST ACTIVE:

* blue frost on armor joints
* blue-white core
* ice-like particles

THORN CAGE ACTIVE:

* dark roots around legs/chest
* green/black visual effects

CORE EXPOSED:

* chest plate open
* extremely visible bright core

ENRAGED:

* red-gold core
* stronger glow-like particles
* aggressive movement

RENDERER:

Create a GeckoLib renderer appropriate for the entity.

Requirements:

* correct shadow size for the boss
* correct scaling
* correct texture path
* correct model path
* correct animation path
* compatible with Fabric 1.20.1
* no Forge renderer classes
* no NeoForge renderer classes
* no GeckoLib 5 APIs

If the boss requires custom rendering for the glowing core, implement it in a clean renderer layer or supported GeckoLib rendering mechanism rather than modifying gameplay code.

RESOURCE FILES:

Create all required files in the proper paths, for example:

assets/elemental/geo/sir_solvane.geo.json
assets/elemental/animations/sir_solvane.animation.json
assets/elemental/textures/entity/sir_solvane.png

Use the exact paths expected by the Java model and renderer.

MODEL QUALITY:
The model must be a real multi-bone entity model, not a flat placeholder.
Armor pieces should have depth.
The chest core must be a recognizable focal point.
The cloak should have enough geometry to animate naturally.
The silhouette must be recognizable from the front and side.

DO NOT:

* create a player reskin
* use a simple cube with a helmet
* make the core part of one unanimated body cube
* make every animation just rotate the entire model
* trigger gameplay damage from animation keyframes
* use client-side animation state to determine gameplay outcomes

INTEGRATION:
Connect the animations to the gameplay state created in Prompt 2.

Examples:

* ATTACK_WINDUP → slash wind-up
* ATTACK_ACTIVE → attack animation
* FROST_FROZEN → Frostburst reaction
* THORN_CAGED → Thorn Cage reaction
* CORE_EXPOSED → exposed core animation
* STAGGERED → stagger animation
* PHASE_TRANSITION → phase animation
* ENRAGED → enrage animation
* DEATH → death animation

VALIDATION:
After creating everything:

1. Build the project.
2. Fix model JSON syntax errors.
3. Fix animation JSON syntax errors.
4. Fix GeckoLib API errors.
5. Verify resource paths.
6. Verify the entity renderer loads.
7. Verify the boss does not appear invisible/magenta.
8. Verify idle/walk/run animation works.
9. Verify special animations trigger from actual boss states.
10. Verify death animation plays once.
11. Verify the core changes visually between regeneration states.
12. Verify multiplayer clients receive the correct visual states.

OUTPUT:
Provide:

* all created Java files
* all created JSON files
* texture file
* exact resource paths
* renderer/model integration
* animation controller explanation
* build/compile verification
* any required Blockbench/GeckoLib asset instructions

Do not leave fake placeholder JSON or pseudocode where an actual project file is required.
