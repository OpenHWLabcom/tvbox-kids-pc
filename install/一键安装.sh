#!/system/bin/sh
# ================================================================
# 机顶盒 → 少儿电脑 · 一键安装（B860AV1.1-T2 实测）
# 在【盒子上以 root/shell】运行（先开 ADB 或串口 root shell）。
# 用法：把整个安装目录拷到盒子，例如 /sdcard/kid-install/，然后：
#     sh /sdcard/kid-install/一键安装.sh
# ================================================================
D=$(cd "$(dirname "$0")" && pwd)

echo "== [1/4] 安装「少儿电脑」桌面 App (com.laifu.kids) =="
pm install -r "$D/kid.apk" || { echo "安装失败：确认已 root / 有 pm 权限"; exit 1; }

echo "== [2/4] 部署开机自启兜底 =="
cp -f "$D/kidboot.sh" /data/kidboot.sh
chmod 755 /data/kidboot.sh
# 改开机兜底脚本里的桌面包名（存在才改）
[ -f /data/bootfix.sh ] && sed -i 's/com\.openhardwarelab\.ktv/com.laifu.kids/g; s/com\.laifu\.kiosk/com.laifu.kids/g' /data/bootfix.sh 2>/dev/null
# 挂进 ROM 的 post_boot 钩子（保证开机把桌面拉前台）
F=/system/etc/init.zte.post_boot.sh
mount -o remount,rw /system 2>/dev/null
if [ -f "$F" ]; then
  cp -n "$F" "$F.bak-kid" 2>/dev/null
  grep -q kidboot "$F" || echo "[ -f /data/kidboot.sh ] && /system/xbin/busybox nohup /system/bin/sh /data/kidboot.sh >/dev/null 2>&1 &" >> "$F"
  echo "   已写入 $F"
fi

echo "== [3/4] 设为唯一桌面(HOME) =="
echo "   Android 4.4 没有 pm set-home-activity —— 做法是【卸载旧桌面】后本 App 即默认桌面："
echo "     pm list packages | grep -i launcher     # 找旧桌面包名"
echo "     pm uninstall <旧桌面包名>               # 卸载后重启 → 开机直进少儿桌面"
echo "   （若装了旧版 KTV 点歌台：pm uninstall com.openhardwarelab.ktv）"

echo "== [4/4] 完成 =="
echo "   重启后应直接进入「少儿电脑」桌面。"
echo "   浏览器默认打开电脑版百度；「设置」免密直进。"
