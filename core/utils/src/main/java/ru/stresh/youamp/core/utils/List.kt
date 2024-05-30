package ru.stresh.youamp.core.utils

fun <T> List<T>.swap(from: Int, to: Int): List<T> {
    val mutable = toMutableList()
    val item = mutable.removeAt(from)
    mutable.add(to, item)
    return mutable.toList()
}