package com.urosjarc.dbanalyser.app.table

import com.urosjarc.dbanalyser.app.schema.SchemaRepo
import com.urosjarc.dbanalyser.shared.Repository
import org.koin.core.component.inject

class TableRepo : Repository<Table>() {
	val schemaRepo by this.inject<SchemaRepo>()

	init {
		this.schemaRepo.onData { this.set(it.flatMap { it.tables }) }
		this.schemaRepo.onSelect { this.set(it.flatMap { it.tables }) }
	}

	fun find(t: String) = this.data.firstOrNull { it.name == t }
}
