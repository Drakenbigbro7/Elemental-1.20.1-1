"""
Procedural texture generator for Rootbound Axe (64x64 RGBA).
Generates texture mapped to assets/elemental/geo/rootbound_axe.geo.json UV layout.
"""

from PIL import Image, ImageDraw
import random
import os

def create_rootbound_axe_texture(output_path: str):
    # Fix seed for reproducible, high quality pixel art
    random.seed(42)

    width, height = 64, 64
    img = Image.new("RGBA", (width, height), (0, 0, 0, 0))
    pixels = img.load()

    def clamp(val, min_v=0, max_v=255):
        return max(min_v, min(max_v, int(val)))

    def apply_noise(color, variance):
        r, g, b, a = color if len(color) == 4 else (*color, 255)
        d = random.randint(-variance, variance)
        return (clamp(r + d), clamp(g + d), clamp(b + d), a)

    # 1. Fill background / clear
    # 2. Texture Bark / Handle Region: U: 0..13, V: 0..44
    bark_base = (64, 42, 24, 255)
    bark_dark = (42, 27, 15, 255)
    bark_light = (82, 54, 32, 255)

    for x in range(0, 14):
        for y in range(0, 45):
            # Vertical grain variation with occasional knot
            noise = random.randint(-8, 8)
            grain = ((x * 3 + y * 7) % 5)
            if grain == 0:
                base = bark_dark
            elif grain == 1:
                base = bark_light
            else:
                base = bark_base
            pixels[x, y] = apply_noise(base, 10)

    # 3. Axe Head Stone & Poll Region: U: 14..28, V: 0..45
    stone_base = (55, 62, 65, 255)
    stone_dark = (40, 46, 48, 255)
    stone_light = (75, 84, 88, 255)

    for x in range(14, 28):
        for y in range(0, 45):
            # Mottled stone noise
            stone_pattern = (x + y * 2) % 4
            if stone_pattern == 0:
                base = stone_dark
            elif stone_pattern == 1:
                base = stone_light
            else:
                base = stone_base
            # Subtle greenish patina/moss on stone
            patina = random.randint(0, 10) > 7
            if patina:
                col = (base[0] - 10, base[1] + 12, base[2] - 5, 255)
            else:
                col = base
            pixels[x, y] = apply_noise(col, 8)

    # 4. Sharpened Cutting Edge: U: 28..34, V: 20..34
    edge_base = (185, 200, 210, 255)
    edge_highlight = (230, 245, 255, 255)
    edge_bevel = (140, 155, 165, 255)

    for x in range(28, 35):
        for y in range(20, 35):
            # Gradient towards highlight at the apex
            if x in (30, 31):
                col = edge_highlight
            elif x in (29, 32):
                col = edge_base
            else:
                col = edge_bevel
            pixels[x, y] = apply_noise(col, 6)

    # 5. Root Tendrils: U: 30..61, V: 0..21
    root_base = (98, 62, 32, 255)
    root_dark = (70, 44, 22, 255)
    root_light = (122, 78, 41, 255)

    for x in range(30, 62):
        for y in range(0, 22):
            fibers = (y + (x % 3)) % 3
            if fibers == 0:
                base = root_dark
            elif fibers == 1:
                base = root_light
            else:
                base = root_base
            pixels[x, y] = apply_noise(base, 10)

    # 6. Foliage / Moss / Leaves: U: 35..61, V: 22..33
    leaf_deep = (38, 115, 36, 255)
    leaf_vibrant = (55, 168, 52, 255)
    leaf_moss = (95, 142, 45, 255)
    leaf_sage = (145, 195, 65, 255)

    for x in range(35, 62):
        for y in range(22, 34):
            leaf_type = (x * 2 + y * 3) % 4
            if leaf_type == 0:
                base = leaf_deep
            elif leaf_type == 1:
                base = leaf_vibrant
            elif leaf_type == 2:
                base = leaf_moss
            else:
                base = leaf_sage
            pixels[x, y] = apply_noise(base, 12)

    # 7. Bioluminescent Glow Runes & Sap Nodes: U: 0..19, V: 48..55
    glow_core = (235, 255, 220, 255)
    glow_bright = (130, 255, 85, 255)
    glow_border = (65, 205, 35, 255)

    for x in range(0, 19):
        for y in range(48, 55):
            # Center bright rune core
            sub_x = x % 4
            sub_y = (y - 48) % 4
            if (sub_x in (1, 2)) and (sub_y in (1, 2)):
                base = glow_core
            elif (sub_x in (1, 2)) or (sub_y in (1, 2)):
                base = glow_bright
            else:
                base = glow_border
            pixels[x, y] = apply_noise(base, 5)

    # Ensure output directory exists and save
    os.makedirs(os.path.dirname(output_path), exist_ok=True)
    img.save(output_path, "PNG")
    print(f"Successfully generated Rootbound Axe texture at {output_path} (64x64 RGBA)")

if __name__ == "__main__":
    target = os.path.abspath("src/main/resources/assets/elemental/textures/item/rootbound_axe.png")
    create_rootbound_axe_texture(target)
