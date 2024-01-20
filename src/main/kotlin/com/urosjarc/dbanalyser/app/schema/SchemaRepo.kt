package com.urosjarc.dbanalyser.app.schema

import com.urosjarc.dbanalyser.app.client.ClientRepo
import com.urosjarc.dbanalyser.shared.Repository
import org.apache.logging.log4j.kotlin.logger
import org.koin.core.component.inject

class SchemaRepo : Repository<Schema>() {
	val clientRepo by this.inject<ClientRepo>()
	override val log = this.logger()

	init {
		this.clientRepo.onChose {
			this.set(it?.schemas() ?: listOf())
		}
	}

	fun find(t: String) = this.data.firstOrNull { it.name == t }
}
