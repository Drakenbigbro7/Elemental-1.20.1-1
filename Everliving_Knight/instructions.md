You are a professional Minecraft Java/Fabric 1.20.1 developer.

Implement the complete server-side entity, AI, combat, phase, regeneration, core, and arena systems for:

# Sir Solvane, the Everliving Knight

Entity ID:

`elemental:everliving_knight`

## EXACT ENVIRONMENT

* Minecraft: 1.20.1
* Yarn mappings: 1.20.1+build.10
* Fabric Loader: 0.19.5
* Fabric Loom: 1.18-SNAPSHOT
* Fabric API: 0.92.12+1.20.1
* Mod ID: elemental
* Java: use the Java version compatible with this exact Minecraft/Fabric 1.20.1 project.
* GeckoLib 4 compatible with Minecraft 1.20.1.

The attached `EVERLIVING_KNIGHT_GUIDE(1).md` is the authoritative mechanics specification.

Do not simplify, remove, or replace the mechanics specified there.

# 1. BASE ATTRIBUTES

Implement:

* Max Health: 800 HP
* Base Attack Damage: 14.0
* Armor: 16
* Armor Toughness: 6.0
* Knockback Resistance: 0.9
* Movement Speed: 0.25
* Follow Range: 48 blocks

Boss bar:

* Red progress bar
* Name: `Sir Solvane, the Everliving Knight`

# 2. TARGETING AND AI

Implement dedicated AI goals where appropriate.

The boss must:

* Detect players within 48 blocks.
* Prioritize Survival and Adventure players.
* Also track Creative players for testing.
* Chase targets using navigation.
* Use approximately 1.35x chase speed.
* Use 1.6x chase speed during Phase 3.
* Switch from pursuit to attack behavior when the target enters attack range.

Create clean AI architecture instead of putting every decision into `tick()`.

Suggested goal classes:

`KnightTargetGoal`

`KnightChaseTargetGoal`

Additional attack goals/classes may be created if useful.

# 3. ATTACK STATE MACHINE

The boss uses:

`windup -> active -> recovery -> cooldown`

Do not allow attacks to execute repeatedly every tick.

Each attack must have an independent cooldown and clearly defined timing.

## MELEE SLASH

ID:
`slash`

Trigger:
Target between 4.0 and 5.5 blocks.

Windup:
12 ticks.

Active:
3 ticks.

Damage:
16 physical damage.

Recovery:
20 ticks.

Cooldown:
30 ticks.

During windup:

* Turn toward target.
* Trigger `slash_windup`.

During attack:

* Trigger `slash`.
* Damage entities within reach.

Prevent the attack from hitting the same target multiple times during the same active window unless the mechanics explicitly require it.

# 4. SHIELD BASH

ID:

`shield_bash`

Trigger:
Target within 4 blocks.

Windup:
10 ticks.

Active:
5 ticks.

Attack shape:
4-block directional cone.

Cone:
approximately 45 degrees in front of the boss.

Damage:
10 physical damage.

Effects:

* Strong directional knockback.
* Slowness X for 10 ticks.

Recovery:
15 ticks.

Cooldown:
80 ticks.

Trigger:
`shield_bash_windup`
then
`shield_bash`

The boss must face the attack direction and the cone must actually respect its facing direction.

# 5. CHARGE / RUSH

ID:

`charge`

Trigger:
Target between 4 and 18 blocks.

Windup:
18 ticks.

During windup:

* Face/lock onto the target position.
* Trigger `charge_windup`.

Execution:
Dash toward the locked target position with approximately 2.5x speed vector.

Damage:
20 physical damage.

Effects:

* 1.5x horizontal knockback.
* +0.5 vertical knockback.

Recovery:
30 ticks.

Cooldown:
120 ticks.

Trigger:
`charge_windup`
then
`charge`.

The charge must not continuously redirect toward the player after the dash begins.

# 6. RADIANT CORE PULSE

ID:

`core_pulse`

Windup:
20 ticks.

Radius:
6 blocks.

Damage:
10 magic damage.

The damage must bypass normal physical armor as specified by the mechanics.

Effects:

* Radial outward knockback.
* Flash particles.
* End rod radial particles.
* Explosion-style audio cue.

Recovery:
20 ticks.

Cooldown:
100 ticks.

Probability:

* Phase 1: 8%
* Phase 2: 12%
* Phase 3: 20%

Trigger:
`core_pulse_windup`
then
`core_pulse`

# 7. PHASE SYSTEM

Three phases:

Phase 1:
100% -> 70%

Phase 2:
70% -> 35%

Phase 3:
35% -> 0%

## PHASE 1

Normal attacks.

Regeneration:
2% maximum health per second = 16 HP/s.

## PHASE 2

Triggered below 70% health / 560 HP.

Trigger animation:

`phase_two`

Implement Healing Flames as a separate system/entity/helper if appropriate.

Core Pulse frequency increases by 50%.

## PHASE 3

Triggered below 35% health / 280 HP.

Trigger:

`phase_three`

Start a 30-second / 600 tick enrage timer.

When enrage activates:

Trigger:

`enrage`

Attack Damage:
21.0

Movement:
1.6x chase speed.

Core Pulse chance:
20%.

Healing flames are removed/dispelled.

# 8. IMMORTAL CORE MECHANIC

This is a CRITICAL mechanic.

The boss must NOT die while:

`coreBroken == false`

If incoming damage would reduce health below 1 HP:

* Clamp health to 1 HP.
* Prevent death.
* Keep the entity alive.
* Allow players to continue interacting with the boss.

The core must initially have:

`100 integrity`

# 9. REGENERATION

Normal regeneration:

16 HP/s.

Implement tick-based healing carefully so it results in approximately 16 HP per second rather than accidentally healing 16 HP every tick.

Healing modifiers:

Normal:
100% = 16 HP/s

Thorn Cage:
40% = 6.4 HP/s

Frostburst:
0% for 300 ticks.

Frostburst + Thorn Cage:
0% for 300 ticks, then 40%.

When core is broken:
0% permanently.

# 10. CORE BREAKING

Each successful Thorn Cage elemental interaction:

`coreIntegrity -= 20`

When integrity reaches 0:

* `coreBroken = true`
* Regeneration permanently becomes 0.
* Trigger `core_exposed`.
* Enter staggered state.
* Trigger `stagger`.
* Boss becomes fully vulnerable to lethal damage.

Do not allow core integrity to become negative.

# 11. ELEMENTAL REACTION API

Create a clean method/API that other systems can call later, for example:

`applyThornCageReaction()`

`applyFrostburstReaction()`

These methods must:

* Apply the correct healing modification.
* Update core integrity where applicable.
* Trigger appropriate state changes.

Do not hard-code these reactions into unrelated weapon classes.

# 12. STAGGER

When the core breaks:

* Stop normal attacks temporarily.
* Make the boss vulnerable to lethal damage.
* Trigger `stagger`.
* Expose the core.
* Prevent regeneration.

The exact stagger duration should be implemented as a clearly named constant so it can be changed easily.

# 13. ARENA BOUNDARY

Arena radius:

14 blocks.

The boss has a designated arena center.

If it moves more than 14 blocks away:

* Apply a soft force/velocity correction toward the center.
* Do not teleport the boss abruptly.
* Do not break navigation unnecessarily.

Create reusable methods for setting/getting the arena center.

# 14. DAMAGE HANDLING

Implement damage handling carefully.

The following must work:

* Normal player attacks.
* Projectile damage.
* Magic damage.
* Environmental damage where appropriate.
* Core protection.
* Core-broken lethal damage.

Avoid creating an infinite damage/heal loop.

Do not use unsafe mixins unless absolutely necessary.

Prefer overriding the appropriate entity damage/death methods where Fabric/Yarn 1.20.1 provides suitable hooks.

# 15. SERVER/CLIENT SEPARATION

Server:

* AI
* attacks
* damage
* regeneration
* phases
* core
* cooldowns
* arena
* boss bar

Client:

* rendering
* GeckoLib animation controller
* animation triggers
* visual effects

Do not perform authoritative combat calculations only on the client.

# 16. CODE QUALITY

Create separate classes when appropriate.

Possible structure:

`EverlivingKnightEntity`

`KnightTargetGoal`

`KnightChaseTargetGoal`

`KnightAttackController`

`KnightCombatState`

`KnightBossPhase`

`KnightCoreSystem`

`KnightAttackType`

Do not blindly follow these names if a better architecture is appropriate, but maintain modularity.

# 17. COMPATIBILITY

Absolutely do NOT:

* use Forge APIs
* use NeoForge APIs
* use Minecraft 1.21+ methods
* use newer Yarn mappings
* use newer Fabric API methods
* invent nonexistent 1.20.1 classes/methods

Before producing code, verify every API against Minecraft 1.20.1 Yarn mappings.

# OUTPUT

Create the complete required source files.

For every file:

1. Exact project path.
2. Complete code.
3. Purpose.
4. Any required changes to existing files.
5. Any required build.gradle dependency.

At the end provide:

* `/summon elemental:everliving_knight`
* useful testing commands
* a concise test checklist for every attack and phase.

Do NOT implement crafting recipes.
Do NOT create unrelated weapons/items.
Do NOT modify the mod ID.
