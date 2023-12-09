package com.urosjarc.dbanalyser.app

import com.urosjarc.dbanalyser.app.client.ClientRepo
import com.urosjarc.dbanalyser.app.db.DbRepo
import com.urosjarc.dbanalyser.app.logs.LogRepo
import com.urosjarc.dbanalyser.app.logs.LogService
import com.urosjarc.dbanalyser.app.table.TableRepo
import com.urosjarc.dbanalyser.app.table.TableService
import kotlinx.serialization.json.Json
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module

object App {
    enum class Tip { PRODUCTION, DEVELOPMENT, TEST }

    fun modul(tip: Tip = Tip.TEST) = module {
        this.single<DbRepo> { DbRepo("db.json") }
        this.single<LogRepo> { LogRepo("log.json") }
        this.single<ClientRepo> { ClientRepo() }
        this.single<TableRepo> { TableRepo() }

        this.single { TableService(get()) }
        this.single { LogService(get()) }
    }

    fun init(tip: Tip = Tip.TEST) {
        startKoin { this.modules(modul(tip = tip)) }
    }

    fun reset() = stopKoin()
}
