package com.mellivora.compose_app.bean


data class GithubRepositoryBean(
    val allow_forking: String? = null,
    val archive_url: String? = null,
    val archived: String? = null,
    val assignees_url: String? = null,
    val blobs_url: String? = null,
    val branches_url: String? = null,
    val clone_url: String? = null,
    val collaborators_url: String? = null,
    val comments_url: String? = null,
    val commits_url: String? = null,
    val compare_url: String? = null,
    val contents_url: String? = null,
    val contributors_url: String? = null,
    val created_at: String? = null,
    val default_branch: String? = null,
    val deployments_url: String? = null,
    val description: String? = null,
    val disabled: String? = null,
    val downloads_url: String? = null,
    val events_url: String? = null,
    val fork: String? = null,
    val forks: String? = null,
    val forks_count: String? = null,
    val forks_url: String? = null,
    val full_name: String? = null,
    val git_commits_url: String? = null,
    val git_refs_url: String? = null,
    val git_tags_url: String? = null,
    val git_url: String? = null,
    val has_discussions: String? = null,
    val has_downloads: String? = null,
    val has_issues: String? = null,
    val has_pages: String? = null,
    val has_projects: String? = null,
    val has_wiki: String? = null,
    val homepage: String? = null,
    val hooks_url: String? = null,
    val html_url: String? = null,
    val id: String? = null,
    val is_template: String? = null,
    val issue_comment_url: String? = null,
    val issue_events_url: String? = null,
    val issues_url: String? = null,
    val keys_url: String? = null,
    val labels_url: String? = null,
    val language: String? = null,
    val languages_url: String? = null,
    val license: License? = null,
    val merges_url: String? = null,
    val milestones_url: String? = null,
    val mirror_url: Any? = null,
    val name: String? = null,
    val node_id: String? = null,
    val notifications_url: String? = null,
    val open_issues: String? = null,
    val open_issues_count: String? = null,
    val owner: Owner? = null,
    val `private`: String? = null,
    val pulls_url: String? = null,
    val pushed_at: String? = null,
    val releases_url: String? = null,
    val size: String? = null,
    val ssh_url: String? = null,
    val stargazers_count: String? = null,
    val stargazers_url: String? = null,
    val statuses_url: String? = null,
    val subscribers_url: String? = null,
    val subscription_url: String? = null,
    val svn_url: String? = null,
    val tags_url: String? = null,
    val teams_url: String? = null,
    val topics: List<Any?>? = null,
    val trees_url: String? = null,
    val updated_at: String? = null,
    val url: String? = null,
    val visibility: String? = null,
    val watchers: String? = null,
    val watchers_count: String? = null,
    val web_commit_signoff_required: String? = null
)

data class License(
    val key: String? = null,
    val name: String? = null,
    val node_id: String? = null,
    val spdx_id: String? = null,
    val url: String? = null
)

data class Owner(
    val avatar_url: String? = null,
    val events_url: String? = null,
    val followers_url: String? = null,
    val following_url: String? = null,
    val gists_url: String? = null,
    val gravatar_id: String? = null,
    val html_url: String? = null,
    val id: String? = null,
    val login: String? = null,
    val node_id: String? = null,
    val organizations_url: String? = null,
    val received_events_url: String? = null,
    val repos_url: String? = null,
    val site_admin: String? = null,
    val starred_url: String? = null,
    val subscriptions_url: String? = null,
    val type: String? = null,
    val url: String? = null
)