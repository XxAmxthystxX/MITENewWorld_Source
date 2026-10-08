"""
按映射表搬迁 assets 下的贴图 / 模型，并同步改写全部引用。

用法: python migrate_assets.py <resources根目录> <映射文件.json> [--apply]

映射文件格式: [["旧路径(相对 textures/ 或 models/, 无扩展名)", "新路径"], ...]

注意：贴图路径与模型路径在 JSON 引用里写法相同（都是 mitenewworld:item/xxx），
因此脚本对同一路径的贴图与模型**一起搬**，引用替换才能同时覆盖两者。
"""
import json
import shutil
import sys
from pathlib import Path

NS = 'mitenewworld'


def load_map(path):
    return json.loads(Path(path).read_text(encoding='utf-8'))


def move_files(base: Path, mapping, apply, log):
    """base 为 assets/<ns>/textures 或 assets/<ns>/models"""
    for old, new in mapping:
        for ext in ('.png', '.json', '.png.mcmeta'):
            src = base / (old + ext)
            if not src.exists():
                continue
            dst = base / (new + ext)
            dst.parent.mkdir(parents=True, exist_ok=True)
            if apply:
                shutil.move(str(src), str(dst))
            log.append(f'  move {base.name}/{old}{ext} -> {new}{ext}')


def rewrite(roots, mapping, apply, log):
    import re
    pat = re.compile(r'mitenewworld:([A-Za-z0-9_/.\-]+)')
    changed = 0
    for root in roots:
        if not root.exists():
            continue
        for p in root.rglob('*'):
            if not p.is_file() or p.suffix not in ('.json', '.java'):
                continue
            try:
                text = p.read_text(encoding='utf-8')
            except Exception:
                continue
            orig = text
            for old, new in mapping:
                if old == new:
                    continue
                text = text.replace(f'mitenewworld:{old}"', f'mitenewworld:{new}"')
                text = text.replace(f'textures/{old}.png"', f'textures/{new}.png"')
            if text != orig:
                changed += 1
                if apply:
                    p.write_text(text, encoding='utf-8')
    log.append(f'  改写引用文件数: {changed}')


def main():
    args = [a for a in sys.argv[1:] if a != '--apply']
    root = Path(args[0])
    mapping = load_map(args[1])
    apply = '--apply' in sys.argv

    assets = root / 'assets' / NS
    log = []
    move_files(assets / 'textures', mapping, apply, log)
    move_files(assets / 'models', mapping, apply, log)
    rewrite([root, root.parent / 'java'], mapping, apply, log)

    print('模式:', '实际写入' if apply else '演练')
    for line in log:
        print(line)
    if not apply:
        print('\n(加 --apply 才会真正执行)')


if __name__ == '__main__':
    main()
