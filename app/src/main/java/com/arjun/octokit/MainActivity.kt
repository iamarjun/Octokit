package com.arjun.octokit

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arjun.octokit.model.GithubResponseItem
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var searchJob: Job? = null
    private val viewModel: GithubViewModel by viewModels()
    private val githubAdapter: GithubAdapter by lazy {
        GithubAdapter(object : Interaction {
            override fun onItemSelected(position: Int, item: GithubResponseItem) {
                Timber.d("${item.id} at $position")
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        list.apply {
            layoutManager = LinearLayoutManager(this@MainActivity, RecyclerView.VERTICAL, false)
            addItemDecoration(DividerItemDecoration(this@MainActivity, RecyclerView.HORIZONTAL))
            adapter = githubAdapter
        }

        githubAdapter.withLoadStateHeaderAndFooter(
            header = LoadStateAdapter { githubAdapter.retry() },
            footer = LoadStateAdapter { githubAdapter.retry() }
        )

        githubAdapter.addLoadStateListener { loadState ->
            // Only show the list if refresh succeeds.
            // Show loading spinner during initial load or refresh.
            progress_bar.isVisible = loadState.source.refresh is LoadState.Loading
            // Show the retry state if initial load or refresh fails.
            retry_button.isVisible = loadState.source.refresh is LoadState.Error

            // Toast on any error, regardless of whether it came from RemoteMediator or PagingSource
            val errorState = loadState.source.append as? LoadState.Error
                ?: loadState.source.prepend as? LoadState.Error
                ?: loadState.append as? LoadState.Error
                ?: loadState.prepend as? LoadState.Error
            errorState?.let {
                Toast.makeText(
                    this,
                    "\uD83D\uDE28 Wooops ${it.error}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        searchJob = lifecycleScope.launch {
            viewModel.fetchRepo().collectLatest {
                githubAdapter.submitData(it)
            }
        }

    }
}