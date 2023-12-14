package com.urosjarc.dbanalyser.app.schema

import com.urosjarc.dbanalyser.app.client.ClientRepo
import com.urosjarc.dbanalyser.shared.Repository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SchemaRepo : Repository<Schema>() {
//    val clientRepo by this.inject<ClientRepo>()
//
//    init {
//        this.clientRepo.onSelect { this.setAll(it.schemas()) }
//    }

    fun find(t: String) = this.data.firstOrNull { it.name == t }
}
