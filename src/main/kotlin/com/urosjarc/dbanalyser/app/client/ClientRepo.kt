package com.urosjarc.dbanalyser.app.client

import com.urosjarc.dbanalyser.app.db.Db
import com.urosjarc.dbanalyser.app.db.DbRepo
import com.urosjarc.dbanalyser.app.logs.LogService
import com.urosjarc.dbanalyser.shared.Repository
import com.urosjarc.dbanalyser.shared.startThread
import org.apache.logging.log4j.kotlin.logger
import org.koin.core.component.inject

class ClientRepo : Repository<Client>() {
	val dbRepo by this.inject<DbRepo>()
	val logService by this.inject<LogService>()

	override val log = this.logger()

	init {
		this.dbRepo.onChose(runLater = false) {
			if (it != null) startThread {
				this.logService.reset()
				val client = when (it.type) {
					Db.Type.SQLITE -> SqliteClient(it)
					Db.Type.MS_SQL -> MsSqlClient(it)
					Db.Type.POSTGRESQL -> PostgreSqlClient(it)
					Db.Type.MYSQL -> MySqlClient(it)
				}
				if (client.inited()) {
					this.logService.info("Client connected active!")
					this.chose(client)
				} else {
					this.logService.warn("Client could not connect!")
				}

			}
		}
	}
}
