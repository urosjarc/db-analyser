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
import com.urosjarc.dbanalyser.app.tableConnection.TableConnectionRepo
import com.urosjarc.dbanalyser.gui.events.ShowConnectionsEvent
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module

object App {
	fun modul() = module {
		this.single<DbRepo> { DbRepo("db.json") }
		this.single<LogRepo> { LogRepo() }
		this.single<ClientRepo> { ClientRepo() }
		this.single<ClientService> { ClientService() }
		this.single<TableRepo> { TableRepo() }
		this.single<ShowConnectionsEvent> { ShowConnectionsEvent() }
		this.single<TableConnectionRepo> { TableConnectionRepo() }
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
