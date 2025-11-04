# Explorer3 Quick Start Guide

## Overview
Explorer3 is a unified search and block explorer feature that allows users to search across transactions, addresses, blocks, tokens, and contracts across all supported blockchains.

## Project Structure

```
modules/explorer3/
├── Explorer3Module.kt          # Data models, ViewModel factory
├── Explorer3ViewModel.kt        # State management
├── Explorer3Service.kt          # Search logic
├── Explorer3Fragment.kt         # Fragment wrapper
└── ui/
    ├── Explorer3Screen.kt       # Main Compose UI
    ├── SearchResults.kt         # Results display
    └── SearchResultItems.kt     # Individual result items
```

## Key Components

### 1. Explorer3Module.kt
- `SearchResult` sealed class (Transaction, Address, Block, Token, Contract)
- `SearchFilters` data class
- ViewModel Factory

### 2. Explorer3Service.kt
- Coordinates searches across blockchain adapters
- Integrates with MarketKit for token searches
- Parses queries (hash vs address vs block number)
- Caches results

### 3. Explorer3ViewModel.kt
- Manages search state (query, results, loading, error)
- Handles user interactions
- Maintains search history

### 4. Explorer3Screen.kt
- Compose UI with search bar
- Filter chips
- Results list grouped by type
- Loading/error/empty states

## Implementation Order

1. **Create module structure** - Basic classes and data models
2. **Implement core search** - Transaction/address/block lookups
3. **Build UI** - Compose screen and components
4. **Integrate navigation** - Add to navigation graph
5. **Add enhancements** - History, filters, optimizations

## Key Dependencies

- `MarketKitWrapper` - Token searches
- `ITransactionsAdapter` - Transaction lookups
- `IReceiveAdapter` - Address validation
- Existing `SearchBar` component
- Navigation Component

## Navigation

Add to `main_graph.xml`:
```xml
<fragment
    android:id="@+id/explorer3Fragment"
    android:name="io.horizontalsystems.bankwallet.modules.explorer3.Explorer3Fragment"/>
```

Navigate using:
```kotlin
navController.slideFromRight(R.id.explorer3Fragment)
```

## See Full Plan

See `EXPLORER3_BUILD_PLAN.md` for complete implementation details, architecture, and testing considerations.
