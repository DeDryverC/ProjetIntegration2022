package com.example.integration.listener

import com.example.integration.model.CartModel

interface ICartLoadListener {
    fun onCartLoadSuccess(CartModelList: List<CartModel?>?)
    fun onCartLoadFailed(message: String?)
}