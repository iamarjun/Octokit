package com.arjun.octokit

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.arjun.octokit.model.GithubResponseItem
import kotlinx.android.synthetic.main.recipe_item.view.*

class ListViewHolder(
    itemView: View,
    private val interaction: Interaction?
) :
    RecyclerView.ViewHolder(itemView) {

    private var title: AppCompatTextView = itemView.recipe_title
    private var publisher: AppCompatTextView = itemView.recipe_publisher
    private var socialScore: AppCompatTextView = itemView.recipe_social_rating

    fun bind(item: GithubResponseItem?) {
        item?.let { githubResponseItem ->
            itemView.setOnClickListener {
                interaction?.onItemSelected(absoluteAdapterPosition, githubResponseItem)
            }

            title.text = githubResponseItem.fullName
            publisher.text = githubResponseItem.description
            socialScore.text = githubResponseItem.openIssuesCount.toString()
        }
    }

    companion object {

        fun create(
            parent: ViewGroup,
            interaction: Interaction?
        ): ListViewHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.recipe_item, parent, false)
            return ListViewHolder(view, interaction)
        }
    }
}