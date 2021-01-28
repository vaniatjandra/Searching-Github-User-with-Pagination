package com.mvhousedeveloper.tiketdotcomtest.data

import com.mvhousedeveloper.tiketdotcomtest.api.GithubService
import com.mvhousedeveloper.tiketdotcomtest.model.SearchResult
import com.mvhousedeveloper.tiketdotcomtest.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import retrofit2.HttpException
import java.io.IOException

private const val GITHUB_STARTING_PAGE_INDEX = 1

class GithubRepository(private val service: GithubService) {

    private val inMemoryCache = mutableListOf<User>()

    private val searchResults = MutableSharedFlow<SearchResult>(replay = 1)

    private var lastRequestedPage = GITHUB_STARTING_PAGE_INDEX

    private var isRequestInProgress = false

    suspend fun getSearchResultStream(query: String): Flow<SearchResult> {
        lastRequestedPage = 1
        inMemoryCache.clear()
        requestAndSaveData(query)

        return searchResults
    }

    suspend fun requestMore(query: String) {
        if (isRequestInProgress) return
        val successful = requestAndSaveData(query)
        if (successful) {
            lastRequestedPage++
        }
    }

    private suspend fun requestAndSaveData(query: String): Boolean {
        isRequestInProgress = true
        var successful = false

        val apiQuery = query //+ IN_QUALIFIER
        try {
            val response = service.searchRepos(apiQuery, lastRequestedPage, ITEM_PAGE)
            val repos = response.items ?: emptyList()
            inMemoryCache.addAll(repos)
            val reposByName = getUserByName(query)
            searchResults.emit(SearchResult.Success(reposByName))
            successful = true
        } catch (exception: IOException) {
            searchResults.emit(SearchResult.Error(exception))
        } catch (exception: HttpException) {
            searchResults.emit(SearchResult.Error(exception))
        }
        isRequestInProgress = false
        return successful
    }

    private fun getUserByName(query: String): List<User> {
        return inMemoryCache.filter {
            it.login.contains(query, true) ||
                    (it.login != null && it.login.contains(query, true))
        }
    }

    companion object {
        private const val ITEM_PAGE = 100
    }
}
