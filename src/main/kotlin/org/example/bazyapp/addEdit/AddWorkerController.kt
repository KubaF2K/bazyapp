package org.example.bazyapp.addEdit

import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.control.Alert
import javafx.scene.control.TextField
import javafx.stage.Stage
import org.example.bazyapp.BaseController
import org.example.bazyapp.DBConn
import org.example.bazyapp.WarehousePickerController

class AddWorkerController(val parent: BaseController, private val dbConn: DBConn) {
    @FXML lateinit var txtFldWarehouseId: TextField
    @FXML lateinit var txtFldName: TextField
    @FXML lateinit var txtFldSurname: TextField
    @FXML lateinit var txtFldAddress: TextField
    @FXML lateinit var txtFldPesel: TextField
    @FXML lateinit var txtFldPayout: TextField

    fun pickWarehouse() {
        val pickerWindow = Stage()
        pickerWindow.title = "Wybierz magazyn"
        val loader = FXMLLoader(javaClass.getResource("../warehouse-picker-view.fxml"))
        val pickerController = WarehousePickerController(this)
        loader.setController(pickerController)
        val scene = Scene(loader.load(), 600.0, 400.0)
        pickerWindow.scene = scene
        pickerWindow.show()
    }

    fun add() {
        try {
            if (txtFldName.text.isNotBlank() && txtFldSurname.text.isNotBlank() && txtFldAddress.text.isNotBlank()) {
                dbConn.addWorker(
                    txtFldWarehouseId.text.toInt(),
                    txtFldName.text,
                    txtFldSurname.text,
                    txtFldAddress.text,
                    txtFldPesel.text.toLong(),
                    (txtFldPayout.text.toFloat() * 100).toInt()
                )
                parent.refresh()
                (txtFldWarehouseId.scene.window as Stage).close()
            } else {
                val alert = Alert(Alert.AlertType.WARNING)
                alert.title = "Błąd!"
                alert.headerText = "Brak tekstu w polu tekstowym!"
                alert.show()
            }
        } catch (e: NumberFormatException) {
            val alert = Alert(Alert.AlertType.WARNING)
            alert.title = "Błąd!"
            alert.headerText = "Błędny format liczby!"
            alert.show()
        }
    }
}