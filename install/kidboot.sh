#!/system/bin/sh
# OpenHWLab kidboot: 开机兜底 —— 关系统弹窗 + 把「少儿电脑」桌面拉到前台
BB=/system/xbin/busybox
export PATH=/system/bin:/system/xbin:/sbin
LOG=/data/kidboot.log
echo "=== kidboot start $(date) ===" >> $LOG
n=0
while [ "$(getprop sys.boot_completed)" != "1" ] && [ $n -lt 150 ]; do $BB sleep 2; n=$((n+1)); done
echo "boot_completed waited $((n*2))s" >> $LOG
# 确保 chroot 的 sshd 起来（安卓侧 startlinux 有时漏起）
[ -x /data/linux/usr/sbin/sshd ] && chroot /data/linux /usr/sbin/sshd 2>/dev/null
# KTV-era pidfile 清理
rm -f /data/linux/run/ktvnet.pid 2>/dev/null
SP=$($BB pidof system_server)
BC=$($BB tr '\0' '\n' < /proc/$SP/environ 2>/dev/null | $BB grep ^BOOTCLASSPATH= | $BB cut -d= -f2-)
[ -z "$BC" ] && BC="/system/framework/core.jar:/system/framework/framework.jar:/system/framework/framework2.jar:/system/framework/services.jar:/system/framework/ext.jar:/system/framework/android.policy.jar"
export ANDROID_ROOT=/system ANDROID_DATA=/data LD_LIBRARY_PATH=/vendor/lib:/system/lib BOOTCLASSPATH="$BC"
i=0
while [ $i -lt 10 ]; do
  $BB sleep 12
  am broadcast -a android.intent.action.CLOSE_SYSTEM_DIALOGS >/dev/null 2>&1
  if dumpsys window windows 2>/dev/null | $BB grep -q "mCurrentFocus=.*com.laifu.kids"; then
    echo "kid foreground @iter $i" >> $LOG; break
  fi
  am start -n com.laifu.kids/.MainActivity >> $LOG 2>&1
  i=$((i+1))
done
echo "=== kidboot done (iter=$i) ===" >> $LOG
