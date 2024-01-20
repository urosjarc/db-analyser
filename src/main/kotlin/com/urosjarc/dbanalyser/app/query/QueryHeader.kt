package com.urosjarc.dbanalyser.app.query

import kotlinx.serialization.Serializable

@Serializable
data class QueryHeader(val name: String, val type: String) {
	enum class SqlType {
		BIT,
		TINYINT, SMALLINT, INT, INTEGER, BIGINT,
		DECIMAL, NUMERIC, FLOAT, REAL,
		DATE, TIME, DATETIME, TIMESTAMP, YEAR,
		CHAR,
		VARCHAR, VARCHA, TEXT, NCHAR, NVARCHAR, NVARCHA, NTEXT,
		BINARY, VARBINARY, VARBINAR, IMAGE, CLOB, BLOB,
		XML, JSON,
	}

	enum class LangType { BOOLEAN, INT, FLOAT, DATE, CHAR, STRING, UNKNOWN, TIME, DATETIME, BIN }

	val langType
		get(): LangType {
			try {
				val sqlType = this.type.uppercase().filter { !it.isDigit() }
				val enumType: SqlType = SqlType.valueOf(sqlType)
				return when (enumType) {
					SqlType.BIT -> LangType.BOOLEAN
					SqlType.TINYINT -> LangType.INT
					SqlType.SMALLINT -> LangType.INT
					SqlType.INT -> LangType.INT
					SqlType.INTEGER -> LangType.INT
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
