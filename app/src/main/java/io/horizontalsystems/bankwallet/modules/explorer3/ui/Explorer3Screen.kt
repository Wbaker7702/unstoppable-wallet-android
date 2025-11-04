package io.horizontalsystems.bankwallet.modules.explorer3.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import io.horizontalsystems.bankwallet.R
import io.horizontalsystems.bankwallet.core.slideFromRight
import io.horizontalsystems.bankwallet.modules.coin.CoinFragment
import io.horizontalsystems.bankwallet.modules.explorer3.Explorer3ViewModel
import io.horizontalsystems.bankwallet.modules.explorer3.Explorer3Module.SearchResult
import io.horizontalsystems.bankwallet.ui.compose.ComposeAppTheme
import io.horizontalsystems.bankwallet.ui.compose.components.HSCircularProgressIndicator
import io.horizontalsystems.bankwallet.ui.compose.components.ListEmptyView
import io.horizontalsystems.bankwallet.ui.compose.components.SearchBar

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun Explorer3Screen(
    viewModel: Explorer3ViewModel,
    navController: NavController,
    transactionsViewModel: io.horizontalsystems.bankwallet.modules.transactions.TransactionsViewModel? = null
) {
    val focusRequester = remember { FocusRequester() }
    val uiState = viewModel.uiState
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Column(
        modifier = Modifier.background(ComposeAppTheme.colors.tyler)
    ) {
        SearchBar(
            title = stringResource(R.string.Explorer3_Title),
            searchHintText = stringResource(R.string.Explorer3_SearchHint),
            searchOnlyMode = true,
            searchModeInitial = true,
            focusRequester = focusRequester,
            onClose = { navController.popBackStack() },
            onSearchTextChanged = { query -> viewModel.search(query) }
        )

        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    HSCircularProgressIndicator()
                }
            }
            
            uiState.error != null -> {
                ListEmptyView(
                    text = uiState.error ?: stringResource(R.string.Explorer3_NoResults),
                    icon = R.drawable.ic_not_found
                )
            }
            
            uiState.results.isEmpty() && uiState.query.isNotBlank() -> {
                ListEmptyView(
                    text = stringResource(R.string.Explorer3_NoResults),
                    icon = R.drawable.ic_not_found
                )
            }
            
            uiState.results.isEmpty() -> {
                // Show recent searches or empty state when no query
                // TODO: Show recent searches
            }
            
            else -> {
                Explorer3Results(
                    results = uiState.results,
                    onResultClick = { result ->
                        when (result) {
                            is SearchResult.TokenResult -> {
                                navController.slideFromRight(
                                    R.id.coinFragment,
                                    CoinFragment.Input(result.coinUid)
                                )
                            }
                            else -> {
                                Explorer3Navigation.handleResultClick(
                                    result, 
                                    navController, 
                                    context,
                                    getTransactionItem = transactionsViewModel?.let { vm ->
                                        { recordUid -> vm.getTransactionItem(recordUid) }
                                    },
                                    setTransactionItemToShow = transactionsViewModel?.let { vm ->
                                        { item -> vm.tmpItemToShow = item }
                                    }
                                )
                            }
                        }
                    }
                )
            }
        }
    }
}
