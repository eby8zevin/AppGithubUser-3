package com.ahmadabuhasan.appgithubuser.model

import com.google.gson.annotations.SerializedName

class ResponseDetailUser(

    @field:SerializedName("login")
    var login: String? = null,

    @field:SerializedName("id")
    var id: Int? = null,

    @field:SerializedName("avatar_url")
    var avatarUrl: String? = null,

    @field:SerializedName("html_url")
    var htmlUrl: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("company")
    val company: String? = null,

    @field:SerializedName("location")
    val location: String? = null,

    @field:SerializedName("public_repos")
    val publicRepos: String? = null,

    @field:SerializedName("followers")
    val followers: String? = null,

    @field:SerializedName("following")
    val following: String? = null
)
