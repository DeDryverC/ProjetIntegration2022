package com.example.integration

var random1 = (0..10000).random()
var random2 = (0..10000).random()
var random3 = (0..10000).random()
var random4 = (0..10000).random()
var randomTotal = random1 + random2 + random3 + random4

fun unique(): String {
    randomTotal
    return randomTotal.toString()
}