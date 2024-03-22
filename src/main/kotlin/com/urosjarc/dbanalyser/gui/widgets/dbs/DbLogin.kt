package com.urosjarc.dbanalyser.gui.widgets.dbs

import com.urosjarc.dbanalyser.app.client.ClientRepo
import com.urosjarc.dbanalyser.app.db.Db
import com.urosjarc.dbanalyser.app.db.DbRepo
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.scene.input.MouseEvent
import javafx.stage.FileChooser
import org.apache.logging.log4j.kotlin.logger
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


abstract class DbLoginUi : KoinComponent {

	@FXML
	lateinit var nameTF: TextField

	@FXML
	lateinit var urlTF: TextField

	@FXML
	lateinit var usernameTF: TextField

	@FXML
	lateinit var passwordTF: PasswordField

	@FXML
	lateinit var loginB: Button

	@FXML
	lateinit var typeCB: ChoiceBox<Db.Type>

	@FXML
	lateinit var dbLV: ListView<Db>

	@FXML
	lateinit var deleteB: Button

	@FXML
	lateinit var filesB: Button

	@FXML
	lateinit var saveB: Button

	@FXML
	lateinit var clearB: Button
}

class DbLogin : DbLoginUi() {
	val log = this.logger()
	val dbRepo by this.inject<DbRepo>()

	@FXML
	fun initialize() {
		this.log.info(this.javaClass)
		this.dbLV.items.setAll(this.dbRepo.data)
		this.typeCB.items.setAll(Db.Type.values().toList())
		this.dbRepo.onData { this.dbLV.items.setAll(this.dbRepo.data) }
		this.loginB.setOnAction { this.login() }
		this.saveB.setOnAction { this.save() }
		this.clearB.setOnAction { this.setDb() }

		this.dbLV.setOnMouseClicked { this.select(it) }
		this.deleteB.setOnAction { this.delete() }
		this.filesB.setOnAction { this.files() }
	}

	fun files() {
		FileChooser().also {
			it.title = "Database Path"
			it.extensionFilters.addAll(
				FileChooser.ExtensionFilter("SQLite", "*.sqlite"),
				FileChooser.ExtensionFilter("SQLite", "*.h2")
			)
			it.showOpenDialog(null)?.let { file ->
				this.nameTF.text = file.nameWithoutExtension
				this.urlTF.text = "jdbc:${file.extension}:${file.path}"
			}
		}
	}

	fun select(mouseEvent: MouseEvent) {
		if (mouseEvent.clickCount == 2) {
			this.login()
			return
		}
		var db: Db = this.dbLV.selectionModel.selectedItem ?: return
		db = this.dbRepo.find(db) ?: db
		this.setDb(db = db)
	}

	fun setDb(db: Db? = null) {
		this.nameTF.text = db?.name ?: ""
		this.usernameTF.text = db?.user ?: ""
		this.passwordTF.text = db?.password ?: ""
		this.urlTF.text = db?.url ?: ""
		this.typeCB.value = db?.type
	}

	private fun login() {
		this.getDb()?.let {
			val saved = this.dbRepo.save(it)
			dbRepo.chose(saved)
		}
	}

	fun save() {
		this.getDb()?.let { this.dbRepo.save(it) }
	}


	fun delete() {
		this.dbRepo.delete(dbName = this.nameTF.text, dbType = this.typeCB.value)
		this.setDb()
	}

	fun getDb(): Db? {
		val type: Db.Type?

		if (typeCB.value == null)
			return null
		else type = typeCB.value

		return Db(
			name = nameTF.text.ifBlank { return null },
			user = usernameTF.text,
			password = passwordTF.text,
			url = urlTF.text,
			type = type
		)
	}
}
