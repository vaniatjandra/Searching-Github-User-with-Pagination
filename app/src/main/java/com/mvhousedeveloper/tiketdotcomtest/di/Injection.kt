package com.mvhousedeveloper.tiketdotcomtest.di

import androidx.lifecycle.ViewModelProvider
import com.mvhousedeveloper.tiketdotcomtest.api.GithubService
import com.mvhousedeveloper.tiketdotcomtest.viewModel.ViewModelFactory
import com.mvhousedeveloper.tiketdotcomtest.data.GithubRepository

object Injection {

    private fun provideGithubRepository(): GithubRepository {
        return GithubRepository(GithubService.create())
    }

    fun provideViewModelFactory(): ViewModelProvider.Factory {
        return ViewModelFactory(provideGithubRepository())
    }
}
