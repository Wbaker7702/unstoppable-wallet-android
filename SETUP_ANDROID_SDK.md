# Android SDK Setup Instructions

## Quick Setup (for this environment)

The build requires Android SDK which needs to be installed manually. Here are the options:

## Option 1: Install via Android Studio (Recommended)

1. Download and install Android Studio from https://developer.android.com/studio
2. Open Android Studio → Configure → SDK Manager
3. Install Android SDK Platform 34 (or version specified in build.gradle)
4. Set ANDROID_HOME environment variable:
   ```bash
   export ANDROID_HOME=$HOME/Android/Sdk
   export PATH=$PATH:$ANDROID_HOME/platform-tools
   ```

## Option 2: Install Command Line Tools Only

```bash
# Create SDK directory
mkdir -p ~/Android/Sdk

# Download command line tools (manual download required)
# Visit: https://developer.android.com/studio#command-tools
# Download: commandlinetools-linux-XXXXX_latest.zip

# Extract to proper location
cd ~/Android/Sdk
unzip commandlinetools-linux-XXXXX_latest.zip
mkdir -p cmdline-tools
mv cmdline-tools latest
mv latest cmdline-tools/

# Accept licenses and install platform
export ANDROID_HOME=$HOME/Android/Sdk
export PATH=$PATH:$ANDROID_HOME/cmdline-tools/latest/bin

yes | sdkmanager --licenses
sdkmanager "platform-tools" "platforms;android-34" "build-tools;34.0.0"
```

## Option 3: Use Existing SDK (if available)

If you have Android SDK installed elsewhere:

```bash
# Set ANDROID_HOME to existing SDK location
export ANDROID_HOME=/path/to/android/sdk

# Or create local.properties file
echo "sdk.dir=/path/to/android/sdk" > local.properties
```

## Verify Setup

```bash
# Check ANDROID_HOME
echo $ANDROID_HOME

# Check SDK tools
ls $ANDROID_HOME/platform-tools/adb

# Try building
cd /workspace/unstoppable-wallet-android
./gradlew assembleDebug
```

## Current Status

- ✅ Java 17 installed
- ✅ Gradle wrapper working
- ⚠️  Android SDK needs to be installed

Once Android SDK is installed, run:
```bash
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
export PATH=$PATH:$JAVA_HOME/bin
export ANDROID_HOME=$HOME/Android/Sdk
cd /workspace/unstoppable-wallet-android
./deploy.sh
```
