package com.urosjarc.dbanalyser.app.query

import com.urosjarc.dbanalyser.app.db.DbRepo
import com.urosjarc.dbanalyser.shared.Repository
import org.apache.logging.log4j.kotlin.logger
import org.koin.core.component.inject

class QueryRepo : Repository<Query>() {

	val dbRepo by this.inject<DbRepo>()
	override val log = this.logger()

	init {
		this.dbRepo.onChose {
			this.set(it?.queries ?: listOf())
		}
	}

	override fun save(t: Query): Query {
		val saved = super.save(t)
		dbRepo.chosen?.queries?.add(saved)
		this.dbRepo.save()
		return saved
	}

	fun delete(name: String) {
		dbRepo.chosen?.let { db ->
			db.queries.removeIf { name == it.name }
			this.set(db.queries)
			this.dbRepo.save()
		}
	}
}
