package com.mvhousedeveloper.tiketdotcomtest.viewModel

import androidx.lifecycle.*
import com.mvhousedeveloper.tiketdotcomtest.data.GithubRepository
import com.mvhousedeveloper.tiketdotcomtest.model.SearchResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(private val repository: GithubRepository) : ViewModel() {

    companion object {
        private const val VISIBLE_THRESHOLD = 3
    }

    private val queryLiveData = MutableLiveData<String>()
    var repoResult: LiveData<SearchResult> = queryLiveData.switchMap { queryString ->
        liveData {
            if(queryString.trim().isNotEmpty())
            {
                val repos = repository.getSearchResultStream(queryString).asLiveData(Dispatchers.Main)
                emitSource(repos)
            }
        }
    }

    fun searchUser(queryString: String) {
        queryLiveData.postValue(queryString)
    }

    fun listScrolled(visibleItemCount: Int, lastVisibleItemPosition: Int, totalItemCount: Int) {
        if (visibleItemCount + lastVisibleItemPosition + VISIBLE_THRESHOLD >= totalItemCount) {
            val immutableQuery = queryLiveData.value
            if (immutableQuery != null) {
                viewModelScope.launch {
                    repository.requestMore(immutableQuery)
                }
            }
        }
    }
}