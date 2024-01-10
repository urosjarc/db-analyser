package com.urosjarc.dbanalyser.app

import com.urosjarc.dbanalyser.app.client.ClientRepo
import com.urosjarc.dbanalyser.app.client.ClientService
import com.urosjarc.dbanalyser.app.db.DbRepo
import com.urosjarc.dbanalyser.app.logs.LogRepo
import com.urosjarc.dbanalyser.app.logs.LogService
import com.urosjarc.dbanalyser.app.query.QueryRepo
import com.urosjarc.dbanalyser.app.schema.SchemaRepo
import com.urosjarc.dbanalyser.app.table.TableRepo
import com.urosjarc.dbanalyser.app.table.TableService
import kotlinx.datetime.Clock
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module

object App {
	fun modul() = module {
		this.single<DbRepo> { DbRepo("db.json") }
		this.single<LogRepo> { LogRepo("log_${Clock.System.now()}.json") }
		this.single<ClientRepo> { ClientRepo() }
		this.single<ClientService> { ClientService() }
		this.single<TableRepo> { TableRepo() }
		this.single<SchemaRepo> { SchemaRepo() }
		this.single<QueryRepo> { QueryRepo() }

		this.single { TableService(get()) }
		this.single { LogService(get()) }
	}

	fun init() {
		startKoin { this.modules(modul()) }
	}

	fun reset() = stopKoin()
}
