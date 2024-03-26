package com.urosjarc.dbanalyser.app.commit

import com.urosjarc.dbanalyser.app.db.DbRepo
import com.urosjarc.dbanalyser.shared.Repository
import org.apache.logging.log4j.kotlin.logger
import org.koin.core.component.inject

class CommitRepo : Repository<Commit>() {

    val dbRepo by this.inject<DbRepo>()
    override val log = this.logger()

    init {
        this.dbRepo.onChose {
            this.set(it?.commits ?: listOf())
        }
    }

    override fun save(t: Commit): Commit {
        val saved = super.save(t)
        dbRepo.chosen?.commits?.add(saved)
        this.dbRepo.save()
        return saved
    }

    override fun delete(t: Commit) {
        dbRepo.chosen?.let { db ->
            db.commits.removeIf { it == t }
            this.set(db.commits)
            this.dbRepo.save()
        }
        super.delete(t)
    }
}
