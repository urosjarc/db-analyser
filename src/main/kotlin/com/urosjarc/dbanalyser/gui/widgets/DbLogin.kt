package com.urosjarc.dbanalyser.gui.widgets

import com.urosjarc.dbanalyser.app.client.ClientRepo
import com.urosjarc.dbanalyser.app.db.Db
import com.urosjarc.dbanalyser.app.db.DbRepo
import com.urosjarc.dbanalyser.app.logs.LogService
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.scene.input.MouseEvent
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

}

class DbLogin : DbLoginUi() {
    val dbRepo by this.inject<DbRepo>()
    val clientRepo by this.inject<ClientRepo>()
    val logService by this.inject<LogService>()

    @FXML
    fun initialize() {
        this.logService.info("Inited!!!")
        this.dbLV.items.setAll(this.dbRepo.data)
        this.typeCB.items.setAll(Db.Type.values().toList())
        this.dbRepo.onChange { this.dbLV.items.setAll(this.dbRepo.data) }
        this.loginB.setOnAction { this.login() }
        this.dbLV.setOnMouseClicked { this.select(it) }
        this.clientRepo.onError { this.clientRepoError(msg = it) }
    }

    fun clientRepoError(msg: String) {
        Alert(Alert.AlertType.ERROR, msg, ButtonType.OK).show()
    }

    fun select(mouseEvent: MouseEvent) {
        if(mouseEvent.clickCount >= 2) return this.login()
        var db = this.dbLV.selectionModel.selectedItem
        db = this.dbRepo.find(db) ?: db
        this.nameTF.text = db.name
        this.usernameTF.text = db.user
        this.passwordTF.text = db.password
        this.urlTF.text = db.url
        this.typeCB.value = db.type
        this.dbRepo.select(db)
    }

    fun login() {
        val db = Db(
            name = this.nameTF.text,
            user = this.usernameTF.text,
            password = this.passwordTF.text,
            url = this.urlTF.text,
            type = this.typeCB.value
        )
        this.dbRepo.save(db)
        this.dbRepo.select(db)
    }
}
