package com.urosjarc.dbanalyser.app.client

import com.urosjarc.dbanalyser.app.column.Column
import com.urosjarc.dbanalyser.app.column.ForeignKey
import com.urosjarc.dbanalyser.app.db.Db
import com.urosjarc.dbanalyser.app.logs.LogService
import com.urosjarc.dbanalyser.app.schema.Schema
import com.urosjarc.dbanalyser.app.table.Table
import org.apache.logging.log4j.kotlin.logger
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.sql.*
import java.util.*

abstract class Client(private val db: Db) : KoinComponent {
    val log = this.logger()
    val logService by this.inject<LogService>()

    var con: Connection? = null

    abstract fun schemaResults(onResultSet: (rs: ResultSet) -> Unit)
    abstract fun tableResults(onResultSet: (rs: ResultSet) -> Unit)
    abstract fun columnResults(tables: MutableMap<String, Table>, onResultSet: (rs: ResultSet) -> Unit)
    abstract fun foreignKeyResults(tables: MutableMap<String, Table>, onResultSet: (rs: ResultSet) -> Unit)

    init {
        val info = Properties()
        info.set(key = "user", value = db.user)
        info.set(key = "password", value = db.password)

        try {
            this.logService.info("Db selected: $db")
            Class.forName("oracle.jdbc.driver.OracleDriver")
            Class.forName ("org.mariadb.jdbc.Driver")
            this.con = DriverManager.getConnection(db.url, info)
            this.logService.info("Db connected: $db")
        } catch (e: SQLException) {
            this.logService.err(e)
        }
    }

    fun inited() = con != null

    fun schemas(): List<Schema> {
        this.logService.info("Analysing schemas...")
        /**
         * Schemas
         */
        val schemas = mutableMapOf<String, Schema>()
        this.schemaResults {
            val name = it.getString("name")
            schemas[name] = Schema(
                name = name,
            )
        }
        this.logService.info("Schemas: ${schemas.size}")

        /**
         * Tables
         */
        val tables = mutableMapOf<String, Table>()
        this.tableResults {
            val schemaName = it.getString("_schema")
            val name = it.getString("name")
            val schema = schemas[schemaName]

            if (schema != null) {
                val table = Table(
                    schema = schema,
                    name = name,
                    created = null,
                    modified = null,
                    escaping = when (db.type) {
                        Db.Type.SQLITE -> '"'
                        Db.Type.MS_SQL -> '"'
                        Db.Type.POSTGRESQL -> '"'
                        Db.Type.MYSQL -> '`'
                        Db.Type.MARIA -> '`'
                        Db.Type.ORACLE -> '"'
                        Db.Type.DB2 -> '"'
                    }
                )
                tables["$schema.$name"] = table
                schema.tables.add(table)
            } else {
                this.logService.warn("Missing schema: $schemaName")
            }
        }
        this.logService.info("Tables: ${tables.size}")

        /**
         * Columns
         */
        val columns = mutableMapOf<String, Column>()
        this.columnResults(tables = tables) {
            val schemaName = it.getString("_schema")
            val tableName = it.getString("_table")
            val name = it.getString("name")
            val table = tables["$schemaName.$tableName"]

            if (table != null) {
                val column = Column(
                    table = table,
                    name = name,
                    type = it.getString("type"),
                    notNull = !it.getBoolean("isNull"),
                    defaultValue = null,
                    primaryKey = it.getBoolean("primaryKey"),
                    foreignKey = null
                )
                columns["$schemaName.$tableName.$name"] = column
                table.columns.add(column)
            } else {
                this.logService.warn("Missing table: $schemaName.$tableName")
            }
        }
        this.logService.info("Columns: ${columns.size}")

        /**
         * Foreign keys
         */

        var foreignKeys = 0
        this.foreignKeyResults(tables = tables) {
            val fromSchema = it.getString("fromSchema")
            val fromTable = it.getString("fromTable")
            val fromColumnStr = it.getString("fromColumn")
            val toSchema = it.getString("toSchema")
            val toTable = it.getString("toTable")
            val toColumnStr = it.getString("toColumn")

            val fromColumn = columns["$fromSchema.$fromTable.$fromColumnStr"]
            val toColumn = columns["$toSchema.$toTable.$toColumnStr"]


            if (fromColumn == null) {
                this.logService.warn("Missing column: $fromSchema.$fromTable.$fromColumnStr")
                return@foreignKeyResults
            } else if (toColumn == null) {
                this.logService.warn("Missing column: $toSchema.$toTable.$toColumnStr")
                return@foreignKeyResults
            } else {
                fromColumn.foreignKey = ForeignKey(
                    from = fromColumn,
                    to = toColumn
                )
                foreignKeys++
            }
        }
        this.logService.info("Foreign keys: $foreignKeys")
        this.logService.info("Finished with analysing...")

        return schemas.values.toList()
    }

    fun exec(sql: String, onResultSet: (rs: ResultSet) -> Unit) {
        this.log.debug("\n\n$sql\n\n")
        try {
            this.con!!.createStatement().use { statement ->
                val rs = statement.executeQuery(sql)
                while (rs.next()) {
                    onResultSet(rs)
                }
            }
        } catch (e: SQLException) {
            this.logService.err(e)
        }
    }

    fun execMany(sql: String, onNewResultSet: (rs: ResultSet) -> Unit) {
        this.log.debug("\n\n$sql\n\n")
        try {
            val stmt: Statement = this.con!!.createStatement()
            var isResultSet = stmt.execute(sql)

            var count = 0
            while (true) {
                if (isResultSet) {
                    val rs = stmt.resultSet
                    onNewResultSet(rs)
                } else if (stmt.updateCount == -1) break
                count++
                isResultSet = stmt.moreResults
            }

        } catch (e: SQLException) {
            this.logService.err(e)
        }
    }

    fun <T : Any> execOne(sql: String, onResultSet: (rs: ResultSet) -> T?): T? {
        this.log.debug("\n\n$sql\n\n")
        try {
            this.con!!.createStatement().use { statement ->
                val rs = statement.executeQuery(sql)
                if (rs.next()) return onResultSet(rs)
            }
        } catch (e: SQLException) {
            this.logService.err(e)
        }
        return null
    }
}
