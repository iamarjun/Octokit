package com.arjun.octokit

import com.arjun.octokit.model.GithubResponseItem


interface Interaction {
    fun onItemSelected(position: Int, item: GithubResponseItem)
}
