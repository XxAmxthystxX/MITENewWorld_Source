# -*- coding: utf-8 -*-
"""生成 13 张金属工具箱物品贴图（16x16）。

造型：带圆角盖子的工具箱，按 ModAlloyMetals 里各金属的颜色着色。
明暗处理：顶盖提亮、箱体本色、底部压暗，锁扣用深色金属灰。
"""
from PIL import Image, ImageDraw
import os

OUT = os.path.join(os.path.dirname(os.path.dirname(os.path.abspath(__file__))),
                   'src', 'main', 'resources', 'assets', 'mitenewworld',
                   'textures', 'item', 'toolbox')
os.makedirs(OUT, exist_ok=True)

# 与 ModAlloyMetals 一致
METALS = [
    ('copper', 0xE77C56), ('silver', 0xE8E8F0), ('iron', 0xD8D8D8),
    ('titanium', 0x98A8C0), ('mithril', 0xB8D8F0), ('adamantium', 0x6A4A8A),
    ('ancient_metal', 0x2A9088), ('tin', 0xA8B8B8), ('gold', 0xFFD966),
    ('aluminium', 0xC8D0D8), ('platinum', 0xE0D8C8), ('iridium', 0xA8C0D8),
    ('starlight', 0x9080E8),
]

OUTLINE = (40, 34, 30, 255)          # 外描边
DARK = 0.62                          # 暗部系数
LIGHT = 1.28                         # 亮部系数
LATCH = (70, 66, 62, 255)            # 锁扣


def shade(rgb, f):
    return tuple(min(255, int(c * f)) for c in rgb)


def toolbox_image(base):
    r, g, b = base
    body = (r, g, b, 255)
    dark = shade(base, DARK) + (255,)
    light = shade(base, LIGHT) + (255,)
    deep = shade(base, DARK * 0.8) + (255,)

    im = Image.new('RGBA', (16, 16), (0, 0, 0, 0))
    d = ImageDraw.Draw(im)

    # 箱体：x 2..13, y 6..13
    d.rectangle([2, 6, 13, 13], fill=body, outline=OUTLINE)
    # 顶盖：x 2..13, y 4..6（略亮）
    d.rectangle([2, 4, 13, 5], fill=light)
    d.line([2, 4, 13, 4], fill=OUTLINE)
    d.line([2, 6, 13, 6], fill=deep)
    # 提手：y 2..3，x 6..9
    d.rectangle([6, 2, 9, 3], fill=dark, outline=OUTLINE)
    d.point([7, 3], fill=OUTLINE)
    d.point([8, 3], fill=OUTLINE)
    # 箱体竖向棱线
    d.line([4, 7, 4, 12], fill=deep)
    d.line([11, 7, 11, 12], fill=deep)
    # 底部阴影
    d.line([3, 12, 12, 12], fill=deep)
    # 锁扣：中央
    d.rectangle([7, 7, 8, 9], fill=LATCH, outline=OUTLINE)
    # 高光
    d.point([3, 7], fill=light)
    d.point([12, 7], fill=light)
    return im


for name, color in METALS:
    rgb = ((color >> 16) & 0xFF, (color >> 8) & 0xFF, color & 0xFF)
    toolbox_image(rgb).save(os.path.join(OUT, '%s_toolbox.png' % name))
    print('+ %s_toolbox.png  base=#%06X' % (name, color))
print('共 %d 张' % len(METALS))
