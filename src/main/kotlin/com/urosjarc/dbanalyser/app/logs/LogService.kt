package com.urosjarc.dbanalyser.app.logs

import org.apache.logging.log4j.kotlin.logger

class LogService(
	val logRepo: LogRepo
) {
	val log = this.logger()

	fun debug(data: String) {
		this.log.debug(data)
		this.logRepo.save(Log(type = Log.Type.DEBUG, data = data))
	}

	fun info(data: String) {
		this.log.info(data)
		this.logRepo.save(Log(type = Log.Type.INFO, data = data))
	}

	fun warn(data: String) {
		this.log.warn(data)
		this.logRepo.save(Log(type = Log.Type.WARN, data = data))
	}

	fun err(data: Throwable) {
		this.log.error(data)
		this.logRepo.save(Log(type = Log.Type.ERROR, data = data.localizedMessage))
	}

	fun fatal(data: Throwable) {
		this.log.fatal(data)
		this.logRepo.save(Log(type = Log.Type.FATAL, data = data.localizedMessage))
	}

	fun reset() = this.logRepo.reset()
}
