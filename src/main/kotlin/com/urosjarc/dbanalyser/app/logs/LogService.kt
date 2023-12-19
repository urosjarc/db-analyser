package com.urosjarc.dbanalyser.app.logs

class LogService(
    val logRepo: LogRepo
) {
    fun debug(data: String) = this.logRepo.save(Log(type = Log.Type.DEBUG, data = data))
    fun info(data: String) = this.logRepo.save(Log(type = Log.Type.INFO, data = data))
    fun warn(data: String) = this.logRepo.save(Log(type = Log.Type.WARN, data = data))
    fun err(data: String) = this.logRepo.save(Log(type = Log.Type.ERROR, data = data))
    fun fatal(data: String) = this.logRepo.save(Log(type = Log.Type.FATAL, data = data))
}
