#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
对比「原版熔炼配方」与「MITENewWorld 模组熔炉配方」，列出模组缺失的配方。

用法:
    python tools/diff_smelting_recipes.py [--md 报告.md]

数据来源:
    原版: ~/.gradle/caches/fabric-loom/<mc_ver>/minecraft-merged.jar -> data/minecraft/recipe/*.json
    模组: src/main/resources/data/mitenewworld/recipe/smelting/*.json

比对逻辑:
    1. 展开标签(#xxx)为具体物品集合
    2. 以「产物物品」为主键, 收集模组已有的可接受输入集合
    3. 原版每条熔炼配方取其输入集合, 逐物品检查是否被模组覆盖
"""
import argparse
import json
import os
import re
import sys
import zipfile

ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
MC_JAR = os.path.expanduser(
    "~/.gradle/caches/fabric-loom/1.21.10/minecraft-merged.jar"
)
MOD_SMELTING_DIR = os.path.join(
    ROOT, "src/main/resources/data/mitenewworld/recipe/smelting"
)
MOD_TAG_DIR = os.path.join(ROOT, "src/main/resources/data/mitenewworld/tags/item")


# ---------------------------------------------------------------- 标签加载
def load_vanilla_tags(z):
    """从 jar 读取 data/minecraft/tags/item/*.json -> {'minecraft:xxx': [items]}"""
    tags = {}
    for n in z.namelist():
        if not n.startswith("data/minecraft/tags/item/"):
            continue
        if not n.endswith(".json"):
            continue
        tid = "minecraft:" + n[len("data/minecraft/tags/item/"): -5]
        try:
            d = json.loads(z.read(n))
        except Exception:
            continue
        vals = []
        for v in d.get("values", []):
            if isinstance(v, str):
                vals.append(v)
            elif isinstance(v, dict) and "id" in v:
                vals.append(v["id"])
        tags[tid] = vals
    return tags


def load_mod_tags():
    tags = {}
    if not os.path.isdir(MOD_TAG_DIR):
        return tags
    for dirpath, _, files in os.walk(MOD_TAG_DIR):
        for f in files:
            if not f.endswith(".json"):
                continue
            rel = os.path.relpath(os.path.join(dirpath, f), MOD_TAG_DIR)
            rel = rel.replace("\\", "/")[:-5]
            tid = "minecraft:" + rel if ":" not in rel else rel
            if not tid.startswith("minecraft:"):
                tid = "mitenewworld:" + rel
            try:
                with open(os.path.join(dirpath, f), "r", encoding="utf-8") as fh:
                    d = json.load(fh)
            except Exception:
                continue
            vals = []
            for v in d.get("values", []):
                if isinstance(v, str):
                    vals.append(v)
                elif isinstance(v, dict) and "id" in v:
                    vals.append(v["id"])
            tags[tid] = vals
    return tags


def make_expander(tags):
    cache = {}

    def expand(token, depth=0):
        """把一个 ingredient token 展开成物品 id 集合"""
        if depth > 8:
            return set()
        if token in cache:
            return cache[token]
        if token.startswith("#"):
            tid = token[1:]
            if not ":" in tid:
                tid = "minecraft:" + tid
            out = set()
            for v in tags.get(tid, []):
                if v.startswith("#"):
                    out |= expand(v, depth + 1)
                else:
                    out.add(v)
        else:
            out = {token if ":" in token else "minecraft:" + token}
        cache[token] = out
        return out

    return expand


# ---------------------------------------------------------------- 配方加载
def vanilla_ingredient_tokens(ing):
    """原版 ingredient 字段: 字符串 / 对象 / 列表"""
    out = []
    if isinstance(ing, str):
        out.append(ing)
    elif isinstance(ing, dict):
        if "item" in ing:
            out.append(ing["item"])
        elif "tag" in ing:
            out.append("#" + ing["tag"])
    elif isinstance(ing, list):
        for x in ing:
            out.extend(vanilla_ingredient_tokens(x))
    return out


def load_vanilla_smelting(z):
    recs = []
    for n in sorted(z.namelist()):
        if not n.startswith("data/minecraft/recipe/"):
            continue
        if not n.endswith(".json"):
            continue
        try:
            d = json.loads(z.read(n))
        except Exception:
            continue
        if d.get("type") != "minecraft:smelting":
            continue
        toks = vanilla_ingredient_tokens(d.get("ingredient"))
        res = d.get("result") or {}
        rid = res.get("id") if isinstance(res, dict) else None
        recs.append({
            "file": n[len("data/minecraft/recipe/"):],
            "tokens": toks,
            "result": rid,
            "count": (res.get("count", 1) if isinstance(res, dict) else 1),
            "exp": d.get("experience", 0.0),
            "time": d.get("cookingtime", 200),
            "group": d.get("group"),
            "category": d.get("category", "misc"),
        })
    return recs


def load_mod_smelting():
    recs = []
    if not os.path.isdir(MOD_SMELTING_DIR):
        return recs
    for f in sorted(os.listdir(MOD_SMELTING_DIR)):
        if not f.endswith(".json"):
            continue
        with open(os.path.join(MOD_SMELTING_DIR, f), "r", encoding="utf-8") as fh:
            d = json.load(fh)
        inp = d.get("input")
        toks = []
        if isinstance(inp, str):
            toks = [inp]
        elif isinstance(inp, list):
            toks = []
            for x in inp:
                toks.extend(vanilla_ingredient_tokens(x))
        elif isinstance(inp, dict):
            toks = vanilla_ingredient_tokens(inp)
        res = d.get("result") or {}
        recs.append({
            "file": f,
            "tokens": toks,
            "result": res.get("id") if isinstance(res, dict) else None,
            "level": d.get("furnacelevel", 1),
            "burntick": d.get("burntick", 200),
            "exp": d.get("experience", 0.0),
        })
    return recs


def main():
    ap = argparse.ArgumentParser()
    ap.add_argument("--md", help="输出 markdown 报告路径")
    ap.add_argument("--jar", default=MC_JAR, help="原版 merged jar 路径")
    args = ap.parse_args()

    if not os.path.isfile(args.jar):
        print("找不到原版 jar: %s" % args.jar)
        return 1
    z = zipfile.ZipFile(args.jar)

    tags = load_vanilla_tags(z)
    tags.update(load_mod_tags())
    expand = make_expander(tags)

    van = load_vanilla_smelting(z)
    mod = load_mod_smelting()

    # 模组: 产物 -> 已覆盖的输入物品集合
    mod_by_result = {}
    for r in mod:
        s = mod_by_result.setdefault(r["result"], set())
        for t in r["tokens"]:
            s |= expand(t)

    missing = []      # 产物完全没有配方
    partial = []      # 有产物配方但部分输入没覆盖
    for r in van:
        covered = mod_by_result.get(r["result"])
        if covered is None:
            missing.append((r, []))
            continue
        ins = set()
        for t in r["tokens"]:
            ins |= expand(t)
        lack = sorted(ins - covered)
        if lack:
            partial.append((r, lack))

    lines = []
    lines.append("# 原版熔炼配方 vs 模组熔炉配方 差分报告")
    lines.append("")
    lines.append("- 原版 `minecraft:smelting` 配方: **%d** 条" % len(van))
    lines.append("- 模组 `mitenewworld:modfurnace` 配方: **%d** 条" % len(mod))
    lines.append("- 产物完全缺失: **%d** 条" % len(missing))
    lines.append("- 产物存在但输入未覆盖: **%d** 条" % len(partial))
    lines.append("")

    def row(r, lack):
        res = "%s x%d" % (r["result"], r["count"]) if r["count"] > 1 else r["result"]
        return "| `%s` | %s | %s | %.2f | %d |" % (
            r["file"], ", ".join("`%s`" % t for t in r["tokens"]), res,
            r["exp"], r["time"])

    if missing:
        lines.append("## 一、产物完全缺失（模组熔炉根本烧不出这个东西）")
        lines.append("")
        lines.append("| 原版配方文件 | 输入 | 产物 | 经验 | 耗时(tick) |")
        lines.append("|---|---|---|---|---|")
        for r, lack in missing:
            lines.append(row(r, lack))
        lines.append("")

    if partial:
        lines.append("## 二、产物有配方，但部分输入没覆盖")
        lines.append("")
        lines.append("| 原版配方文件 | 输入 | 未覆盖输入 | 产物 |")
        lines.append("|---|---|---|---|")
        for r, lack in partial:
            lines.append("| `%s` | %s | %s | `%s` |" % (
                r["file"], ", ".join("`%s`" % t for t in r["tokens"]),
                ", ".join("`%s`" % x for x in lack[:12]) +
                (" …" if len(lack) > 12 else ""), r["result"]))
        lines.append("")

    text = "\n".join(lines)
    print(text)
    if args.md:
        os.makedirs(os.path.dirname(os.path.abspath(args.md)), exist_ok=True)
        with open(args.md, "w", encoding="utf-8") as fh:
            fh.write(text + "\n")
        print("\n[报告已写入] %s" % args.md)
    return 0


if __name__ == "__main__":
    sys.exit(main())
