# 游戏大地图实时跟点助手（本分支）

原项目：[761696148/Game-Map-Tracker](https://github.com/761696148/Game-Map-Tracker) — 基于 **SIFT** / **LoFTR** 的屏幕小地图与大地图对齐、悬浮窗跟点。

本仓库在原作者架构上做了稳定性、性能与工程化方面的增强；**逻辑底图、显示图等数据仍须自行准备或从下方资源链接获取**。

---

## 相对原版本的主要改进

| 方向 | 说明 |
|------|------|
| **Windows 多线程截屏** | `mss` 在后台跟踪线程中按线程创建/复用实例，避免 `srcdc` 类跨线程错误。 |
| **性能** | 局部锚点上限与子采样、先缩放再绘制悬浮窗、可配置的小图/大地图 SIFT 与 FLANN 参数；缩小稳态局部搜索半径默认值，减轻 FLANN 负担。 |
| **界面** | FPS 文字无黑底；可选**显示插帧**（平滑视口中心），在定位帧率不高时减轻画面卡顿感。 |
| **信息展示** | 在悬浮窗大地图视口中绘制**相邻两次跟踪之间的位移方向**箭头（地图坐标差）。 |
| **诊断** | 可选**分段耗时**（截屏 / 预处理 / 小图 SIFT / 选锚 / knn / ratio / 位姿），写入 txt，便于定位瓶颈。 |
| **打包发布** | `config` 支持 PyInstaller 冻结路径（`out` 与 exe 同目录）；提供**独立 venv** + `requirements-main_sift.txt` 的 `build_sift_exe.bat`，避免把 Anaconda 冗余包打进 exe。 |

未改动原项目的核心算法主线：**逻辑图提特征 + 小地图 SIFT + 匹配 + 单应性求位置**；AI 模式（`main_ai.py`）仍依赖 PyTorch，与 SIFT 精简打包相互独立。

---

## 可执行文件与地图资源下载

可将 **exe** 与 **`out` 依赖** 分开展示下载链接（网盘 / Release 等）：

| 内容 | 链接 |
|------|------|
| **Windows 可执行文件**（`SIFT_Map_Tracker.exe`） | _（在此填写）_ |
| **`out` 内图像与锚点缓存**（见下表） | _（在此填写）_ |

### 推荐发布目录结构

程序从 **exe 所在目录** 查找同级文件夹 `out`（与 `config.py` 中冻结运行逻辑一致）。推荐打成压缩包时采用如下结构，解压后保持相对路径不变即可运行：

```text
exe_files/
├── SIFT_Map_Tracker.exe
└── out/
    ├── rocom_base_z8.png          # 逻辑底图（SIFT 搜索用）
    ├── rocom_caiji_overlay.png    # 显示大地图（悬浮窗展示）
    └── sift_anchors_rocom_base_z8.npz   # 锚点缓存（可省略，首次运行会生成/重算）
```

说明：

- 根目录名 `exe_files` 可任意命名；**必须保证** `SIFT_Map_Tracker.exe` 与 **`out` 文件夹在同一级**。
- 若文件名与默认 `config` 不一致，请修改配置或重命名文件以对应。

---

## 从源码运行（SIFT）

```bash
pip install -r requirements.txt   # 或仅 SIFT：见 requirements-main_sift.txt
python main_sift.py
```

首次运行建议通过全屏框选校准小地图区域；仅命令行 `--no-pick` 时使用 `config.py` 里的 `MINIMAP` 数值。

---

## 自行打包 exe（仅 SIFT 依赖）

在项目根目录执行 **`build_sift_exe.bat`**：会创建隔离虚拟环境 `.venv_sift_build`，仅安装 `requirements-main_sift.txt` 与 PyInstaller，再生成 `dist/SIFT_Map_Tracker.exe`。详见同目录 `main_sift.spec`。

---

## 致谢

核心思路与仓库结构来自 [Game-Map-Tracker](https://github.com/761696148/Game-Map-Tracker)；若你二次分发，请保留上游许可与致谢信息（若原仓库指定许可证，以原仓库为准）。
