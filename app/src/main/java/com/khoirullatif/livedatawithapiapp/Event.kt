package com.khoirullatif.livedatawithapiapp

open class Event<out T>(private val content: T) {
    @Suppress("MemberVisibilityCanBePrivate")
    var hasBeenHandled = false
        private set //allow to read prevent to write by external

    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    //to see content value even it's already been handled
    fun peekContent(): T = content
}