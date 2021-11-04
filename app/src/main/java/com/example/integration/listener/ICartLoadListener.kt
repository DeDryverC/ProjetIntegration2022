package com.example.integration.listener

import com.example.integration.model.CartModel

interface ICartLoadListener {
    fun onCartLoadSuccess(cartModelList: List<CartModel?>?)
    fun onCartLoadFailed(message: String?)
}