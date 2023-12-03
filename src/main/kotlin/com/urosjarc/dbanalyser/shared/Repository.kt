package com.urosjarc.dbanalyser.shared

import org.koin.core.component.KoinComponent

abstract class Repository<T : Any> : KoinComponent {
    var data = mutableListOf<T>()
    var selected: T? = null

    private val onChangeCb = mutableListOf<() -> Unit>()
    private val onSelectCb = mutableListOf<(t: T) -> Unit>()
    private val onErrorCb = mutableListOf<(t: String) -> Unit>()
    fun onChange(watcher: () -> Unit) {
        this.onChangeCb.add(watcher)
    }

    fun onSelect(watcher: (t: T) -> Unit) {
        this.onSelectCb.add(watcher)
    }

    fun onError(watcher: (t: String) -> Unit) {
        this.onErrorCb.add(watcher)
    }

    fun error(msg: String) {
        this.onErrorCb.forEach { it(msg) }
    }

    fun setAll(t: List<T>) {
        this.data = t.toMutableList()
        this.onChangeCb.forEach { it() }
    }

    fun save(t: T) {
        if (this.data.contains(t)) {
            val i = this.data.indexOf(t)
            this.data[i] = t
        } else {
            this.data.add(t)
        }
        this.onChangeCb.forEach { it() }
    }

    fun select(t: T) {
        this.selected = t
        this.onSelectCb.forEach { it(t) }
    }

    fun find(t: T): T? {
        return this.data.filter { it.equals(t) }.firstOrNull()
    }
}
