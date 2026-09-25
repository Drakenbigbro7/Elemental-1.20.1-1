You are generating code for a Fabric Loader mod targeting Minecraft 1.20.1.
The project already has a tool/weapon template system (base classes, registration helpers, data components, etc.).
Your job is to only create the files and logic specific to one new item: the Frostwake Pick.
Changing gradle files and other item files or files not related to Frostwake Pick strictly probihited

Use these exact environment versions:

text
minecraft_version=1.20.1
yarn_mappings=1.20.1+build.10
loader_version=0.19.5
loom_version=1.18-SNAPSHOT
fabric_api_version=0.92.12+1.20.1
Assume:

The mod ID is elemental

There is already:

A base tool/weapon class or interface that your item should extend/implement.

A central item registration class (e.g. ModItems) with a helper method to register items.

A standard resource layout for models, textures, and lang files.

You must not rewrite the entire mod; only add what’s needed for this one item to work perfectly within the existing template.

Item overview
Name: Frostwake Pick
Type: Hybrid tool/weapon (pickaxe-like mining + heavy melee weapon).
Core fantasy: Ice-themed pick that freezes water, mines ice/snow blocks well, slows enemies, and can freeze an area.

Functions
Tool function

Mines these blocks efficiently (faster than normal pickaxe or with special behavior):

minecraft:ice

minecraft:packed_ice

minecraft:blue_ice

Snow-related blocks:

minecraft:snow

minecraft:snow_block

minecraft:powder_snow

Should still be able to mine normal stone/ores as a pickaxe if your template expects that; tune mining speed/durability to feel like a late-game pick.

Weapon function

Heavy, slow attack:

Higher attack damage than a standard diamond/netherite pick.

Slower attack speed (longer cooldown).

Implement using Fabric/1.20.1 mechanics for tool materials and/or custom item attack attributes.

Passive effect: Chill

When hitting an entity:

Apply a short Slowness effect (e.g. Slowness I for 1–2 seconds).

Repeated hits increase the slow duration or level slightly, up to a cap.

Use Minecraft’s status effect system (MobEffects.MOVEMENT_SLOWDOWN or the mapped name in 1.20.1 yarn).

Ensure this does not apply to bosses (see limitations).

Active ability: Frostburst

Activation method:

Right-click (or use a keybound “ability use” if your template supports it).

Effect:

Creates a small spherical area around the player where:

Entities are briefly slowed/frozen (stronger Slowness or short Freeze-like behavior if your mod has a custom effect).

Water blocks in a small radius turn to ice (or frosted ice if available).

Visual/audio feedback (particles, sound) if your template has helpers for that.

Cooldown:

Global cooldown on the item (use ItemCooldownManager or your template’s cooldown system).

Limitation:

The freeze/slow part of this ability does not affect boss entities.

Boss detection can be done via entity tags, a boss flag, or your mod’s existing boss classification if present.

Biome interaction: Snowbound Efficiency

When the player is in a snowy/cold biome:

Reduce the active ability cooldown.

Optionally slightly increase mining speed on ice/snow blocks.

Use biome tags like #minecraft:is_snowy or equivalent in 1.20.1.

Limitations

Freeze/slow effects from both passive and active:

Do not apply to boss entities.

Active ability should not be spammable; enforce cooldown strictly.

Upgrade path: Glacial Core

Design the item so it can be upgraded via an item/component called Glacial Core.

When upgraded:

Increase the radius of the Frostburst area.

Optionally increase the slow strength or duration slightly.

Implementation options (choose what fits your template best):

A data component on the item (e.g. glacial_core_installed boolean).

An NBT/component-based upgrade system already present in the mod.

Provide logic to:

Check if the upgrade is installed.

Adjust ability radius/strength accordingly.

What you must generate
Create only the files and code necessary to integrate this item into the existing template. Typical outputs:

Item class

e.g. FrostwakePickItem.java (or .kt if your template uses Kotlin).

Extends/implements the mod’s base tool/weapon class/interface.

Implements:

Mining behavior for ice/snow blocks.

Attack damage/speed tuning.

Passive chill on hit.

Active ability on right-click/use.

Biome-based cooldown reduction.

Upgrade logic for Glacial Core.

Uses 1.20.1 yarn mappings consistent with:

minecraft_version=1.20.1

yarn_mappings=1.20.1+build.10

Registration snippet

Code to register FrostwakePickItem in the mod’s item registry, e.g. in ModItems.java:

A static field public static final Item FROSTWAKE_PICK.

A call to your existing register(...) helper.

Ensure the item’s Item.Properties / Item.Settings match your template (max stack size 1, durability, etc.).

Data components / NBT (if needed)

If your mod uses data components for upgrades:

Define or reference a component like GLACIAL_CORE_INSTALLED.

Show how it’s added/checked on the Frostwake Pick.

If using NBT instead, provide methods to read/write the upgrade flag.

Cooldown & ability logic

Use ItemCooldownManager (or your template’s cooldown system) to:

Start cooldown on ability use.

Reduce cooldown when in snowy biomes.

Ensure boss entities are excluded from freeze/slow effects.

Resources

Provide JSON/lang entries:

assets/yourmodid/lang/en_us.json:

"item.yourmodid.frostwake_pick": "Frostwake Pick"

Ability tooltip lines if your template uses dynamic tooltips.

Model and texture paths (you don’t need to draw the texture, just specify):

assets/yourmodid/models/item/frostwake_pick.json

assets/yourmodid/textures/item/frostwake_pick.png

If your template uses data-driven tooltips or ability descriptions, add the necessary JSON fields.

Integration points

Clearly mark where this item plugs into:

The mod’s creative tab / item group.

Any loot table injections or recipe helpers if your template uses them (optional).

Do not redefine global systems; just call existing helpers.

Coding constraints and style
Target Minecraft 1.20.1 with Fabric API 0.92.12+1.20.1.

Use yarn 1.20.1+build.10 mappings.

Follow the existing template’s:

Package structure (e.g. com.yourname.yourmod.item, ...component, etc.).

Naming conventions.

Registration patterns.

Keep code clean, well-commented where non-obvious, and consistent with Java 17 (the version used by 1.20.1).

Do not include build.gradle or fabric.mod.json changes unless absolutely required for this item; assume those are already configured for custom items/components.

Behavior details to implement
Implement the following logic precisely:

Mining:

When mining ice/packed_ice/blue_ice/snow/powder_snow:

Use a higher mining speed than normal.

Ensure correct tool type so these blocks drop properly.

Attack:

Set attack damage and speed to feel “heavy”:

More damage than netherite pick.

Slower attack speed.

Passive chill:

On entity hit:

If entity is not a boss:

Apply Slowness I for ~1.5s.

If the entity already has this chill debuff from this item within a short window, increase level or duration slightly, up to a cap (e.g. Slowness II max).

Active Frostburst:

On right-click:

If not on cooldown:

Determine radius:

Base radius (e.g. 3 blocks).

If Glacial Core installed: larger radius (e.g. 5 blocks).

For each entity in radius:

If not boss:

Apply stronger Slowness (e.g. Slowness II–III for 3–4s) or a freeze-like effect if your mod defines one.

For each water block in radius:

Convert to ice (or frosted ice if you want extra flavor and it’s safe).

Spawn particles/sound if helpers exist.

Start cooldown:

Base cooldown (e.g. 15 seconds).

If in snowy biome: reduce cooldown (e.g. to 10 seconds).

Boss immunity:

No slow/freeze effects on bosses from either passive or active.

Output format
Return:

Full file contents for each new file, with:

A comment at the top with the file path (e.g. // File: src/main/java/com/yourname/yourmod/item/FrostwakePickItem.java).

Short notes where you assume something about the template (e.g. “Assumes ModComponents.GLACIAL_CORE_INSTALLED exists; adapt name if needed.”).

Do not explain basic Fabric setup; focus only on the Frostwake Pick implementation within the existing template.
create .md file to explain the logic and explain the weapons ability

