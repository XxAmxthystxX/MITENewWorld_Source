# 仓库整理与 GitHub 上传说明

> 项目：MITENewWorld（Fabric 1.21.10，Java 21）
> 目标：提交一份**干净、可复现**的源码仓库——克隆后执行 `./gradlew build` 即可产出 mod jar。

---

## 一、必要文件（必须提交）

### 1. 构建脚本与 Wrapper
| 路径 | 说明 |
|---|---|
| `build.gradle` | 构建脚本，缺一不可 |
| `settings.gradle` | 项目名与插件仓库 |
| `gradle.properties` | 所有版本号（MC / Yarn / Loader / Fabric API / mod 版本） |
| `gradlew` / `gradlew.bat` | 跨平台构建入口 |
| `gradle/wrapper/gradle-wrapper.jar` | **wrapper jar，必须提交**（否则 CI 与本地都无法构建） |
| `gradle/wrapper/gradle-wrapper.properties` | Gradle 版本声明 |

### 2. 源码与资源
| 路径 | 大小 | 说明 |
|---|---|---|
| `src/main/java/` | 2.2 MB / 220 个 .java | 全部 mod 逻辑（block / item / recipe / datagen / mixin …） |
| `src/main/resources/` | 7.7 MB / 4411 文件 | 真正随 jar 打包的内容 |

`src/main/resources/` 明细：
- `fabric.mod.json`、`mitenewworld.mixins.json`、`mitenewworld.accesswidener` — 模组元数据
- `assets/mitenewworld/` (5.3 MB) — 模型 2533 json + 贴图 555 png + lang + equipment + icon
- `data/mitenewworld/` (1.8 MB) — 配方 / 标签 / 战利品表 / 世界生成 / 维度
- `data/minecraft/` (717 KB) — 对原版数据的覆盖与追加（dimension / loot_table / recipe / tags / worldgen）
- `assets/minecraft/` (2 个 json) — torch / wall_torch 的 blockstate 覆盖

### 3. 工程规范与文档
| 路径 | 说明 |
|---|---|
| `LICENSE` | 许可证（构建时会被打进 jar） |
| `.gitattributes` | 换行符规则（`gradlew` 用 LF，`*.bat` 用 CRLF）—— **重要**，关系到 Windows/CI 兼容 |
| `.gitignore` | 已重写，见下文 |
| `CODE_STYLE.md` | 代码规范 |
| `RESOURCE_LAYOUT.md` | 资源目录约定 |
| `docs/` | 设计文档：`furnace_material_design.md`、`unobtainable_report.md` |
| `tools/` | 辅助脚本（见 §3 说明） |

### 4. CI
| 路径 | 说明 |
|---|---|
| `.github/workflows/build.yml` | push / PR 自动构建并上传 `build/libs/` 产物 |

---

## 二、可删除 / 可重新生成的文件（不要提交）

| 路径 | 大小 | 性质 |
|---|---|---|
| `.gradle/` | **846 MB** | Gradle 缓存与配置缓存，首次构建自动重建 |
| `build/` | **39 MB** | 编译输出，`./gradlew build` 重新生成 |
| `run/` | **74 MB** | Fabric 开发运行目录（含世界存档、日志） |
| `logs/` | 12 KB | 运行日志 |
| `.idea/` | 111 KB | IntelliJ 本地配置 |
| `.workbuddy/` | 72 KB | 本地 AI 助手缓存与预览图（含 `preview/*.png`） |
| `path_to_output_json_file.json` | **3.9 MB** | `devtools/NbtJsonConverter` 的转换输出，非源码 |
| `path_to_output_nbt_file.dat` | 484 KB | 同上，NBT 输出 |
| `src/main/java/.../UnderWorldPortalCoreEntity.java.bak` | 13 KB | 编辑器备份，正体 `.java` 已存在 |
| `src/main/resources/assets/temp/` | 0 | 空目录，Git 本来就不会追踪 |

> 以上除 `_archive/` 外，全部已在新的 `.gitignore` 中排除。

---

## 三、需要你决策的边界情况

### 1. `_archive/unused_textures/`（63 KB，51 个文件）
当前未被 mod 引用（放在 `_archive` 而非 `src`，因此不会被加载）。

- **推荐**：保留提交。体积很小，且是「已弃用但需追溯」的素材，符合 `_archive` 语义。
- 若确定永久不要：直接删除该目录。

### 2. `tools/`（118 KB）
Python 辅助脚本 + `asset_backup/`（2 张 png）：

- `validate_refs.py`、`check_unobtainable.py`、`analyze_refs.py`、`classify_recipes.py`、`scan_style.py`、`diff_smelting_recipes.py` — 资源/配方审计，**建议提交**（对你的冶金配方核查流程很有价值）
- `migrate_assets.py`、`restore_lost_recipes.py`、`fix_style.py` — 一次性迁移脚本，历史任务已完成
- `gen_*_assets.py` — 贴图生成脚本，可复现
- `asset_backup/*.png` — 2 张贴图备份，确认无用可删
- `report_unobtainable.md` — 与 `docs/unobtainable_report.md` 内容重叠，建议只保留一份

**建议**：整体提交，但在 `tools/README.md` 里标注每个脚本的用途与「一次性/可复用」。

### 3. `.idea/`（111 KB）
若团队都用 IntelliJ，可以提交 `codeStyles/`、`inspectionProfiles/` 等共享配置（仅忽略 `workspace.xml`、`shelf/`）。当前 `.gitignore` 已整体忽略，保持简单即可。

### 4. `path_to_output_*` 同时被 `NbtJsonConverter.java` 注释引用
该文件是 devtools 的**输出**而非输入，已加入 `.gitignore`。若脚本需要固定输出路径，建议改为 `run/` 下或加 `--output` 参数，不要污染仓库根目录。

---

## 四、执行步骤

```bash
cd G:/mc/mitenewworld-template-1.21.1

# 1) 清理应删除的临时文件（.gradle/build/run/logs 保留在本地即可，无需删）
rm -f path_to_output_json_file.json path_to_output_nbt_file.dat
rm -f src/main/java/com/mitenewworld/entity/blockentity/portal/UnderWorldPortalCoreEntity.java.bak
rmdir src/main/resources/assets/temp 2>/dev/null

# 2) 初始化仓库
git init
git add .
git status          # 确认没有 build/ .gradle/ run/ 等被误加

# 3) 提交
git commit -m "chore: initial commit of MITENewWorld (Fabric 1.21.10)"

# 4) 关联远端并推送
git remote add origin https://github.com/<你的用户名>/<仓库名>.git
git branch -M main
git push -u origin main
```

### 上传前自检清单
- [ ] `git status` 中**不出现** `build/`、`.gradle/`、`run/`、`.idea/`、`path_to_output_*`
- [ ] `gradle/wrapper/gradle-wrapper.jar` **在**提交列表里（有些全局 gitignore 会误杀 `*.jar`）
- [ ] `gradlew` 行尾为 LF（检查 `.gitattributes` 生效：`git ls-files --eol gradlew`）
- [ ] `src/main/resources/` 完整（约 4400 文件），确认贴图/模型未被误忽略
- [ ] 仓库内无 `run/saves/` 存档、无个人日志

### 验证可复现性
```bash
git clone <你的仓库> /tmp/verify && cd /tmp/verify
./gradlew build     # 应能直接成功，产物在 build/libs/
```
