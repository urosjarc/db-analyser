package com.urosjarc.dbanalyser.shared

abstract class Repository<T : Any> {
    val data = mutableListOf<T>()

    val watchers = mutableListOf<() -> Unit>()
    fun watch(watcher: () -> Unit) {
        this.watchers.add(watcher)
    }

    fun notifyWatchers() {
        this.watchers.forEach { it() }
    }
    fun get(): MutableList<T> = this.data
    fun save(t: T) {
        this.data.add(t)
        this.notifyWatchers()
    }
}
