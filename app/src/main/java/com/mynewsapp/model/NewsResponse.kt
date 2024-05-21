package com.mynewsapp.model

import kotlinx.serialization.Serializable


@Serializable
data class NewsResponse(
    val status: String,
    val articles: List<Article>
)

@Serializable
data class Article(
    val source:Source,
    val title: String,
    val author: String?,
    val description: String?,
    val url: String,
    val urlToImage: String?,
    val publishedAt: String?,
    val content: String?,
)

@Serializable
data class Source(val id:String?,val name:String?)