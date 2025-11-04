# Explorer3 Build Plan

## Overview
Build a unified search and block explorer feature ("Explorer3") that allows users to search across transactions, addresses, blocks, tokens, and contracts across all supported blockchains.

## Architecture

### Module Structure
```
modules/explorer3/
├── Explorer3Module.kt          # Module definition, factory, data classes
├── Explorer3ViewModel.kt        # ViewModel managing UI state
├── Explorer3Service.kt          # Core search service
├── Explorer3Fragment.kt         # Fragment/Compose screen
└── ui/
    ├── Explorer3Screen.kt       # Main Compose screen
    ├── SearchResults.kt         # Results display components
    └── SearchFilters.kt         # Filter UI components
```

## Components Breakdown

### 1. Explorer3Module.kt
**Purpose**: Define module structure, data models, and ViewModel factory

**Key Data Classes**:
- `SearchResult` (sealed class):
  - `TransactionResult(hash, blockchain, timestamp, url)`
  - `AddressResult(address, blockchain, balance, url)`
  - `BlockResult(blockNumber, blockchain, timestamp, url)`
  - `TokenResult(token, blockchain, symbol, url)`
  - `ContractResult(address, blockchain, name, url)`
- `SearchQuery` (data class with query string, filters, blockchain)
- `SearchState` (loading, results, error)

**Factory**: Creates ViewModel with dependencies (adapters, services, managers)

### 2. Explorer3Service.kt
**Purpose**: Core search logic coordinating multiple data sources

**Responsibilities**:
- Parse search query and determine search type (hash, address, block number)
- Coordinate searches across blockchain adapters
- Integrate with MarketKit for token searches
- Query block explorer APIs for external data
- Aggregate and rank results
- Cache recent searches

**Dependencies**:
- `MarketKitWrapper` (for token searches) - via `App.marketKit`
- `TransactionAdapterManager` (for transaction lookups) - via `App.transactionAdapterManager`
- `AdapterManager` (for address validation) - via `App.adapterManager`
- `LocalStorage` (for search history) - via `App.localStorage`
- Block explorer API clients (optional, for external data)
- Note: Transactions are queried from adapters in real-time, not from database

**Key Methods**:
- `search(query: String, filters: SearchFilters): Flow<List<SearchResult>>`
- `searchTransaction(hash: String, blockchain: BlockchainType?): Single<TransactionResult?>`
  - Queries adapters via `TransactionAdapterManager` for active wallets
  - Falls back to external block explorer APIs if not found locally
- `searchAddress(address: String, blockchain: BlockchainType?): Single<AddressResult?>`
  - Validates address format for different blockchains
  - Queries adapters if wallet exists for that blockchain
  - Falls back to external APIs for balance/transaction count
- `searchBlock(blockNumber: Long, blockchain: BlockchainType): Single<BlockResult?>`
  - Uses external block explorer APIs (adapters don't support block lookups)
- `searchToken(symbol: String): Single<List<TokenResult>>`
  - Uses `MarketKitWrapper.fullCoins()` similar to MarketSearch

### 3. Explorer3ViewModel.kt
**Purpose**: Manage UI state and handle user interactions

**State Management**:
- `UiState`:
  - `query: String`
  - `results: List<SearchResult>`
  - `isLoading: Boolean`
  - `error: String?`
  - `selectedFilters: SearchFilters`
  - `searchHistory: List<String>`

**Key Methods**:
- `search(query: String)`
- `clearSearch()`
- `selectResult(result: SearchResult)`
- `applyFilters(filters: SearchFilters)`
- `loadSearchHistory()`
- `addToHistory(query: String)`

### 4. Explorer3Screen.kt (Compose)
**Purpose**: Main UI screen for Explorer3

**UI Components**:
- Search bar (reuse existing `SearchBar` component)
- Filter chips (Blockchain, Result Type)
- Results list with sections:
  - Recent searches
  - Quick actions (if query looks like hash/address)
  - Search results grouped by type
- Empty state
- Loading state
- Error state

**Compose Structure**:
```kotlin
@Composable
fun Explorer3Screen(
    viewModel: Explorer3ViewModel,
    onNavigateToTransaction: (String, BlockchainType) -> Unit,
    onNavigateToAddress: (String, BlockchainType) -> Unit,
    onNavigateToBlock: (Long, BlockchainType) -> Unit,
    onNavigateToToken: (String) -> Unit,
    onClose: () -> Unit
)
```

### 5. Search Result Components
**Purpose**: Display different types of search results

**Components**:
- `TransactionResultItem`: Shows tx hash, status, amount, timestamp
- `AddressResultItem`: Shows address, balance, transaction count
- `BlockResultItem`: Shows block number, timestamp, transaction count
- `TokenResultItem`: Shows token symbol, name, price (reuse from MarketSearch)
- `ContractResultItem`: Shows contract address, name, verification status

### 6. Integration Points

**Blockchain Adapters**:
- Extend `ITransactionsAdapter` interface usage
- Use `getTransactionUrl()` for explorer links
- Add address lookup methods if not present
- Integrate with existing adapters:
  - `BitcoinAdapter`
  - `EvmTransactionsAdapter` (Ethereum, BSC, etc.)
  - `SolanaTransactionsAdapter`
  - `TonTransactionsAdapter`
  - `TronTransactionsAdapter`

**Block Explorer APIs** (Optional):
- Etherscan API (for Ethereum)
- BscScan API (for BSC)
- Blockchain.info API (for Bitcoin)
- Solscan API (for Solana)
- TronScan API (for Tron)

**Navigation**:
- Transaction details: `TransactionInfoFragment`
- Address details: New screen or extend existing
- Block details: New screen
- Token details: `CoinOverviewFragment`

## Implementation Steps

### Phase 1: Foundation
1. Create module structure and basic classes
2. Implement `Explorer3Module` with data classes
3. Create `Explorer3Service` skeleton
4. Set up `Explorer3ViewModel` with basic state management

### Phase 2: Core Search Logic
1. Implement transaction hash search
2. Implement address search
3. Implement block number search
4. Integrate token search from MarketKit
5. Add query parsing (detect hash vs address vs block number)

### Phase 3: UI Implementation
1. Create `Explorer3Screen` Compose UI
2. Implement search bar integration
3. Create result item components
4. Add filter UI
5. Implement loading/error/empty states

### Phase 4: Integration
1. Wire up navigation routes
2. Integrate with blockchain adapters
3. Add explorer URL generation
4. Connect to transaction/address detail screens
5. Add search history persistence

### Phase 5: Enhancement
1. Add block explorer API integration (optional)
2. Implement result ranking algorithm
3. Add multi-chain search support
4. Optimize search performance (debouncing, caching)
5. Add analytics/statistics tracking

## Dependencies

**Existing**:
- `MarketKitWrapper` - Token searches
- `ITransactionsAdapter` - Transaction lookups
- `IReceiveAdapter` - Address validation
- `MarketSearchService` - Token search patterns
- `SearchBar` component - UI reuse

**New** (if needed):
- Block explorer API clients (optional)
- Search history storage (extend `LocalStorage`)
- Result caching (in-memory or persistent)

## Testing Considerations

1. Unit tests for `Explorer3Service` search logic
2. Unit tests for query parsing
3. UI tests for `Explorer3Screen`
4. Integration tests with blockchain adapters
5. Mock block explorer API responses

## Future Enhancements

1. Search suggestions/autocomplete
2. Advanced filters (date range, amount range)
3. Saved searches
4. Search analytics dashboard
5. Multi-query search (batch lookups)
6. QR code scanning integration
7. Deep linking support for search results

## Files to Create

```
app/src/main/java/io/horizontalsystems/bankwallet/modules/explorer3/
├── Explorer3Module.kt
├── Explorer3ViewModel.kt
├── Explorer3Service.kt
├── Explorer3Fragment.kt
└── ui/
    ├── Explorer3Screen.kt
    ├── SearchResults.kt
    ├── SearchResultItems.kt
    └── SearchFilters.kt
```

## Files to Modify

- `app/src/main/res/navigation/main_graph.xml` - Add Explorer3 fragment entry:
  ```xml
  <fragment
      android:id="@+id/explorer3Fragment"
      android:name="io.horizontalsystems.bankwallet.modules.explorer3.Explorer3Fragment"/>
  ```
- `NavController.kt` - Add navigation helper methods if needed
- `MainActivity.kt` - Add navigation handling if required
- `app/src/main/res/values/strings.xml` - Add Explorer3 strings:
  - `Explorer3_Title` - "Explorer"
  - `Explorer3_SearchHint` - "Search transactions, addresses, blocks..."
  - `Explorer3_NoResults` - "No results found"
  - `Explorer3_RecentSearches` - "Recent Searches"
  - `Explorer3_Transactions` - "Transactions"
  - `Explorer3_Addresses` - "Addresses"
  - `Explorer3_Blocks` - "Blocks"
  - `Explorer3_Tokens` - "Tokens"
  - `Explorer3_Contracts` - "Contracts"
- `LocalStorage.kt` (if exists) - Add search history storage keys
- Main navigation menu (if Explorer3 should be accessible from main menu)

## Success Criteria

1. Users can search for transactions by hash
2. Users can search for addresses
3. Users can search for blocks by number
4. Users can search for tokens
5. Results are displayed in organized, scannable format
6. Navigation to detail screens works correctly
7. Search works across all supported blockchains
8. Performance is acceptable (< 2s for most searches)
9. Error states are handled gracefully
10. UI follows app design patterns
