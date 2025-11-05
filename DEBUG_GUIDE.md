# Debug Guide for Unstoppable Wallet Android

## Quick Start

### 1. Set Environment Variables

Add to your `~/.bashrc` or `~/.zshrc`:
```bash
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
export ANDROID_HOME=$HOME/Android/Sdk
export PATH=$PATH:$ANDROID_HOME/platform-tools:$ANDROID_HOME/cmdline-tools/latest/bin
```

Then reload:
```bash
source ~/.bashrc  # or source ~/.zshrc
```

### 2. Verify Installation

```bash
# Check Java
java -version
# Should show: openjdk version "17.0.16"

# Check Android SDK
ls $ANDROID_HOME/platforms
# Should show: android-34

# Check Gradle
cd /workspace/unstoppable-wallet-android
./gradlew --version
# Should show: Gradle 8.6
```

## Build Commands

### List All Available Tasks
```bash
./gradlew tasks --all
```

### Clean Build
```bash
./gradlew clean
```

### Build Debug APK
```bash
./gradlew assembleDebug
```

Output location: `app/build/outputs/apk/debug/app-debug.apk`

### Build Release APK
```bash
./gradlew assembleRelease
```

Output location: `app/build/outputs/apk/release/app-release.apk`

### Build All Variants
```bash
./gradlew assemble
```

### Run Unit Tests
```bash
./gradlew test
```

### Run Specific Module Tests
```bash
./gradlew :app:test
./gradlew :core:test
```

### Lint Check
```bash
./gradlew lint
```

### Check Dependencies
```bash
./gradlew app:dependencies
```

### Check for Dependency Updates
```bash
./gradlew dependencyUpdates
```

## Debugging Build Issues

### View Detailed Build Output
```bash
./gradlew assembleDebug --stacktrace --info
```

### Debug Mode (Most Verbose)
```bash
./gradlew assembleDebug --stacktrace --debug
```

### Check Configuration
```bash
./gradlew app:properties
```

### Verify Android SDK Installation
```bash
$ANDROID_HOME/cmdline-tools/latest/bin/sdkmanager --list
```

### View Build Variants
```bash
./gradlew app:signingReport
```

## Common Issues

### Issue 1: JitPack Dependencies Not Found

**Error:**
```
Could not find com.github.horizontalsystems:bitcoin-kit-android:ced5801
```

**Solutions:**

1. **Check JitPack Status:**
   - Visit: https://jitpack.io/#horizontalsystems/bitcoin-kit-android/ced5801
   - Click "Look up" to trigger a build
   - Wait for JitPack to build the artifact (can take 15-30 minutes)

2. **Force Refresh Dependencies:**
   ```bash
   ./gradlew build --refresh-dependencies
   ```

3. **Clear Gradle Cache:**
   ```bash
   rm -rf ~/.gradle/caches/
   ./gradlew clean build
   ```

4. **Check Repository Connectivity:**
   ```bash
   curl -I https://jitpack.io
   ```

### Issue 2: Out of Memory

**Error:**
```
Expiring Daemon because JVM heap space is exhausted
```

**Solution:**
Edit `gradle.properties` and increase memory:
```properties
org.gradle.jvmargs=-Xmx4096m -XX:MaxMetaspaceSize=512m
```

### Issue 3: SDK Not Found

**Error:**
```
SDK location not found
```

**Solution:**
Ensure `local.properties` exists with correct path:
```bash
echo "sdk.dir=$HOME/Android/Sdk" > local.properties
```

### Issue 4: Build Tools Not Found

**Error:**
```
Failed to install the following Android SDK packages
```

**Solution:**
Install specific build tools version:
```bash
$ANDROID_HOME/cmdline-tools/latest/bin/sdkmanager "build-tools;34.0.0"
```

### Issue 5: Kotlin Compilation Error

**Error:**
```
e: Compilation failed
```

**Solution:**
1. Clean and rebuild:
   ```bash
   ./gradlew clean
   ./gradlew assembleDebug
   ```

2. Invalidate caches:
   ```bash
   rm -rf .gradle/
   rm -rf app/build/
   ```

## Profiling and Performance

### Generate Build Scan
```bash
./gradlew assembleDebug --scan
```

### Profile Build
```bash
./gradlew assembleDebug --profile
```

Report location: `build/reports/profile/`

### Check Build Performance
```bash
./gradlew assembleDebug --dry-run
```

## Module Structure

```
unstoppable-wallet-android/
├── app/                    # Main application module
├── core/                   # Core business logic
└── components/
    ├── chartview/         # Chart UI component
    └── icons/             # Icon resources
```

### Build Specific Module
```bash
./gradlew :core:build
./gradlew :components:icons:build
```

## Android Debugging

### Connect Device via ADB
```bash
adb devices
```

### Install Debug APK
```bash
adb install app/build/outputs/apk/debug/app-debug.apk
```

### View Logcat
```bash
adb logcat | grep "Unstoppable"
```

### Clear App Data
```bash
adb shell pm clear io.horizontalsystems.bankwallet.dev
```

### Uninstall App
```bash
adb uninstall io.horizontalsystems.bankwallet.dev
```

## Continuous Integration

### Run CI-like Build
```bash
./gradlew clean build --no-daemon --stacktrace
```

### Check All
```bash
./gradlew clean check assembleDebug assembleRelease
```

## Code Quality

### Run Detekt (Static Analysis)
```bash
./gradlew detekt
```

### Run All Checks
```bash
./gradlew check
```

### Generate Code Coverage
```bash
./gradlew jacocoTestReport
```

## Release Build

### Build Signed Release APK
```bash
./gradlew assembleRelease
```

Note: Requires signing configuration in `app/build.gradle`

### Generate Bundle for Play Store
```bash
./gradlew bundleRelease
```

Output: `app/build/outputs/bundle/release/app-release.aab`

## Useful Gradle Properties

Create/edit `gradle.properties`:

```properties
# Performance
org.gradle.jvmargs=-Xmx4096m -XX:MaxMetaspaceSize=512m
org.gradle.parallel=true
org.gradle.caching=true
org.gradle.daemon=true

# Android
android.useAndroidX=true
android.enableJetifier=true

# Kotlin
kotlin.code.style=official
kotlin.incremental=true
```

## Environment Aliases

Add to `~/.bashrc` or `~/.zshrc`:

```bash
# Unstoppable Wallet shortcuts
alias uwbuild='cd /workspace/unstoppable-wallet-android && ./gradlew assembleDebug'
alias uwclean='cd /workspace/unstoppable-wallet-android && ./gradlew clean'
alias uwtest='cd /workspace/unstoppable-wallet-android && ./gradlew test'
alias uwinstall='cd /workspace/unstoppable-wallet-android && ./gradlew installDebug'
```

## References

- [Android Developer Documentation](https://developer.android.com/docs)
- [Gradle Build Tool](https://docs.gradle.org/)
- [Kotlin Documentation](https://kotlinlang.org/docs/home.html)
- [JitPack Documentation](https://jitpack.io/docs/)

## Support

For issues specific to this project:
- GitHub: https://github.com/horizontalsystems/unstoppable-wallet-android
- Email: support.unstoppable@protonmail.com
