#!/bin/bash

# Explorer3 Deployment Script
# This script builds and prepares the Explorer3 feature for deployment

set -e  # Exit on error

echo "🚀 Explorer3 Deployment Script"
echo "================================"
echo ""

# Colors for output
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

# Check prerequisites
echo "📋 Checking prerequisites..."

# Check Java
if ! command -v java &> /dev/null; then
    echo -e "${RED}❌ Java not found. Please install Java 17+${NC}"
    exit 1
fi
JAVA_VERSION=$(java -version 2>&1 | head -n 1)
echo -e "${GREEN}✅ Java found: $JAVA_VERSION${NC}"

# Check Android SDK
if [ -z "$ANDROID_HOME" ]; then
    echo -e "${YELLOW}⚠️  ANDROID_HOME not set. Build may fail.${NC}"
else
    echo -e "${GREEN}✅ ANDROID_HOME: $ANDROID_HOME${NC}"
fi

# Check Gradle wrapper
if [ ! -f "./gradlew" ]; then
    echo -e "${RED}❌ Gradle wrapper not found${NC}"
    exit 1
fi
echo -e "${GREEN}✅ Gradle wrapper found${NC}"

echo ""
echo "🧹 Cleaning previous builds..."
./gradlew clean

echo ""
echo "🔍 Running lint checks..."
./gradlew lint || echo -e "${YELLOW}⚠️  Lint warnings found (non-blocking)${NC}"

echo ""
echo "🏗️  Building release APK..."
./gradlew assembleRelease

if [ $? -eq 0 ]; then
    APK_PATH="app/build/outputs/apk/release/app-release.apk"
    if [ -f "$APK_PATH" ]; then
        APK_SIZE=$(du -h "$APK_PATH" | cut -f1)
        echo ""
        echo -e "${GREEN}✅ Build successful!${NC}"
        echo "📦 APK Location: $APK_PATH"
        echo "📊 APK Size: $APK_SIZE"
        echo ""
        echo "🎯 Next steps:"
        echo "   1. Install APK on test device: adb install $APK_PATH"
        echo "   2. Run manual tests (see EXPLORER3_TEST_DEPLOY.md)"
        echo "   3. Upload to Firebase App Distribution or Play Console"
    else
        echo -e "${RED}❌ APK not found at expected location${NC}"
        exit 1
    fi
else
    echo -e "${RED}❌ Build failed${NC}"
    exit 1
fi

echo ""
echo "🏗️  Building Android App Bundle (AAB) for Play Store..."
./gradlew bundleRelease

if [ $? -eq 0 ]; then
    AAB_PATH="app/build/outputs/bundle/release/app-release.aab"
    if [ -f "$AAB_PATH" ]; then
        AAB_SIZE=$(du -h "$AAB_PATH" | cut -f1)
        echo ""
        echo -e "${GREEN}✅ AAB build successful!${NC}"
        echo "📦 AAB Location: $AAB_PATH"
        echo "📊 AAB Size: $AAB_SIZE"
        echo ""
        echo "🎯 Ready for Play Store upload"
    else
        echo -e "${YELLOW}⚠️  AAB not found at expected location${NC}"
    fi
fi

echo ""
echo "✅ Deployment preparation complete!"
echo ""
echo "📚 See EXPLORER3_TEST_DEPLOY.md for testing procedures"
echo "📚 See EXPLORER3_DEPLOYMENT_SUMMARY.md for deployment checklist"
