You are an expert Minecraft Fabric mod developer who knows GeckoLib 4.x and Blockbench's Bedrock-style geo format.

## TASK
My mod already has a working weapon: `elemental:rootbound_axe`. Its mechanics (sweep attack, healing root, thorn cage, tooltip, cooldowns) are already implemented. Your job is ONLY to:
1. Create the 3D model (geo), the texture, and the animations.
2. Attach them to the existing Rootbound Axe using GeckoLib 4.

Do not re-implement or alter any gameplay logic.

## STRICT RULES: WHAT YOU MUST NOT TOUCH
- Do NOT modify build.gradle, gradle.properties, settings.gradle, or any other Gradle file. Assume GeckoLib is already set up as a dependency. If it is not, do not edit Gradle. Tell me at the end exactly which lines I need to add myself.
- Do NOT modify any other weapon, item, or class in the mod. No other item's code, model, texture, or lang entry may change.
- Do NOT rename, move, or delete existing files.
- In the existing Rootbound Axe item class, make ONLY the minimal changes needed to attach GeckoLib:
    - add `implements GeoItem`
    - add the AnimatableInstanceCache field and `getAnimatableInstanceCache()`
    - add `registerControllers()` with a "main" controller (default `idle`, plus triggerable `sweep`, `thorn_cage`, `heal_proc`, `equip`)
    - add `createRenderer(Consumer<Object>)` (GeckoLib 4 Fabric style) and `registerSyncedAnimatable` in the constructor
    - add the `triggerAnim(...)` calls at the points where the existing code already performs the sweep, the healing-root proc, and the thorn cage. Insert one line at each point and do not change the surrounding logic.
      Show these as a diff-style snippet (lines added only) so I can see that nothing else changed. Do not paste the entire existing class back with rewrites.
- Do NOT touch the existing mechanics code, the tool material, attributes, tooltips, or cooldowns.

## ENVIRONMENT
- minecraft_version = 1.20.1
- yarn_mappings = 1.20.1+build.10 (use Yarn names, NOT Mojang names)
- loader_version = 0.19.5, loom_version = 1.18-SNAPSHOT, fabric_api_version = 0.92.12+1.20.1
- Java 17
- GeckoLib 4.x Fabric for 1.20.1. Use only GeckoLib 4 APIs (GeoItem, GeoModel, GeoItemRenderer, AnimatableManager, RawAnimation, SingletonGeoAnimatable.registerSyncedAnimatable, triggerAnim). No GeckoLib 3 APIs (IAnimatable, AnimationFactory, etc.).
- Mod ID: `elemental`

## BEFORE WRITING ANYTHING
First inspect my project and tell me:
- the exact path and class name of the existing Rootbound Axe item
- whether GeckoLib is already in my Gradle files and fabric.mod.json (read-only check)
- where my client initializer is, and whether a model JSON for this item already exists
  Then proceed, following the order below.

## ORDER OF WORK (do it in this exact order)
### Step 1: Model
Create `assets/elemental/geo/rootbound_axe.geo.json` (Bedrock geo format 1.12.0, valid for GeckoLib 4; use the `geo` folder GeckoLib 4 expects).
Design: a nature/forest axe, about 1.5x the size of a vanilla axe, that reads well at GUI size.
- Handle: a gnarled wooden shaft of twisted bark with 2-3 visible bends (several small rotated cubes, not one straight box)
- Axe head: dark stone or hardened wood with a lighter sharpened edge, wrapped in roots and vines
- 4-6 root tendril cubes coiling around the head and shaft, grouped in separate bones so they animate independently
- Small flat leaf/moss cubes in 2-3 shades of green on the head and grip
- A few tiny light-green glow accent cubes near the head
- Bone hierarchy, named exactly: `root` > `handle`, `head`, `roots_a`, `roots_b`, `roots_c`, `leaves`, `glow`
- Under ~60 cubes, with correct pivots so rotations happen around sensible points
- Set `texture_width` / `texture_height` and per-face or box UVs that match the texture from Step 2

### Step 2: Texture
Create `assets/elemental/textures/item/rootbound_axe.png`. Since you can't give me a binary PNG, provide a Python (Pillow) script that generates it procedurally and matches the UV layout of the geo file. State the exact texture dimensions and the exact command to run the script. The texture should have bark detail on the handle, stone/wood grain on the head, root texture, and green leaf variation.

### Step 3: Animations
Create `assets/elemental/animations/rootbound_axe.animation.json` (GeckoLib 4 format, format_version 1.8.0). Use only bone names from the geo file. Animation names:
- `animation.rootbound_axe.idle` (loop): slow root sway, leaves rustling, glow pulsing via scale
- `animation.rootbound_axe.sweep` (once, ~0.7s): wind-up, a fast 180° swing, a short recovery, with roots whipping outward at the peak
- `animation.rootbound_axe.thorn_cage` (once, ~1.0s): the axe rises, roots spread outward, glow brightens, then settles
- `animation.rootbound_axe.heal_proc` (once, ~0.6s): roots pulse, leaves flutter, a small glow flare
- `animation.rootbound_axe.equip` (once, ~0.4s): roots uncurl and settle
  Use easing (easeInOutSine, easeOutBack, etc.) where it helps. Make `idle` the default state and have each one-shot return to `idle` afterward.

### Step 4: Attach to the weapon
Create these new files:
- `RootboundAxeModel.java` (GeoModel pointing to the geo, texture, and animation files above)
- `RootboundAxeRenderer.java` (GeoItemRenderer)
  Then add the minimal changes to the existing axe class described in the STRICT RULES section. If the item model JSON `assets/elemental/models/item/rootbound_axe.json` needs to be a GeckoLib-compatible builtin/entity model, create or update ONLY that one file, and show me the before/after if it already existed.
  Register the renderer in the client initializer with the smallest possible addition (a single line or a small block). Do not restructure the client initializer.

## OUTPUT FORMAT
1. A file tree split into "NEW files" and "EXISTING files edited (minimal)". The second list should contain only the axe item class, the client initializer, and possibly its item model JSON. If any other existing file would need editing, stop and ask me first.
2. Each new file in a fenced code block with its full path as the heading.
3. For existing files, show only the added or changed lines, with a few lines of surrounding context.
4. The Python texture script and how to run it.
5. A "Verify nothing else changed" checklist (for example `git status` and `git diff --stat`, which should list only the files named above).
6. A testing checklist: `/give @s elemental:rootbound_axe`, and what I should see for idle, equip, sweep, thorn cage, and healing proc, including in multiplayer.
7. Common pitfalls (wrong geo folder, bone name mismatch, missing synced-animatable registration, missing builtin/entity parent) with fixes.
8. Assumptions you made and anything I must check manually in Blockbench.

Do not leave TODOs or placeholders. All code must compile against Yarn 1.20.1+build.10 and GeckoLib 4.