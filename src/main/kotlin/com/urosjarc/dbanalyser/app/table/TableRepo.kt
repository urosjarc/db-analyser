package com.urosjarc.dbanalyser.app.table

import com.urosjarc.dbanalyser.app.schema.Schema
import com.urosjarc.dbanalyser.app.schema.SchemaRepo
import com.urosjarc.dbanalyser.shared.Repository
import org.apache.logging.log4j.kotlin.logger
import org.koin.core.component.inject

class TableRepo : Repository<Table>() {
	override val log = this.logger()
	val schemaRepo by this.inject<SchemaRepo>()
	val tableRepo by this.inject<TableRepo>()

	init {
		this.schemaRepo.onData { this.update(it) }
		this.schemaRepo.onSelect { this.update(it) }
	}

	fun update(schemas: List<Schema>) {
		val tables = schemas.flatMap { it.tables }
		this.set(tables)
	}

	fun find(t: String) = this.data.firstOrNull { it.name == t }
}
