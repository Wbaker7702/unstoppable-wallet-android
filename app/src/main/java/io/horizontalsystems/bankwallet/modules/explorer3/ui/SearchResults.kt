package io.horizontalsystems.bankwallet.modules.explorer3.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.horizontalsystems.bankwallet.R
import io.horizontalsystems.bankwallet.modules.explorer3.Explorer3Module.SearchResult
import io.horizontalsystems.bankwallet.ui.compose.components.HeaderStick

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Explorer3Results(
    results: List<SearchResult>,
    onResultClick: (SearchResult) -> Unit
) {
    if (results.isEmpty()) {
        return
    }

    // Group results by type
    val transactions = results.filterIsInstance<SearchResult.TransactionResult>()
    val addresses = results.filterIsInstance<SearchResult.AddressResult>()
    val blocks = results.filterIsInstance<SearchResult.BlockResult>()
    val tokens = results.filterIsInstance<SearchResult.TokenResult>()
    val contracts = results.filterIsInstance<SearchResult.ContractResult>()

    LazyColumn {
        if (transactions.isNotEmpty()) {
            stickyHeader {
                HeaderStick(
                    borderTop = true,
                    text = stringResource(R.string.Explorer3_Transactions)
                )
            }
            items(transactions) { result ->
                TransactionResultItem(result, onClick = { onResultClick(result) })
            }
        }

        if (addresses.isNotEmpty()) {
            stickyHeader {
                HeaderStick(
                    borderTop = transactions.isNotEmpty(),
                    text = stringResource(R.string.Explorer3_Addresses)
                )
            }
            items(addresses) { result ->
                AddressResultItem(result, onClick = { onResultClick(result) })
            }
        }

        if (blocks.isNotEmpty()) {
            stickyHeader {
                HeaderStick(
                    borderTop = transactions.isNotEmpty() || addresses.isNotEmpty(),
                    text = stringResource(R.string.Explorer3_Blocks)
                )
            }
            items(blocks) { result ->
                BlockResultItem(result, onClick = { onResultClick(result) })
            }
        }

        if (tokens.isNotEmpty()) {
            stickyHeader {
                HeaderStick(
                    borderTop = transactions.isNotEmpty() || addresses.isNotEmpty() || blocks.isNotEmpty(),
                    text = stringResource(R.string.Explorer3_Tokens)
                )
            }
            items(tokens) { result ->
                TokenResultItem(result, onClick = { onResultClick(result) })
            }
        }

        if (contracts.isNotEmpty()) {
            stickyHeader {
                HeaderStick(
                    borderTop = transactions.isNotEmpty() || addresses.isNotEmpty() || blocks.isNotEmpty() || tokens.isNotEmpty(),
                    text = stringResource(R.string.Explorer3_Contracts)
                )
            }
            items(contracts) { result ->
                ContractResultItem(result, onClick = { onResultClick(result) })
            }
        }

        item {
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
