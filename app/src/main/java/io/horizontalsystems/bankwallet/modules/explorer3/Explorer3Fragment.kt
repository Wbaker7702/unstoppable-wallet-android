package io.horizontalsystems.bankwallet.modules.explorer3

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.navGraphViewModels
import io.horizontalsystems.bankwallet.R
import io.horizontalsystems.bankwallet.core.BaseComposeFragment
import io.horizontalsystems.bankwallet.modules.transactions.TransactionsModule
import io.horizontalsystems.bankwallet.modules.transactions.TransactionsViewModel

class Explorer3Fragment : BaseComposeFragment() {
    @Composable
    override fun GetContent(navController: NavController) {
        val viewModel = viewModel<Explorer3ViewModel>(
            factory = Explorer3Module.Factory()
        )
        
        // Try to get TransactionsViewModel for transaction navigation
        val transactionsViewModel: TransactionsViewModel? = try {
            navGraphViewModels<TransactionsViewModel>(R.id.mainFragment) { 
                TransactionsModule.Factory() 
            }.value
        } catch (e: IllegalStateException) {
            // Not available in this navigation scope
            null
        }
        
        Explorer3Screen(
            viewModel = viewModel,
            navController = navController,
            transactionsViewModel = transactionsViewModel
        )
    }
}
