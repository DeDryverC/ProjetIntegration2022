package com.example.integration.listener

import com.example.integration.model.ArticleModel

interface ArticleLoadListener {
    fun onArticleLoadSuccess(ArticleModelList: List<ArticleModel?>?)
    fun onArticleFailed(message: String?)
}