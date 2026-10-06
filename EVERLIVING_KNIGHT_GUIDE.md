# Sir Solvane, the Everliving Knight - Boss Combat & Mechanics Guide

## 1. Overview & Identity

**Sir Solvane, the Everliving Knight** (`elemental:everliving_knight`) is a formidable armored boss mob cursed and sustained by an ancient radiant regenerative core. Unlike typical mobs, Sir Solvane features a multi-phase fight, telegraphed combat abilities, elemental interactions, and an invulnerability mechanic tied to his core integrity.

### Base Statistics
| Attribute | Value | Description |
| :--- | :--- | :--- |
| **Max Health** | 800 HP (400 Hearts) | Large boss health pool |
| **Base Attack Damage** | 14.0 | High physical damage (scales up in Phase 3) |
| **Armor** | 16 | Heavy defense against raw damage |
| **Armor Toughness** | 6.0 | Resistance against high-damage attacks |
| **Knockback Resistance**| 0.9 (90%) | Near immunity to standard knockback |
| **Base Movement Speed**| 0.25 (chase: 1.35x / 1.6x) | Agile armored pursuer |
| **Follow Range** | 48 Blocks | Long detection and engagement radius |
| **Boss Bar** | Red Progress Bar | Labeled *"Sir Solvane, the Everliving Knight"* |

---

## 2. Combat Moves & Attack Abilities

Sir Solvane utilizes a telegraphed attack system with windups, active damage windows, recoveries, and cooldowns.

### ⚔️ 1. Melee Slash (`slash`)
* **Trigger Condition:** Target is within **4.0 - 5.5 blocks** and cooldown has elapsed.
* **Windup:** 12 ticks (0.6s) accompanied by the `slash_windup` animation. The knight turns to face the target.
* **Active Damage:** 3 ticks.
* **Damage:** **16.0 physical damage** to all entities within reach.
* **Recovery:** 20 ticks (1.0s).
* **Cooldown:** 30 ticks (1.5s).
* **Counterplay:** Backpedal or roll backwards during the windup animation.

---

### 🛡️ 2. Shield Bash (`shield_bash`)
* **Trigger Condition:** Target is within **4.0 blocks** (approx. 35% chance per tick when ready).
* **Windup:** 10 ticks (0.5s) accompanied by the `shield_bash_windup` animation.
* **Active Damage:** 5 ticks in a **4-block directional cone** (~45° in front of the boss).
* **Damage:** **10.0 physical damage**.
* **Special Effects:**
  * Heavy directional knockback pushing players backward.
  * **Stun Effect:** Inflicts **Slowness X for 10 ticks** (0.5s), temporarily rooting the target in place.
* **Recovery:** 15 ticks (0.75s).
* **Cooldown:** 80 ticks (4.0s).
* **Counterplay:** Dodge sideways or strafe around the boss to avoid the front-facing cone.

---

### ⚡ 3. Charge / Rush (`charge`)
* **Trigger Condition:** Target is at medium-to-long range (**4.0 to 18.0 blocks**).
* **Warning / Windup:** 18 ticks (0.9s) accompanied by `charge_windup`. The knight locks eyes and charges forward momentum.
* **Execution:** High-velocity dash (**2.5x speed vector**) toward the locked player coordinates.
* **Damage:** **20.0 physical damage** to all entities intercepted along the path.
* **Special Effects:** Strong vertical and horizontal knockback (`1.5x` horizontal, `+0.5` vertical).
* **Recovery:** 30 ticks (1.5s) upon completing the rush.
* **Cooldown:** 120 ticks (6.0s).
* **Counterplay:** Sprint perpendicular to the knight's charge path to let him overshoot.

---

### 💥 4. Radiant Core Pulse (`core_pulse`)
* **Trigger Condition:** Proximity move with phase-dependent frequency:
  * **Phase 1:** 8% chance per ready cycle
  * **Phase 2:** 12% chance per ready cycle
  * **Phase 3:** 20% chance per ready cycle
* **Warning / Windup:** 20 ticks (1.0s) accompanied by `core_pulse_windup`.
* **Execution:** Radial AoE shockwave with a **6.0-block blast radius**.
* **Damage:** **10.0 magic damage** (bypasses normal physical armor).
* **Special Effects:**
  * Centered outward knockback launching all nearby targets back.
  * Visual flash particles (`FLASH`) and end rod radial bursts (`END_ROD`).
  * Explosion audio cue (`ENTITY_GENERIC_EXPLODE`).
* **Recovery:** 20 ticks (1.0s).
* **Cooldown:** 100 ticks (5.0s).
* **Counterplay:** Disengage immediately and get at least 6 blocks away when the core glows.

---

## 3. Boss Phases

Sir Solvane transitions across three distinct phases based on remaining health percentage:

```
[ Phase 1: 100% - 70% HP ] ---> [ Phase 2: 70% - 35% HP ] ---> [ Phase 3: 35% - 0% HP ]
       Armored Knight                 Healing Flames                 Enraged & Relentless
```

### Phase 1: The Resilient Sentinel (100% - 70% Health)
* Standard attack patterns: Slash, Shield Bash, Charge, and occasional Core Pulses.
* Default baseline regeneration: **2% max health per second (16 HP/s)**.

### Phase 2: Radiant Renewal (70% - 35% Health)
* Trigger: Health drops below 70% (560 HP). Plays `phase_two` animation.
* **Healing Flames:** Summons regenerative flame spirits to assist his healing.
* Core Pulse frequency increases by 50%.

### Phase 3: Unchained Fury / Enrage (35% - 0% Health)
* Trigger: Health drops below 35% (280 HP). Plays `phase_three` animation.
* **Enrage Timer:** Initiates a 30-second countdown (600 ticks). When triggered (`enrage` animation):
  * **Attack Damage:** Increases by +50% (base damage becomes **21.0**).
  * **Movement Speed:** Increases by +30% (chase speed becomes **1.6x**).
* Core Pulse frequency reaches maximum (20%).
* Healing flames are dispelled to concentrate raw offensive power.

---

## 4. The Core & Regeneration System

### 🛡️ Immortality Condition
> **Crucial Mechanic:** Sir Solvane **cannot die** while his core is intact! If his health drops to 1 HP, damage is clamped and he remains alive until players break his core.

### 💖 Regeneration System
* **Base Rate:** `16 HP / second` (2% of max HP).
* Modifiable through elemental reactions:
  * **Normal State:** 100% healing rate (`16 HP/s`).
  * **Thorn Cage Reaction:** Healing reduced to **40%** (`6.4 HP/s`).
  * **Frostburst Reaction:** Healing reduced to **0%** (frozen) for 300 ticks (15 seconds).
  * **Frostburst + Thorn Cage:** Healing frozen for 300 ticks, then resumes at 40%.
  * **Core Broken:** Healing permanently reduced to **0%** for the remainder of the battle.

### 💔 Breaking the Core
* Initial Core Integrity: **100 points**.
* Each successful **Thorn Cage** elemental interaction shreds **20 integrity**.
* When integrity reaches **0**:
  * `coreBroken = true`
  * Regeneration is **permanently disabled**.
  * The knight's chest plate exposes the shattered core (`core_exposed` animation).
  * The knight enters a **Staggered State** (`stagger` animation), becoming completely vulnerable to lethal blows.

---

## 5. AI Navigation & Arena Boundaries

* **Targeting (`KnightTargetGoal`):**
  * Actively detects and tracks players up to **48 blocks** away.
  * Prioritizes Survival and Adventure mode players.
  * Gracefully tracks Creative mode players during testing to showcase animations and mechanics.
* **Pursuit (`KnightChaseTargetGoal`):**
  * Continuously updates pathfinding navigation toward targets at **1.35x speed** (**1.6x speed in Phase 3**).
  * Seamlessly passes controls to attack goals once within strike range.
* **Arena Boundary Enforcement:**
  * If the boss drifts more than **14 blocks** from the designated arena center (`ARENA_RADIUS = 14`), soft velocity forces gently guide the knight back into the battle perimeter.

---

## 6. GeckoLib Animations & Network Synchronization

All animations are registered via GeckoLib 4 with synchronized server-to-client network triggers:

| Animation Name | Trigger Identifier | Description |
| :--- | :--- | :--- |
| `animation.everliving_knight.idle` | `idle` | Breathing idle pose |
| `animation.everliving_knight.walk` | Auto | Normal strolling pathfinding |
| `animation.everliving_knight.run` | Auto | High-speed pursuit sprint |
| `animation.everliving_knight.slash_windup` | `slash_windup` | Sword raised windup |
| `animation.everliving_knight.slash` | `slash` | Powerful horizontal cleave |
| `animation.everliving_knight.shield_bash_windup` | `shield_bash_windup`| Bracing shield forward |
| `animation.everliving_knight.shield_bash` | `shield_bash` | Shield ram impact |
| `animation.everliving_knight.charge_windup` | `charge_windup` | Low stance rush preparation |
| `animation.everliving_knight.charge` | `charge` | Full-speed charging rush |
| `animation.everliving_knight.core_pulse_windup`| `core_pulse_windup` | Core glowing energy intake |
| `animation.everliving_knight.core_pulse` | `core_pulse` | Radiant shockwave explosion |
| `animation.everliving_knight.stagger` | `stagger` | Stunned, kneeling vulnerability pose |
| `animation.everliving_knight.core_exposed` | `core_exposed` | Cracked, exposed chest core |
| `animation.everliving_knight.phase_two` | `phase_two` | Phase 2 transition roar |
| `animation.everliving_knight.phase_three` | `phase_three` | Phase 3 rage transition |
| `animation.everliving_knight.enrage` | `enrage` | Enrage empowerment stance |
| `animation.everliving_knight.death` | `death` | Final defeat animation |
