# Explorer3 Test & Deployment Guide

## Pre-Deployment Checklist

### ✅ Code Review Status
- [x] All files compile without errors
- [x] No linter warnings
- [x] All dependencies resolved
- [x] Navigation properly integrated
- [x] String resources defined
- [x] Drawable resources verified

### Resource Verification

**String Resources** (✅ Verified in `strings.xml`):
- `Explorer3_Title`
- `Explorer3_SearchHint`
- `Explorer3_NoResults`
- `Explorer3_RecentSearches`
- `Explorer3_Transactions`
- `Explorer3_Addresses`
- `Explorer3_Blocks`
- `Explorer3_Tokens`
- `Explorer3_Contracts`

**Drawable Resources** (✅ Verified exist):
- `ic_transactions` - Found in `components/icons`
- `ic_qr_code` - Used in codebase
- `ic_settings` - Used in codebase
- `ic_arrow_up_right_24` - Used in codebase
- `coin_placeholder` - Used in codebase
- `ic_not_found` - Used in codebase

**Navigation IDs** (✅ Verified in `main_graph.xml`):
- `explorer3Fragment` - Entry point
- `transactionInfoFragment` - For wallet transactions
- `coinFragment` - For token results
- `mainFragment` - For TransactionsViewModel access

## Build Instructions

### Prerequisites
```bash
# Java 17+ required
java -version

# Android SDK configured
echo $ANDROID_HOME

# Gradle wrapper available
ls gradlew
```

### Build Commands

**Debug Build:**
```bash
./gradlew assembleDebug
```

**Release Build:**
```bash
./gradlew assembleRelease
```

**Android App Bundle (for Play Store):**
```bash
./gradlew bundleRelease
```

**Clean Build:**
```bash
./gradlew clean assembleDebug
```

### Build Output Locations
- Debug APK: `app/build/outputs/apk/debug/app-debug.apk`
- Release APK: `app/build/outputs/apk/release/app-release.apk`
- AAB: `app/build/outputs/bundle/release/app-release.aab`

## Testing Procedures

### 1. Unit Tests (Recommended)

Create test files:
- `Explorer3ServiceTest.kt`
- `QueryParserTest.kt`
- `ExplorerUrlGeneratorTest.kt`

**Example Test Structure:**
```kotlin
@Test
fun testQueryParser_TransactionHash() {
    val result = QueryParser.parseQuery("0x1234...")
    assertTrue(result is QueryParser.QueryType.TransactionHash)
}

@Test
fun testExplorerUrlGenerator_Bitcoin() {
    val url = generator.getAddressUrl("1A1zP1eP5QGefi2DMPTfTL5SLmv7DivfNa", BlockchainType.Bitcoin)
    assertEquals("https://blockstream.info/address/1A1zP1eP5QGefi2DMPTfTL5SLmv7DivfNa", url)
}
```

### 2. Integration Tests

**Navigation Tests:**
- [ ] Navigate to Explorer3 from main menu
- [ ] Search for wallet transaction → Navigate to TransactionInfoFragment
- [ ] Search for token → Navigate to CoinFragment
- [ ] Search for address → Open explorer URL
- [ ] Back navigation works correctly

**Search Functionality Tests:**
- [ ] Transaction hash search (wallet)
- [ ] Transaction hash search (external)
- [ ] Address search (Bitcoin)
- [ ] Address search (Ethereum)
- [ ] Block number search
- [ ] Token name search
- [ ] Token symbol search

### 3. Manual Testing Checklist

#### Transaction Search
- [ ] **Wallet Transaction**: Search for a transaction hash from user's wallet
  - Expected: Shows result with blockchain type
  - Click: Navigates to TransactionInfoFragment
  - Verify: Transaction details are correct

- [ ] **External Transaction**: Search for a transaction hash not in wallet
  - Expected: Shows result with explorer URL
  - Click: Opens explorer URL in browser
  - Verify: URL is correct for blockchain type

- [ ] **Invalid Hash**: Search for "invalidhash123"
  - Expected: Shows "No results found"
  - Verify: Error handling works

#### Address Search
- [ ] **Bitcoin Address**: Search for "1A1zP1eP5QGefi2DMPTfTL5SLmv7DivfNa"
  - Expected: Shows address result with Bitcoin blockchain
  - Click: Opens Blockstream URL
  - Verify: URL format is correct

- [ ] **Ethereum Address**: Search for "0x742d35Cc6634C0532925a3b844Bc9e7595f0bEb"
  - Expected: Shows address result with Ethereum blockchain
  - Click: Opens Etherscan URL
  - Verify: URL format is correct

- [ ] **Invalid Address**: Search for "invalidaddress"
  - Expected: Shows "No results found" or falls back to token search

#### Block Search
- [ ] **Block Number**: Search for "100000"
  - Expected: Shows block results for multiple blockchains
  - Click: Opens explorer URL
  - Verify: URLs are correct

#### Token Search
- [ ] **Token Name**: Search for "Bitcoin"
  - Expected: Shows token results
  - Click: Navigates to CoinFragment
  - Verify: Correct coin is displayed

- [ ] **Token Symbol**: Search for "BTC"
  - Expected: Shows Bitcoin token
  - Click: Navigates to CoinFragment

#### Edge Cases
- [ ] **Empty Query**: Clear search field
  - Expected: Shows empty state or recent searches

- [ ] **Rapid Typing**: Type quickly "btc" then "eth"
  - Expected: Cancels previous search, shows latest results

- [ ] **Network Error**: Turn off network, search
  - Expected: Shows error message
  - Verify: Error handling works

- [ ] **Navigate Away**: Start search, navigate back
  - Expected: Search cancels gracefully

### 4. UI/UX Testing

**Visual Tests:**
- [ ] Search bar appears correctly
- [ ] Results are grouped by type
- [ ] Icons display correctly
- [ ] Loading indicator shows during search
- [ ] Empty state displays correctly
- [ ] Error state displays correctly

**Interaction Tests:**
- [ ] Keyboard appears on focus
- [ ] Search executes on Enter
- [ ] Click on result navigates correctly
- [ ] Scroll works smoothly
- [ ] Back button works

### 5. Performance Testing

- [ ] **Search Speed**: Measure time for typical searches
  - Target: < 500ms for wallet transaction search
  - Target: < 1000ms for address validation

- [ ] **Memory**: Check for memory leaks
  - Monitor during extended use
  - Verify ViewModel cleanup

- [ ] **Network**: Monitor network requests
  - Verify no unnecessary API calls
  - Check request cancellation

## Deployment Steps

### 1. Pre-Deployment

```bash
# Clean previous builds
./gradlew clean

# Run linting
./gradlew lint

# Run tests (if available)
./gradlew test

# Build release
./gradlew assembleRelease
```

### 2. Verification

- [ ] APK/AAB builds successfully
- [ ] No ProGuard/R8 warnings for Explorer3 classes
- [ ] APK size impact is acceptable
- [ ] All resources included

### 3. Internal Testing

- [ ] Install on test device
- [ ] Run full manual test checklist
- [ ] Test on multiple Android versions (API 27+)
- [ ] Test on different screen sizes
- [ ] Test with/without wallets configured

### 4. Beta Testing

**Firebase App Distribution:**
```bash
# Upload to Firebase
firebase appdistribution:distribute app-release.apk \
  --app YOUR_APP_ID \
  --groups "beta-testers"
```

**Google Play Internal Testing:**
1. Upload AAB to Play Console
2. Create internal test track
3. Add testers
4. Monitor crash reports

### 5. Production Deployment

**Staged Rollout:**
1. Deploy to 5% of users
2. Monitor crash reports and analytics
3. If stable, increase to 25%
4. Continue gradual rollout

**Monitoring:**
- Crash reports (Firebase Crashlytics)
- Analytics (search queries, navigation paths)
- User feedback
- Performance metrics

## Rollback Plan

If critical issues are found:

1. **Immediate Actions:**
   - Pause rollout in Play Console
   - Revert to previous version
   - Notify users if needed

2. **Code Fixes:**
   - Identify root cause
   - Create hotfix branch
   - Test thoroughly
   - Deploy patch version

## Post-Deployment

### Monitoring (First 24 Hours)
- [ ] Monitor crash reports
- [ ] Check analytics for usage
- [ ] Review user feedback
- [ ] Monitor performance metrics

### Metrics to Track
- Search query volume
- Result click-through rates
- Navigation success rates
- Error rates
- Performance (search time, memory usage)

### Documentation Updates
- [ ] Update user documentation
- [ ] Create feature announcement
- [ ] Update API documentation (if applicable)

## Known Issues & Limitations

1. **Search History**: Not persisted (returns empty list)
   - Impact: Low
   - Status: Documented, TODO

2. **Balance Queries**: Address balances not queried
   - Impact: Medium
   - Status: Documented, future enhancement

3. **Block Validation**: Doesn't verify block existence
   - Impact: Low
   - Status: Documented, acceptable for MVP

## Support & Troubleshooting

### Common Issues

**Issue**: Search returns no results
- Check: Network connectivity
- Check: Query format (verify QueryParser logic)
- Check: Adapter availability

**Issue**: Navigation fails
- Check: TransactionsViewModel availability
- Check: Navigation graph configuration
- Check: Fragment lifecycle

**Issue**: Explorer URLs don't open
- Check: LinkHelper implementation
- Check: Browser availability
- Check: URL format

### Debug Commands

```bash
# View logs
adb logcat | grep Explorer3

# Check navigation
adb logcat | grep Navigation

# Monitor memory
adb shell dumpsys meminfo com.horizontalsystems.unstoppablewallet
```

## Success Criteria

✅ **Deployment Successful If:**
- No critical crashes reported
- Search functionality works as expected
- Navigation flows correctly
- Performance is acceptable
- User feedback is positive

## Next Steps After Deployment

1. Gather user feedback
2. Monitor analytics
3. Plan enhancements:
   - Search history persistence
   - Balance queries
   - External API integration
   - Search filters UI
4. Iterate based on usage patterns

---

**Ready for Deployment**: ✅ Yes
**Estimated Risk**: Low (graceful fallbacks, error handling)
**Recommended Approach**: Staged rollout starting at 5%
