# Explorer3 Deployment Checklist

## Pre-Deployment Verification

### Code Quality ✅
- [x] All files committed to git
- [x] No linter errors
- [x] All resources verified
- [x] Navigation integrated
- [x] Error handling implemented

### Files Status
- [x] 10 Kotlin source files created
- [x] Navigation graph updated
- [x] String resources added
- [x] Drawable resources verified
- [x] Documentation complete

## Build Steps

### 1. Environment Setup
```bash
# Verify Java 17+ installed
java -version

# Verify Android SDK configured
echo $ANDROID_HOME

# Make deployment script executable
chmod +x deploy.sh
```

### 2. Run Deployment Script
```bash
./deploy.sh
```

**Or manually:**
```bash
# Clean previous builds
./gradlew clean

# Build release APK
./gradlew assembleRelease

# Build AAB for Play Store
./gradlew bundleRelease
```

### 3. Verify Build Outputs

**APK Location:**
- `app/build/outputs/apk/release/app-release.apk`

**AAB Location:**
- `app/build/outputs/bundle/release/app-release.aab`

**Verify:**
- [ ] APK/AAB files exist
- [ ] File sizes are reasonable
- [ ] No build errors
- [ ] No ProGuard warnings

## Testing (Before Deployment)

### Quick Smoke Test
```bash
# Install on connected device
adb install app/build/outputs/apk/release/app-release.apk

# Or use Android Studio to run
```

### Manual Test Checklist

**Critical Paths:**
- [ ] Open app → Navigate to Explorer3
- [ ] Search for wallet transaction → Verify navigation to TransactionInfoFragment
- [ ] Search for external transaction → Verify opens explorer URL
- [ ] Search for address → Verify opens explorer URL
- [ ] Search for token → Verify navigation to CoinFragment
- [ ] Search for block → Verify opens explorer URL

**Edge Cases:**
- [ ] Empty search query
- [ ] Invalid input
- [ ] Network offline
- [ ] Rapid query changes

**UI/UX:**
- [ ] Search bar displays correctly
- [ ] Results grouped properly
- [ ] Icons display correctly
- [ ] Loading states work
- [ ] Error states display

## Deployment Options

### Option 1: Firebase App Distribution (Recommended for Beta)

```bash
# Install Firebase CLI if not installed
npm install -g firebase-tools

# Login to Firebase
firebase login

# Distribute to testers
firebase appdistribution:distribute app/build/outputs/apk/release/app-release.apk \
  --app YOUR_APP_ID \
  --groups "beta-testers" \
  --release-notes "Explorer3: Unified search and block explorer feature"
```

### Option 2: Google Play Console Internal Testing

1. **Upload AAB:**
   - Go to Play Console → Your App → Testing → Internal testing
   - Create new release
   - Upload `app-release.aab`
   - Add release notes
   - Review and rollout

2. **Add Testers:**
   - Add internal testers
   - Share opt-in URL

### Option 3: Direct APK Installation

```bash
# For internal testing only
adb install app/build/outputs/apk/release/app-release.apk
```

## Staged Rollout Plan

### Phase 1: Internal Testing (Days 1-3)
- **Target:** Internal team + QA
- **Users:** 10-20 people
- **Goal:** Verify core functionality
- **Success Criteria:** No critical bugs

### Phase 2: Beta Testing (Days 4-7)
- **Target:** Beta testers
- **Users:** 100-500 people
- **Goal:** Find edge cases and UX issues
- **Success Criteria:** < 1% crash rate

### Phase 3: Staged Production (Days 8+)
- **Week 1:** 5% rollout
- **Week 2:** 25% rollout (if stable)
- **Week 3:** 50% rollout (if stable)
- **Week 4:** 100% rollout (if stable)

## Monitoring Setup

### Firebase Crashlytics
- [ ] Verify Crashlytics is enabled
- [ ] Set up alerts for critical crashes
- [ ] Monitor Explorer3-specific crashes

### Firebase Analytics
- [ ] Verify Analytics is enabled
- [ ] Track Explorer3 screen views
- [ ] Track search queries (anonymized)
- [ ] Track navigation events

### Key Metrics to Monitor

**First 24 Hours:**
- Crash-free rate (target: > 99%)
- Search query volume
- Navigation success rate
- Error rate

**First Week:**
- User engagement with Explorer3
- Most searched queries
- Navigation patterns
- Performance metrics

## Rollback Procedure

### If Critical Issues Found:

1. **Immediate Actions:**
   ```bash
   # Pause rollout in Play Console
   # Or disable feature flag if implemented
   ```

2. **Communication:**
   - Notify team
   - Document issue
   - Create hotfix branch

3. **Fix & Redeploy:**
   ```bash
   # Create hotfix branch
   git checkout -b hotfix/explorer3-[issue-name]
   
   # Fix issue
   # Test thoroughly
   # Build and deploy patch version
   ```

## Post-Deployment

### Day 1 Checklist
- [ ] Monitor crash reports
- [ ] Check analytics dashboard
- [ ] Review user feedback
- [ ] Check performance metrics
- [ ] Verify no regressions

### Week 1 Checklist
- [ ] Analyze usage patterns
- [ ] Review search query trends
- [ ] Check navigation success rates
- [ ] Gather user feedback
- [ ] Plan improvements

### Success Criteria

**Deployment Successful If:**
- ✅ Crash-free rate > 99%
- ✅ No critical bugs reported
- ✅ Navigation flows work correctly
- ✅ Performance is acceptable
- ✅ User feedback is positive

## Support Resources

- **Documentation:** See `EXPLORER3_*` markdown files
- **Build Script:** `deploy.sh`
- **Test Guide:** `EXPLORER3_TEST_DEPLOY.md`
- **Review:** `EXPLORER3_REVIEW.md`

## Emergency Contacts

- **Team Lead:** [To be filled]
- **QA Lead:** [To be filled]
- **DevOps:** [To be filled]

---

**Status:** ✅ Ready for Deployment
**Risk Level:** 🟢 Low
**Confidence:** High

**Last Updated:** [Current Date]
**Deployed By:** [Your Name]
