You are an expert Minecraft Fabric mod developer. The mod targets Minecraft 1.20.1 with these versions:

minecraft_version=1.20.1

yarn_mappings=1.20.1+build.10

loader_version=0.19.5

loom_version=1.18-SNAPSHOT

fabric_api_version=0.92.12+1.20.1

The mod already has:

A working Fabric project structure (Gradle, fabric.mod.json, main mod class, etc.).

A template system for items (base classes, registration helpers, data generators, models, lang, etc.).

Existing examples of custom tools and weapons using the same pattern.

Your task: Generate ONLY the new files and code changes required to add the Rootbound Axe as a new item, using the existing templates and patterns. Do not regenerate unchanged base files. Clearly mark where code should be added or modified in existing files.Do not change any gradle files and other mod files that are not related to rootbound axe

Item overview
Item ID: rootbound_axe

Mod ID: <MOD_ID> (use the project’s existing mod ID constant)

Type: Axe tool + weapon with special behaviors

Base behavior:

Functions as an axe for woodcutting (same mining speed / durability tier as diamond/netherite axe; choose based on existing mod balance).

Has a melee attack with a wide sweep attack similar to vanilla axe sweep, but slightly wider or with a small visual root effect.

Passive ability:

On hit or on critical hit, has a configurable chance to spawn a short-lived healing root at the target’s location or near the player.

Healing root:

Applies regeneration to nearby entities (configurable radius and duration).

Stronger effect in forest and jungle biomes (e.g., higher amplitude or longer duration).

Can be destroyed by fire/lava (if an entity standing in it takes fire damage, the root effect ends early or the block/entity representing the root is removed).

Active ability:

Right-click (or use keybind if the mod uses ability keys) to create a thorn cage:

Spawns a small circular area around the player or targeted block/entity.

Entities inside take periodic piercing damage and are slightly slowed.

Duration and cooldown are configurable.

Visuals: thorny vines rising from the ground; use existing particle / sound hooks in the mod if available.

Limitations:

Healing roots are weaker or do not spawn in non-forest/jungle biomes.

Roots (both passive and active) are vulnerable to fire: fire/lava damage cancels them early.

Implementation requirements
Use the existing mod templates for:

Item classes (e.g., BaseToolItem, BaseWeaponItem, or similar).

Registration (e.g., ModItems.register() pattern).

Data generation (models, recipes, tags, lang entries).

Capability/attachment systems if the mod uses them (e.g., component data, NBT, or Fabric Data Attachments).

Generate:

New Java/Kotlin files (choose the project’s language) for:

RootboundAxeItem (main item class).

Any helper classes needed, e.g.:

HealingRootEffect / HealingRootComponent

ThornCageArea / ThornCageComponent

If the mod uses components:

Component type definitions for root/thorn data.

Packet handlers if client–server sync is required (e.g., for visuals).

Modifications to existing files:

Item registration file: show exact lines to add for ROOTBOUND_AXE.

Any central config file if abilities need tuning (chance, radius, duration, cooldown).

Tag files if needed (e.g., #fabric:axes, #minecraft:tools, or custom mod tags).

Lang file entries (en_us.json) for:

Item name: “Rootbound Axe”

Ability tooltips / descriptions.

Recipe JSON (if using crafting):

Example: shapeless/shapeful recipe using existing mod materials + vanilla sticks/logs.

Model JSON and texture path references (just the file content and paths; assume textures will be added separately if needed).

Behavior details to implement:

Woodcutting
- Inherit from the mod’s axe base class so it:
- Has correct mining speed on logs/wood.
- Can strip logs if the base class supports it.
- No extra logic needed unless the mod has special “fast woodcutting” hooks; if so, integrate with those.

Sweep attack
- Override the attack method to:
- Perform a sweep similar to vanilla axe but with:
- Slightly larger angle/radius (configurable).
- Optional root particle effect along the arc.
- Respect existing combat attributes (attack damage, attack speed).

Passive: Healing root
- On hit (or crit, depending on design), roll a chance (configurable) to:
- Spawn a temporary “healing root” at the target’s feet or near the player.
- Implementation options (pick what fits the mod’s pattern):
- Place a custom block that emits a regeneration area effect and self-destructs after a duration.
- Or use an area-effect entity / custom component that applies regeneration each tick.
- Biome interaction:
- Check biome at the root’s position.
- If biome is in forest/jungle family (use BiomeTags or biome registry checks), increase:
- Regeneration amplifier, and/or
- Duration.
- Fire limitation:
- If the root (block/entity/component) is exposed to fire/lava damage, end its effect early and remove it.

Active: Thorn cage
- On right-click (or ability key):
- Consume some resource if the mod uses energy/mana/stamina; otherwise just enforce cooldown.
- Create a thorn cage area:
- Centered on player or targeted block.
- Radius: small (e.g., 3–4 blocks).
- Duration: a few seconds.
- Each tick:
- Apply piercing damage to hostile entities inside.
- Apply slight slowness.
- Spawn thorn/vine particles.
- Prevent overlapping cages from the same player (optional, configurable).
- Add cooldown logic:
- Prevent spamming; show cooldown in tooltip or HUD if the mod supports it.

Configuration

Add entries to the mod’s config (if it has one) for:

Passive trigger chance.

Healing root:

Base duration.

Base radius.

Forest/jungle bonus multiplier.

Thorn cage:

Radius.

Duration.

Damage per tick.

Slow level.

Cooldown.

Provide sensible defaults balanced around diamond/netherite tools.

Tooltips and localization

Add tooltip lines explaining:

“Wide sweep attack”

“Chance to create healing roots (stronger in forests and jungles)”

“Active: Thorn cage – damages and slows enemies”

“Roots are destroyed by fire”

Use the mod’s existing tooltip style (colors, formatting).

Code quality

Follow the existing code style in the mod (naming, formatting, nullability, logging).

Use the mod’s existing logging utility if present.

Avoid hardcoding; use constants or config values.

Add brief JavaDoc/KDoc comments for new public classes/methods.

Output format
Produce your answer as:

A short explanation of the approach (2–4 sentences).

For each new file:

A header line: File: <full path relative to src/main/...>

Then the full file content in a code block.

For each modified file:

A header line: Modify: <full path>

Show the relevant snippet with clear markers like:

// ADD START

// ADD END
or

// REPLACE START … // REPLACE END

Only show the changed sections, not the entire file.

Do not include Gradle build changes unless absolutely necessary (e.g., new dependencies). Assume all required Fabric APIs and mod libraries are already configured.