package com.urosjarc.dbanalyser.app.query

import kotlinx.serialization.Serializable

@Serializable
data class QueryResult(
	val title: String,
	val comment: String,
	val data: String,
	val headers: List<Header>
) {
	@Serializable
	data class Header(val name: String, val type: String) {
		enum class SqlType {
			BIT,
			TINYINT, SMALLINT, INT, BIGINT,
			DECIMAL, NUMERIC, FLOAT, REAL,
			DATE, TIME, DATETIME, TIMESTAMP, YEAR,
			CHAR,
			VARCHAR, VARCHA, TEXT, NCHAR, NVARCHAR, NVARCHA, NTEXT,
			BINARY, VARBINARY, VARBINAR, IMAGE, CLOB, BLOB,
			XML, JSON,
		}

		enum class LangType { BOOLEAN, INT, FLOAT, DATE, CHAR, STRING, UNKNOWN, TIME, DATETIME, BIN }

		enum class Lang { JAVA, TYPESCRIPT, KOTLIN}

		val langType
			get(): LangType {
				try {
					val enumType: SqlType = SqlType.valueOf(this.type.uppercase())
					return when (enumType) {
						SqlType.BIT -> LangType.BOOLEAN
						SqlType.TINYINT -> LangType.INT
						SqlType.SMALLINT -> LangType.INT
						SqlType.INT -> LangType.INT
						SqlType.BIGINT -> LangType.INT
						SqlType.DECIMAL -> LangType.FLOAT
						SqlType.NUMERIC -> LangType.FLOAT
						SqlType.FLOAT -> LangType.FLOAT
						SqlType.REAL -> LangType.FLOAT
						SqlType.DATE -> LangType.DATE
						SqlType.TIME -> LangType.TIME
						SqlType.DATETIME -> LangType.DATETIME
						SqlType.TIMESTAMP -> LangType.INT
						SqlType.YEAR -> LangType.INT
						SqlType.CHAR -> LangType.CHAR
						SqlType.VARCHAR -> LangType.STRING
						SqlType.VARCHA -> LangType.STRING
						SqlType.TEXT -> LangType.STRING
						SqlType.NCHAR -> LangType.STRING
						SqlType.NVARCHAR -> LangType.STRING
						SqlType.NVARCHA -> LangType.STRING
						SqlType.NTEXT -> LangType.STRING
						SqlType.BINARY -> LangType.BIN
						SqlType.VARBINARY -> LangType.BIN
						SqlType.VARBINAR -> LangType.BIN
						SqlType.IMAGE -> LangType.BIN
						SqlType.CLOB -> LangType.BIN
						SqlType.BLOB -> LangType.BIN
						SqlType.XML -> LangType.STRING
						SqlType.JSON -> LangType.STRING
					}
				} catch (e: IllegalArgumentException) {
					return LangType.UNKNOWN
				}
			}

	}

	enum class Type { SELECT, UPDATE, INSERT, DELETE, CREATE }

	fun javaDTO(): String {
		val headerStr = this.headers.map {
			when (it.langType) {
				Header.LangType.BOOLEAN -> "boolean ${it.name};"
				Header.LangType.INT -> "int ${it.name};"
				Header.LangType.FLOAT -> "float ${it.name};"
				Header.LangType.DATE -> "Date ${it.name};"
				Header.LangType.CHAR -> "char ${it.name};"
				Header.LangType.STRING -> "String ${it.name};"
				Header.LangType.UNKNOWN -> "UNKNOWN ${it.name};"
				Header.LangType.TIME -> "Date ${it.name};"
				Header.LangType.DATETIME -> "Date ${it.name};"
				Header.LangType.BIN -> "UNKNOWN ${it.name};"
			}
		}.joinToString("\n" + "\t".repeat(4))
		return """
			// $comment
			public class $title {
				$headerStr	
			}
		""".trimIndent()
	}

	fun kotlinDTO(): String {
		val headerStr = this.headers.map {
			when (it.langType) {
				Header.LangType.BOOLEAN -> "val ${it.name}: Boolean"
				Header.LangType.INT -> "val ${it.name}: Int"
				Header.LangType.FLOAT -> "val ${it.name}: Float"
				Header.LangType.DATE -> "val ${it.name}: LocalDate"
				Header.LangType.CHAR -> "val ${it.name}: Char"
				Header.LangType.STRING -> "val ${it.name}: String"
				Header.LangType.UNKNOWN -> "val ${it.name}: UNKNOWN"
				Header.LangType.TIME -> "val ${it.name}: LocalTime"
				Header.LangType.DATETIME -> "val ${it.name}: LocalDateTime"
				Header.LangType.BIN -> "val ${it.name}: UNKNOWN"
			}
		}.joinToString(",\n" + "\t".repeat(4))
		return """
			// $comment
			data class $title(
				$headerStr	
			)
		""".trimIndent()
	}

	fun typescriptDTO(): String {
		val headerStr = this.headers.map {
			when (it.langType) {
				Header.LangType.BOOLEAN -> "${it.name}: boolean;"
				Header.LangType.INT -> "${it.name}: number;"
				Header.LangType.FLOAT -> "${it.name}: number;"
				Header.LangType.DATE -> "${it.name}: date;"
				Header.LangType.CHAR -> "${it.name}: string;"
				Header.LangType.STRING -> "${it.name}: string;"
				Header.LangType.UNKNOWN -> "${it.name}: UNKNOWN;"
				Header.LangType.TIME -> "${it.name}: date;"
				Header.LangType.DATETIME -> "${it.name}: date;"
				Header.LangType.BIN -> "${it.name}: UNKNOWN;"
			}
		}.joinToString("\n" + "\t".repeat(4))
		return """
			// $comment
			export class $title {
				$headerStr	
			}
		""".trimIndent()
	}
}
