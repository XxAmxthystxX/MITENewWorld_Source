"""校验资源引用是否悬空：assets 下的 mitenewworld: 引用必须能落到 models/ 或 textures/；
Java 里的 textures/xxx.png 必须存在。"""
import re
import sys
from pathlib import Path

ROOT = Path(sys.argv[1])
NS = 'mitenewworld'
assets = ROOT / 'assets' / NS
models = assets / 'models'
textures = assets / 'textures'

bad = []

# 非资源路径的 mitenewworld: 引用：代码里注册的染色源 id
NON_ASSET = {'alloy', 'casting_table'}

# 1) assets 下的 JSON 引用
for p in assets.rglob('*.json'):
    rel = p.relative_to(assets).as_posix()
    # equipment 的贴图按图层目录相对解析，跳过
    if rel.startswith('equipment/'):
        continue
    try:
        text = p.read_text(encoding='utf-8')
    except Exception:
        continue
    for m in re.finditer(r'mitenewworld:([A-Za-z0-9_/.\-]+)', text):
        ref = m.group(1)
        if ref in NON_ASSET:
            continue
        if (models / (ref + '.json')).exists():
            continue
        if (textures / (ref + '.png')).exists():
            continue
        bad.append((f'assets/{NS}/{rel}', f'mitenewworld:{ref}'))

# 2) Java 里的 textures 引用
java_root = ROOT.parent / 'java'
if java_root.exists():
    for p in java_root.rglob('*.java'):
        try:
            text = p.read_text(encoding='utf-8')
        except Exception:
            continue
        for m in re.finditer(r'textures/([A-Za-z0-9_/.\-]+\.png)', text):
            ref = m.group(1)
            # Identifier.ofVanilla 指向原版贴图，不在本 mod 目录内
            start = max(0, m.start() - 40)
            if 'ofVanilla' in text[start:m.start()]:
                continue
            if not (textures / ref).exists():
                bad.append((f'java/{p.name}', f'textures/{ref}'))

if not bad:
    print('校验通过：未发现悬空引用')
else:
    print(f'发现 {len(bad)} 处悬空引用：')
    seen = set()
    for src, ref in bad:
        key = (src, ref)
        if key in seen:
            continue
        seen.add(key)
        print(f'  {src}  ->  {ref}')
    print(f'\n去重后 {len(seen)} 处')
