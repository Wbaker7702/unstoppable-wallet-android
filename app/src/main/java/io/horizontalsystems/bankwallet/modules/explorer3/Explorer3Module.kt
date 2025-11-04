package io.horizontalsystems.bankwallet.modules.explorer3

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.horizontalsystems.bankwallet.core.App
import io.horizontalsystems.marketkit.models.BlockchainType
import javax.annotation.concurrent.Immutable

object Explorer3Module {

    class Factory : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return Explorer3ViewModel(
                Explorer3Service(
                    App.transactionAdapterManager,
                    App.marketKit,
                    App.localStorage,
                    io.horizontalsystems.bankwallet.modules.address.AddressHandlerFactory(App.appConfigProvider.udnApiKey)
                )
            ) as T
        }
    }

    @Immutable
    sealed class SearchResult {
        abstract val title: String
        abstract val subtitle: String?
        abstract val explorerUrl: String?

        data class TransactionResult(
            val hash: String,
            val blockchainType: BlockchainType,
            val timestamp: Long?,
            override val explorerUrl: String?,
            val recordUid: String? = null // UID from TransactionRecord if found in user's wallet
        ) : SearchResult() {
            override val title = hash
            override val subtitle = "Transaction on ${blockchainType.name}"
        }

        data class AddressResult(
            val address: String,
            val blockchainType: BlockchainType,
            val balance: String?,
            val transactionCount: Int?,
            override val explorerUrl: String?
        ) : SearchResult() {
            override val title = address
            override val subtitle = balance ?: "Address on ${blockchainType.name}"
        }

        data class BlockResult(
            val blockNumber: Long,
            val blockchainType: BlockchainType,
            val timestamp: Long?,
            val transactionCount: Int?,
            override val explorerUrl: String?
        ) : SearchResult() {
            override val title = "Block #$blockNumber"
            override val subtitle = "Block on ${blockchainType.name}"
        }

        data class TokenResult(
            val coinUid: String,
            val symbol: String,
            val name: String,
            val blockchainType: BlockchainType,
            override val explorerUrl: String?
        ) : SearchResult() {
            override val title = symbol
            override val subtitle = name
        }

        data class ContractResult(
            val address: String,
            val blockchainType: BlockchainType,
            val name: String?,
            val isVerified: Boolean?,
            override val explorerUrl: String?
        ) : SearchResult() {
            override val title = address
            override val subtitle = name ?: "Contract on ${blockchainType.name}"
        }
    }

    data class SearchFilters(
        val blockchainType: BlockchainType? = null,
        val resultTypes: Set<ResultType> = ResultType.values().toSet()
    )

    enum class ResultType {
        Transaction,
        Address,
        Block,
        Token,
        Contract
    }
}
