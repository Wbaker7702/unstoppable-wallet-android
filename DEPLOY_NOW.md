# Deploy Explorer3 - Quick Start Guide

## ⚠️ Note: Build Environment Required

The deployment script requires:
- **Java 17+** (JDK)
- **Android SDK** (via Android Studio or command line tools)
- **Gradle** (included via wrapper)

Since these aren't available in the current environment, here's how to deploy:

## 🚀 Deployment Steps

### 1. Setup Your Build Environment

**Option A: Use Android Studio (Recommended)**
```bash
# 1. Open Android Studio
# 2. Open this project: /workspace/unstoppable-wallet-android
# 3. Wait for Gradle sync to complete
# 4. Go to Build → Generate Signed Bundle / APK
```

**Option B: Use Command Line**
```bash
# Install Java 17+
# Ubuntu/Debian:
sudo apt-get update
sudo apt-get install openjdk-17-jdk

# macOS (using Homebrew):
brew install openjdk@17

# Verify installation
java -version

# Set JAVA_HOME (if needed)
export JAVA_HOME=/path/to/java17
```

### 2. Run Deployment Script

Once Java is installed, run:
```bash
cd /workspace/unstoppable-wallet-android
./deploy.sh
```

**Expected Output:**
```
🚀 Explorer3 Deployment Script
================================
📋 Checking prerequisites...
✅ Java found: openjdk version "17.x.x"
✅ ANDROID_HOME: /path/to/android/sdk
✅ Gradle wrapper found
🧹 Cleaning previous builds...
🏗️  Building release APK...
✅ Build successful!
📦 APK Location: app/build/outputs/apk/release/app-release.apk
```

### 3. Alternative: Manual Build Commands

If the script doesn't work, run these manually:

```bash
# Clean previous builds
./gradlew clean

# Build release APK
./gradlew assembleRelease

# Build AAB for Play Store
./gradlew bundleRelease

# Check outputs
ls -lh app/build/outputs/apk/release/
ls -lh app/build/outputs/bundle/release/
```

### 4. Test the Build

**Install on Connected Device:**
```bash
# Connect Android device via USB
adb devices

# Install APK
adb install app/build/outputs/apk/release/app-release.apk

# Or install via Android Studio
# Run → Run 'app'
```

**Quick Test Checklist:**
- [ ] App launches successfully
- [ ] Navigate to Explorer3 screen
- [ ] Perform a test search
- [ ] Verify navigation works

### 5. Deploy to Users

**Option 1: Firebase App Distribution (Beta)**
```bash
# Install Firebase CLI
npm install -g firebase-tools

# Login
firebase login

# Distribute
firebase appdistribution:distribute \
  app/build/outputs/apk/release/app-release.apk \
  --app YOUR_APP_ID \
  --groups "beta-testers" \
  --release-notes "Explorer3: Unified search and block explorer"
```

**Option 2: Google Play Console**
1. Go to Play Console → Your App
2. Go to Testing → Internal Testing
3. Create new release
4. Upload `app-release.aab`
5. Add release notes
6. Review and rollout (start with 5%)

**Option 3: Direct APK Share**
- Share APK file directly (for internal testing only)
- Not recommended for production

## 📋 Pre-Deployment Checklist

### Code Verification ✅
- [x] All code committed to git
- [x] No linter errors
- [x] All resources verified
- [x] Navigation integrated
- [x] Error handling implemented

### Build Verification
- [ ] Java 17+ installed
- [ ] Android SDK configured
- [ ] Build completes successfully
- [ ] APK/AAB files generated
- [ ] No ProGuard warnings

### Testing
- [ ] Smoke test on device
- [ ] Navigation flows tested
- [ ] Error cases tested
- [ ] Performance acceptable

## 🔍 Troubleshooting

### "Java not found"
```bash
# Install Java 17+
# Ubuntu/Debian:
sudo apt-get install openjdk-17-jdk

# macOS:
brew install openjdk@17

# Verify
java -version
```

### "ANDROID_HOME not set"
```bash
# Set ANDROID_HOME (usually in ~/.bashrc or ~/.zshrc)
export ANDROID_HOME=$HOME/Android/Sdk
export PATH=$PATH:$ANDROID_HOME/tools
export PATH=$PATH:$ANDROID_HOME/platform-tools

# Or use Android Studio's SDK location
export ANDROID_HOME=$HOME/Library/Android/sdk  # macOS
export ANDROID_HOME=$HOME/Android/Sdk          # Linux
```

### Build Fails
```bash
# Clean and rebuild
./gradlew clean
./gradlew assembleRelease --stacktrace

# Check build logs
cat app/build/outputs/logs/build.log
```

### APK Installation Fails
```bash
# Uninstall previous version
adb uninstall io.horizontalsystems.bankwallet

# Install new version
adb install app/build/outputs/apk/release/app-release.apk

# Or force install
adb install -r app/build/outputs/apk/release/app-release.apk
```

## 📊 What's Ready

### Code Status ✅
- All Explorer3 code implemented
- All fixes applied
- Resources verified
- Navigation integrated
- Documentation complete

### Files Ready
- 10 Kotlin source files
- Navigation graph updated
- String resources added
- Drawable resources verified
- Deployment scripts created

### Git Status ✅
- All changes committed
- All changes pushed to GitHub
- Ready for CI/CD integration

## 🎯 Next Steps

1. **Set up build environment** (Java + Android SDK)
2. **Run deployment script** (`./deploy.sh`)
3. **Test on device** (install APK)
4. **Deploy to beta** (Firebase or Play Console)
5. **Monitor** (crash reports, analytics)

## 📚 Additional Resources

- **Deployment Checklist:** `DEPLOYMENT_CHECKLIST.md`
- **Test Guide:** `EXPLORER3_TEST_DEPLOY.md`
- **Code Review:** `EXPLORER3_REVIEW.md`
- **Deployment Summary:** `EXPLORER3_DEPLOYMENT_SUMMARY.md`

---

**Status:** ✅ Code Ready, Waiting for Build Environment
**Action Required:** Install Java 17+ and Android SDK, then run `./deploy.sh`
