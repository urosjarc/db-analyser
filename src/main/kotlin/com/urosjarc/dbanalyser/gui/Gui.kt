package com.urosjarc.dbanalyser.gui

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage
import jfxtras.styles.jmetro.JMetro
import jfxtras.styles.jmetro.Style

class Gui : Application() {
    override fun start(stage: Stage) {

        val fxmlLoader = FXMLLoader(Gui::class.java.getResource("windows/DbAnalyser.fxml"))
        val scene = Scene(fxmlLoader.load(), 320.0, 240.0)
        val jMetro = JMetro(Style.DARK)
        stage.scene = scene
        jMetro.scene = scene
        stage.show()
    }

    companion object {
        fun init() = launch(Gui::class.java)
    }

}
