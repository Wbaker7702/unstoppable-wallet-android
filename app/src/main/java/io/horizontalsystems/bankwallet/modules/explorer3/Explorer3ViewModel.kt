package io.horizontalsystems.bankwallet.modules.explorer3

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.horizontalsystems.bankwallet.modules.explorer3.Explorer3Module.SearchFilters
import io.horizontalsystems.bankwallet.modules.explorer3.Explorer3Module.SearchResult
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class Explorer3ViewModel(
    private val explorer3Service: Explorer3Service
) : ViewModel() {

    private var searchState = explorer3Service.stateFlow.value

    var uiState by mutableStateOf(
        UiState(
            query = searchState.query,
            results = searchState.results,
            isLoading = searchState.isLoading,
            error = searchState.error,
            searchHistory = explorer3Service.getSearchHistory()
        )
    )
        private set

    init {
        viewModelScope.launch {
            explorer3Service.stateFlow.collect { state ->
                uiState = UiState(
                    query = state.query,
                    results = state.results,
                    isLoading = state.isLoading,
                    error = state.error,
                    searchHistory = explorer3Service.getSearchHistory()
                )
            }
        }
    }

    fun search(query: String) {
        explorer3Service.setQuery(query)
        if (query.isNotBlank()) {
            explorer3Service.addToHistory(query)
        }
    }

    fun clearSearch() {
        explorer3Service.setQuery("")
    }

    data class UiState(
        val query: String,
        val results: List<SearchResult>,
        val isLoading: Boolean,
        val error: String?,
        val searchHistory: List<String>
    )
}
