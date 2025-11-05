# Getting Started - Build & Debug

## ✅ Setup Complete!

Your build environment is ready. Java 17 and Android SDK are installed and configured.

## 🚀 Quick Start (3 Easy Steps)

### Step 1: Source Environment (Required)

Every time you open a new terminal, run:
```bash
cd /workspace/unstoppable-wallet-android
source <(cat << 'EOF'
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
export ANDROID_HOME=$HOME/Android/Sdk
export PATH=$PATH:$ANDROID_HOME/platform-tools:$ANDROID_HOME/cmdline-tools/latest/bin
EOF
)
```

Or add to `~/.bashrc` for permanent setup:
```bash
cat >> ~/.bashrc << 'EOF'
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
export ANDROID_HOME=$HOME/Android/Sdk
export PATH=$PATH:$ANDROID_HOME/platform-tools:$ANDROID_HOME/cmdline-tools/latest/bin
EOF
source ~/.bashrc
```

### Step 2: Use the Build Script

We've created a helper script for you:

```bash
# Verify setup
./build.sh

# List all available tasks
./build.sh tasks

# Build debug APK
./build.sh debug

# Build release APK
./build.sh release

# Run tests
./build.sh test

# Check dependencies
./build.sh deps

# Clean build
./build.sh clean
```

### Step 3: Or Use Gradle Directly

```bash
# Build debug APK
./gradlew assembleDebug

# Build release APK
./gradlew assembleRelease

# Run tests
./gradlew test
```

## ⚠️ Current Known Issue

**JitPack Dependencies Missing**

Three dependencies need to be built on JitPack:
- bitcoin-kit-android:ced5801
- ethereum-kit-android:0c770e3
- tron-kit-android:dc3dca7

### Quick Fix:

1. **Trigger JitPack builds** - Visit these URLs and click "Get it":
   - https://jitpack.io/#horizontalsystems/bitcoin-kit-android/ced5801
   - https://jitpack.io/#horizontalsystems/ethereum-kit-android/0c770e3
   - https://jitpack.io/#horizontalsystems/tron-kit-android/dc3dca7

2. **Wait 15-30 minutes** for JitPack to build them

3. **Retry the build**:
   ```bash
   ./build.sh debug --refresh-dependencies
   ```

## 📱 Build Outputs

After successful build, find your APKs here:

- **Debug APK**: `app/build/outputs/apk/debug/app-debug.apk`
- **Release APK**: `app/build/outputs/apk/release/app-release.apk`

## 🐛 Debug Tips

### View Detailed Errors
```bash
./gradlew assembleDebug --stacktrace --info
```

### Check What's Failing
```bash
./build.sh deps | grep FAILED
```

### Clear Cache and Retry
```bash
./gradlew clean
rm -rf ~/.gradle/caches/
./gradlew assembleDebug --refresh-dependencies
```

### Verify Environment
```bash
echo "Java: $(java -version 2>&1 | head -1)"
echo "Android SDK: $(ls $ANDROID_HOME/platforms)"
echo "Build Tools: $(ls $ANDROID_HOME/build-tools)"
```

## 📚 Documentation

- **SETUP_COMPLETE.md** - Full setup summary
- **DEBUG_GUIDE.md** - Comprehensive debugging guide  
- **BUILD_STATUS.md** - Current build status
- **Development.md** - Development guidelines

## 🎯 Development Workflow

### 1. Make Code Changes
Edit files in:
- `app/src/main/java/` - Main app code
- `core/src/main/java/` - Core logic
- `app/src/main/res/` - Resources (layouts, strings, etc.)

### 2. Build
```bash
./build.sh debug
```

### 3. Install on Device/Emulator
```bash
adb install app/build/outputs/apk/debug/app-debug.apk
```

Or:
```bash
./gradlew installDebug
```

### 4. Test
```bash
./build.sh test
```

### 5. View Logs
```bash
adb logcat | grep "Unstoppable"
```

## 🛠️ Available Build Variants

- `assembleDebug` - Dev build with debug symbols
- `assembleAppcenterDebug` - AppCenter test build
- `assembleAppcenterRelease` - AppCenter release build
- `assembleRelease` - Production build

## 💡 Pro Tips

1. **Use the build script** - It handles environment setup automatically
2. **Check dependencies first** - Run `./build.sh deps` to see what's available
3. **Clean when confused** - `./build.sh clean` fixes many issues
4. **Use --stacktrace** - Always add `--stacktrace` for detailed errors
5. **Check JitPack** - If deps fail, check https://jitpack.io status

## ✨ Success Checklist

- [x] Java 17 installed
- [x] Android SDK installed (Platform 34)
- [x] Gradle working
- [x] Project configured
- [ ] JitPack dependencies resolved (pending)
- [ ] First successful build (pending)

## 🆘 Need Help?

1. Check `DEBUG_GUIDE.md` for common issues
2. Run diagnostics: `./build.sh`
3. View logs: `./gradlew assembleDebug --stacktrace`
4. Check GitHub issues: https://github.com/horizontalsystems/unstoppable-wallet-android

## 🎉 You're Ready!

The build environment is fully configured. Once the JitPack dependencies are resolved, you'll be able to build and debug the Unstoppable Wallet Android app.

**Next Steps:**
1. Trigger JitPack builds (see above)
2. Wait for builds to complete
3. Run: `./build.sh debug`
4. Start developing!

---

Happy coding! 🚀
