package io.horizontalsystems.bankwallet.modules.explorer3

import io.horizontalsystems.bankwallet.core.ILocalStorage
import io.horizontalsystems.bankwallet.core.managers.MarketKitWrapper
import io.horizontalsystems.bankwallet.core.managers.TransactionAdapterManager
import io.horizontalsystems.bankwallet.modules.address.AddressHandlerFactory
import io.horizontalsystems.bankwallet.modules.explorer3.Explorer3Module.ResultType
import io.horizontalsystems.bankwallet.modules.explorer3.Explorer3Module.SearchFilters
import io.horizontalsystems.bankwallet.modules.explorer3.Explorer3Module.SearchResult
import io.horizontalsystems.bankwallet.modules.explorer3.QueryParser.QueryType
import io.horizontalsystems.bankwallet.modules.transactions.FilterTransactionType
import io.horizontalsystems.marketkit.models.BlockchainType
import io.reactivex.Single
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.reactive.await

class Explorer3Service(
    private val transactionAdapterManager: TransactionAdapterManager,
    private val marketKit: MarketKitWrapper,
    private val localStorage: ILocalStorage,
    private val addressHandlerFactory: AddressHandlerFactory
) {
    private val explorerUrlGenerator = ExplorerUrlGenerator(transactionAdapterManager)
    companion object {
        private const val SEARCH_HISTORY_KEY = "explorer3_search_history"
        private const val MAX_HISTORY_SIZE = 20
    }

    private val _stateFlow = MutableStateFlow(State())
    val stateFlow: StateFlow<State> = _stateFlow.asStateFlow()
    
    private val coroutineScope = CoroutineScope(Dispatchers.Default)
    private var searchJob: Job? = null

    data class State(
        val query: String = "",
        val results: List<SearchResult> = emptyList(),
        val isLoading: Boolean = false,
        val error: String? = null
    )

    fun setQuery(query: String) {
        searchJob?.cancel()
        
        _stateFlow.update { it.copy(query = query, results = emptyList(), error = null) }
        
        if (query.isBlank()) {
            return
        }

        _stateFlow.update { it.copy(isLoading = true) }
        
        // Parse query and determine search type
        val queryType = QueryParser.parseQuery(query)
        val filters = SearchFilters()
        
        searchJob = coroutineScope.launch {
            try {
                val results = performSearch(queryType, filters)
                _stateFlow.update {
                    it.copy(
                        results = results,
                        isLoading = false,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _stateFlow.update {
                    it.copy(
                        results = emptyList(),
                        isLoading = false,
                        error = e.message ?: "Search failed"
                    )
                }
            }
        }
    }

    private suspend fun performSearch(queryType: QueryType, filters: SearchFilters): List<SearchResult> {
        val results = mutableListOf<SearchResult>()
        
        when (queryType) {
            is QueryType.TransactionHash -> {
                // Search for transaction by hash
                val txResults = searchTransactionByHash(queryType.hash, null)
                results.addAll(txResults)
            }
            
            is QueryType.Address -> {
                // Search for address
                val addressResults = searchAddress(queryType.address, null)
                results.addAll(addressResults)
            }
            
            is QueryType.BlockNumber -> {
                // Search for block - generate result with explorer URL
                // Note: We don't validate if block exists, just provide navigation
                val blockResults = queryType.blockNumber.let { blockNum ->
                    // Try all common blockchains, user can refine later
                    listOf(
                        BlockchainType.Bitcoin,
                        BlockchainType.Ethereum,
                        BlockchainType.BinanceSmartChain
                    ).mapNotNull { blockchainType ->
                        val explorerUrl = explorerUrlGenerator.getBlockUrl(blockNum, blockchainType)
                        explorerUrl?.let {
                            SearchResult.BlockResult(
                                blockNumber = blockNum,
                                blockchainType = blockchainType,
                                timestamp = null,
                                transactionCount = null,
                                explorerUrl = it
                            )
                        }
                    }
                }
                results.addAll(blockResults)
            }
            
            is QueryType.TokenOrContract -> {
                // Search tokens/contracts
                if (ResultType.Token in filters.resultTypes) {
                    val tokens = marketKit.fullCoins(queryType.query, limit = 10)
                    results.addAll(
                        tokens.map { fullCoin ->
                            val blockchainType = fullCoin.coin.platforms.firstOrNull()?.blockchainType
                                ?: BlockchainType.Ethereum
                            // For tokens, we could use the token's contract address if available
                            // For now, generate a generic explorer URL based on blockchain
                            val explorerUrl = fullCoin.coin.platforms.firstOrNull()?.address?.let { address ->
                                explorerUrlGenerator.getAddressUrl(address, blockchainType)
                            }
                            
                            SearchResult.TokenResult(
                                coinUid = fullCoin.coin.uid,
                                symbol = fullCoin.coin.code,
                                name = fullCoin.coin.name,
                                blockchainType = blockchainType,
                                explorerUrl = explorerUrl
                            )
                        }
                    )
                }
            }
        }
        
        return results
    }
    
    private suspend fun searchTransactionByHash(hash: String, blockchainType: BlockchainType?): List<SearchResult.TransactionResult> {
        val results = mutableListOf<SearchResult.TransactionResult>()
        
        // Get all available adapters from TransactionAdapterManager
        val adapters = transactionAdapterManager.adaptersMap
        
        adapters.forEach { (source, adapter) ->
            // If blockchain filter is specified, skip non-matching blockchains
            if (blockchainType != null && source.blockchain.type != blockchainType) {
                return@forEach
            }
            
            try {
                // Try to find transaction by querying with hash
                // Note: Most adapters don't support direct hash lookup, so we query recent transactions
                val transactions = adapter.getTransactionsAsync(
                    from = null,
                    token = null,
                    limit = 100, // Check recent transactions
                    transactionType = FilterTransactionType.All,
                    address = null
                ).await()
                
                val matchingTx = transactions.find { 
                    it.transactionHash.equals(hash, ignoreCase = true) ||
                    it.transactionHash.removePrefix("0x").equals(hash.removePrefix("0x"), ignoreCase = true)
                }
                
                matchingTx?.let { tx ->
                    val explorerUrl = adapter.getTransactionUrl(tx.transactionHash)
                    results.add(
                        SearchResult.TransactionResult(
                            hash = tx.transactionHash,
                            blockchainType = source.blockchain.type,
                            timestamp = tx.timestamp,
                            explorerUrl = explorerUrl,
                            recordUid = tx.uid // Store the transaction record UID for navigation
                        )
                    )
                }
            } catch (e: Exception) {
                // Continue searching other adapters
            }
        }
        
        return results
    }
    
    private suspend fun searchAddress(address: String, blockchainType: BlockchainType?): List<SearchResult.AddressResult> {
        val results = mutableListOf<SearchResult.AddressResult>()
        
        // Get supported blockchain types
        val supportedBlockchains = if (blockchainType != null) {
            listOf(blockchainType)
        } else {
            // Try all supported blockchains
            listOf(
                BlockchainType.Bitcoin,
                BlockchainType.Ethereum,
                BlockchainType.BinanceSmartChain,
                BlockchainType.Polygon,
                BlockchainType.Avalanche,
                BlockchainType.Optimism,
                BlockchainType.Base,
                BlockchainType.ArbitrumOne,
                BlockchainType.Tron,
                BlockchainType.Solana,
                BlockchainType.Ton,
                BlockchainType.Litecoin,
                BlockchainType.BitcoinCash,
                BlockchainType.Dash,
                BlockchainType.Zcash
            )
        }
        
        // Create parser chain for address validation
        val parserChain = addressHandlerFactory.parserChain(
            blockchainTypes = supportedBlockchains,
            blockchainTypesWithEns = listOf(BlockchainType.Ethereum)
        )
        
        // Try to find a handler that supports this address
        val handler = parserChain.supportedHandler(address)
        handler?.let {
            try {
                val parsedAddress = handler.parseAddress(address)
                // Address is valid for this blockchain
                // TODO: Query balance and transaction count (requires external APIs or adapter queries)
                val explorerUrl = explorerUrlGenerator.getAddressUrl(
                    parsedAddress.hex,
                    handler.blockchainType
                )
                
                results.add(
                    SearchResult.AddressResult(
                        address = parsedAddress.hex,
                        blockchainType = handler.blockchainType,
                        balance = null, // TODO: Query balance
                        transactionCount = null, // TODO: Query transaction count
                        explorerUrl = explorerUrl
                    )
                )
            } catch (e: Exception) {
                // Failed to parse address
            }
        }
        
        return results
    }

    fun searchTransaction(hash: String, blockchainType: BlockchainType?): Single<SearchResult.TransactionResult?> {
        // TODO: Implement transaction search
        return Single.just(null)
    }

    fun searchAddress(address: String, blockchainType: BlockchainType?): Single<SearchResult.AddressResult?> {
        // TODO: Implement address search
        return Single.just(null)
    }

    fun searchBlock(blockNumber: Long, blockchainType: BlockchainType): Single<SearchResult.BlockResult?> {
        // TODO: Implement block search (requires external API)
        return Single.just(null)
    }

    fun getSearchHistory(): List<String> {
        // Use reflection or extension to access SharedPreferences
        // For now, we'll use a workaround with marketSearchRecentCoinUids pattern
        // TODO: Add proper extension to ILocalStorage or use SharedPreferences directly
        return emptyList() // Placeholder - will implement properly
    }

    fun addToHistory(query: String) {
        if (query.isBlank()) return
        // TODO: Implement search history persistence
    }
}
