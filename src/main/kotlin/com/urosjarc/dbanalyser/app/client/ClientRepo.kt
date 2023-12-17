package com.urosjarc.dbanalyser.app.client

import com.urosjarc.dbanalyser.app.db.Db
import com.urosjarc.dbanalyser.app.db.DbRepo
import com.urosjarc.dbanalyser.shared.Repository
import org.koin.core.component.inject

class ClientRepo : Repository<Client>() {
    val dbRepo by this.inject<DbRepo>()

    init {
        this.dbRepo.onChose {
            val client = when (it.type) {
                Db.Type.SQLITE -> SqliteClient(it)
                Db.Type.MS_SQL -> MsSqlClient(it)
            }
            if (client.inited()) {
                this.chose(client)
            }
            else {
                this.log.err("Client could not connect!")
                this.error(msg = "Client could not connect!")
            }
        }
    }
}
