package com.arjun.octokit

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.arjun.octokit.model.GithubResponseItem
import com.arjun.octokit.repository.GithubRepository
import kotlinx.coroutines.flow.Flow

class GithubViewModel @ViewModelInject constructor(
    private val githubRepository: GithubRepository
) : ViewModel() {

    fun fetchRepo(): Flow<PagingData<GithubResponseItem>> =
        githubRepository.resultStream()
            .cachedIn(viewModelScope)

}