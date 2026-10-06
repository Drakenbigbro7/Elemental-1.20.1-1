# Elemental Mod Weapons & Tools Overview

This document explains the weapons and specialized tools available in the Elemental mod for Minecraft 1.20.1. Each item features unique mechanics, special abilities, and thematic elements tied to their respective elemental affinities.

These weapons are specifically designed for use against the Regeneration Knight boss (Sir Solvane, the Everliving Knight), where their abilities interact with the boss's regeneration system to create strategic combat opportunities.

## 1. Sunforged Scimitar

**Item ID:** `elemental:sunforged_scimitar`  
**Type:** Sword (Weapon)  
**Rarity:** Common  
**Primary Affinity:** Solar/Sun

### Core Mechanics
- **Durability:** 300 (balanced between iron and diamond)
- **Base Attack Damage:** 4 (yielding 7-8 total damage with tool material)
- **Attack Speed:** 1.8 attacks per second (faster than standard swords)
- **Repair Ingredient:** Gold Ingot

### Special Abilities

#### Solar-Powered Undead Damage
- Deals bonus magic damage (+3.0) to undead entities (zombies, skeletons, etc.)
- Only active during daylight when the player is exposed to direct sunlight
- Damage type: Magic (bypasses armor)

#### Heat System
- **Heat Level:** Ranges from 0-150, stored in item NBT
- **Heat Gain:** +1 per second in hot biomes (desert, savanna, etc.) under direct sunlight
- **Heat Decay:** -1 per second outside hot biomes or in shade/rain
- **Heat Bonus Damage:** +1.0 fire damage when heat ≥ 100 threshold
- **Visual Feedback:** Tooltip displays current heat level

#### Solar Arc Projectile (Active Ability)
- **Activation:** Hold right-click to charge (minimum 5 ticks, maximum 500 ticks)
- **Projectile:** Launches a Solar Arc entity that deals magic damage
- **Damage Scaling:** Increases with charge level (50%-100% based on charge)
- **Speed Scaling:** Increases with charge and optional Solar Core upgrades
- **Cooldown:** 240 ticks base (12 seconds)
- **Desert Biome Cooldown:** 180 ticks (9 seconds)
- **Sound:** Firecharge-like pitch that varies with charge level

#### Sand Affinity
- **Enhanced Mining:** 8.0x mining speed on sand, red sand, and suspicious sand
- **Suitability:** Can effectively mine sand blocks as if using proper tool

### Upgrade System
- **Solar Core:** Upgradeable system (placeholder for future implementation)
  - Increases projectile speed and damage scaling
  - Currently requires manual NBT modification for testing

### Tooltip Information
- Displays current Heat Level / MAX_HEAT
- Shows Solar Core Level when upgraded (>0)
- Provides real-time feedback on weapon state

### Boss Fight Usage (Regeneration Knight)
In the boss fight against Sir Solvane, the Everliving Knight:
- **Solar Arc** removes exactly 5% of the boss's maximum health
- **Ability cooldown:** 240 ticks (12 seconds) [*Changed from Void_Warden.md: was 80 ticks*]
- **Wind-up:** 15 ticks [*Matches Void_Warden.md*]
- **Active window:** 3 ticks [*Matches Void_Warden.md*]
- **Range:** 8 blocks [*Matches Void_Warden.md*]
- **Damage type:** Solar / special
- **Important:** Solar Arc should not automatically make the boss's health decrease faster than intended. It is best used as a reliable damage tool that creates visible progress, not as an unlimited instant-kill ability.
- **Formula:** `solar_damage = boss_max_health × 0.05`
- For an 800 HP boss: `solar_damage = 800 × 0.05 = 40 HP`

---
## 2. Rootbound Axe

**Item ID:** `elemental:rootbound_axe`  
**Type:** Axe (Weapon/Tool)  
**Rarity:** Common  
**Primary Affinity:** Nature/Forest

### Core Mechanics
- **Durability:** 1800 (between diamond and netherite axe tiers)
- **Base Attack Damage:** 3 (yielding 8-9 total damage with tool material)
- **Attack Speed:** 0.8 attacks per second (slower, typical for axes)
- **Repair Ingredient:** Netherite Scrap

### Special Abilities

#### Wide Sweep Attack
- **Radius:** 6 blocks (larger than vanilla axe ~1.5)
- **Angle:** 180° sweep cone (wider than vanilla ~120°)
- **Damage:** 50% of base attack damage to secondary targets
- **Effects:** Knockback and root particle effects along sweep arc
- **Targeting:** Only affects non-player, non-boss entities

#### Passive: Healing Root
- **Trigger Chance:** 15% on hit, 30% on critical hit
- **Effect:** Spawns a temporary healing area at target's location
- **Healing Effect:** Applies Regeneration to nearby entities
- **Visuals:** GLOW and HAPPY_VILLAGER particles with grass placement sound

##### Forest/Jungle Bonuses
- **Increased Duration:** +3 seconds (60 → 100 ticks)
- **Increased Radius:** +1.5 blocks (3.0 → 4.5)
- **Stronger Regeneration:** +1 amplifier (Regeneration I → II)
- **Increased Regeneration Duration:** +2 seconds (60 → 100 ticks)

#### Active: Thorn Cage (Right-Click)
- **Radius:** 10.0 block area around player
- **Duration:** 5 seconds (100 ticks)
- **Damage:** 2.0 piercing damage per tick (every 0.5 seconds)
- **Slowness:** Applies Slowness II (amplifier 1) for 1 second per tick
- **Visuals:** COMPOSTER, HAPPY_VILLAGER, and CRIT particles
- **Sounds:** Vine placement and player sweep attack sounds
- **Cooldown:** 300 ticks base (15 seconds)
- **Forest/Jungle Biome Cooldown:** 300 ticks (15 seconds)

#### Woodcutting Affinity
- **Enhanced Mining:** 10.0x mining speed on logs and wood
- **Log Stripping:** Can strip logs when appropriate (inherited from AxeItem)
- **Suitability:** Properly identifies log/wood blocks for efficient harvesting

### Fire Vulnerability
- **Lore Mechanic:** Roots (both passive and active) are vulnerable to fire
- **Implementation Note:** Fire/lava damage would cancel root effects early
- **Tooltip Warning:** "Roots are destroyed by fire"

### Tooltip Information
- Lists all major abilities:
  - "Wide sweep attack"
  - "Chance to create healing roots (stronger in forests and jungles)"
  - "Active: Thorn cage - damages and slows enemies"
  - "Roots are destroyed by fire"
  - Shows cooldown times for active ability

### Boss Fight Usage (Regeneration Knight)
In the boss fight against Sir Solvane, the Everliving Knight:
- **Thorn Cage** damages the boss's regenerative core and slows its regeneration
- **Core damage:** 10% of core integrity per hit [*Matches Void_Warden.md*]
- **Regeneration after hit:** 40% of normal [*Matches Void_Warden.md*]
- **Ability cooldown:** 300 ticks (15 seconds) [*Changed from Void_Warden.md: was 240 ticks*]
- **Wind-up:** 25 ticks [*Matches Void_Warden.md*]
- **Cage duration:** 12 seconds visual [*Matches Void_Warden.md*]
- **Root duration on boss:** 100 ticks (5 seconds) [*Matches Void_Warden.md*]
- **Core integrity system:** Starts at 100, Thorn Cage deals 20 damage per use, requires 5 uses to break core [*Matches Void_Warden.md*]
- When core integrity reaches zero:
  - The chest armor breaks
  - The boss loses regeneration permanently
  - The boss enters a staggered state
  - The final phase begins or the boss becomes permanently vulnerable

---
## 3. Frostwake Pick

**Item ID:** `elemental:frostwake_pick`  
**Type:** Pickaxe (Tool)  
**Rarity:** Common  
**Primary Affiliation:** Ice/Cold

### Core Mechanics
- **Durability:** 2000 (higher than diamond pickaxe)
- **Base Attack Damage:** 6 (respectable weapon damage for a tool)
- **Attack Speed:** 0.64 attacks per second (slowest of the three)
- **Repair Ingredient:** Blue Ice

### Special Abilities

#### Ice Mining Affinity
- **Enhanced Mining:**
  - 12.0x speed on ice, packed ice, and blue ice
  - 10.0x speed on snow, snow block, and powder snow
- **Suitability:** Properly identifies ice/snow blocks for efficient harvesting

#### Passive: Chill on Hit
- **Mechanic:** Applies Slowness effect on successful hits
- **Stacking System:** 
  - Max 2 stacks (requires recent hits within 100 ticks)
  - Stack 1: Slowness for 1.5 seconds (30 ticks)
  - Stack 2: Slowness for 3 seconds (60 ticks) + Slowness II
- **Decay:** Stacks reset if no hits for 5 seconds (100 ticks)

#### Active: Frostburst (Right-Click)
- **Radius:** 
  - Base: 3.0 blocks
  - Upgraded (with Glacial Core): 5.0 blocks
- **Effects:**
  - Applies Slowness to entities in area
  - Base: Slowness III (amplifier 2) for 4 seconds
  - Upgraded: Slowness IV (amplifier 3) for 4 seconds
- **Environmental:** Converts still water to ice within radius
- **Visuals:** SNOWFLAKE and ITEM_SNOWBALL particles
- **Sounds:** Glass break and honey bottle drink sounds
- **Cooldown:** 300 ticks base (15 seconds)
- **Snowy Biome Cooldown:** 300 ticks (15 seconds)

#### Glacial Core Upgrade
- **Upgrade System:** Installable core that enhances Frostburst
  - Increases radius from 3.0 to 5.0 blocks
  - Increases slowness level from III to IV
  - Currently requires manual NBT modification for testing

### Tooltip Information
- Displays Glacial Core status when installed
- Shows current Frostburst radius
- Provides usage instructions: "Right-click to unleash Frostburst"
- Lists cooldown times (base and snowy biome)
- Describes passive effect: "Passive: Chills enemies on hit"

### Boss Fight Usage (Regeneration Knight)
In the boss fight against Sir Solvane, the Everliving Knight:
- **Frostburst** freezes the boss's regeneration for 15 seconds
- **Regeneration freeze:** 15 seconds (300 ticks) [*Matches Void_Warden.md*]
- **Ability cooldown:** 300 ticks (15 seconds) [*Changed from Void_Warden.md: was 360 ticks*]
- **Wind-up:** 20 ticks [*Matches Void_Warden.md*]
- **Radius:** 5 blocks [*Matches Void_Warden.md*]
- **Damage:** Low or moderate
- **Main effect:** Regeneration becomes 0%
- **Important:** Do not allow infinite duration stacking. Use the longer remaining duration:
  ```java
  regenerationFrozenTicks = Math.max(regenerationFrozenTicks, 300);
  ```
  Alternatively, allow Frostburst to refresh only after the cooldown ends.
- **Player strategy:** Frostburst is the main damage-window creator. The player should use it when:
  - The boss has lost health from Solar Arc
  - Thorn Cage has already weakened regeneration
  - The boss is not in an invulnerable phase transition
  - The player is ready to commit to attacking the core

---
## Weapon Comparison Summary

| Weapon | Primary Role | Attack Speed | Special Mechanic | Biome Dependency |
|--------|--------------|--------------|------------------|------------------|
| Sunforged Scimitar | Sword/DPS | Fast (1.8/sec) | Heat system + Solar Arc | Desert (cooldown reduction) |
| Rootbound Axe | Aoe/Crowd Control | Slow (0.8/sec) | Healing roots + Thorn Cage | Forest/Jungle (bonuses) |
| Frostwake Pick | Utility/Control | Very Slow (0.64/sec) | Chilling + Frostburst | Snowy (cooldown reduction) |

### Boss Fight Roles
| Weapon | Boss Fight Role | Key Ability | Effect on Boss |
|--------|----------------|-------------|----------------|
| Sunforged Scimitar | Health Removal | Solar Arc | Removes 5% of max HP |
| Frostwake Pick | Regeneration Control | Frostburst | Freezes regeneration for 15s |
| Rootbound Axe | Core Weakening | Thorn Cage | Damages core, reduces regen to 40% |

## Design Philosophy

Each weapon in the Elemental mod follows these design principles:
1. **Thematic Consistency:** Abilities and mechanics align with elemental affinities (solar, nature, ice)
2. **Risk/Reward Systems:** Resources that must be managed (heat, charges, cooldowns)
3. **Environmental Interaction:** Behavior changes based on biome and world conditions
4. **Upgrade Paths:** Systems allowing for future enhancement (Solar Core, Glacial Core)
5. **Visual & Audio Feedback:** Distinct particle effects and sounds for each ability
6. **Tool Functionality:** All maintain their primary tool purposes alongside combat abilities
7. **Boss Fight Synergy:** Weapons are designed to work together in a specific sequence against the Regeneration Knight

## Boss Fight Mechanics & Strategy

### Regeneration System
The boss has a base regeneration of 2% of maximum health per second (16 HP/sec for 800 HP boss). The three weapons interact with this system differently:

- **Solar Arc:** Directly removes health (5% of max HP per use)
- **Frostburst:** Temporarily stops regeneration (0% for 15 seconds)
- **Thorn Cage:** Permanently reduces regeneration rate (to 40% after each use)

### Recommended Ability Sequence
The intended encounter rhythm is:
```text
1. Thorn Cage (weakens regeneration)
2. Frostburst (stops regeneration)
3. Solar Arc (removes health)
4. Attack during the frozen regeneration window
5. Repeat until the core breaks
```

The reason for using Thorn Cage first is to reduce future regeneration before spending the Frostburst window.

### Alternative Sequences
While the recommended sequence is optimal, other orders are valid:
```text
Solar Arc → Frostburst → Thorn Cage → attack
```
The fight should require all three weapons but should not punish every order unnecessarily.

### Core Integrity System
- Starts at 100 points
- Each Thorn Cage hit reduces integrity by 20 points
- At 0 integrity, the core is broken and regeneration is permanently disabled
- Requires 5 successful Thorn Cage hits to break the core

### Regeneration States
| State | Regeneration Rate |
|-------|------------------|
| Normal | 100% |
| Thorn Cage only | 40% |
| Frostburst only | 0% for 15 seconds |
| Frostburst + Thorn Cage | 0% for 15 seconds, then 40% |
| Core broken | 0% |

---
## Implementation Notes

- All weapons use NBT data to store state information (heat levels, charges, timers, etc.)
- Active abilities typically consume durability and trigger cooldowns
- Passive abilities trigger on hit/crit with probabilistic chances
- Boss fight mechanics use additional NBT/data tracking for:
  - Boss regeneration state (NORMAL, FROZEN, SLOWED, FROZEN_AND_SLOWED, DISABLED)
  - Core integrity (0-100)
  - Regeneration frozen ticks
  - Regeneration multiplier
- Particle effects utilize vanilla Minecraft particles for performance
- Sound effects use existing vanilla sounds where appropriate
- Server-side validation ensures abilities only work with correct weapons
- GeckoLib animations provide visual feedback for both weapons and boss reactions