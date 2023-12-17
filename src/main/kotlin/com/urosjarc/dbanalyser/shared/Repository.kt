package com.urosjarc.dbanalyser.shared

import com.urosjarc.dbanalyser.app.logs.LogService
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

abstract class Repository<T : Any> : KoinComponent {
	val log by this.inject<LogService>()
	val data = mutableListOf<T>()
	var selected = mutableListOf<T>()
	var chosen: T? = null

	private val onDataCb = mutableListOf<(t: List<T>) -> Unit>()
	private val onSelectCb = mutableListOf<(t: List<T>) -> Unit>()
	private val onChoseCb = mutableListOf<(t: T) -> Unit>()
	private val onErrorCb = mutableListOf<(t: String) -> Unit>()

	fun onData(watcher: (t: List<T>) -> Unit) {
		this.onDataCb.add(watcher)
	}

	fun onSelect(watcher: (t: List<T>) -> Unit) {
		this.onSelectCb.add(watcher)
	}

	fun onChose(watcher: (t: T) -> Unit) {
		this.onChoseCb.add(watcher)
	}

	fun onError(watcher: (t: String) -> Unit) {
		this.onErrorCb.add(watcher)
	}

	fun error(msg: String) {
		this.onErrorCb.forEach { it(msg) }
	}

	fun set(t: List<T>) {
		this.data.clear()
		this.data.addAll(t)
		this.onDataCb.forEach { it(t) }
		this.save()
	}

	open fun save(t: T) {
		if (this.data.contains(t)) {
			val i = this.data.indexOf(t)
			this.data[i] = t
		} else {
			this.data.add(t)
		}
		this.onDataCb.forEach { it(this.data) }
		this.save()
	}

	fun select(t: List<T>) {
		this.selected = t.toMutableList()
		this.onSelectCb.forEach { it(t) }
	}

	fun chose(t: T) {
		this.chosen = t
		this.onChoseCb.forEach { it(t) }
	}

	fun find(t: T): T? {
		return this.data.filter { it.equals(t) }.firstOrNull()
	}

	open fun load() {}

	open fun save() {}
}
