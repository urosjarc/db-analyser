package com.urosjarc.dbanalyser.app.table

import com.urosjarc.dbanalyser.app.client.ClientRepo
import com.urosjarc.dbanalyser.app.schema.SchemaRepo
import com.urosjarc.dbanalyser.shared.Repository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class TableRepo : Repository<Table>() {
	val clientRepo by this.inject<ClientRepo>()

	init {
		this.clientRepo.onSelect { this.setAll(it.schemas().flatMap { it.tables }) }
	}

	fun find(t: String) = this.data.firstOrNull { it.name == t }
}
