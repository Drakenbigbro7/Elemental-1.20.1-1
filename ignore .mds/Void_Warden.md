# First Boss Design: The Regeneration Knight

## Overview

The **Regeneration Knight** is an armored knight boss designed around a regeneration puzzle. The player cannot defeat it by simply dealing normal damage. Its regeneration must be controlled with three custom weapons:

- **Sunforged Scimitar** — uses **Solar Arc** to take down 5% of the boss's maximum health.
- **Frostwake Pick** — uses **Frostburst** to freeze the boss's regeneration for 15 seconds.
- **Rootbound Axe** — uses **Thorn Cage** to damage the boss's core and slow its regeneration.

The intended encounter rhythm is:

```text
Solar Arc removes health
        ↓
Frostburst stops regeneration for 15 seconds
        ↓
Thorn Cage damages the core and slows future regeneration
        ↓
Use the regeneration-free window to deal meaningful damage
```

The boss is a knight who has an exposed regenerative core beneath its armor. The three weapons do not perform the same job: the scimitar removes health, the pick controls time, and the axe weakens the source of regeneration.

## Boss identity

| Property | Value |
|---|---|
| Name | Sir Solvane, the Everliving Knight |
| Role | Armored regeneration knight |
| Theme | A cursed knight kept alive by a radiant core |
| Recommended players | 1–4 |
| Recommended equipment | Iron to diamond armor plus the three custom weapons |
| Base health | 800 HP |
| Base attack damage | 14 |
| Armor | 16 |
| Armor toughness | 6 |
| Knockback resistance | 0.9 |
| Movement speed | 0.25 |
| Arena size | 29 × 29 blocks |
| Main weakness | Regeneration core exposed by weapon abilities |
| Main defense | Rapid regeneration |
| Phases | 3 |

## Core design principle

The boss should be difficult because the player must create and use short damage windows, not because the boss has an unreasonable amount of health.

### Weapon roles

| Weapon | Special ability | Main purpose |
|---|---|---|
| Sunforged Scimitar | Solar Arc | Removes 5% of maximum health |
| Frostwake Pick | Frostburst | Stops regeneration for 15 seconds |
| Rootbound Axe | Thorn Cage | Damages the core and slows regeneration |

## Boss visual design

### Appearance

- Tall, heavily armored knight.
- Broken gold-and-black plate armor.
- A glowing core visible through cracks in the chest plate.
- A tattered white or dark-red cloak.
- A large knight helmet with a narrow solar visor.
- Regeneration energy flows from the core through the armor.
- Roots appear around the boots when Thorn Cage is active.
- Frost spreads across the armor when Frostburst is successful.

### Visual states

| State | Visual effect |
|---|---|
| Normal | Gold particles flow from the chest core |
| Regenerating | Green-gold healing streams connect to the core |
| Frostburst active | Blue frost covers armor joints |
| Thorn Cage active | Dark roots surround the chest and legs |
| Core exposed | Chest plate opens and core pulses white |
| Enraged | Red-gold light leaks from the helmet and armor |

## Arena

The fight takes place in a circular knightly arena.

### Arena features

- Central stone platform.
- Four knight statues at the edges.
- A cracked altar behind the boss.
- Four pillars that can block projectiles but cannot permanently trap the boss.
- A visible core symbol in the floor.
- No random holes or unavoidable environmental damage.

### Arena rules

- The boss remains inside a 29 × 29 boundary.
- The entrance closes when the fight begins.
- The boss resets only after all players leave for 20 seconds.
- The arena is protected from ordinary block destruction.

## Regeneration system

The regeneration system is the center of the encounter.

### Base regeneration

```text
Base regeneration: 2% of maximum health per second
```

For an 800 HP boss:

```text
Base regeneration: 16 HP per second
```

This is strong enough to punish passive damage but not so strong that the boss is impossible to defeat.

### Regeneration states

```java
public enum RegenerationState {
    NORMAL,
    FROZEN,
    SLOWED,
    FROZEN_AND_SLOWED,
    DISABLED
}
```

### Regeneration modifiers

| State | Regeneration |
|---|---:|
| Normal | 100% |
| Thorn Cage only | 40% |
| Frostburst only | 0% for 15 seconds |
| Frostburst + Thorn Cage | 0% for 15 seconds, then 40% |
| Core broken | 0% |

The Frostwake Pick should create the important short-term damage window, while the Rootbound Axe provides a longer-lasting improvement to the fight.

## Special ability 1: Solar Arc

### Weapon

```text
Sunforged Scimitar
```

### Purpose

Solar Arc is the boss’s health-removal mechanic. It immediately takes down 5% of the boss’s maximum health when it hits the correct target.

### Recommended behavior

Instead of allowing Solar Arc to instantly remove unlimited health, make the 5% effect a **maximum-health damage event** subject to a cooldown and phase rules.

| Property | Value |
|---|---:|
| Health removed | 5% of maximum health |
| Damage at 800 HP | 40 HP |
| Ability cooldown | 80 ticks |
| Wind-up | 15 ticks |
| Active window | 3 ticks |
| Range | 8 blocks |
| Damage type | Solar / special |
| Regeneration interaction | Cannot be immediately regenerated during the hit animation |

### Formula

```text
solar_damage = boss_max_health × 0.05
```

For an 800 HP boss:

```text
solar_damage = 800 × 0.05 = 40 HP
```

### Animation

```text
animation.sunforged_scimitar.solar_arc
animation.solvane.solar_arc_reaction
```

### Visual telegraph

- The scimitar becomes bright orange-white.
- A crescent-shaped arc appears in front of the player.
- The boss’s chest core flashes gold.
- A sharp solar sound plays.
- The boss briefly raises its guard before the impact.

### Successful hit

When Solar Arc hits the boss:

1. Server validates that the player holds the Sunforged Scimitar.
2. Server checks that the ability is off cooldown.
3. Server applies 5% maximum-health damage.
4. Boss enters a short solar-reaction animation.
5. A golden crack appears on the armor.
6. The regeneration state remains unchanged unless Frostburst or Thorn Cage is active.

### Important balance rule

Solar Arc should not automatically make the boss’s health decrease faster than intended. It is best used as a reliable damage tool that creates visible progress, not as an unlimited instant-kill ability.

## Special ability 2: Frostburst

### Weapon

```text
Frostwake Pick
```

### Purpose

Frostburst freezes the boss’s regeneration for 15 seconds.

| Property | Value |
|---|---:|
| Regeneration freeze | 15 seconds |
| Duration in ticks | 300 ticks |
| Ability cooldown | 360 ticks |
| Wind-up | 20 ticks |
| Radius | 5 blocks |
| Damage | Low or moderate |
| Main effect | Regeneration becomes 0 |

One second equals 20 Minecraft ticks, so 15 seconds equals 300 ticks.

### Animation

```text
animation.frostwake_pick.frostburst
animation.solvane.frostburst_reaction
```

### Visual telegraph

- The pick gathers blue-white frost.
- Ice particles spiral around the player.
- A circular frost pattern appears under the boss.
- The boss’s regeneration streams freeze and shatter.
- The core becomes blue-white.

### Successful hit

When Frostburst hits:

1. Server verifies the player holds the Frostwake Pick.
2. Server checks the ability cooldown.
3. Server sets `regenerationFrozenTicks = 300`.
4. Boss healing stops immediately.
5. Frost status is synchronized to all clients.
6. A visible frozen-core animation begins.

### Repeated use

Do not allow infinite duration stacking. Use the longer remaining duration:

```java
regenerationFrozenTicks = Math.max(regenerationFrozenTicks, 300);
```

Alternatively, allow Frostburst to refresh only after the cooldown ends.

### Player strategy

Frostburst is the main damage-window creator. The player should use it when:

- The boss has lost health from Solar Arc.
- Thorn Cage has already weakened regeneration.
- The boss is not in an invulnerable phase transition.
- The player is ready to commit to attacking the core.

## Special ability 3: Thorn Cage

### Weapon

```text
Rootbound Axe
```

### Purpose

Thorn Cage damages the boss’s regenerative core and permanently slows regeneration for the current phase.

| Property | Value |
|---|---:|
| Core damage | 10% of core integrity |
| Regeneration after hit | 40% of normal |
| Ability cooldown | 240 ticks |
| Wind-up | 25 ticks |
| Cage duration | 12 seconds visual duration |
| Root duration on boss | 100 ticks |
| Main effect | Core damage and regeneration slowdown |

### Core integrity

Use a separate core value instead of treating the core as only visual data:

```text
Core integrity: 100
Thorn Cage damage: 20 per successful ability use
Required uses to break core: 5
```

### Animation

```text
animation.rootbound_axe.thorn_cage
animation.solvane.thorn_cage_reaction
```

### Visual telegraph

- The axe strikes the ground.
- Roots spread in a circular pattern.
- Thorn walls rise around the boss.
- Green damage numbers or particles appear near the chest core.
- The boss is briefly pulled toward the center of the cage.

### Successful hit

When Thorn Cage hits:

1. Server verifies the Rootbound Axe.
2. Server checks the cooldown.
3. Server damages core integrity.
4. Regeneration multiplier is reduced to 40%.
5. Root hazards appear around the boss.
6. If the core reaches zero, regeneration is disabled permanently.

### Core break

When core integrity reaches zero:

- The chest armor breaks.
- The boss loses regeneration permanently.
- The boss enters a staggered state.
- The final phase begins or the boss becomes permanently vulnerable.

## Recommended ability sequence

The player can use abilities in different tactical orders, but the recommended sequence is:

```text
1. Thorn Cage
2. Frostburst
3. Solar Arc
4. Attack during the frozen regeneration window
5. Repeat until the core breaks
```

The reason for using Thorn Cage first is to reduce future regeneration before spending the Frostburst window.

### Alternative sequence

```text
Solar Arc → Frostburst → Thorn Cage → attack
```

This is still valid and should not be rejected by the boss. The fight should require all three weapons but should not punish every order unnecessarily.

## Boss phases

### Phase 1: The Everliving Knight

Health range: 100–70%.

Features:

- Normal regeneration.
- Basic sword combo.
- Shield bash.
- Charge attack.
- Simple regeneration telegraph.
- No arena hazards.

The purpose of phase 1 is to teach the player how the boss moves and when its abilities can be used.

### Phase 2: The Rekindled Core

Health range: 70–35%.

Features:

- Faster regeneration pulses.
- Core becomes more visible.
- Boss gains Solar Shield Bash.
- Boss summons two small healing flames.
- Thorn Cage becomes especially important.

The healing flames should be destroyable with the Sunforged Scimitar or Rootbound Axe so that the arena does not become an unavoidable add-management problem.

### Phase 3: The Failing Immortal

Health range: 35–0%.

Features:

- Boss armor cracks open.
- Core is permanently visible.
- Boss uses a wider sword combo.
- Regeneration pulses happen more frequently.
- Boss begins a 30-second enrage timer.
- All three custom abilities remain necessary for the final victory.

### Final condition

The boss cannot be defeated while its core is intact:

```text
if (coreIntegrity > 0) {
    health = Math.max(health, 1.0f);
}
```

After the core is broken:

- Regeneration becomes 0.
- The boss can be killed by any custom weapon.
- Normal weapons deal only 25% damage if desired.
- The boss remains at full combat behavior until defeated.

## Knight combat abilities

### Knight Slash

| Property | Value |
|---|---:|
| Damage | 16 |
| Range | 4 blocks |
| Wind-up | 12 ticks |
| Active window | 3 ticks |
| Recovery | 20 ticks |
| Cooldown | 30 ticks |

Counterplay: move sideways, block, or move behind the boss.

### Shield Bash

| Property | Value |
|---|---:|
| Damage | 10 |
| Knockback | High |
| Stun | 10 ticks |
| Cooldown | 80 ticks |

Counterplay: avoid the front-facing cone.

### Knightly Charge

| Property | Value |
|---|---:|
| Charge distance | 9 blocks |
| Damage | 20 |
| Warning | 18 ticks |
| Cooldown | 120 ticks |
| Recovery after miss | 30 ticks |

Counterplay: move perpendicular to the charge line or hide behind a pillar.

### Core Pulse

| Property | Value |
|---|---:|
| Radius | 6 blocks |
| Damage | 10 |
| Warning | 20 ticks |
| Cooldown | 100 ticks |
|

Counterplay: move outside the gold warning circle.

### Healing Flame Summon

Available in phase 2.

- Maximum flames: 2.
- Flame health: 45.
- Each flame restores 1% boss health per second.
- Sunforged Scimitar destroys a flame in two Solar Arc hits.
- Rootbound Axe destroys a flame in one Thorn Cage if inside the cage.
- Flames disappear when the boss enters final vulnerability.

## State machine

```java
public enum SolvaneState {
    INTRO,
    PATROL,
    CHASE,
    ATTACK_WINDUP,
    ATTACK_ACTIVE,
    ATTACK_RECOVERY,
    REGENERATING,
    FROST_FROZEN,
    THORN_CAGED,
    CORE_EXPOSED,
    STAGGERED,
    PHASE_TRANSITION,
    ENRAGED,
    DEATH
}
```

### State priority

The boss should process states in this order:

```text
DEATH
→ PHASE_TRANSITION
→ CORE_EXPOSED
→ STAGGERED
→ FROST_FROZEN
→ THORN_CAGED
→ ATTACK
→ CHASE
→ PATROL
```

This prevents normal AI from immediately overriding a special reaction or phase transition.

## Regeneration implementation model

Store regeneration data on the boss entity:

```java
private int regenerationFrozenTicks;
private float regenerationMultiplier = 1.0f;
private int coreIntegrity = 100;
private boolean coreBroken;
```

Server-side regeneration logic:

```java
private void tickRegeneration() {
    if (coreBroken) {
        return;
    }

    if (regenerationFrozenTicks > 0) {
        regenerationFrozenTicks--;
        return;
    }

    float healAmount = 0.02f * getMaxHealth()
            * regenerationMultiplier / 20.0f;

    heal(healAmount);
}
```

When Thorn Cage succeeds:

```java
regenerationMultiplier = 0.40f;
coreIntegrity -= 20;
```

When Frostburst succeeds:

```java
regenerationFrozenTicks = Math.max(
        regenerationFrozenTicks,
        300
);
```

When the core breaks:

```java
coreBroken = true;
regenerationMultiplier = 0.0f;
regenerationFrozenTicks = 0;
```

## Ability validation

The server must validate the held weapon by registered item identity:

```java
private boolean isSunforgedScimitar(ItemStack stack) {
    return stack.is(ModItems.SUNFORGED_SCIMITAR);
}

private boolean isFrostwakePick(ItemStack stack) {
    return stack.is(ModItems.FROSTWAKE_PICK);
}

private boolean isRootboundAxe(ItemStack stack) {
    return stack.is(ModItems.ROOTBOUND_AXE);
}
```

Do not validate by item name, lore text, or client-provided strings.

## GeckoLib animation plan

### Boss animations

```text
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
```

### Weapon animations

```text
animation.sunforged_scimitar.solar_arc
animation.frostwake_pick.frostburst
animation.rootbound_axe.thorn_cage
```

### Keyframe effects

Use GeckoLib keyframes for:

- Solar Arc slash trail.
- Frostburst impact sound.
- Ice crack particles.
- Thorn Cage roots rising.
- Core pulse audio.
- Armor cracking visuals.
- Phase transition effects.

Keep the following server-side:

- 5% health removal.
- 15-second regeneration freeze.
- Core damage.
- Regeneration multiplier.
- Phase changes.
- Death condition.
- Loot generation.

GeckoLib supports animation keyframe callbacks for custom particles, sounds, and effects. [web:73]

## Boss UI

### Boss bar

```text
Sir Solvane, the Everliving Knight
Regeneration: Active
Regeneration: Frozen — 15s
Regeneration: Slowed
Core Integrity: 80%
Core Integrity: Broken
```

### Action-bar messages

```text
Solar Arc tears through the knight's armor!
Frostburst has frozen the knight's regeneration!
Thorn Cage damages the regenerative core!
The knight's regeneration is slowing!
The regenerative core is exposed!
The knight can finally be defeated!
```

### Recommended visual meters

- Boss health bar: vanilla boss bar.
- Regeneration state: colored icon or text.
- Core integrity: secondary boss-bar segment or arena display.
- Frostburst timer: blue countdown effect.
- Thorn Cage: green cracks around the core.

## Cooldown table

| Ability | Cooldown | Duration or effect |
|---|---:|---|
| Solar Arc | 80 ticks | Removes 5% maximum health |
| Frostburst | 360 ticks | Freezes regeneration for 300 ticks |
| Thorn Cage | 240 ticks | Damages core and reduces regen to 40% |
| Knight Slash | 30 ticks | Melee attack |
| Shield Bash | 80 ticks | Knockback and short stun |
| Knightly Charge | 120 ticks | Gap closer |
| Core Pulse | 100 ticks | Area damage |
| Healing Flame Summon | 240 ticks | Creates healing flames |

## Defeat rules

The boss is defeated only when:

```text
coreBroken == true
and health <= 0
```

Before the core breaks:

```text
health = max(health, 1.0f)
```

Normal weapons should not be able to bypass the core mechanic. You can choose one of two approaches:

### Strict mode

Only the three custom weapons can deal meaningful damage at any time.

```text
Custom weapon damage: 100%
Normal weapon damage: 0%
```

### Soft mode

Normal weapons can deal chip damage but cannot kill the boss.

```text
Custom weapon damage: 100%
Normal weapon damage: 25%
Boss cannot die before core break
```

Soft mode is usually better for multiplayer because players can participate even if they temporarily switch weapons, while the core mechanic remains mandatory.

## Loot

### Guaranteed loot

- Everliving Core.
- Knight's Sigil.
- Experience.
- Boss advancement.

### Weapon upgrades

**Sunforged Scimitar:** Solar Arc gains a second, narrower arc.

**Frostwake Pick:** Frostburst lasts 18 seconds after upgrading.

**Rootbound Axe:** Thorn Cage slows regeneration to 25% instead of 40%.

### Optional rare loot

- Everliving Knight helmet.
- Solvane's cloak cosmetic.
- Regeneration-core block.
- Knightly arena banner.
- Recipe for a combined weapon.

## Testing checklist

### Ability tests

- Solar Arc removes exactly 5% of maximum health.
- Solar Arc cannot be used while on cooldown.
- Frostburst freezes regeneration for exactly 300 ticks.
- Frostburst does not stack beyond the intended limit.
- Thorn Cage damages core integrity.
- Thorn Cage changes regeneration to the correct multiplier.
- Core break permanently disables regeneration.

### Defeat tests

- Normal weapons cannot kill the boss before core break.
- The boss remains at 1 HP when the core is intact.
- Any custom weapon can finish the boss after the core breaks.
- Death and loot occur only once.

### Multiplayer tests

- Different players can use different custom weapons.
- Frostburst state synchronizes to every client.
- Core integrity updates for every player.
- Two players cannot double-apply one ability tick.
- A client cannot submit fake 5% damage.
- Boss bar messages remain correct.

### Balance tests

- Regeneration does not exceed the player group's realistic damage output.
- Frostburst creates a meaningful but not excessive damage window.
- Thorn Cage provides long-term value.
- Solar Arc feels powerful without making other weapons irrelevant.
- The boss has a visible recovery window after missed attacks.

## Implementation order

1. Register the three custom weapons.
2. Implement Solar Arc with server-side 5% health removal.
3. Implement the boss and normal knight attacks.
4. Add server-side regeneration.
5. Implement Frostburst and the 300-tick freeze timer.
6. Implement Thorn Cage and core integrity.
7. Add the one-health protection before core break.
8. Add phase transitions.
9. Add GeckoLib animations and keyframe effects.
10. Add boss bar and regeneration UI.
11. Add loot and weapon upgrades.
12. Test multiplayer and cooldown edge cases.

## Final encounter loop

```text
The knight attacks
        ↓
Use Thorn Cage to damage the core
        ↓
Regeneration becomes slower
        ↓
Use Frostburst
        ↓
Regeneration freezes for 15 seconds
        ↓
Use Solar Arc to remove 5% maximum health
        ↓
Deal damage during the frozen window
        ↓
Repeat until the core breaks
        ↓
Defeat Sir Solvane with the custom weapons
```

The three weapons now have distinct strategic identities:

- **Sunforged Scimitar** creates reliable health progress.
- **Frostwake Pick** creates a temporary no-regeneration window.
- **Rootbound Axe** weakens the boss permanently and exposes the core over time.

This makes the knight boss feel like a planned encounter rather than a normal mob with a large health bar.
