package com.urosjarc.dbanalyser.app

import com.urosjarc.dbanalyser.app.client.ClientRepo
import com.urosjarc.dbanalyser.app.db.DbRepo
import com.urosjarc.dbanalyser.app.table.TableRepo
import kotlinx.serialization.json.Json
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module

object App {
    enum class Tip { PRODUCTION, DEVELOPMENT, TEST }

    fun modul(tip: Tip = Tip.TEST) = module {
        this.single { DbRepo() }
        this.single { ClientRepo() }
        this.single { TableRepo() }
        this.single { Json { this.prettyPrint = true } }
    }

    fun init(tip: Tip = Tip.TEST) {
        startKoin { this.modules(modul(tip = tip)) }
    }

    fun reset() = stopKoin()
}
