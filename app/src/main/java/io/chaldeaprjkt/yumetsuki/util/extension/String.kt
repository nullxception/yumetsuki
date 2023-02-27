package io.chaldeaprjkt.yumetsuki.util.extension

fun String.trimQuotes() = trim().removeSurrounding("\"").removeSurrounding("\'")
