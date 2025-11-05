#!/bin/bash
# Build Environment Setup Script for Unstoppable Wallet Android

set -e

echo "=========================================="
echo "Unstoppable Wallet Android - Build Setup"
echo "=========================================="
echo ""

# Set environment variables
echo "📦 Setting up environment variables..."
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
export ANDROID_HOME=$HOME/Android/Sdk
export PATH=$PATH:$ANDROID_HOME/platform-tools:$ANDROID_HOME/cmdline-tools/latest/bin

# Verify Java
echo ""
echo "☕ Verifying Java installation..."
java -version
echo "✅ Java OK"

# Verify Android SDK
echo ""
echo "📱 Verifying Android SDK..."
if [ -d "$ANDROID_HOME/platforms/android-34" ]; then
    echo "✅ Android SDK Platform 34: Found"
else
    echo "❌ Android SDK Platform 34: Not found"
    exit 1
fi

if [ -d "$ANDROID_HOME/build-tools/34.0.0" ]; then
    echo "✅ Build Tools 34.0.0: Found"
else
    echo "❌ Build Tools 34.0.0: Not found"
    exit 1
fi

# Verify Gradle
echo ""
echo "🔨 Verifying Gradle..."
./gradlew --version | grep "Gradle"
echo "✅ Gradle OK"

# Check local.properties
echo ""
echo "⚙️  Checking project configuration..."
if [ -f "local.properties" ]; then
    echo "✅ local.properties exists"
    cat local.properties
else
    echo "⚠️  local.properties not found, creating..."
    echo "sdk.dir=$HOME/Android/Sdk" > local.properties
    echo "✅ Created local.properties"
fi

echo ""
echo "=========================================="
echo "Environment Setup Complete!"
echo "=========================================="
echo ""
echo "Available commands:"
echo "  ./build.sh tasks    - List all Gradle tasks"
echo "  ./build.sh clean    - Clean build artifacts"
echo "  ./build.sh debug    - Build debug APK"
echo "  ./build.sh release  - Build release APK"
echo "  ./build.sh test     - Run unit tests"
echo "  ./build.sh deps     - Check dependencies"
echo ""

# Handle command line arguments
if [ $# -eq 0 ]; then
    echo "💡 Usage: ./build.sh [tasks|clean|debug|release|test|deps]"
    exit 0
fi

case "$1" in
    tasks)
        echo "📋 Listing available tasks..."
        ./gradlew tasks
        ;;
    clean)
        echo "🧹 Cleaning build artifacts..."
        ./gradlew clean
        ;;
    debug)
        echo "🔨 Building debug APK..."
        ./gradlew assembleDebug
        echo ""
        echo "✅ Build complete!"
        echo "📦 Output: app/build/outputs/apk/debug/app-debug.apk"
        ;;
    release)
        echo "🔨 Building release APK..."
        ./gradlew assembleRelease
        echo ""
        echo "✅ Build complete!"
        echo "📦 Output: app/build/outputs/apk/release/app-release.apk"
        ;;
    test)
        echo "🧪 Running tests..."
        ./gradlew test
        ;;
    deps)
        echo "📦 Checking dependencies..."
        ./gradlew app:dependencies --configuration debugCompileClasspath
        ;;
    *)
        echo "❌ Unknown command: $1"
        echo "💡 Available: tasks, clean, debug, release, test, deps"
        exit 1
        ;;
esac
