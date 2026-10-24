#!/bin/bash
# 构建 少儿电脑「桌面」APK (com.laifu.kids, HOME, 开机直进)
set -e
cd "$(dirname "$0")"
SRC=$HOME/kid-desktop
export JAVA_HOME=$PWD/jdk8u504-b01
export PATH=$JAVA_HOME/bin:$PATH
export LD_LIBRARY_PATH=$PWD/aapt81/usr/lib/aarch64-linux-gnu:$PWD/aapt81/usr/lib/aarch64-linux-gnu/android
AAPT=$PWD/aapt81/usr/bin/aapt
AJ=$PWD/plat/android-4.4.2/android.jar

rm -rf kkclasses kkout kid.unsigned.apk kid.apk
mkdir -p kkclasses kkout

echo "[1/5] javac"
javac -source 7 -target 7 -bootclasspath "$AJ" -d kkclasses $SRC/src/com/laifu/kids/*.java

echo "[2/5] d8"
java -cp bt/android-11/lib/d8.jar com.android.tools.r8.D8 --min-api 19 --lib "$AJ" \
     --output kkout kkclasses/com/laifu/kids/*.class

echo "[3/5] aapt package"
$AAPT package -f -M "$SRC/AndroidManifest.xml" -I "$AJ" -A "$SRC/assets" -F kid.unsigned.apk

echo "[4/5] add classes.dex"
( cd kkout && $AAPT add ../kid.unsigned.apk classes.dex )

echo "[5/5] sign"
[ -f kiosk.keystore ] || keytool -genkeypair -keystore kiosk.keystore -alias k -keyalg RSA \
  -keysize 2048 -validity 10000 -storepass android -keypass android \
  -dname "CN=laifu, OU=dev, O=laifu, L=SG, S=SG, C=SG"
jarsigner -keystore kiosk.keystore -storepass android -keypass android kid.unsigned.apk k
cp -f kid.unsigned.apk kid.apk
echo "=== OK ==="; ls -l kid.apk
$AAPT dump badging kid.apk 2>/dev/null | grep -E "package:|launchable" | head
