# MITENewWorld 代码规范

本项目当前强制以下两条规则，其余排版不做强制要求。

## 规则 1：参数列表换行门槛 = 4

方法**声明**和方法**调用**的参数列表，只有参数个数 **大于 4** 时才允许跨行；
参数个数 ≤ 4 时必须写在同一行。

```java
// 正确：3 个参数，单行
PlacedFeatures.register(featureRegisterable, ORE_MAGMA, registryEntry);

// 错误：3 个参数却跨行
PlacedFeatures.register(
        featureRegisterable, ORE_MAGMA, registryEntry
);
```

**例外：链式调用不算"参数列表换行"。**
构建器、流式 API 按 `.` 换行是链式调用，不是把某个参数列表拆开，因此不受本规则约束：

```java
// 允许：链式调用按点号换行
this.setDefaultState(getDefaultState()
        .with(PHASE, 0)
        .with(ACTIVE, false));
```

同理，参数里含 lambda、匿名内部类、行内注释导致的结构性换行也不在约束范围内。

## 规则 2：控制语句一律加大括号

`if` / `else` / `while` / `for` 无论作用域多小，**必须**带大括号，哪怕只有一行。

```java
// 正确
if (world.isClient()) {
    return ActionResult.SUCCESS;
}

// 错误
if (world.isClient()) return ActionResult.SUCCESS;
```

`else` 与前一个 `}` 同行，写作 `} else {`。

## 自动检查

仓库根目录提供了检查脚本（需要 Python 3.9+）：

```bash
# 只扫描并输出违规报告
python tools/scan_style.py src/main/java

# 自动修复（会直接改写文件，建议先提交或备份）
python tools/fix_style.py src/main/java --apply
```

`fix_style.py` 不带 `--apply` 时为演练模式，只统计不写文件。
脚本会逐文件保留原有的换行符（CRLF / LF）。

> 注意：脚本无法判断语义，自动修复后请务必重新编译确认。
