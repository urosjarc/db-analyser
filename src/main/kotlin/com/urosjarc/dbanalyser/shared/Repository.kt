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

    fun error(msg: String){
        this.onErrorCb.forEach { it(msg) }
    }

    fun setAll(t: List<T>) {
        this.data = t.toMutableList()
        this.onChangeCb.forEach { it() }
    }

    fun save(t: T) {
        this.data.add(t)
        this.onChangeCb.forEach { it() }
        this.select(t)
    }

    fun select(t: T) {
        this.selected = t
        this.onSelectCb.forEach { it(t) }
    }
}
