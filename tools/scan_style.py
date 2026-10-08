"""
扫描 Java 源码中的两类风格违规：

规则 1: 方法调用 / 方法声明的实参（形参）列表，参数个数 <= 4 时不允许跨行。
规则 2: if / while / for / else 等语句无论作用域多小，都必须带大括号。

用法:
    python scan_style.py <java根目录> [--json 输出文件]
"""

import json
import re
import sys
from pathlib import Path


def build_code_mask(src: str) -> list:
    """返回一个与 src 等长的 bool 列表，True 表示该字符属于"代码"，
    False 表示位于注释 / 字符串 / 字符字面量 / 文本块内部。"""
    n = len(src)
    mask = [True] * n
    i = 0
    while i < n:
        c = src[i]
        if c == '/' and i + 1 < n:
            nxt = src[i + 1]
            if nxt == '/':
                j = src.find('\n', i)
                j = n if j == -1 else j
                for k in range(i, j):
                    mask[k] = False
                i = j
                continue
            if nxt == '*':
                j = src.find('*/', i + 2)
                j = n if j == -1 else j + 2
                for k in range(i, j):
                    mask[k] = False
                i = j
                continue
        if c == '"':
            # 文本块 """ ... """
            if src.startswith('"""', i):
                j = src.find('"""', i + 3)
                j = n if j == -1 else j + 3
                for k in range(i, j):
                    mask[k] = False
                i = j
                continue
            j = i + 1
            while j < n:
                if src[j] == '\\':
                    j += 2
                    continue
                if src[j] == '"':
                    j += 1
                    break
                j += 1
            for k in range(i, min(j, n)):
                mask[k] = False
            i = j
            continue
        if c == "'":
            j = i + 1
            while j < n:
                if src[j] == '\\':
                    j += 2
                    continue
                if src[j] == "'":
                    j += 1
                    break
                j += 1
            for k in range(i, min(j, n)):
                mask[k] = False
            i = j
            continue
        i += 1
    return mask


def match_paren(src: str, mask: list, open_idx: int) -> int:
    """给定 '(' 的下标，返回匹配的 ')' 下标；找不到返回 -1。"""
    depth = 0
    for i in range(open_idx, len(src)):
        if not mask[i]:
            continue
        c = src[i]
        if c in '([{':
            depth += 1
        elif c in ')]}':
            depth -= 1
            if depth == 0:
                return i
    return -1


def split_top_level(src: str, mask: list, start: int, end: int, sep: str = ',') -> list:
    """在 [start, end) 区间内按顶层 sep 切分，返回各段 (起, 止)。"""
    parts = []
    depth = 0
    last = start
    i = start
    while i < end:
        if not mask[i]:
            i += 1
            continue
        c = src[i]
        if c in '([{':
            depth += 1
        elif c in ')]}':
            depth -= 1
        elif c == sep and depth == 0:
            parts.append((last, i))
            last = i + 1
        i += 1
    parts.append((last, end))
    return parts


KEYWORD_RE = re.compile(r'\b(if|while|for|else)\b')


def scan_braces(src: str, mask: list, rel_path: str, out: list):
    """规则 2: 检测无大括号的 if / while / for / else。"""
    for m in KEYWORD_RE.finditer(src):
        i = m.start()
        if not mask[i]:
            continue
        kw = m.group(1)
        # 前一个非空白字符不能是字母数字下划线或 '.'（避免匹配标识符里的子串）
        p = i - 1
        while p >= 0 and src[p] in ' \t\r\n':
            p -= 1
        if p >= 0 and (src[p].isalnum() or src[p] in '_.'):
            continue

        if kw == 'else':
            # else 后面跳过空白与注释，遇到 if 则交给 if 分支处理
            j = next_code_char(src, mask, m.end())
            if j == -1:
                continue
            if src.startswith('if', j) and is_word_boundary(src, j + 2):
                continue  # else if -> 由 if 分支负责
            if src[j] != '{':
                line = src.count('\n', 0, i) + 1
                out.append({
                    'rule': 'brace',
                    'file': rel_path,
                    'line': line,
                    'kind': 'else',
                    'snippet': snippet_at(src, i),
                })
            continue

        # if / while / for: 必须有紧跟的 '('
        j = next_code_char(src, mask, m.end())
        if j == -1 or src[j] != '(':
            continue
        close = match_paren(src, mask, j)
        if close == -1:
            continue
        k = next_code_char(src, mask, close + 1)
        if k == -1:
            continue
        if src[k] != '{':
            line = src.count('\n', 0, i) + 1
            out.append({
                'rule': 'brace',
                'file': rel_path,
                'line': line,
                'kind': kw,
                'snippet': snippet_at(src, i),
            })


def next_code_char(src: str, mask: list, i: int) -> int:
    n = len(src)
    while i < n and (src[i] in ' \t\r\n' or not mask[i]):
        if not mask[i]:
            i += 1
            continue
        i += 1
    return i if i < n else -1


def is_word_boundary(src: str, i: int) -> bool:
    if i >= len(src):
        return True
    return not (src[i].isalnum() or src[i] == '_')


def snippet_at(src: str, i: int, length: int = 90) -> str:
    end = src.find('\n', i)
    if end == -1:
        end = len(src)
    s = src[i:min(end, i + length)].strip()
    return s


def scan_args(src: str, mask: list, rel_path: str, out: list):
    """规则 1: 检测 <= 4 个参数却跨行的方法调用 / 声明参数列表。"""
    n = len(src)
    i = 0
    while i < n:
        if src[i] != '(' or not mask[i]:
            i += 1
            continue
        close = match_paren(src, mask, i)
        if close == -1:
            i += 1
            continue
        inner_start, inner_end = i + 1, close
        inner = src[inner_start:inner_end]

        # 只在括号内确实存在换行时才关心
        if '\n' in inner:
            # 判断左括号前是否是方法名 / 关键字（排除 if/while/for/switch/catch/synchronized 的控制括号）
            p = i - 1
            while p >= 0 and src[p] in ' \t\r\n':
                p -= 1
            if p >= 0:
                # 取左括号前的标识符
                q = p
                while q >= 0 and (src[q].isalnum() or src[q] in '_$.)'):
                    q -= 1
                ident = src[q + 1:p + 1]
                tail = ident.split('.')[-1]
                if tail in ('if', 'while', 'for', 'switch', 'catch', 'synchronized', 'return'):
                    i = close + 1
                    continue
                if not ident:
                    i = close + 1
                    continue

            parts = split_top_level(src, mask, inner_start, inner_end, ',')
            # 空参数列表
            if len(parts) == 1 and not src[parts[0][0]:parts[0][1]].strip():
                argc = 0
            else:
                argc = len(parts)

            if argc <= 4:
                # 排除 lambda / 匿名类 / 多行数组初始化等"结构性换行"
                structural = False
                for (a, b) in parts:
                    piece = src[a:b]
                    if '->' in piece or '{' in piece or '}' in piece:
                        structural = True
                        break
                # 注释导致的换行也算合理换行
                comment_nl = False
                for (a, b) in parts:
                    if any(not mask[k] for k in range(a, b)):
                        # 段内含有注释 -> 可能是注释独占一行
                        if '\n' in src[a:b]:
                            comment_nl = True
                if not structural and not comment_nl:
                    line = src.count('\n', 0, i) + 1
                    out.append({
                        'rule': 'args',
                        'file': rel_path,
                        'line': line,
                        'count': argc,
                        'snippet': snippet_at(src, i, 120),
                    })
        i = close + 1


def main():
    root = Path(sys.argv[1])
    json_out = None
    if '--json' in sys.argv:
        json_out = sys.argv[sys.argv.index('--json') + 1]

    files = sorted(root.rglob('*.java'))
    results = []
    for f in files:
        try:
            src = f.read_text(encoding='utf-8')
        except UnicodeDecodeError:
            src = f.read_text(encoding='utf-8', errors='replace')
        mask = build_code_mask(src)
        rel = str(f.relative_to(root))
        scan_braces(src, mask, rel, results)
        scan_args(src, mask, rel, results)

    brace = [r for r in results if r['rule'] == 'brace']
    args = [r for r in results if r['rule'] == 'args']

    print(f'扫描文件数: {len(files)}')
    print(f'规则2(缺少大括号): {len(brace)}')
    print(f'规则1(<=4 参数却跨行): {len(args)}')
    print()

    print('--- 缺大括号 按文件分布 (TOP 20) ---')
    by_file = {}
    for r in brace:
        by_file[r['file']] = by_file.get(r['file'], 0) + 1
    for k, v in sorted(by_file.items(), key=lambda x: -x[1])[:20]:
        print(f'  {v:4d}  {k}')
    print()

    print('--- 参数跨行 按文件分布 (TOP 20) ---')
    by_file2 = {}
    for r in args:
        by_file2[r['file']] = by_file2.get(r['file'], 0) + 1
    for k, v in sorted(by_file2.items(), key=lambda x: -x[1])[:20]:
        print(f'  {v:4d}  {k}')
    print()

    print('--- 缺大括号 样例 (前 25) ---')
    for r in brace[:25]:
        print(f"  {r['file']}:{r['line']} [{r['kind']}] {r['snippet']}")
    print()

    print('--- 参数跨行 样例 (前 25) ---')
    for r in args[:25]:
        print(f"  {r['file']}:{r['line']} [{r['count']}参] {r['snippet']}")

    if json_out:
        Path(json_out).write_text(json.dumps(results, ensure_ascii=False, indent=1), encoding='utf-8')
        print(f'\n完整结果已写入: {json_out}')


if __name__ == '__main__':
    main()
