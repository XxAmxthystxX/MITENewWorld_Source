"""
按项目规范批量修复 Java 源码：

规则 1: 方法调用 / 方法声明的参数列表，参数个数 <= 4 时必须写在同一行。
规则 2: if / while / for / else 无论作用域多小，都必须带大括号。

策略：逐文件反复「扫描 -> 修复最后一个违规 -> 重扫」，直到该文件无违规。
从后往前修保证前面的下标始终有效。逐文件保留原始换行符（CRLF / LF）。

用法:
    python fix_style.py <java根目录> [--apply] [--limit N]
不带 --apply 时只做演练，打印将要发生的改动。
"""

import re
import sys
from pathlib import Path

from scan_style import (
    build_code_mask,
    match_paren,
    split_top_level,
    next_code_char,
    is_word_boundary,
)

MAX_ARGS_ON_ONE_LINE = 4
INDENT = '    '


# ---------------------------------------------------------------- 扫描


def find_brace_violations(src, mask):
    """返回按出现顺序排列的缺大括号违规，每个元素为 (kw_start, kind, body_start)。"""
    out = []
    for m in re.finditer(r'\b(if|while|for|else)\b', src):
        i = m.start()
        if not mask[i]:
            continue
        kw = m.group(1)
        p = i - 1
        while p >= 0 and src[p] in ' \t\r\n':
            p -= 1
        if p >= 0 and (src[p].isalnum() or src[p] in '_.'):
            continue

        if kw == 'else':
            j = next_code_char(src, mask, m.end())
            if j == -1:
                continue
            if src.startswith('if', j) and is_word_boundary(src, j + 2):
                continue
            if src[j] != '{':
                out.append((i, 'else', j))
            continue

        j = next_code_char(src, mask, m.end())
        if j == -1 or src[j] != '(':
            continue
        close = match_paren(src, mask, j)
        if close == -1:
            continue
        # do { } while (...); 的 while 不算违规
        if kw == 'while' and _is_do_while(src, mask, i):
            continue
        k = next_code_char(src, mask, close + 1)
        if k == -1:
            continue
        if src[k] != '{':
            out.append((i, kw, k))
    return out


def _is_do_while(src, mask, while_idx):
    """判断该 while 是否是 do-while 的尾部 while。"""
    p = while_idx - 1
    while p >= 0 and (src[p] in ' \t\r\n' or not mask[p]):
        p -= 1
    if p < 0 or src[p] != '}':
        return False
    depth = 0
    q = p
    while q >= 0:
        if mask[q] and src[q] == '}':
            depth += 1
        elif mask[q] and src[q] == '{':
            depth -= 1
            if depth == 0:
                break
        q -= 1
    if q < 0:
        return False
    r = q - 1
    while r >= 0 and (src[r] in ' \t\r\n' or not mask[r]):
        r -= 1
    if r < 1:
        return False
    return src[r - 1:r + 1] == 'do' and (r - 2 < 0 or not (src[r - 2].isalnum() or src[r - 2] in '_$'))


def find_arg_violations(src, mask):
    """返回 <= 4 个参数却跨行的参数列表，每个元素为 (open_idx, close_idx, argc)。"""
    out = []
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
        if '\n' in inner:
            if not _is_call_or_decl(src, mask, i):
                i = close + 1
                continue
            if '"""' in inner:
                i = close + 1
                continue
            # 换行后紧跟 '.' 说明是构建器/链式调用，不属于"参数列表换行"
            if re.search(r'\n\s*\.', inner):
                i = close + 1
                continue
            parts = split_top_level(src, mask, inner_start, inner_end, ',')
            if len(parts) == 1 and not src[parts[0][0]:parts[0][1]].strip():
                argc = 0
            else:
                argc = len(parts)
            if argc <= MAX_ARGS_ON_ONE_LINE:
                structural = any(
                    '->' in src[a:b] or '{' in src[a:b] or '}' in src[a:b]
                    for (a, b) in parts
                )
                comment_nl = any(
                    '\n' in src[a:b] and any(not mask[k] for k in range(a, b))
                    for (a, b) in parts
                )
                if not structural and not comment_nl:
                    out.append((i, close, argc))
        i = close + 1
    return out


def _is_call_or_decl(src, mask, open_idx):
    p = open_idx - 1
    while p >= 0 and src[p] in ' \t\r\n':
        p -= 1
    if p < 0:
        return False
    q = p
    while q >= 0 and (src[q].isalnum() or src[q] in '_$.)'):
        q -= 1
    ident = src[q + 1:p + 1]
    if not ident:
        return False
    tail = ident.split('.')[-1]
    if tail in ('if', 'while', 'for', 'switch', 'catch', 'synchronized', 'return', 'new'):
        return False
    return True


# ---------------------------------------------------------------- 语句边界


def find_statement_end(src, mask, start):
    """从 start 开始，返回该语句结束后的下标（不含）。"""
    n = len(src)
    if src[start] == '{':
        depth = 0
        for i in range(start, n):
            if mask[i] and src[i] == '{':
                depth += 1
            elif mask[i] and src[i] == '}':
                depth -= 1
                if depth == 0:
                    return i + 1
        return n

    m = re.match(r'(if|while|for)\b', src[start:start + 8])
    if m:
        j = next_code_char(src, mask, start + m.end())
        if j != -1 and src[j] == '(':
            close = match_paren(src, mask, j)
            if close != -1:
                k = next_code_char(src, mask, close + 1)
                if k != -1:
                    return find_statement_end(src, mask, k)

    depth = 0
    i = start
    while i < n:
        if not mask[i]:
            i += 1
            continue
        c = src[i]
        if c in '([{':
            depth += 1
        elif c in ')]}':
            if depth == 0:
                # 语句意外结束，交给调用方
                return i
            depth -= 1
        elif c == ';' and depth == 0:
            return i + 1
        i += 1
    return n


# ---------------------------------------------------------------- 修复


def line_start(src, idx):
    return src.rfind('\n', 0, idx) + 1


def leading_ws_of_line(src, idx):
    ls = line_start(src, idx)
    i = ls
    while i < len(src) and src[i] in ' \t':
        i += 1
    return src[ls:i]


def only_ws_before(src, idx):
    ls = line_start(src, idx)
    return src[ls:idx].strip() == ''


def fix_brace(src, mask, kw_start, kind, body_start, nl='\n'):
    """给单个缺大括号的语句补上大括号，返回新源码。"""
    indent = leading_ws_of_line(src, kw_start)
    end = find_statement_end(src, mask, body_start)
    text = src[body_start:end]

    lines = text.split('\n')

    own_line = only_ws_before(src, body_start)

    # 插入点：控制括号（或 else 关键字）之后
    if kind == 'else':
        insert_at = kw_start + 4
    else:
        j = next_code_char(src, mask, kw_start + len(kind))
        close = match_paren(src, mask, j) if j != -1 and src[j] == '(' else -1
        insert_at = close + 1 if close != -1 else body_start
    # 若 ) 与语句之间夹了注释，则退回原位置，避免吃掉注释
    if insert_at < body_start and any(not mask[k] for k in range(insert_at, body_start)):
        insert_at = body_start

    if own_line:
        w0 = leading_ws_of_line(src, body_start)
        shift = len(indent) + len(INDENT) - len(w0)
    else:
        shift = len(INDENT)

    new_lines = []
    for idx, line in enumerate(lines):
        if idx == 0:
            new_lines.append(indent + INDENT + line.lstrip(' \t'))
        else:
            if shift >= 0:
                new_lines.append(' ' * shift + line)
            else:
                stripped = line.lstrip(' \t')
                removed = len(line) - len(stripped)
                keep = max(0, removed + shift)
                new_lines.append(' ' * keep + stripped)
    body = '\n'.join(new_lines)

    prefix = src[:insert_at]
    # 让 } else { 同行，而不是 else 另起一行
    if kind == 'else':
        p = kw_start - 1
        while p >= 0 and src[p] in ' \t\r\n':
            p -= 1
        if p >= 0 and src[p] == '}':
            prefix = src[:p + 1] + ' ' + src[kw_start:insert_at]

    sep = '' if insert_at == body_start else ' '
    return prefix + sep + '{' + '\n' + body + '\n' + indent + '}' + src[end:]


def fix_args(src, mask, open_idx, close_idx):
    """把跨行的参数列表合并成一行。"""
    inner = src[open_idx + 1:close_idx]
    joined = re.sub(r'[ \t]*\r?\n\s*', ' ', inner).strip()
    joined = re.sub(r'\s{2,}', ' ', joined)
    return src[:open_idx + 1] + joined + src[close_idx:]


# ---------------------------------------------------------------- 主流程


def process_file(path, apply, stats, log):
    raw = path.read_bytes()
    try:
        src = raw.decode('utf-8')
    except UnicodeDecodeError:
        src = raw.decode('utf-8', errors='replace')
    crlf = b'\r\n' in raw
    src = src.replace('\r\n', '\n')
    orig = src

    guard = 0
    while True:
        guard += 1
        if guard > 3000:
            log.append(f'!! {path} 迭代次数超限，已跳过')
            return
        mask = build_code_mask(src)
        braces = find_brace_violations(src, mask)
        args = find_arg_violations(src, mask)
        if not braces and not args:
            break
        # 取位置最靠后的违规先修
        candidates = [(b[0], 'brace', b) for b in braces] + [(a[0], 'args', a) for a in args]
        candidates.sort(key=lambda x: x[0])
        _, kind, payload = candidates[-1]
        if kind == 'brace':
            src = fix_brace(src, mask, payload[0], payload[1], payload[2])
            stats['brace'] += 1
        else:
            src = fix_args(src, mask, payload[0], payload[1])
            stats['args'] += 1

    # 收尾：把换行另起的 else 贴回上一个 }
    src = re.sub(r'\}[ \t]*\n[ \t]*else\b', '} else', src)

    if src != orig:
        stats['files'] += 1
        out = src.replace('\n', '\r\n') if crlf else src
        if apply:
            path.write_bytes(out.encode('utf-8'))
        log.append(f'{"已修改" if apply else "将修改"} {path}')


def main():
    args = sys.argv[1:]
    apply = '--apply' in args
    if '--apply' in args:
        args.remove('--apply')
    root = Path(args[0])

    files = sorted(root.rglob('*.java'))
    stats = {'brace': 0, 'args': 0, 'files': 0}
    log = []
    for f in files:
        process_file(f, apply, stats, log)

    print(f'模式: {"实际写入" if apply else "演练(未写入)"}')
    print(f'受影响文件数: {stats["files"]} / {len(files)}')
    print(f'补大括号: {stats["brace"]}')
    print(f'参数合并为单行: {stats["args"]}')
    if not apply:
        print('\n(加 --apply 才会真正写文件)')


if __name__ == '__main__':
    main()
