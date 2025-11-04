package io.horizontalsystems.bankwallet.modules.explorer3.ui

import android.content.Context
import androidx.navigation.NavController
import io.horizontalsystems.bankwallet.modules.explorer3.Explorer3Module.SearchResult
import io.horizontalsystems.bankwallet.ui.helpers.LinkHelper

/**
 * Handles navigation from Explorer3 search results to detail screens
 * 
 * For transactions found in user's wallet, this will try to navigate to TransactionInfoFragment.
 * For all other results (addresses, blocks, contracts) or external transactions,
 * this will open the block explorer URL in the browser.
 */
object Explorer3Navigation {
    
    /**
     * Handle click on a search result
     * @param result The search result that was clicked
     * @param navController Navigation controller for fragment navigation
     * @param context Android context for opening URLs
     * @param getTransactionItem Optional function to get TransactionItem by recordUid for wallet transactions
     * @param setTransactionItemToShow Optional function to set the transaction item before navigating
     */
    fun handleResultClick(
        result: SearchResult,
        navController: NavController,
        context: Context,
        getTransactionItem: ((String) -> Any?)? = null,
        setTransactionItemToShow: ((Any?) -> Unit)? = null
    ) {
        when (result) {
            is SearchResult.TransactionResult -> {
                handleTransactionClick(
                    result, 
                    navController, 
                    context,
                    getTransactionItem,
                    setTransactionItemToShow
                )
            }
            is SearchResult.AddressResult -> {
                handleAddressClick(result, context)
            }
            is SearchResult.BlockResult -> {
                handleBlockClick(result, context)
            }
            is SearchResult.ContractResult -> {
                handleContractClick(result, context)
            }
            is SearchResult.TokenResult -> {
                // Token navigation is handled in Explorer3Screen
            }
        }
    }
    
    private fun handleTransactionClick(
        result: SearchResult.TransactionResult,
        navController: NavController,
        context: Context,
        getTransactionItem: ((String) -> Any?)?,
        setTransactionItemToShow: ((Any?) -> Unit)?
    ) {
        // Try to navigate to TransactionInfoFragment if transaction is in user's wallet
        result.recordUid?.let { recordUid ->
            if (getTransactionItem != null && setTransactionItemToShow != null) {
                try {
                    val transactionItem = getTransactionItem(recordUid)
                    transactionItem?.let {
                        setTransactionItemToShow(it)
                        navController.slideFromBottom(io.horizontalsystems.bankwallet.R.id.transactionInfoFragment)
                        return
                    }
                } catch (e: Exception) {
                    // Failed to get transaction item, fall through to open explorer URL
                }
            }
        }
        
        // If transaction not in wallet or navigation failed, open explorer URL
        result.explorerUrl?.let { url ->
            LinkHelper.openLinkInAppBrowser(context, url)
        }
    }
    
    private fun handleAddressClick(
        result: SearchResult.AddressResult,
        context: Context
    ) {
        result.explorerUrl?.let { url ->
            LinkHelper.openLinkInAppBrowser(context, url)
        }
    }
    
    private fun handleBlockClick(
        result: SearchResult.BlockResult,
        context: Context
    ) {
        result.explorerUrl?.let { url ->
            LinkHelper.openLinkInAppBrowser(context, url)
        }
    }
    
    private fun handleContractClick(
        result: SearchResult.ContractResult,
        context: Context
    ) {
        result.explorerUrl?.let { url ->
            LinkHelper.openLinkInAppBrowser(context, url)
        }
    }
}
