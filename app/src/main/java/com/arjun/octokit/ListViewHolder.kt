package com.arjun.octokit

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arjun.octokit.databinding.GithubItemBinding
import com.arjun.octokit.model.GithubResponseItem

class ListViewHolder(
    itemView: View,
    private val interaction: Interaction?
) :
    RecyclerView.ViewHolder(itemView) {

    private val binding: GithubItemBinding = GithubItemBinding.bind(itemView)

    fun bind(item: GithubResponseItem?) {
        item?.let { githubResponseItem ->
            binding.apply {
                root.setOnClickListener {
                    interaction?.onItemSelected(absoluteAdapterPosition, githubResponseItem)
                }
                repoName.text = githubResponseItem.name
                repoDescription.text = githubResponseItem.description
                repoLicense.text = githubResponseItem.license?.name
                repoIssues.text = githubResponseItem.openIssuesCount.toString()
                repoForks.text = githubResponseItem.forksCount.toString()
                repoStars.text = githubResponseItem.stargazersCount.toString()
                repoPermission.text = """
                    Admin: ${githubResponseItem.permissions?.admin} 
                    Pull: ${githubResponseItem.permissions?.pull} 
                    Push: ${githubResponseItem.permissions?.push} 
                """.trimIndent()
            }
        }
    }

    companion object {

        fun create(
            parent: ViewGroup,
            interaction: Interaction?
        ): ListViewHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.github_item, parent, false)
            return ListViewHolder(view, interaction)
        }
    }
}