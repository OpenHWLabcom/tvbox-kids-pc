安装文件说明（机顶盒 → 少儿电脑）
=================================

kid.apk              少儿电脑「桌面」App（com.laifu.kids，Win 风格，原生控件，开机直进）
kidboot.sh           开机自启兜底脚本（等系统就绪后把桌面拉到前台）
一键安装.sh           一键安装（装 App + 配开机自启 + 提示设为桌面）

在盒子上（root/shell）跑：
  sh /sdcard/kid-install/一键安装.sh

再按提示卸载旧桌面，重启即开机直进「少儿电脑」。

要求：Android 4.4+（实测 ZXV10 B860AV1.1-T2 / zx296716 / Android 4.4.2）；
      盒子已 root（能 pm install / 改 /system）。
详见 docs/安装教程.md 与 docs/案例总结.md。
