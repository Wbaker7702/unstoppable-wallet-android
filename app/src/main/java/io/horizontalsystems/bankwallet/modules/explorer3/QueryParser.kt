package io.horizontalsystems.bankwallet.modules.explorer3

import io.horizontalsystems.marketkit.models.BlockchainType

object QueryParser {
    
    /**
     * Determines the likely type of search query
     */
    fun parseQuery(query: String): QueryType {
        val trimmed = query.trim()
        
        when {
            // Check if it's a block number (pure numeric, reasonable range)
            trimmed.matches(Regex("^\\d+$")) -> {
                val blockNum = trimmed.toLongOrNull()
                if (blockNum != null && blockNum > 0 && blockNum < Long.MAX_VALUE) {
                    return QueryType.BlockNumber(blockNum)
                }
            }
            
            // Check if it's a transaction hash (hex string, typical lengths)
            trimmed.matches(Regex("^0x[a-fA-F0-9]+$")) -> {
                val hash = trimmed.removePrefix("0x")
                when (hash.length) {
                    64 -> return QueryType.TransactionHash(trimmed) // Ethereum-style
                    66 -> return QueryType.TransactionHash(trimmed) // Some EVM chains
                }
            }
            
            // Bitcoin-style transaction hash (64 hex chars, no 0x prefix)
            trimmed.matches(Regex("^[a-fA-F0-9]{64}$")) -> {
                return QueryType.TransactionHash(trimmed)
            }
            
            // Check if it looks like an address (starts with specific prefixes or patterns)
            isAddressLike(trimmed) -> {
                return QueryType.Address(trimmed)
            }
        }
        
        // Default: treat as token/contract search
        return QueryType.TokenOrContract(trimmed)
    }
    
    private fun isAddressLike(value: String): Boolean {
        // Bitcoin addresses (Base58): 26-35 chars, start with 1, 3, or bc1
        if (value.matches(Regex("^[13][a-km-zA-HJ-NP-Z1-9]{25,34}$")) ||
            value.startsWith("bc1") ||
            value.startsWith("ltc1") ||
            value.startsWith("zcc") ||
            value.startsWith("zs1")) {
            return true
        }
        
        // Ethereum addresses (0x + 40 hex chars)
        if (value.matches(Regex("^0x[a-fA-F0-9]{40}$"))) {
            return true
        }
        
        // Tron addresses (T + Base58, 34 chars)
        if (value.matches(Regex("^T[A-Za-z1-9]{33}$"))) {
            return true
        }
        
        // Solana addresses (Base58, 32-44 chars)
        if (value.matches(Regex("^[1-9A-HJ-NP-Za-km-z]{32,44}$")) && value.length in 32..44) {
            return true
        }
        
        // TON addresses (various formats)
        if (value.startsWith("EQ") || value.startsWith("UQ") || value.startsWith("0:")) {
            return true
        }
        
        return false
    }
    
    sealed class QueryType {
        data class TransactionHash(val hash: String) : QueryType()
        data class Address(val address: String) : QueryType()
        data class BlockNumber(val number: Long) : QueryType()
        data class TokenOrContract(val query: String) : QueryType()
    }
}
