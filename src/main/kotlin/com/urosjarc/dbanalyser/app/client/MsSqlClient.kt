package com.urosjarc.dbanalyser.app.client

import com.urosjarc.dbanalyser.app.column.Column
import com.urosjarc.dbanalyser.app.column.ForeignKey
import com.urosjarc.dbanalyser.app.db.Db
import com.urosjarc.dbanalyser.app.schema.Schema
import com.urosjarc.dbanalyser.app.table.Table
import com.urosjarc.dbanalyser.shared.toDateTime
import java.sql.ResultSet


class MsSqlClient(db: Db) : Client(db) {


	override fun schemas(): List<Schema> {
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
		this.log.info("Schemas: ${schemas.size}")

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
					created = toDateTime(it.getTimestamp("created")),
					modified = toDateTime(it.getTimestamp("modified"))
				)
				tables["$schema.$name"] = table
				schema.tables.add(table)
			} else {
				this.log.warn("Missing schema: $schemaName")
			}
		}
		this.log.info("Tables: ${tables.size}")

		/**
		 * Columns
		 */
		val columns = mutableMapOf<String, Column>()
		this.columnResults {
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
					defaultValue = null, //TODO: Make this happend
					primaryKey = it.getBoolean("primaryKey"),
					foreignKey = null
				)
				columns["$schemaName.$tableName.$name"] = column
				table.columns.add(column)
			} else {
				this.log.warn("Missing table: $schemaName.$tableName")
			}
		}
		this.log.info("Columns: ${columns.size}")

		/**
		 * Foreign keys
		 */

		var foreignKeys = 0
		this.foreignKeyResults {
			val fromSchema = it.getString("fromSchema")
			val fromTable = it.getString("fromTable")
			val fromColumnStr = it.getString("fromColumn")
			val toSchema = it.getString("toSchema")
			val toTable = it.getString("toTable")
			val toColumnStr = it.getString("toColumn")

			val fromColumn = columns["$fromSchema.$fromTable.$fromColumnStr"]
			val toColumn = columns["$toSchema.$toTable.$toColumnStr"]


			if (fromColumn == null) {
				this.log.warn("Missing column: $fromSchema.$fromTable.$fromColumnStr")
				return@foreignKeyResults
			} else if (toColumn == null) {
				this.log.warn("Missing column: $toSchema.$toTable.$toColumnStr")
				return@foreignKeyResults
			} else {
				fromColumn.foreignKey = ForeignKey(
					from = fromColumn,
					to = toColumn
				)
				foreignKeys++
			}
		}
		this.log.info("Foreign keys: $foreignKeys")

		return schemas.values.toList()
	}

	fun schemaResults(onResultSet: (rs: ResultSet) -> Unit) = this.exec(
		"""
			select sch.name as name
			from sys.schemas sch
        """, onResultSet = onResultSet
	)

	fun tableResults(onResultSet: (rs: ResultSet) -> Unit) = this.exec(
		"""
			select sch.name        as _schema,
				   tab.name        as name,
				   tab.create_date as created,
				   tab.modify_date as modified
			from sys.tables tab
					 INNER JOIN sys.schemas sch ON sch.schema_id = tab.schema_id
        """, onResultSet = onResultSet
	)

	fun columnResults(onResultSet: (rs: ResultSet) -> Unit) = this.exec(
		"""
			SELECT sch.name        as _schema,
				   tab.name        as _table,
				   col.name        as name,
				   col.is_nullable as isNull,
				   typ.name        as type,
				   col.is_identity as primaryKey
			FROM sys.columns col
					 INNER JOIN sys.tables tab on col.object_id = tab.object_id
					 INNER JOIN sys.schemas sch ON sch.schema_id = tab.schema_id
					 inner JOIN sys.types typ ON col.user_type_id = typ.user_type_id
		""", onResultSet = onResultSet
	)

	fun foreignKeyResults(onResultSet: (rs: ResultSet) -> Unit) = this.exec(
		"""
			SELECT sch1.name as fromSchema,
				   tab1.name AS fromTable,
				   col1.name AS fromColumn,
				   sch2.name AS toSchema,
				   tab2.name AS toTable,
				   col2.name AS toColumn
			FROM sys.foreign_key_columns fkc
					 INNER JOIN sys.tables tab1 ON tab1.object_id = fkc.parent_object_id
					 INNER JOIN sys.tables tab2 ON tab2.object_id = fkc.referenced_object_id
					 INNER JOIN sys.schemas sch1 ON tab1.schema_id = sch1.schema_id
					 INNER JOIN sys.schemas sch2 ON tab2.schema_id = sch2.schema_id
					 INNER JOIN sys.columns col1 ON col1.column_id = parent_column_id AND col1.object_id = tab1.object_id
					 INNER JOIN sys.columns col2 ON col2.column_id = referenced_column_id
				AND col2.object_id = tab2.object_id;
        """, onResultSet = onResultSet
	)

}
