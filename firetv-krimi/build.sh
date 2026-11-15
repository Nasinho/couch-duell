#!/bin/zsh
# Baut die Fire-TV-App „Couch-Krimi“ ohne Gradle: Ressourcen → Java → DEX → APK → signieren
set -e
cd "$(dirname "$0")"
SDK=${ANDROID_HOME:-$HOME/Library/Android/sdk}
BT=$SDK/build-tools/35.0.0
JAR=$SDK/platforms/android-34/android.jar
KS=$HOME/.android/debug.keystore
rm -rf build && mkdir -p build/gen build/classes build/dex
$BT/aapt2 compile --dir res -o build/res.zip
$BT/aapt2 link -o build/base.apk -I $JAR --manifest AndroidManifest.xml --java build/gen build/res.zip \
  --min-sdk-version 22 --target-sdk-version 34 --version-code ${VERSION_CODE:-1} --version-name ${VERSION_NAME:-1.0}
javac -Xlint:-options --release 11 -cp $JAR -d build/classes $(find src build/gen -name '*.java')
$BT/d8 --lib $JAR --min-api 22 --output build/dex $(find build/classes -name '*.class')
(cd build/dex && zip -q -j ../base.apk classes.dex)
$BT/zipalign -f 4 build/base.apk build/aligned.apk
[ -f $KS ] || { mkdir -p $HOME/.android; keytool -genkeypair -keystore $KS -storepass android -keypass android -alias androiddebugkey \
  -keyalg RSA -keysize 2048 -validity 10000 -dname "CN=Android Debug,O=Android,C=US" >/dev/null 2>&1; }
$BT/apksigner sign --ks $KS --ks-pass pass:android --key-pass pass:android --out build/couch-krimi-firetv.apk build/aligned.apk
$BT/apksigner verify build/couch-krimi-firetv.apk && ls -la build/couch-krimi-firetv.apk
