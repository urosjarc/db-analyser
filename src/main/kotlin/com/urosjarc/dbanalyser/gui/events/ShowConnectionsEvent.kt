package com.urosjarc.dbanalyser.gui.events

import com.urosjarc.dbanalyser.shared.Repository
import org.apache.logging.log4j.kotlin.logger

class ShowConnectionsEvent: Repository<Boolean>() {
	override val log = this.logger()
}
