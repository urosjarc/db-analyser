package com.urosjarc.dbanalyser.app.client

import com.urosjarc.dbanalyser.app.db.Db
import com.urosjarc.dbanalyser.app.db.DbRepo
import com.urosjarc.dbanalyser.shared.Repository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ClientRepo : Repository<Client>() {
    val dbRepo by this.inject<DbRepo>()

    init {
        this.dbRepo.onSelect {
            val client = when (it.type) {
                Db.Type.SQLITE -> SqliteClient(it)
                Db.Type.MS_SQL -> MsSqlClient(it)
            }
            if (client.inited()) this.select(client)
            else this.error(msg = "Client could not connect!")
        }
    }
}
