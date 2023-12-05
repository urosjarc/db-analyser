package com.urosjarc.dbanalyser.shared

class Observer<T : Any> {
    private lateinit var _value: T
    var value: T
        get() = this._value
        set(v) {
            this._value = v
            this.onchangeCb.forEach { it(this._value) }
        }

    val onchangeCb = mutableListOf<(value: T) -> Unit>()
    fun onChange(cb: ((value: T) -> Unit)) {
        this.onchangeCb.add(cb)
    }

}
