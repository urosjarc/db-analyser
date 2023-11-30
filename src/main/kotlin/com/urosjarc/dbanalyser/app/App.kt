package com.urosjarc.dbanalyser.app

import com.urosjarc.dbanalyser.app.db.DbRepo
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module

object App {
    enum class Tip { PRODUCTION, DEVELOPMENT, TEST }

    fun modul(tip: Tip = Tip.TEST) = module {
        this.single { DbRepo() }
    }

    fun init(tip: Tip = Tip.TEST) {
        startKoin { this.modules(modul(tip = tip)) }
    }

    fun reset() = stopKoin()
}
