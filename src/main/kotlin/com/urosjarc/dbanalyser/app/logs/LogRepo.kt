package com.urosjarc.dbanalyser.app.logs

import com.urosjarc.dbanalyser.shared.Repository
import org.apache.logging.log4j.kotlin.logger

class LogRepo : Repository<Log>() {

	override val log = this.logger()

	init {
		this.load()
	}

	override fun save(t: Log): Log {
		val old = super.save(t)
		this.chose(old)
		return old
	}
}
