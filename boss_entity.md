/mYou are a senior Minecraft Fabric 1.20.1 Java developer specializing in custom boss entities, hostile AI, GeckoLib entities, synchronized state, boss bars, damage handling, and multiplayer-safe server-side mechanics.

The project is:

* Minecraft 1.20.1
* Yarn mappings 1.20.1+build.10
* Fabric Loader 0.19.5
* Fabric Loom 1.18-SNAPSHOT
* Fabric API 0.92.12+1.20.1
* Mod ID: elemental
* Boss: Sir Solvane, the Everliving Knight

The prerequisite setup has already been completed by Prompt 1.

YOUR TASK:
Implement the COMPLETE server-side gameplay entity for Sir Solvane.

Do not create the final GeckoLib model or texture in this prompt.
Do not create crafting recipes.
Do not rewrite the three weapon implementations unless a very small compatibility hook is required.

BOSS IDENTITY:
Name: Everliving Knight
Role: armored regeneration knight
Theme: cursed knight sustained by a radiant regenerative core

BASE STATS:

* Max health: 800 HP
* Attack damage: 14
* Armor: 16
* Armor toughness: 6
* Knockback resistance: 0.9
* Movement speed: 0.25

The boss should behave as a real combat entity rather than simply being a large-health mob.

REGENERATION SYSTEM:

Base regeneration:

* 2% of maximum health per second
* For 800 HP this equals 16 HP/sec

Implement regeneration server-side.

Recommended calculation:
healAmount = 0.02f * getMaxHealth() * regenerationMultiplier / 20.0f

Required fields/data:

* regenerationFrozenTicks
* regenerationMultiplier
* coreIntegrity
* coreBroken
* current SolvaneState
* current RegenerationState
* current phase
* ability cooldown timers
* attack/action timers
* enrage timer where required

Regeneration states:

NORMAL:
100% regeneration

THORN_CAGE:
40% regeneration

FROSTBURST:
0% regeneration for 300 ticks

FROSTBURST + THORN_CAGE:
0% regeneration for the remaining 300 ticks, then 40%

CORE BROKEN:
0% regeneration permanently

If the core is broken:

* regenerationFrozenTicks = 0
* regenerationMultiplier = 0
* regeneration must never resume

CORE SYSTEM:

Initial core integrity:
100

Each successful Thorn Cage boss interaction:

* subtract 20 integrity

At 0:

* coreBroken = true
* permanent regeneration disable
* chest/core becomes exposed
* boss enters staggered state
* final vulnerability begins

The boss must not be killable before the core breaks.

Before the core breaks:

* Boss minimum health is 1 HP.

After the core breaks:

* The boss can die normally.

Do not repeatedly call healing or damage in a way that causes recursive events or duplicate server processing.

WEAPON/BOSS INTERACTION:

The boss must integrate with the three existing weapons:

1. Sunforged Scimitar
   Ability:
   Solar Arc

Boss effect:

* removes exactly 5% of the boss's maximum health
* formula:
  bossMaxHealth * 0.05

For 800 HP:
40 HP

Server must verify that the interaction came from the actual registered Sunforged Scimitar ability.
Do not trust client-provided damage values.

Solar Arc boss-specific parameters:

* Wind-up: 15 ticks
* Active window: 3 ticks
* Range: 8 blocks

Do not turn the Solar Arc into ordinary weapon damage.
It is a special maximum-health-based boss interaction.

When successfully hit:

* reduce boss health by 5% of max health
* trigger the solar reaction state/animation
* play appropriate particles/sound
* preserve current regeneration state

2. Frostwake Pick
   Ability:
   Frostburst

Boss effect:

* regeneration becomes 0
* duration: 300 ticks
* radius: 5 blocks

When successful:

* regenerationFrozenTicks = Math.max(regenerationFrozenTicks, 300)
* regeneration stops immediately
* boss enters/uses the Frost Frozen reaction state
* synchronize the visual state to clients

Do not allow repeated activation to stack to unlimited duration.

3. Rootbound Axe
   Ability:
   Thorn Cage

Boss effect:

* core integrity decreases by 20
* regeneration multiplier becomes 0.40
* boss is rooted/slowed for 100 ticks
* visual cage/reaction occurs
* core becomes progressively damaged

When core reaches 0:

* break core
* disable regeneration permanently
* transition into staggered/final vulnerable state

ABILITY VALIDATION:
Use item identity checks such as:
stack.is(ModItems.SUNFORGED_SCIMITAR)
stack.is(ModItems.FROSTWAKE_PICK)
stack.is(ModItems.ROOTBOUND_AXE)

Never use:

* item display names
* lore
* translated names
* arbitrary strings
* client-submitted identifiers

PHASE SYSTEM:

PHASE 1:
Health 100%–70%

Abilities:

* basic sword combo
* shield bash
* knightly charge
* regeneration telegraph

No major arena hazards.

PHASE 2:
Health 70%–35%

Abilities:

* faster regeneration pulses/telegraph
* core becomes more visible
* Solar Shield Bash
* summon up to 2 healing flames

Healing flames:

* maximum 2 active
* 45 HP each
* restore 1% boss health per second
* must be destroyable by the custom weapon mechanics
* disappear when final vulnerability begins

PHASE 3:
Health 35%–0%

Abilities:

* wider sword combo
* exposed/cracked armor
* more frequent regeneration pulses
* 30-second enrage timer
* all three custom abilities remain relevant

Make phase transitions deterministic and server-authoritative.

IMPORTANT:
Do not immediately retrigger a phase transition every tick.
Store the current phase and only execute transition logic when the phase actually changes.

COMBAT ABILITIES:

KNIGHT SLASH:

* Damage: 16
* Range: 4 blocks
* Wind-up: 12 ticks
* Active window: 3 ticks
* Recovery: 20 ticks
* Cooldown: 30 ticks

SHIELD BASH:

* Damage: 10
* High knockback
* Stun: 10 ticks
* Cooldown: 80 ticks
* Front-facing attack cone

KNIGHTLY CHARGE:

* Distance: 9 blocks
* Damage: 20
* Warning: 18 ticks
* Cooldown: 120 ticks
* Recovery after miss: 30 ticks

CORE PULSE:

* Radius: 6 blocks
* Damage: 10
* Warning: 20 ticks
* Cooldown: 100 ticks

Use a state/timer-based ability system instead of putting every attack in one giant tick method.

ARENA BOUNDARY:
The boss fight is intended for a 29 × 29 block arena.

Implement a clean boundary check so the boss does not wander indefinitely outside the intended combat region.

Do not permanently trap the boss with an exploitative hard teleport every tick.
Use sensible repositioning/pathfinding/boundary behavior.

BOSS AI:
Implement goals for:

* target acquisition
* melee pursuit
* combat attacks
* charge
* shield bash
* phase-specific abilities
* healing flame management where appropriate

Avoid conflicting goals constantly overriding one another.

Use the SolvaneState priority:

DEATH
→ PHASE_TRANSITION
→ CORE_EXPOSED
→ STAGGERED
→ FROST_FROZEN
→ THORN_CAGED
→ ATTACK
→ CHASE
→ PATROL

BOSS BAR:
Create a vanilla boss bar:
"Sir Solvane, the Everliving Knight"

Keep it synchronized with nearby players.

Also provide state information such as:

* Regeneration: Active
* Regeneration: Frozen
* Regeneration: Slowed
* Core Integrity
* Core Broken

Do not flood chat every tick.

DAMAGE HANDLING:
Before core break:

* Never let the boss die.
* If incoming damage would reduce health below 1 HP, clamp to 1 HP.

After core break:

* normal death is allowed.

Prevent duplicate ability damage from being applied multiple times in the same tick/window.

MULTIPLAYER:
This must work with 1–4 players.

Server authority must control:

* core integrity
* regeneration
* phase
* ability cooldowns
* Solar Arc damage
* Frostburst duration
* Thorn Cage effect
* healing flames
* enrage
* death

Client synchronization should be used for visual state only.

NBT/PERSISTENCE:
Persist required boss state through Minecraft entity data/NBT:

* phase
* coreIntegrity
* coreBroken
* regenerationFrozenTicks
* regenerationMultiplier
* required persistent timers/state that must survive saves

Do not persist unnecessary transient client-only data.

ANIMATION HOOKS:
Create clean methods/events that Prompt 3 can connect to:

* intro
* idle
* walk
* run
* slash
* combo
* shield bash
* charge
* Solar Arc reaction
* Frostburst reaction
* Thorn Cage reaction
* core pulse
* core exposed
* stagger
* phase two
* phase three
* enrage
* death

The gameplay logic must not depend on the model existing.

IMPORTANT CODE QUALITY RULES:

* Use Yarn 1.20.1 names.
* Do not use Forge APIs.
* Do not use NeoForge APIs.
* Do not use Minecraft versions newer than 1.20.1 APIs.
* Do not use GeckoLib 5 APIs.
* Avoid reflection unless absolutely necessary.
* Avoid hard-coded client authority.
* Keep code modular.
* Prefer helper methods over huge tick methods.
* Add comments around complicated combat/state transitions.

TEST REQUIREMENTS:
Verify at minimum:

1. Boss spawns correctly.
2. Boss has exactly 800 max HP.
3. Base regeneration is 16 HP/sec at 800 HP.
4. Boss cannot die before core break.
5. Thorn Cage reduces core by 20.
6. Five successful Thorn Cage hits break the core.
7. Frostburst stops regeneration for 300 ticks.
8. Frostburst duration cannot stack infinitely.
9. Solar Arc removes exactly 5% max HP.
10. Phase transitions occur at 70% and 35%.
11. Boss bar updates.
12. Multiplayer synchronization works.
13. Death happens once.
14. Loot/death logic does not duplicate.
15. Boss state survives save/reload where appropriate.

OUTPUT:
Create the actual Java files required.
List every created/modified file.
Explain how the entity registration connects to the boss.
Explain how the three weapon abilities communicate with the boss.
Run a build/compile check and fix all compile errors before finishing.
