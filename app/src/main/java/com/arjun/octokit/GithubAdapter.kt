package com.arjun.octokit

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.arjun.octokit.model.GithubResponseItem

class GithubAdapter(
    private val interaction: Interaction?
) :
    PagingDataAdapter<GithubResponseItem, RecyclerView.ViewHolder>(diffCallback) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ListViewHolder.create(parent, interaction)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            R.layout.github_item -> (holder as ListViewHolder).bind(getItem(position))
        }
    }


    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        val item = getItem(position)
        (holder as ListViewHolder).bind(item)
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.github_item
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<GithubResponseItem>() {

            override fun areItemsTheSame(
                oldItem: GithubResponseItem,
                newItem: GithubResponseItem
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: GithubResponseItem,
                newItem: GithubResponseItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}

