package com.urosjarc.dbanalyser.gui.parts

import com.urosjarc.dbanalyser.app.db.Db
import com.urosjarc.dbanalyser.app.db.DbRepo
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.PasswordField
import javafx.scene.control.TextField
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

abstract class DbLoginPartUi: KoinComponent {

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

}

class DbLoginPart: DbLoginPartUi() {
    val dbRepo by this.inject<DbRepo>()
    @FXML
    fun initialize(){
        this.loginB.setOnAction { this.onLogin() }
    }

    private fun onLogin() {
        this.dbRepo.save(Db(
            name=this.nameTF.text,
            username = this.usernameTF.text,
            password = this.passwordTF.text,
            url=this.urlTF.text
        ))
    }
}
