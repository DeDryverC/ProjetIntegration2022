package com.example.integration.listener

import com.example.integration.model.ArticleModel

interface IArticleLoadListener {
    fun onArticleLoadSuccess(articleModelList:List<ArticleModel>?)
    fun onArticleLoadFailed(message: String?)
}
