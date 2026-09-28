import json
import uuid
import base64
import os
import math
from io import BytesIO
from PIL import Image, ImageDraw

PROJECT_DIR = r"c:\Users\omkat\Desktop\Project-1\Elemental-Trial-1"
ASSETS_DIR = os.path.join(PROJECT_DIR, "src", "main", "resources", "assets", "elemental")

os.makedirs(os.path.join(ASSETS_DIR, "geo"), exist_ok=True)
os.makedirs(os.path.join(ASSETS_DIR, "animations"), exist_ok=True)
os.makedirs(os.path.join(ASSETS_DIR, "textures", "item"), exist_ok=True)
os.makedirs(os.path.join(ASSETS_DIR, "textures", "entity"), exist_ok=True)

def new_uuid():
    return str(uuid.uuid4())

def image_to_base64(img):
    buffered = BytesIO()
    img.save(buffered, format="PNG")
    return "data:image/png;base64," + base64.b64encode(buffered.getvalue()).decode("utf-8")

# =====================================================================
# 1. TEXTURE GENERATION
# =====================================================================

def generate_scimitar_texture():
    img = Image.new("RGBA", (64, 64), (0, 0, 0, 0))
    draw = ImageDraw.Draw(img)

    # 1. Dark Bronze Pommel & Accents (0..16, 0..16)
    for y in range(0, 16):
        for x in range(0, 16):
            pattern = (x * 3 + y * 5) % 7
            if pattern in (0, 1):
                c = (70, 44, 22, 255) # deep bronze shadow
            elif pattern in (2, 3, 4):
                c = (112, 70, 36, 255) # aged bronze
            elif pattern == 5:
                c = (150, 95, 46, 255) # bronze highlight
            else:
                c = (190, 130, 60, 255) # gold inlay
            draw.point((x, y), fill=c)

    # 2. Golden-Brown Leather Wrapped Handle (16..32, 0..16)
    for y in range(0, 16):
        for x in range(16, 32):
            wrap = (x - 16 + (y * 2)) % 6
            if wrap in (0, 1):
                c = (115, 65, 30, 255) # rich saddle leather
            elif wrap == 2:
                c = (155, 92, 45, 255) # embossed ridge highlight
            elif wrap == 3:
                c = (185, 120, 60, 255) # golden stitch / accent
            else:
                c = (70, 38, 16, 255) # crevice shadow
            draw.point((x, y), fill=c)

    # 3. Guard & Sun Emblem (32..64, 0..16)
    for y in range(0, 16):
        for x in range(32, 64):
            dx = abs(x - 48)
            dy = abs(y - 8)
            dist = dx + dy
            if dist <= 3:
                c = (255, 245, 160, 255) # brilliant core gold
            elif dist <= 6:
                c = (245, 185, 35, 255) # sun gold
            elif dist <= 10:
                c = (215, 135, 20, 255) # amber gold
            else:
                c = (160, 90, 15, 255) # antique bronze border
            draw.point((x, y), fill=c)

    # 4. Solar Core (0..16, 16..32)
    for y in range(16, 32):
        for x in range(0, 16):
            cx, cy = x - 8, y - 24
            dist = (cx*cx + cy*cy)**0.5
            if dist <= 2.5:
                c = (255, 255, 245, 255) # white-hot center
            elif dist <= 4.5:
                c = (255, 230, 90, 255) # solar brilliance
            elif dist <= 6.8:
                c = (255, 130, 15, 255) # radiant coronal fire
            elif dist <= 7.8:
                c = (210, 50, 10, 220) # outer orange rim
            else:
                c = (0, 0, 0, 0)
            draw.point((x, y), fill=c)

    # 5. Obsidian / Blackened Metal Blade Body (16..40, 16..64)
    for y in range(16, 64):
        for x in range(16, 40):
            grain = (x * 11 + y * 7) % 13
            if grain in (0, 1):
                c = (42, 38, 52, 255) # obsidian sheen highlight
            elif grain in (2, 3, 4, 5):
                c = (24, 21, 30, 255) # blackened metal base
            elif grain in (6, 7):
                c = (14, 12, 18, 255) # deep void shadow
            else:
                c = (32, 28, 40, 255) # dark metal body
            draw.point((x, y), fill=c)

    # 6. Blade Edge Glow (40..64, 16..64)
    for y in range(16, 64):
        for x in range(40, 64):
            # Gradient from inner fiery amber to radiant white-gold cutting edge
            prog = (x - 40) / 23.0
            if prog > 0.82:
                c = (255, 255, 225, 255) # white-hot blade apex
            elif prog > 0.55:
                c = (255, 215, 45, 255) # searing golden edge
            elif prog > 0.25:
                c = (255, 125, 20, 255) # orange solar flare
            else:
                c = (195, 55, 10, 255) # molten amber base
            draw.point((x, y), fill=c)

    # 7. Solar Particles & Emitter Motes (0..16, 32..48)
    for y in range(32, 48):
        for x in range(0, 16):
            if (x + y) % 3 == 0:
                c = (255, 255, 200, 255) # white-gold sparkle
            elif (x + y) % 3 == 1:
                c = (255, 180, 30, 255) # gold spark
            else:
                c = (255, 100, 15, 255) # orange spark
            draw.point((x, y), fill=c)

    return img

def generate_solar_arc_texture():
    img = Image.new("RGBA", (64, 64), (0, 0, 0, 0))
    draw = ImageDraw.Draw(img)

    # 1. Solar Crescent (0..32, 0..32)
    for y in range(0, 32):
        for x in range(0, 32):
            grad = (x + y) / 62.0
            if grad < 0.22:
                c = (255, 255, 240, 255) # white-hot inner blade
            elif grad < 0.52:
                c = (255, 215, 45, 255) # radiant solar gold
            elif grad < 0.8:
                c = (255, 120, 15, 255) # fiery orange body
            else:
                c = (205, 45, 5, 255) # deep orange rim
            draw.point((x, y), fill=c)

    # 2. Energy Core (32..64, 0..32)
    for y in range(0, 32):
        for x in range(32, 64):
            cx, cy = x - 48, y - 16
            dist = (cx*cx + cy*cy)**0.5
            if dist <= 3.5:
                c = (255, 255, 255, 255) # blinding white core
            elif dist <= 7.0:
                c = (255, 245, 130, 255) # intense golden sunburst
            elif dist <= 11.5:
                c = (255, 140, 20, 255) # orange solar flare ring
            elif dist <= 14.5:
                c = (215, 55, 10, 210) # outer corona
            else:
                c = (0, 0, 0, 0)
            draw.point((x, y), fill=c)

    # 3. Flame Ribbons (0..48, 32..64)
    for y in range(32, 64):
        for x in range(0, 48):
            prog = (y - 32) / 31.0 # 0 at head, 1 at tail
            alpha = int(255 * (1.0 - prog * 0.4))
            wave = (x + y * 2) % 6
            if wave in (0, 1):
                c = (255, 235, 90, alpha) # golden streak
            elif wave in (2, 3):
                c = (255, 135, 25, alpha) # orange plasma
            else:
                c = (210, 45, 5, alpha) # crimson edge
            draw.point((x, y), fill=c)

    # 4. Trail Origin & Particles (48..64, 32..64)
    for y in range(32, 64):
        for x in range(48, 64):
            if (x * 5 + y * 7) % 4 == 0:
                c = (255, 255, 220, 255)
            elif (x * 5 + y * 7) % 4 == 1:
                c = (255, 195, 40, 255)
            else:
                c = (245, 95, 15, 255)
            draw.point((x, y), fill=c)

    return img

# =====================================================================
# 2. MODEL GENERATION - SUNFORGED SCIMITAR
# =====================================================================

def build_sunforged_scimitar_data(tex_b64):
    # Groups required:
    # root, blade, blade_edge_glow, guard, sun_emblem, handle, pommel, solar_core, solar_particles, projectile_origin
    
    group_uuids = {
        "root": new_uuid(),
        "handle": new_uuid(),
        "pommel": new_uuid(),
        "guard": new_uuid(),
        "sun_emblem": new_uuid(),
        "solar_core": new_uuid(),
        "blade": new_uuid(),
        "blade_edge_glow": new_uuid(),
        "solar_particles": new_uuid(),
        "projectile_origin": new_uuid()
    }

    elements = []
    def make_cube(name, from_p, to_p, origin, rot, uv_rect):
        # uv_rect: [u1, v1, u2, v2]
        u1, v1, u2, v2 = uv_rect
        cube_id = new_uuid()
        cube = {
            "name": name,
            "box_uv": False,
            "render_order": "default",
            "locked": False,
            "export": True,
            "scope": 0,
            "allow_mirror_modeling": True,
            "from": [round(from_p[0], 3), round(from_p[1], 3), round(from_p[2], 3)],
            "to": [round(to_p[0], 3), round(to_p[1], 3), round(to_p[2], 3)],
            "autouv": 0,
            "color": 0,
            "origin": [round(origin[0], 3), round(origin[1], 3), round(origin[2], 3)],
            "faces": {
                "north": {"uv": [u1, v1, u2, v2], "texture": 0},
                "east": {"uv": [u1, v1, u2, v2], "texture": 0},
                "south": {"uv": [u1, v1, u2, v2], "texture": 0},
                "west": {"uv": [u1, v1, u2, v2], "texture": 0},
                "up": {"uv": [u1, v1, u2, v2], "texture": 0},
                "down": {"uv": [u1, v1, u2, v2], "texture": 0}
            },
            "type": "cube",
            "uuid": cube_id
        }
        if rot and any(r != 0 for r in rot):
            cube["rotation"] = [round(rot[0], 3), round(rot[1], 3), round(rot[2], 3)]
        elements.append(cube)
        return cube_id

    group_children = {k: [] for k in group_uuids}

    # 1. Pommel
    # Pommel collar
    c = make_cube("pommel_collar", [-1.1, -0.6, -1.1], [1.1, 0.0, 1.1], [0, 0, 0], [0, 0, 0], [0, 0, 6, 4])
    group_children["pommel"].append(c)
    # Pommel faceted head
    c = make_cube("pommel_head", [-1.6, -2.4, -1.6], [1.6, -0.6, 1.6], [0, -1.5, 0], [0, 45, 0], [0, 4, 8, 12])
    group_children["pommel"].append(c)
    # Pommel solar jewel
    c = make_cube("pommel_jewel", [-0.8, -3.0, -0.8], [0.8, -2.4, 0.8], [0, -2.7, 0], [0, 0, 0], [2, 18, 6, 22])
    group_children["pommel"].append(c)

    # 2. Handle
    # Leather grip
    c = make_cube("handle_grip", [-0.9, 0.0, -0.9], [0.9, 7.0, 0.9], [0, 3.5, 0], [0, 0, 0], [16, 0, 24, 16])
    group_children["handle"].append(c)
    # Grip wrap rings (embossed details)
    c = make_cube("handle_ring_1", [-1.0, 1.8, -1.0], [1.0, 2.5, 1.0], [0, 2.1, 0], [0, 0, 0], [24, 0, 32, 4])
    group_children["handle"].append(c)
    c = make_cube("handle_ring_2", [-1.0, 4.3, -1.0], [1.0, 5.0, 1.0], [0, 4.6, 0], [0, 0, 0], [24, 4, 32, 8])
    group_children["handle"].append(c)

    # 3. Guard
    # Crossguard main bar
    c = make_cube("guard_bar", [-4.8, 7.0, -1.4], [4.8, 8.4, 1.4], [0, 7.7, 0], [0, 0, 0], [32, 0, 48, 6])
    group_children["guard"].append(c)
    # Forward curved quillon left
    c = make_cube("guard_quillon_l", [-5.2, 8.2, -1.0], [-4.0, 10.6, 1.0], [-4.6, 8.4, 0], [0, 0, 20], [48, 0, 56, 8])
    group_children["guard"].append(c)
    # Forward curved quillon right
    c = make_cube("guard_quillon_r", [4.0, 8.2, -1.0], [5.2, 10.6, 1.0], [4.6, 8.4, 0], [0, 0, -20], [48, 0, 56, 8])
    group_children["guard"].append(c)

    # 4. Sun Emblem
    # Central solar disk
    c = make_cube("sun_disk", [-2.4, 6.6, -1.5], [2.4, 9.8, 1.5], [0, 8.2, 0], [0, 0, 0], [40, 4, 56, 14])
    group_children["sun_emblem"].append(c)
    # Sun rays (vertical & horizontal spikes)
    c = make_cube("sun_ray_up", [-0.8, 9.8, -0.9], [0.8, 12.2, 0.9], [0, 9.8, 0], [0, 0, 0], [56, 0, 64, 8])
    group_children["sun_emblem"].append(c)
    c = make_cube("sun_ray_left", [-4.2, 7.4, -0.8], [-2.4, 9.0, 0.8], [-2.4, 8.2, 0], [0, 0, 0], [56, 8, 64, 16])
    group_children["sun_emblem"].append(c)
    c = make_cube("sun_ray_right", [2.4, 7.4, -0.8], [4.2, 9.0, 0.8], [2.4, 8.2, 0], [0, 0, 0], [56, 8, 64, 16])
    group_children["sun_emblem"].append(c)
    # Diagonal sun rays
    c = make_cube("sun_ray_diag_ul", [-3.0, 9.0, -0.7], [-1.8, 10.6, 0.7], [-2.4, 9.8, 0], [0, 0, 45], [56, 0, 64, 8])
    group_children["sun_emblem"].append(c)
    c = make_cube("sun_ray_diag_ur", [1.8, 9.0, -0.7], [3.0, 10.6, 0.7], [2.4, 9.8, 0], [0, 0, -45], [56, 0, 64, 8])
    group_children["sun_emblem"].append(c)

    # 5. Solar Core
    # Inner glowing sphere
    c = make_cube("solar_core_inner", [-1.0, 7.2, -1.0], [1.0, 9.2, 1.0], [0, 8.2, 0], [0, 0, 0], [4, 20, 12, 28])
    group_children["solar_core"].append(c)
    # Outer crystal confinement shell
    c = make_cube("solar_core_shell", [-1.25, 6.95, -1.25], [1.25, 9.45, 1.25], [0, 8.2, 0], [0, 45, 0], [2, 18, 14, 30])
    group_children["solar_core"].append(c)

    # 6. Blade (Dark Obsidian / Blackened Metal Body)
    # Segment 1: Blade base straight section
    c = make_cube("blade_base", [-1.2, 8.4, -0.6], [1.2, 13.5, 0.6], [0, 8.4, 0], [0, 0, 0], [18, 18, 26, 32])
    group_children["blade"].append(c)
    # Segment 2: Lower curve
    c = make_cube("blade_mid_low", [-0.8, 13.5, -0.55], [1.8, 18.5, 0.55], [0, 13.5, 0], [0, 0, -4], [20, 24, 30, 40])
    group_children["blade"].append(c)
    # Segment 3: Scimitar belly curve
    c = make_cube("blade_belly", [0.2, 18.2, -0.5], [3.2, 23.5, 0.5], [1.0, 18.5, 0], [0, 0, -8], [22, 30, 32, 48])
    group_children["blade"].append(c)
    # Segment 4: Forward sweep
    c = make_cube("blade_sweep", [1.4, 23.2, -0.45], [4.4, 27.8, 0.45], [2.0, 23.5, 0], [0, 0, -14], [24, 36, 34, 56])
    group_children["blade"].append(c)
    # Segment 5: Scimitar tip spine
    c = make_cube("blade_tip_spine", [2.6, 27.5, -0.35], [5.0, 31.2, 0.35], [3.2, 27.5, 0], [0, 0, -22], [26, 42, 36, 62])
    group_children["blade"].append(c)
    # Spine ridge reinforcement
    c = make_cube("blade_spine", [-1.4, 8.4, -0.7], [-0.4, 21.0, 0.7], [-1.0, 8.4, 0], [0, 0, -2], [16, 16, 20, 48])
    group_children["blade"].append(c)

    # 7. Blade Edge Glow (Luminous golden cutting edge)
    c = make_cube("edge_glow_base", [1.1, 8.4, -0.65], [1.8, 13.5, 0.65], [0, 8.4, 0], [0, 0, 0], [42, 18, 50, 32])
    group_children["blade_edge_glow"].append(c)
    c = make_cube("edge_glow_low", [1.7, 13.4, -0.6], [2.5, 18.5, 0.6], [0, 13.5, 0], [0, 0, -4], [44, 24, 54, 40])
    group_children["blade_edge_glow"].append(c)
    c = make_cube("edge_glow_belly", [3.0, 18.2, -0.55], [3.9, 23.5, 0.55], [1.0, 18.5, 0], [0, 0, -8], [48, 30, 58, 48])
    group_children["blade_edge_glow"].append(c)
    c = make_cube("edge_glow_sweep", [4.2, 23.2, -0.5], [5.1, 27.8, 0.5], [2.0, 23.5, 0], [0, 0, -14], [52, 36, 62, 56])
    group_children["blade_edge_glow"].append(c)
    c = make_cube("edge_glow_tip", [4.8, 27.5, -0.4], [5.8, 31.6, 0.4], [3.2, 27.5, 0], [0, 0, -22], [54, 42, 64, 62])
    group_children["blade_edge_glow"].append(c)

    # 8. Solar Particles (Sparks hovering around blade & core)
    c = make_cube("particle_core", [-2.2, 10.5, 1.2], [-1.4, 11.3, 2.0], [-1.8, 10.9, 1.6], [15, 25, 0], [2, 34, 6, 38])
    group_children["solar_particles"].append(c)
    c = make_cube("particle_mid", [2.8, 16.5, -1.6], [3.6, 17.3, -0.8], [3.2, 16.9, -1.2], [-20, 15, 30], [6, 38, 10, 42])
    group_children["solar_particles"].append(c)
    c = make_cube("particle_belly", [4.6, 22.8, 1.1], [5.4, 23.6, 1.9], [5.0, 23.2, 1.5], [10, -35, 15], [2, 34, 6, 38])
    group_children["solar_particles"].append(c)
    c = make_cube("particle_tip", [5.8, 29.2, -1.2], [6.6, 30.0, -0.4], [6.2, 29.6, -0.8], [25, 40, -10], [6, 38, 10, 42])
    group_children["solar_particles"].append(c)

    # 9. Projectile Origin
    # Tip of scimitar: [5.8, 31.6, 0], oriented along curved launch tangent [-22 deg on Z]
    c = make_cube("projectile_emitter", [5.4, 31.2, -0.25], [6.2, 32.0, 0.25], [5.8, 31.6, 0], [0, 0, -22], [8, 36, 12, 40])
    group_children["projectile_origin"].append(c)

    # Build the outliner hierarchy:
    # root
    #  -> handle
    #  -> pommel
    #  -> guard
    #  -> sun_emblem
    #  -> solar_core
    #  -> blade
    #  -> blade_edge_glow
    #  -> solar_particles
    #  -> projectile_origin
    outliner = [
        {
            "name": "root",
            "origin": [0, 4, 0],
            "rotation": [0, 0, 0],
            "uuid": group_uuids["root"],
            "export": True,
            "isOpen": True,
            "children": [
                {
                    "name": "handle",
                    "origin": [0, 4, 0],
                    "rotation": [0, 0, 0],
                    "uuid": group_uuids["handle"],
                    "export": True,
                    "isOpen": True,
                    "children": group_children["handle"]
                },
                {
                    "name": "pommel",
                    "origin": [0, 0, 0],
                    "rotation": [0, 0, 0],
                    "uuid": group_uuids["pommel"],
                    "export": True,
                    "isOpen": True,
                    "children": group_children["pommel"]
                },
                {
                    "name": "guard",
                    "origin": [0, 7.7, 0],
                    "rotation": [0, 0, 0],
                    "uuid": group_uuids["guard"],
                    "export": True,
                    "isOpen": True,
                    "children": group_children["guard"]
                },
                {
                    "name": "sun_emblem",
                    "origin": [0, 8.2, 0],
                    "rotation": [0, 0, 0],
                    "uuid": group_uuids["sun_emblem"],
                    "export": True,
                    "isOpen": True,
                    "children": group_children["sun_emblem"]
                },
                {
                    "name": "solar_core",
                    "origin": [0, 8.2, 0],
                    "rotation": [0, 0, 0],
                    "uuid": group_uuids["solar_core"],
                    "export": True,
                    "isOpen": True,
                    "children": group_children["solar_core"]
                },
                {
                    "name": "blade",
                    "origin": [0, 8.4, 0],
                    "rotation": [0, 0, 0],
                    "uuid": group_uuids["blade"],
                    "export": True,
                    "isOpen": True,
                    "children": group_children["blade"]
                },
                {
                    "name": "blade_edge_glow",
                    "origin": [0, 8.4, 0],
                    "rotation": [0, 0, 0],
                    "uuid": group_uuids["blade_edge_glow"],
                    "export": True,
                    "isOpen": True,
                    "children": group_children["blade_edge_glow"]
                },
                {
                    "name": "solar_particles",
                    "origin": [0, 18.0, 0],
                    "rotation": [0, 0, 0],
                    "uuid": group_uuids["solar_particles"],
                    "export": True,
                    "isOpen": True,
                    "children": group_children["solar_particles"]
                },
                {
                    "name": "projectile_origin",
                    "origin": [5.8, 31.6, 0],
                    "rotation": [0, 0, -22],
                    "uuid": group_uuids["projectile_origin"],
                    "export": True,
                    "isOpen": True,
                    "children": group_children["projectile_origin"]
                }
            ]
        }
    ]

    # Build animations
    # 1. animation.sunforged.idle (~2s loop)
    # 2. animation.sunforged.heat (~1s once)
    # 3. animation.sunforged.charge (~0.5s loop)
    # 4. animation.sunforged.release (~0.4s once)
    # 5. animation.sunforged.cooldown (~1s once)

    animations = []

    def make_kf(channel, time_sec, x, y, z, interp="linear"):
        return {
            "uuid": new_uuid(),
            "channel": channel,
            "time": time_sec,
            "interpolation": interp,
            "data_points": [{"x": x, "y": y, "z": z}]
        }

    # 1. IDLE ANIMATION
    idle_anim = {
        "uuid": new_uuid(),
        "name": "animation.sunforged.idle",
        "loop": "loop",
        "override": False,
        "length": 2.0,
        "snapping": 24,
        "animators": {
            group_uuids["root"]: {
                "name": "root",
                "type": "bone",
                "keyframes": [
                    make_kf("position", 0.0, 0, 0, 0),
                    make_kf("position", 1.0, 0, 0.25, 0, "catmullrom"),
                    make_kf("position", 2.0, 0, 0, 0),
                    make_kf("rotation", 0.0, 0, 0, 0),
                    make_kf("rotation", 1.0, 0.4, 0, 0.8, "catmullrom"),
                    make_kf("rotation", 2.0, 0, 0, 0)
                ]
            },
            group_uuids["solar_core"]: {
                "name": "solar_core",
                "type": "bone",
                "keyframes": [
                    make_kf("scale", 0.0, 1.0, 1.0, 1.0),
                    make_kf("scale", 1.0, 1.18, 1.18, 1.18, "catmullrom"),
                    make_kf("scale", 2.0, 1.0, 1.0, 1.0)
                ]
            },
            group_uuids["blade_edge_glow"]: {
                "name": "blade_edge_glow",
                "type": "bone",
                "keyframes": [
                    make_kf("scale", 0.0, 1.0, 1.0, 1.0),
                    make_kf("scale", 0.5, 1.04, 1.01, 1.06, "catmullrom"),
                    make_kf("scale", 1.0, 0.98, 1.0, 0.98, "catmullrom"),
                    make_kf("scale", 1.5, 1.05, 1.02, 1.05, "catmullrom"),
                    make_kf("scale", 2.0, 1.0, 1.0, 1.0)
                ]
            },
            group_uuids["solar_particles"]: {
                "name": "solar_particles",
                "type": "bone",
                "keyframes": [
                    make_kf("rotation", 0.0, 0, 0, 0),
                    make_kf("rotation", 1.0, 0, 180, 0, "linear"),
                    make_kf("rotation", 2.0, 0, 360, 0, "linear"),
                    make_kf("position", 0.0, 0, 0, 0),
                    make_kf("position", 1.0, 0, 0.4, 0.2, "catmullrom"),
                    make_kf("position", 2.0, 0, 0, 0)
                ]
            }
        }
    }
    animations.append(idle_anim)

    # 2. HEAT ANIMATION
    heat_kfs_rot = []
    for step in range(11):
        t = round(step * 0.1, 2)
        if step == 0 or step == 10:
            rx, ry, rz = 0, 0, 0
        else:
            sign = 1 if step % 2 == 1 else -1
            rx = round(sign * (0.4 + step * 0.05), 2)
            ry = round(-sign * (0.3 + step * 0.04), 2)
            rz = round(sign * (0.5 + step * 0.06), 2)
        heat_kfs_rot.append(make_kf("rotation", t, rx, ry, rz))

    heat_anim = {
        "uuid": new_uuid(),
        "name": "animation.sunforged.heat",
        "loop": "once",
        "override": False,
        "length": 1.0,
        "snapping": 24,
        "animators": {
            group_uuids["solar_core"]: {
                "name": "solar_core",
                "type": "bone",
                "keyframes": [
                    make_kf("scale", 0.0, 1.0, 1.0, 1.0),
                    make_kf("scale", 0.4, 1.18, 1.18, 1.18, "catmullrom"),
                    make_kf("scale", 0.7, 1.32, 1.32, 1.32, "catmullrom"),
                    make_kf("scale", 1.0, 1.45, 1.45, 1.45)
                ]
            },
            group_uuids["blade_edge_glow"]: {
                "name": "blade_edge_glow",
                "type": "bone",
                "keyframes": [
                    make_kf("scale", 0.0, 1.0, 1.0, 1.0),
                    make_kf("scale", 0.5, 1.12, 1.04, 1.25, "catmullrom"),
                    make_kf("scale", 1.0, 1.28, 1.08, 1.45)
                ]
            },
            group_uuids["blade"]: {
                "name": "blade",
                "type": "bone",
                "keyframes": heat_kfs_rot
            },
            group_uuids["solar_particles"]: {
                "name": "solar_particles",
                "type": "bone",
                "keyframes": [
                    make_kf("scale", 0.0, 1.0, 1.0, 1.0),
                    make_kf("scale", 1.0, 1.6, 1.6, 1.6),
                    make_kf("rotation", 0.0, 0, 0, 0),
                    make_kf("rotation", 1.0, 0, 360, 180)
                ]
            }
        }
    }
    animations.append(heat_anim)

    # 3. CHARGE ANIMATION (~0.5s loop)
    charge_anim = {
        "uuid": new_uuid(),
        "name": "animation.sunforged.charge",
        "loop": "loop",
        "override": False,
        "length": 0.5,
        "snapping": 24,
        "animators": {
            group_uuids["root"]: {
                "name": "root",
                "type": "bone",
                "keyframes": [
                    make_kf("rotation", 0.0, -15, 8, -6),
                    make_kf("rotation", 0.25, -18, 10, -7, "catmullrom"),
                    make_kf("rotation", 0.5, -15, 8, -6),
                    make_kf("position", 0.0, 0, -0.2, 0.4),
                    make_kf("position", 0.25, 0, -0.35, 0.6, "catmullrom"),
                    make_kf("position", 0.5, 0, -0.2, 0.4)
                ]
            },
            group_uuids["solar_core"]: {
                "name": "solar_core",
                "type": "bone",
                "keyframes": [
                    make_kf("scale", 0.0, 1.25, 1.25, 1.25),
                    make_kf("scale", 0.25, 1.55, 1.55, 1.55, "catmullrom"),
                    make_kf("scale", 0.5, 1.25, 1.25, 1.25)
                ]
            },
            group_uuids["blade_edge_glow"]: {
                "name": "blade_edge_glow",
                "type": "bone",
                "keyframes": [
                    make_kf("position", 0.0, 0, 0.2, 0),
                    make_kf("position", 0.25, 0.2, 1.1, 0, "catmullrom"),
                    make_kf("position", 0.5, 0, 0.2, 0),
                    make_kf("scale", 0.0, 1.15, 1.02, 1.2),
                    make_kf("scale", 0.25, 1.3, 1.06, 1.35, "catmullrom"),
                    make_kf("scale", 0.5, 1.15, 1.02, 1.2)
                ]
            },
            group_uuids["solar_particles"]: {
                "name": "solar_particles",
                "type": "bone",
                "keyframes": [
                    make_kf("rotation", 0.0, 0, 0, 0),
                    make_kf("rotation", 0.25, 0, 180, 180),
                    make_kf("rotation", 0.5, 0, 360, 360),
                    make_kf("scale", 0.0, 1.3, 1.3, 1.3),
                    make_kf("scale", 0.25, 1.7, 1.7, 1.7, "catmullrom"),
                    make_kf("scale", 0.5, 1.3, 1.3, 1.3)
                ]
            },
            group_uuids["projectile_origin"]: {
                "name": "projectile_origin",
                "type": "bone",
                "keyframes": [
                    make_kf("rotation", 0.0, 0, 0, -22),
                    make_kf("rotation", 0.25, 0, 180, -22),
                    make_kf("rotation", 0.5, 0, 360, -22),
                    make_kf("scale", 0.0, 1.2, 1.2, 1.2),
                    make_kf("scale", 0.25, 1.8, 1.8, 1.8, "catmullrom"),
                    make_kf("scale", 0.5, 1.2, 1.2, 1.2)
                ]
            }
        }
    }
    animations.append(charge_anim)

    # 4. RELEASE ANIMATION (~0.4s once)
    release_anim = {
        "uuid": new_uuid(),
        "name": "animation.sunforged.release",
        "loop": "once",
        "override": False,
        "length": 0.4,
        "snapping": 24,
        "animators": {
            group_uuids["root"]: {
                "name": "root",
                "type": "bone",
                "keyframes": [
                    make_kf("rotation", 0.0, -20, 10, -8),
                    make_kf("rotation", 0.08, 32, -8, 14, "catmullrom"),
                    make_kf("rotation", 0.22, 8, -2, 4, "catmullrom"),
                    make_kf("rotation", 0.4, 0, 0, 0),
                    make_kf("position", 0.0, 0, -0.3, -0.5),
                    make_kf("position", 0.08, 0.5, 1.2, 2.5, "catmullrom"),
                    make_kf("position", 0.22, 0.1, 0.3, 0.8, "catmullrom"),
                    make_kf("position", 0.4, 0, 0, 0)
                ]
            },
            group_uuids["solar_core"]: {
                "name": "solar_core",
                "type": "bone",
                "keyframes": [
                    make_kf("scale", 0.0, 1.5, 1.5, 1.5),
                    make_kf("scale", 0.06, 2.1, 2.1, 2.1), # bright flash
                    make_kf("scale", 0.16, 0.8, 0.8, 0.8, "catmullrom"),
                    make_kf("scale", 0.4, 1.0, 1.0, 1.0)
                ]
            },
            group_uuids["blade_edge_glow"]: {
                "name": "blade_edge_glow",
                "type": "bone",
                "keyframes": [
                    make_kf("position", 0.0, 0, 0, 0),
                    make_kf("position", 0.08, 0.6, 2.8, 0, "catmullrom"), # energy slides to tip
                    make_kf("position", 0.2, 0.1, 0.6, 0, "catmullrom"),
                    make_kf("position", 0.4, 0, 0, 0),
                    make_kf("scale", 0.0, 1.3, 1.0, 1.3),
                    make_kf("scale", 0.08, 1.6, 1.1, 1.7), # flash
                    make_kf("scale", 0.2, 0.9, 1.0, 0.9, "catmullrom"),
                    make_kf("scale", 0.4, 1.0, 1.0, 1.0)
                ]
            },
            group_uuids["projectile_origin"]: {
                "name": "projectile_origin",
                "type": "bone",
                "keyframes": [
                    make_kf("scale", 0.0, 1.2, 1.2, 1.2),
                    make_kf("scale", 0.08, 2.8, 2.8, 2.8), # burst at launch origin!
                    make_kf("scale", 0.22, 0.2, 0.2, 0.2, "catmullrom"),
                    make_kf("scale", 0.4, 1.0, 1.0, 1.0),
                    make_kf("rotation", 0.0, 0, 0, -22),
                    make_kf("rotation", 0.08, 90, 180, 45),
                    make_kf("rotation", 0.22, 180, 360, 90),
                    make_kf("rotation", 0.4, 0, 0, -22)
                ]
            },
            group_uuids["solar_particles"]: {
                "name": "solar_particles",
                "type": "bone",
                "keyframes": [
                    make_kf("scale", 0.0, 1.5, 1.5, 1.5),
                    make_kf("scale", 0.08, 2.5, 2.5, 2.5),
                    make_kf("scale", 0.22, 0.3, 0.3, 0.3, "catmullrom"),
                    make_kf("scale", 0.4, 1.0, 1.0, 1.0)
                ]
            }
        }
    }
    animations.append(release_anim)

    # 5. COOLDOWN ANIMATION (~1s once)
    cooldown_anim = {
        "uuid": new_uuid(),
        "name": "animation.sunforged.cooldown",
        "loop": "once",
        "override": False,
        "length": 1.0,
        "snapping": 24,
        "animators": {
            group_uuids["root"]: {
                "name": "root",
                "type": "bone",
                "keyframes": [
                    make_kf("rotation", 0.0, 4, 0, 2),
                    make_kf("rotation", 0.4, 1, 0, 0.5, "catmullrom"),
                    make_kf("rotation", 1.0, 0, 0, 0)
                ]
            },
            group_uuids["solar_core"]: {
                "name": "solar_core",
                "type": "bone",
                "keyframes": [
                    make_kf("scale", 0.0, 0.72, 0.72, 0.72),
                    make_kf("scale", 0.5, 0.88, 0.88, 0.88, "catmullrom"),
                    make_kf("scale", 1.0, 1.0, 1.0, 1.0)
                ]
            },
            group_uuids["blade_edge_glow"]: {
                "name": "blade_edge_glow",
                "type": "bone",
                "keyframes": [
                    make_kf("scale", 0.0, 0.8, 0.95, 0.8),
                    make_kf("scale", 0.5, 0.92, 0.98, 0.92, "catmullrom"),
                    make_kf("scale", 1.0, 1.0, 1.0, 1.0)
                ]
            }
        }
    }
    animations.append(cooldown_anim)

    # Texture entry
    textures = [
        {
            "name": "sunforged_scimitar",
            "folder": "item",
            "namespace": "elemental",
            "id": "0",
            "particle": False,
            "render_mode": "default",
            "visible": True,
            "mode": "bitmap",
            "saved": True,
            "uuid": new_uuid(),
            "source": tex_b64
        }
    ]

    # Blockbench Model JSON
    bbmodel = {
        "meta": {
            "format_version": "4.10",
            "model_format": "geckolib_model",
            "box_uv": False
        },
        "name": "sunforged_scimitar",
        "model_identifier": "sunforged_scimitar",
        "visible_box": [1, 1, 0],
        "resolution": {"width": 64, "height": 64},
        "elements": elements,
        "outliner": outliner,
        "textures": textures,
        "animations": animations,
        "display": {
            "thirdperson_righthand": {"rotation": [0, -90, 55], "translation": [0, 4, 1.5], "scale": [0.75, 0.75, 0.75]},
            "thirdperson_lefthand": {"rotation": [0, 90, -55], "translation": [0, 4, 1.5], "scale": [0.75, 0.75, 0.75]},
            "firstperson_righthand": {"rotation": [0, -90, 25], "translation": [1.13, 3.2, 1.13], "scale": [0.68, 0.68, 0.68]},
            "firstperson_lefthand": {"rotation": [0, 90, -25], "translation": [1.13, 3.2, 1.13], "scale": [0.68, 0.68, 0.68]},
            "gui": {"rotation": [15, -25, -135], "translation": [1, 1, 0], "scale": [0.65, 0.65, 0.65]},
            "ground": {"rotation": [0, 0, 0], "translation": [0, 2, 0], "scale": [0.5, 0.5, 0.5]},
            "fixed": {"rotation": [0, 180, 0], "translation": [0, 0, 0], "scale": [0.6, 0.6, 0.6]}
        },
        "geckolib_model_type": "Item"
    }

    # GeckoLib Export Bedrock Geometry JSON
    geo_bones = []
    # Root
    geo_bones.append({
        "name": "root",
        "pivot": [0, 4, 0]
    })

    # Function to extract cubes for each bone
    def get_bone_cubes(group_name):
        cubes = []
        elem_map = {e["uuid"]: e for e in elements}
        for uid in group_children[group_name]:
            e = elem_map[uid]
            f = e["from"]
            t = e["to"]
            size = [round(t[0]-f[0], 3), round(t[1]-f[1], 3), round(t[2]-f[2], 3)]
            uv = {}
            for side in ["north", "east", "south", "west", "up", "down"]:
                u1, v1, u2, v2 = e["faces"][side]["uv"]
                uv[side] = {"uv": [u1, v1], "uv_size": [round(u2-u1, 3), round(v2-v1, 3)]}
            c_dict = {
                "origin": f,
                "size": size,
                "uv": uv
            }
            if "rotation" in e:
                c_dict["pivot"] = e["origin"]
                c_dict["rotation"] = e["rotation"]
            cubes.append(c_dict)
        return cubes

    bone_pivots = {
        "handle": [0, 4, 0],
        "pommel": [0, 0, 0],
        "guard": [0, 7.7, 0],
        "sun_emblem": [0, 8.2, 0],
        "solar_core": [0, 8.2, 0],
        "blade": [0, 8.4, 0],
        "blade_edge_glow": [0, 8.4, 0],
        "solar_particles": [0, 18.0, 0],
        "projectile_origin": [5.8, 31.6, 0]
    }

    for bname in ["handle", "pommel", "guard", "sun_emblem", "solar_core", "blade", "blade_edge_glow", "solar_particles", "projectile_origin"]:
        b_dict = {
            "name": bname,
            "parent": "root",
            "pivot": bone_pivots[bname],
            "cubes": get_bone_cubes(bname)
        }
        if bname == "projectile_origin":
            b_dict["rotation"] = [0, 0, -22]
        geo_bones.append(b_dict)

    geo_json = {
        "format_version": "1.12.0",
        "minecraft:geometry": [
            {
                "description": {
                    "identifier": "geometry.sunforged_scimitar",
                    "texture_width": 64,
                    "texture_height": 64,
                    "visible_bounds_width": 3,
                    "visible_bounds_height": 4,
                    "visible_bounds_offset": [0, 1.5, 0]
                },
                "bones": geo_bones
            }
        ]
    }

    # GeckoLib Export Animation JSON
    # Build standard 1.8.0 Bedrock/GeckoLib animation dictionary
    def build_gecko_anim_obj(anim_data):
        b_dict = {}
        for b_uuid, animator in anim_data["animators"].items():
            bname = animator["name"]
            b_dict[bname] = {}
            for channel in ["position", "rotation", "scale"]:
                kfs = [k for k in animator["keyframes"] if k["channel"] == channel]
                if kfs:
                    b_dict[bname][channel] = {}
                    for k in kfs:
                        t_str = str(k["time"])
                        dp = k["data_points"][0]
                        b_dict[bname][channel][t_str] = [dp["x"], dp["y"], dp["z"]]
        return {
            "loop": anim_data["loop"] == "loop",
            "animation_length": anim_data["length"],
            "bones": b_dict
        }

    anim_json = {
        "format_version": "1.8.0",
        "animations": {
            anim["name"]: build_gecko_anim_obj(anim) for anim in animations
        }
    }

    return bbmodel, geo_json, anim_json

# =====================================================================
# 3. MODEL GENERATION - SOLAR ARC PROJECTILE
# =====================================================================

def build_solar_arc_data(tex_b64):
    # Groups: projectile_root, solar_crescent, energy_core, flame_ribbons, trail_origin
    group_uuids = {
        "projectile_root": new_uuid(),
        "solar_crescent": new_uuid(),
        "energy_core": new_uuid(),
        "flame_ribbons": new_uuid(),
        "trail_origin": new_uuid()
    }

    elements = []
    def make_cube(name, from_p, to_p, origin, rot, uv_rect):
        u1, v1, u2, v2 = uv_rect
        cube_id = new_uuid()
        cube = {
            "name": name,
            "box_uv": False,
            "render_order": "default",
            "locked": False,
            "export": True,
            "scope": 0,
            "allow_mirror_modeling": True,
            "from": [round(from_p[0], 3), round(from_p[1], 3), round(from_p[2], 3)],
            "to": [round(to_p[0], 3), round(to_p[1], 3), round(to_p[2], 3)],
            "autouv": 0,
            "color": 0,
            "origin": [round(origin[0], 3), round(origin[1], 3), round(origin[2], 3)],
            "faces": {
                "north": {"uv": [u1, v1, u2, v2], "texture": 0},
                "east": {"uv": [u1, v1, u2, v2], "texture": 0},
                "south": {"uv": [u1, v1, u2, v2], "texture": 0},
                "west": {"uv": [u1, v1, u2, v2], "texture": 0},
                "up": {"uv": [u1, v1, u2, v2], "texture": 0},
                "down": {"uv": [u1, v1, u2, v2], "texture": 0}
            },
            "type": "cube",
            "uuid": cube_id
        }
        if rot and any(r != 0 for r in rot):
            cube["rotation"] = [round(rot[0], 3), round(rot[1], 3), round(rot[2], 3)]
        elements.append(cube)
        return cube_id

    group_children = {k: [] for k in group_uuids}

    # 1. Solar Crescent (crescent shaped blade)
    # Center apex
    c = make_cube("crescent_apex", [-1.5, -0.6, 1.2], [1.5, 0.6, 2.8], [0, 0, 2.0], [0, 0, 0], [4, 4, 16, 14])
    group_children["solar_crescent"].append(c)
    # Left inner wing
    c = make_cube("crescent_wing_l", [-3.4, -0.5, -0.4], [-1.2, 0.5, 1.8], [-1.5, 0, 1.2], [0, 28, 0], [8, 8, 20, 20])
    group_children["solar_crescent"].append(c)
    # Right inner wing
    c = make_cube("crescent_wing_r", [1.2, -0.5, -0.4], [3.4, 0.5, 1.8], [1.5, 0, 1.2], [0, -28, 0], [8, 8, 20, 20])
    group_children["solar_crescent"].append(c)
    # Left tip horn
    c = make_cube("crescent_tip_l", [-4.8, -0.4, -2.2], [-2.8, 0.4, 0.2], [-3.4, 0, -0.4], [0, 48, 0], [12, 12, 28, 26])
    group_children["solar_crescent"].append(c)
    # Right tip horn
    c = make_cube("crescent_tip_r", [2.8, -0.4, -2.2], [4.8, 0.4, 0.2], [3.4, 0, -0.4], [0, -48, 0], [12, 12, 28, 26])
    group_children["solar_crescent"].append(c)

    # 2. Energy Core
    # Blazing core
    c = make_cube("energy_core_main", [-1.2, -1.2, 0.2], [1.2, 1.2, 2.2], [0, 0, 1.2], [0, 45, 45], [36, 4, 52, 20])
    group_children["energy_core"].append(c)
    # Brilliant white inner cube
    c = make_cube("energy_core_inner", [-0.7, -0.7, 0.7], [0.7, 0.7, 1.7], [0, 0, 1.2], [45, 0, 45], [44, 12, 52, 20])
    group_children["energy_core"].append(c)

    # 3. Flame Ribbons (flowing backward)
    # Left flame streamer
    c = make_cube("flame_ribbon_l", [-3.0, -0.35, -4.8], [-1.6, 0.35, -0.8], [-2.3, 0, -0.8], [6, 14, -6], [2, 34, 18, 54])
    group_children["flame_ribbons"].append(c)
    # Right flame streamer
    c = make_cube("flame_ribbon_r", [1.6, -0.35, -4.8], [3.0, 0.35, -0.8], [2.3, 0, -0.8], [6, -14, 6], [2, 34, 18, 54])
    group_children["flame_ribbons"].append(c)
    # Center trailing tail
    c = make_cube("flame_ribbon_center", [-0.9, -0.3, -5.6], [0.9, 0.3, -1.2], [0, 0, -1.2], [0, 0, 0], [20, 36, 36, 58])
    group_children["flame_ribbons"].append(c)

    # 4. Trail Origin (locator)
    c = make_cube("trail_origin_node", [-0.2, -0.2, -5.8], [0.2, 0.2, -5.4], [0, 0, -5.6], [0, 0, 0], [50, 34, 54, 38])
    group_children["trail_origin"].append(c)

    # Outliner
    outliner = [
        {
            "name": "projectile_root",
            "origin": [0, 0, 0],
            "rotation": [0, 0, 0],
            "uuid": group_uuids["projectile_root"],
            "export": True,
            "isOpen": True,
            "children": [
                {
                    "name": "solar_crescent",
                    "origin": [0, 0, 1.2],
                    "rotation": [0, 0, 0],
                    "uuid": group_uuids["solar_crescent"],
                    "export": True,
                    "isOpen": True,
                    "children": group_children["solar_crescent"]
                },
                {
                    "name": "energy_core",
                    "origin": [0, 0, 1.2],
                    "rotation": [0, 0, 0],
                    "uuid": group_uuids["energy_core"],
                    "export": True,
                    "isOpen": True,
                    "children": group_children["energy_core"]
                },
                {
                    "name": "flame_ribbons",
                    "origin": [0, 0, -1.0],
                    "rotation": [0, 0, 0],
                    "uuid": group_uuids["flame_ribbons"],
                    "export": True,
                    "isOpen": True,
                    "children": group_children["flame_ribbons"]
                },
                {
                    "name": "trail_origin",
                    "origin": [0, 0, -5.6],
                    "rotation": [0, 0, 0],
                    "uuid": group_uuids["trail_origin"],
                    "export": True,
                    "isOpen": True,
                    "children": group_children["trail_origin"]
                }
            ]
        }
    ]

    # Animations
    # 6. animation.solar_arc.travel (~0.35s loop)
    # 7. animation.solar_arc.impact (~0.25s once)
    animations = []

    def make_kf(channel, time_sec, x, y, z, interp="linear"):
        return {
            "uuid": new_uuid(),
            "channel": channel,
            "time": time_sec,
            "interpolation": interp,
            "data_points": [{"x": x, "y": y, "z": z}]
        }

    travel_anim = {
        "uuid": new_uuid(),
        "name": "animation.solar_arc.travel",
        "loop": "loop",
        "override": False,
        "length": 0.35,
        "snapping": 24,
        "animators": {
            group_uuids["projectile_root"]: {
                "name": "projectile_root",
                "type": "bone",
                "keyframes": [
                    make_kf("rotation", 0.0, 0, 0, 0),
                    make_kf("rotation", 0.175, 0, 0, 180),
                    make_kf("rotation", 0.35, 0, 0, 360)
                ]
            },
            group_uuids["energy_core"]: {
                "name": "energy_core",
                "type": "bone",
                "keyframes": [
                    make_kf("scale", 0.0, 1.0, 1.0, 1.0),
                    make_kf("scale", 0.175, 1.35, 1.35, 1.35, "catmullrom"),
                    make_kf("scale", 0.35, 1.0, 1.0, 1.0)
                ]
            },
            group_uuids["flame_ribbons"]: {
                "name": "flame_ribbons",
                "type": "bone",
                "keyframes": [
                    make_kf("position", 0.0, 0, 0, 0),
                    make_kf("position", 0.09, 0.12, 0.08, -0.6, "catmullrom"),
                    make_kf("position", 0.18, -0.1, -0.06, -0.3, "catmullrom"),
                    make_kf("position", 0.27, 0.08, -0.1, -0.7, "catmullrom"),
                    make_kf("position", 0.35, 0, 0, 0),
                    make_kf("scale", 0.0, 1.0, 1.0, 1.0),
                    make_kf("scale", 0.09, 0.94, 1.15, 1.25, "catmullrom"),
                    make_kf("scale", 0.18, 1.08, 0.92, 1.05, "catmullrom"),
                    make_kf("scale", 0.27, 0.96, 1.1, 1.3, "catmullrom"),
                    make_kf("scale", 0.35, 1.0, 1.0, 1.0),
                    make_kf("rotation", 0.0, 0, 0, 0),
                    make_kf("rotation", 0.09, 4, 3, 2),
                    make_kf("rotation", 0.18, -3, -4, -3),
                    make_kf("rotation", 0.27, 3, -2, 4),
                    make_kf("rotation", 0.35, 0, 0, 0)
                ]
            }
        }
    }
    animations.append(travel_anim)

    impact_anim = {
        "uuid": new_uuid(),
        "name": "animation.solar_arc.impact",
        "loop": "once",
        "override": False,
        "length": 0.25,
        "snapping": 24,
        "animators": {
            group_uuids["energy_core"]: {
                "name": "energy_core",
                "type": "bone",
                "keyframes": [
                    make_kf("scale", 0.0, 1.0, 1.0, 1.0),
                    make_kf("scale", 0.07, 3.8, 3.8, 3.8), # bright flash
                    make_kf("scale", 0.15, 2.2, 2.2, 2.2, "catmullrom"),
                    make_kf("scale", 0.25, 0.0, 0.0, 0.0) # shrink to zero
                ]
            },
            group_uuids["solar_crescent"]: {
                "name": "solar_crescent",
                "type": "bone",
                "keyframes": [
                    make_kf("scale", 0.0, 1.0, 1.0, 1.0),
                    make_kf("scale", 0.08, 1.9, 1.9, 1.9),
                    make_kf("scale", 0.16, 1.2, 1.2, 1.2, "catmullrom"),
                    make_kf("scale", 0.25, 0.0, 0.0, 0.0),
                    make_kf("rotation", 0.0, 0, 0, 0),
                    make_kf("rotation", 0.08, 35, 50, 80),
                    make_kf("rotation", 0.16, 60, 90, 140),
                    make_kf("rotation", 0.25, 80, 120, 180) # outward rotation
                ]
            },
            group_uuids["flame_ribbons"]: {
                "name": "flame_ribbons",
                "type": "bone",
                "keyframes": [
                    make_kf("scale", 0.0, 1.0, 1.0, 1.0),
                    make_kf("scale", 0.07, 2.8, 2.8, 2.8),
                    make_kf("scale", 0.25, 0.0, 0.0, 0.0),
                    make_kf("rotation", 0.0, 0, 0, 0),
                    make_kf("rotation", 0.07, -30, 45, 90),
                    make_kf("rotation", 0.25, -50, 80, 180)
                ]
            },
            group_uuids["projectile_root"]: {
                "name": "projectile_root",
                "type": "bone",
                "keyframes": [
                    make_kf("scale", 0.0, 1.0, 1.0, 1.0),
                    make_kf("scale", 0.22, 0.25, 0.25, 0.25, "catmullrom"),
                    make_kf("scale", 0.25, 0.0, 0.0, 0.0) # complete shrink to 0
                ]
            }
        }
    }
    animations.append(impact_anim)

    # Texture entry
    textures = [
        {
            "name": "solar_arc",
            "folder": "entity",
            "namespace": "elemental",
            "id": "0",
            "particle": False,
            "render_mode": "default",
            "visible": True,
            "mode": "bitmap",
            "saved": True,
            "uuid": new_uuid(),
            "source": tex_b64
        }
    ]

    # Blockbench Model JSON
    bbmodel = {
        "meta": {
            "format_version": "4.10",
            "model_format": "geckolib_model",
            "box_uv": False
        },
        "name": "solar_arc",
        "model_identifier": "solar_arc",
        "visible_box": [1, 1, 0],
        "resolution": {"width": 64, "height": 64},
        "elements": elements,
        "outliner": outliner,
        "textures": textures,
        "animations": animations,
        "geckolib_model_type": "Entity"
    }

    # GeckoLib Bedrock Geometry JSON
    geo_bones = []
    geo_bones.append({
        "name": "projectile_root",
        "pivot": [0, 0, 0]
    })

    def get_bone_cubes(group_name):
        cubes = []
        elem_map = {e["uuid"]: e for e in elements}
        for uid in group_children[group_name]:
            e = elem_map[uid]
            f = e["from"]
            t = e["to"]
            size = [round(t[0]-f[0], 3), round(t[1]-f[1], 3), round(t[2]-f[2], 3)]
            uv = {}
            for side in ["north", "east", "south", "west", "up", "down"]:
                u1, v1, u2, v2 = e["faces"][side]["uv"]
                uv[side] = {"uv": [u1, v1], "uv_size": [round(u2-u1, 3), round(v2-v1, 3)]}
            c_dict = {
                "origin": f,
                "size": size,
                "uv": uv
            }
            if "rotation" in e:
                c_dict["pivot"] = e["origin"]
                c_dict["rotation"] = e["rotation"]
            cubes.append(c_dict)
        return cubes

    bone_pivots = {
        "solar_crescent": [0, 0, 1.2],
        "energy_core": [0, 0, 1.2],
        "flame_ribbons": [0, 0, -1.0],
        "trail_origin": [0, 0, -5.6]
    }

    for bname in ["solar_crescent", "energy_core", "flame_ribbons", "trail_origin"]:
        b_dict = {
            "name": bname,
            "parent": "projectile_root",
            "pivot": bone_pivots[bname],
            "cubes": get_bone_cubes(bname)
        }
        geo_bones.append(b_dict)

    geo_json = {
        "format_version": "1.12.0",
        "minecraft:geometry": [
            {
                "description": {
                    "identifier": "geometry.solar_arc",
                    "texture_width": 64,
                    "texture_height": 64,
                    "visible_bounds_width": 2,
                    "visible_bounds_height": 2,
                    "visible_bounds_offset": [0, 0, 0]
                },
                "bones": geo_bones
            }
        ]
    }

    def build_gecko_anim_obj(anim_data):
        b_dict = {}
        for b_uuid, animator in anim_data["animators"].items():
            bname = animator["name"]
            b_dict[bname] = {}
            for channel in ["position", "rotation", "scale"]:
                kfs = [k for k in animator["keyframes"] if k["channel"] == channel]
                if kfs:
                    b_dict[bname][channel] = {}
                    for k in kfs:
                        t_str = str(k["time"])
                        dp = k["data_points"][0]
                        b_dict[bname][channel][t_str] = [dp["x"], dp["y"], dp["z"]]
        return {
            "loop": anim_data["loop"] == "loop",
            "animation_length": anim_data["length"],
            "bones": b_dict
        }

    anim_json = {
        "format_version": "1.8.0",
        "animations": {
            anim["name"]: build_gecko_anim_obj(anim) for anim in animations
        }
    }

    return bbmodel, geo_json, anim_json


# =====================================================================
# 4. EXECUTION AND FILE EXPORT
# =====================================================================

def main():
    print("Generating textures...")
    scimitar_tex = generate_scimitar_texture()
    scimitar_tex_b64 = image_to_base64(scimitar_tex)

    solar_arc_tex = generate_solar_arc_texture()
    solar_arc_tex_b64 = image_to_base64(solar_arc_tex)

    print("Building Sunforged Scimitar models & animations...")
    scimitar_bbmodel, scimitar_geo, scimitar_anim = build_sunforged_scimitar_data(scimitar_tex_b64)

    print("Building Solar Arc models & animations...")
    solar_arc_bbmodel, solar_arc_geo, solar_arc_anim = build_solar_arc_data(solar_arc_tex_b64)

    # Save to Project Root
    print("Saving files to workspace root...")
    with open(os.path.join(PROJECT_DIR, "sunforged_scimitar.bbmodel"), "w", encoding="utf-8") as f:
        json.dump(scimitar_bbmodel, f, indent=2)
    # Also overwrite 'sunforged scimitar.bbmodel' (with space) so existing file is upgraded!
    with open(os.path.join(PROJECT_DIR, "sunforged scimitar.bbmodel"), "w", encoding="utf-8") as f:
        json.dump(scimitar_bbmodel, f, indent=2)
    with open(os.path.join(PROJECT_DIR, "solar_arc.bbmodel"), "w", encoding="utf-8") as f:
        json.dump(solar_arc_bbmodel, f, indent=2)

    with open(os.path.join(PROJECT_DIR, "sunforged_scimitar.geo.json"), "w", encoding="utf-8") as f:
        json.dump(scimitar_geo, f, indent=2)
    with open(os.path.join(PROJECT_DIR, "sunforged_scimitar.animation.json"), "w", encoding="utf-8") as f:
        json.dump(scimitar_anim, f, indent=2)
    scimitar_tex.save(os.path.join(PROJECT_DIR, "sunforged_scimitar.png"))

    with open(os.path.join(PROJECT_DIR, "solar_arc.geo.json"), "w", encoding="utf-8") as f:
        json.dump(solar_arc_geo, f, indent=2)
    with open(os.path.join(PROJECT_DIR, "solar_arc.animation.json"), "w", encoding="utf-8") as f:
        json.dump(solar_arc_anim, f, indent=2)
    solar_arc_tex.save(os.path.join(PROJECT_DIR, "solar_arc.png"))

    # Save to Mod Assets directory
    print("Saving files to mod assets directory...")
    with open(os.path.join(ASSETS_DIR, "geo", "sunforged_scimitar.geo.json"), "w", encoding="utf-8") as f:
        json.dump(scimitar_geo, f, indent=2)
    with open(os.path.join(ASSETS_DIR, "animations", "sunforged_scimitar.animation.json"), "w", encoding="utf-8") as f:
        json.dump(scimitar_anim, f, indent=2)
    scimitar_tex.save(os.path.join(ASSETS_DIR, "textures", "item", "sunforged_scimitar.png"))

    with open(os.path.join(ASSETS_DIR, "geo", "solar_arc.geo.json"), "w", encoding="utf-8") as f:
        json.dump(solar_arc_geo, f, indent=2)
    with open(os.path.join(ASSETS_DIR, "animations", "solar_arc.animation.json"), "w", encoding="utf-8") as f:
        json.dump(solar_arc_anim, f, indent=2)
    solar_arc_tex.save(os.path.join(ASSETS_DIR, "textures", "entity", "solar_arc.png"))

    print("SUCCESS: All models, textures, animations, and bbmodel files generated!")

if __name__ == "__main__":
    main()
