package com.urosjarc.dbanalyser.shared

class Observer<T : Any> {
    lateinit var _value: T
    var value: T
        get() = this._value
        set(v) {
            this._value = v
            this.observers.forEach {
                it(this._value)

            }
        }

    val observers = mutableListOf<(value: T) -> Unit>()
    fun observe(cb: ((value: T) -> Unit)) {
        this.observers.add(cb)
    }

}
