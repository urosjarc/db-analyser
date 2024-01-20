package com.urosjarc.dbanalyser.app.tableConnection

import com.urosjarc.dbanalyser.app.schema.SchemaRepo
import com.urosjarc.dbanalyser.shared.Repository
import org.apache.logging.log4j.kotlin.logger
import org.koin.core.component.inject

class TableConnectionRepo : Repository<TableConnection>() {
	override val log = this.logger()
	val schemaRepo by this.inject<SchemaRepo>()

	init {
		this.schemaRepo.onData { this.chose(null) }
		this.schemaRepo.onSelect { this.chose(null) }
	}

}
