package io.horizontalsystems.bankwallet.modules.explorer3

import io.horizontalsystems.bankwallet.core.managers.TransactionAdapterManager
import io.horizontalsystems.marketkit.models.BlockchainType

/**
 * Generates block explorer URLs for addresses, transactions, and blocks
 */
class ExplorerUrlGenerator(
    private val transactionAdapterManager: TransactionAdapterManager
) {
    
    /**
     * Generate explorer URL for an address
     */
    fun getAddressUrl(address: String, blockchainType: BlockchainType): String? {
        // Try to get URL from adapter if available
        val adapter = transactionAdapterManager.adaptersMap.entries.firstOrNull { 
            it.key.blockchain.type == blockchainType 
        }?.value
        
        // For now, use standard explorer URLs based on blockchain type
        return when (blockchainType) {
            BlockchainType.Bitcoin -> "https://blockstream.info/address/$address"
            BlockchainType.Ethereum -> "https://etherscan.io/address/$address"
            BlockchainType.BinanceSmartChain -> "https://bscscan.com/address/$address"
            BlockchainType.Polygon -> "https://polygonscan.com/address/$address"
            BlockchainType.Avalanche -> "https://snowtrace.io/address/$address"
            BlockchainType.Optimism -> "https://optimistic.etherscan.io/address/$address"
            BlockchainType.Base -> "https://basescan.org/address/$address"
            BlockchainType.ArbitrumOne -> "https://arbiscan.io/address/$address"
            BlockchainType.Tron -> "https://tronscan.org/#/address/$address"
            BlockchainType.Solana -> "https://solscan.io/account/$address"
            BlockchainType.Ton -> "https://tonscan.org/address/$address"
            BlockchainType.Litecoin -> "https://blockchair.com/litecoin/address/$address"
            BlockchainType.BitcoinCash -> "https://blockchair.com/bitcoin-cash/address/$address"
            BlockchainType.Dash -> "https://blockchair.com/dash/address/$address"
            BlockchainType.Zcash -> "https://explorer.zcha.in/accounts/$address"
            else -> null
        }
    }
    
    /**
     * Generate explorer URL for a transaction hash
     */
    fun getTransactionUrl(hash: String, blockchainType: BlockchainType): String? {
        // Try to get URL from adapter if available
        val adapter = transactionAdapterManager.adaptersMap.entries.firstOrNull { 
            it.key.blockchain.type == blockchainType 
        }?.value
        
        adapter?.let {
            try {
                return it.getTransactionUrl(hash)
            } catch (e: Exception) {
                // Fall through to default URLs
            }
        }
        
        // Use standard explorer URLs based on blockchain type
        return when (blockchainType) {
            BlockchainType.Bitcoin -> "https://blockstream.info/tx/$hash"
            BlockchainType.Ethereum -> "https://etherscan.io/tx/$hash"
            BlockchainType.BinanceSmartChain -> "https://bscscan.com/tx/$hash"
            BlockchainType.Polygon -> "https://polygonscan.com/tx/$hash"
            BlockchainType.Avalanche -> "https://snowtrace.io/tx/$hash"
            BlockchainType.Optimism -> "https://optimistic.etherscan.io/tx/$hash"
            BlockchainType.Base -> "https://basescan.org/tx/$hash"
            BlockchainType.ArbitrumOne -> "https://arbiscan.io/tx/$hash"
            BlockchainType.Tron -> "https://tronscan.org/#/transaction/$hash"
            BlockchainType.Solana -> "https://solscan.io/tx/$hash"
            BlockchainType.Ton -> "https://tonscan.org/tx/$hash"
            BlockchainType.Litecoin -> "https://blockchair.com/litecoin/transaction/$hash"
            BlockchainType.BitcoinCash -> "https://blockchair.com/bitcoin-cash/transaction/$hash"
            BlockchainType.Dash -> "https://blockchair.com/dash/transaction/$hash"
            BlockchainType.Zcash -> "https://explorer.zcha.in/transactions/$hash"
            else -> null
        }
    }
    
    /**
     * Generate explorer URL for a block number
     */
    fun getBlockUrl(blockNumber: Long, blockchainType: BlockchainType): String? {
        return when (blockchainType) {
            BlockchainType.Bitcoin -> "https://blockstream.info/block-height/$blockNumber"
            BlockchainType.Ethereum -> "https://etherscan.io/block/$blockNumber"
            BlockchainType.BinanceSmartChain -> "https://bscscan.com/block/$blockNumber"
            BlockchainType.Polygon -> "https://polygonscan.com/block/$blockNumber"
            BlockchainType.Avalanche -> "https://snowtrace.io/block/$blockNumber"
            BlockchainType.Optimism -> "https://optimistic.etherscan.io/block/$blockNumber"
            BlockchainType.Base -> "https://basescan.org/block/$blockNumber"
            BlockchainType.ArbitrumOne -> "https://arbiscan.io/block/$blockNumber"
            BlockchainType.Tron -> "https://tronscan.org/#/block/$blockNumber"
            BlockchainType.Solana -> "https://solscan.io/block/$blockNumber"
            BlockchainType.Ton -> "https://tonscan.org/block/$blockNumber"
            BlockchainType.Litecoin -> "https://blockchair.com/litecoin/block/$blockNumber"
            BlockchainType.BitcoinCash -> "https://blockchair.com/bitcoin-cash/block/$blockNumber"
            BlockchainType.Dash -> "https://blockchair.com/dash/block/$blockNumber"
            BlockchainType.Zcash -> "https://explorer.zcha.in/blocks/$blockNumber"
            else -> null
        }
    }
}
