"""扫描 resources：统计每个贴图/模型文件被引用的情况，找出死文件。"""
import os
import re
import sys
from pathlib import Path

ROOT = Path(sys.argv[1])
NS = 'mitenewworld'

# 收集所有引用字符串（JSON 里的 "mitenewworld:xxx" 以及 Java 里的 textures/xxx.png）
refs = set()
# 引用可能来自 resources（JSON）也可能来自 src/main/java（Identifier 字符串）
scan_roots = [ROOT, ROOT.parent / 'java']
for p in [f for r in scan_roots if r.exists() for f in r.rglob('*')]:
    if not p.is_file():
        continue
    if p.suffix not in ('.json', '.java', '.accesswidener', '.mcmeta'):
        continue
    try:
        text = p.read_text(encoding='utf-8')
    except Exception:
        continue
    for m in re.finditer(r'mitenewworld:([A-Za-z0-9_/.\-]+)', text):
        refs.add(m.group(1))
    for m in re.finditer(r'textures/([A-Za-z0-9_/.\-]+)', text):
        refs.add('textures/' + m.group(1))

tex_dir = ROOT / 'assets' / NS / 'textures'
model_dir = ROOT / 'assets' / NS / 'models'

print('引用字符串总数:', len(refs))
print()


def check(base_dir, label, prefix_in_ref):
    """prefix_in_ref: 引用里使用的前缀，如 'block/' 或 'item/'"""
    live, dead = [], []
    for f in sorted(base_dir.rglob('*')):
        if not f.is_file():
            continue
        rel = f.relative_to(base_dir).as_posix()
        noext = rel.rsplit('.', 1)[0]
        keys = [prefix_in_ref + noext, 'textures/' + prefix_in_ref + rel]
        hit = any(k in refs for k in keys)
        (live if hit else dead).append(rel)
    print(f'--- {label}: 共 {len(live) + len(dead)}，被引用 {len(live)}，疑似未引用 {len(dead)} ---')
    for d in dead[:40]:
        print('   未引用:', d)
    if len(dead) > 40:
        print(f'   ... 另有 {len(dead) - 40} 个')
    print()
    return dead


check(tex_dir, '贴图 textures', '')
print('=== 细分：未引用贴图按目录统计 ===')
dead_all = []
for f in sorted(tex_dir.rglob('*')):
    if not f.is_file():
        continue
    rel = f.relative_to(tex_dir).as_posix()
    noext = rel.rsplit('.', 1)[0]
    if not any(k in refs for k in [noext, 'textures/' + rel]):
        dead_all.append(rel)
from collections import Counter
c = Counter(x.rsplit('/', 1)[0] if '/' in x else '(根)' for x in dead_all)
for k, v in c.most_common():
    print(f'  {v:4d}  {k}')
