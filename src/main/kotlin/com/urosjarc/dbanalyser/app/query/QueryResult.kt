package com.urosjarc.dbanalyser.app.query

import kotlinx.serialization.Serializable

@Serializable
data class QueryResult(
	val title: String,
	val comment: String,
	val data: String,
	val headers: List<QueryHeader>
) {

	enum class Lang { KOTLIN, JAVA, TYPESCRIPT }

	fun javaDTO(): String {
		val headerStr = this.headers.map {
			when (it.langType) {
				QueryHeader.LangType.BOOLEAN -> "boolean ${it.name};"
				QueryHeader.LangType.INT -> "int ${it.name};"
				QueryHeader.LangType.FLOAT -> "float ${it.name};"
				QueryHeader.LangType.DATE -> "Date ${it.name};"
				QueryHeader.LangType.CHAR -> "char ${it.name};"
				QueryHeader.LangType.STRING -> "String ${it.name};"
				QueryHeader.LangType.UNKNOWN -> "UNKNOWN ${it.name};"
				QueryHeader.LangType.TIME -> "Date ${it.name};"
				QueryHeader.LangType.DATETIME -> "Date ${it.name};"
				QueryHeader.LangType.BIN -> "UNKNOWN ${it.name};"
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
				QueryHeader.LangType.BOOLEAN -> "val ${it.name}: Boolean"
				QueryHeader.LangType.INT -> "val ${it.name}: Int"
				QueryHeader.LangType.FLOAT -> "val ${it.name}: Float"
				QueryHeader.LangType.DATE -> "val ${it.name}: LocalDate"
				QueryHeader.LangType.CHAR -> "val ${it.name}: Char"
				QueryHeader.LangType.STRING -> "val ${it.name}: String"
				QueryHeader.LangType.UNKNOWN -> "val ${it.name}: UNKNOWN"
				QueryHeader.LangType.TIME -> "val ${it.name}: LocalTime"
				QueryHeader.LangType.DATETIME -> "val ${it.name}: LocalDateTime"
				QueryHeader.LangType.BIN -> "val ${it.name}: UNKNOWN"
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
				QueryHeader.LangType.BOOLEAN -> "${it.name}: boolean;"
				QueryHeader.LangType.INT -> "${it.name}: number;"
				QueryHeader.LangType.FLOAT -> "${it.name}: number;"
				QueryHeader.LangType.DATE -> "${it.name}: date;"
				QueryHeader.LangType.CHAR -> "${it.name}: string;"
				QueryHeader.LangType.STRING -> "${it.name}: string;"
				QueryHeader.LangType.UNKNOWN -> "${it.name}: UNKNOWN;"
				QueryHeader.LangType.TIME -> "${it.name}: date;"
				QueryHeader.LangType.DATETIME -> "${it.name}: date;"
				QueryHeader.LangType.BIN -> "${it.name}: UNKNOWN;"
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
