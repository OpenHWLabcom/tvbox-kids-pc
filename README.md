# 机顶盒改 · 少儿电脑（TV Box → Kids' PC）

把吃灰的 **安卓机顶盒** 变成一台**给小学生用的桌面小电脑**：开机直进一个 **Win 风格**的简单桌面（**浏览器 / 文件 / 设置**），设置**免密**，键鼠/遥控都能操作。

🇨🇳 中文 · 🇬🇧 English

- 实测机型：**ZXV10 B860AV1.1-T2**（中兴微 zx296716 / Android 4.4.2 / armv7）
- 桌面 App：**`com.laifu.kids`**（纯原生控件，不依赖 WebView）

## 功能
- **开机直进**简单桌面（原生控件，稳定）：蓝色渐变壁纸 + 左侧图标 + 底部任务栏（开始/时钟）+ 开始菜单（含关机）+ 右下品牌水印。
- **浏览器**：Firefox，默认打开**电脑版百度**；不显示"常用网站"、不残留多标签。
- **设置免密**：桌面「设置」直进系统设置（`needpassword=noneed`）。
- **键盘友好**：有实体键盘时不弹软键盘；方向键导航 + 回车打开。

## 安装
见 [`docs/安装教程.md`](docs/安装教程.md)。快速版：
```
adb push kid-install /sdcard/kid-install
adb shell "sh /sdcard/kid-install/一键安装.sh"
# 再卸载旧桌面 → 重启即开机直进「少儿电脑」
```

## 内容
- `install/` — 安装文件（`kid.apk`、`kidboot.sh`、`一键安装.sh`）
- `docs/` — 安装教程、**案例总结**（含踩坑）
- `src/` — 桌面 App 源码 + 构建脚本（`build-kids.sh`）

## 相关
- 同系列：KTV 点歌台、桌面小电脑、视频追剧版、游戏厅、监控、打印服务器、下载机NAS、去广告DNS。
- 联系：hi@openhwlab.com · QQ 1030889574

## License
见 [`LICENSE`](LICENSE)。

---
# TV Box → Kids' PC

Turn a spare Android TV box into a simple **desktop PC for kids**: boots straight into a plain, Windows-style desktop (Browser / Files / Settings) with password-free Settings.

- Tested: **ZXV10 B860AV1.1-T2** (ZTE zx296716 / Android 4.4.2 / armv7). Desktop app: **`com.laifu.kids`** (native views, no WebView).
- Install: see [`docs/安装教程.md`](docs/安装教程.md).
- Works with keyboard/remote/mouse; hardware-keyboard friendly (no soft keyboard popup).
