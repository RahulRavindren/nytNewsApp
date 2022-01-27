package com.pomeloassignment.android.feature.home.viewmodel

import androidx.annotation.MainThread
import androidx.lifecycle.viewModelScope
import com.pomeloassignment.android.base.BaseViewModel
import com.pomeloassignment.android.debounce
import com.pomeloassignment.android.db.ArticleEntity
import com.pomeloassignment.android.feature.home.repository.INewsRepository
import com.pomeloassignment.android.feature.home.repository.ISearchRepository
import com.pomeloassignment.android.feature.home.repository.SearchRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class HomeViewModel @Inject constructor(
    private val newsRepository: INewsRepository,
    private val searchRepository: ISearchRepository
) : BaseViewModel<HomeViewState>(HomeViewState.FetchArticlesState(isLoading = true)) {

    private val debounceOp =
        debounce<String>(waitMs = 500L, coroutineScope = viewModelScope) { result ->
            viewModelScope.launch {
                //print query in debounce
                search(result)
            }
        }

    @MainThread
    fun fetchNews() {
        viewModelScope.launch {
            newsRepository.fetchNews()
                .catch { error ->
                    //propogate error to UI
                    setState { HomeViewState.FetchArticlesState(error = error, isLoading = false) }
                }
                .collect { articles ->
                    setState {
                        HomeViewState.FetchArticlesState(
                            data = articles,
                            isLoading = false
                        )
                    }
                }

        }
    }

    @MainThread
    fun query(q: String) {
        debounceOp.invoke(q)
    }

    private fun search(q: String) {
        viewModelScope.launch {
            searchRepository.searchArticle(q)
                .catch { error ->
                    setState { HomeViewState.FetchArticlesState(error = error, isLoading = false) }
                }.collect {
                    setState { HomeViewState.FetchArticlesState(data = it, isLoading = false) }
                }
        }
    }
}


sealed class HomeViewState {
    data class FetchArticlesState(
        val data: List<ArticleEntity>? = null,
        val error: Throwable? = null,
        val isLoading: Boolean = false
    ) : HomeViewState()
}