package org.example.bazyapp.addEdit

import javafx.fxml.FXML
import javafx.scene.control.Alert
import javafx.scene.control.TextField
import javafx.stage.Stage
import org.example.bazyapp.BaseController
import org.example.bazyapp.DBConn

class AddWarehouseController(private val parent: BaseController, private val dbConn: DBConn) {
    @FXML lateinit var txtFldAddress: TextField
    @FXML lateinit var txtFldName: TextField

    fun add() {
        if (txtFldAddress.text.isNotBlank() && txtFldName.text.isNotBlank()) {
            dbConn.addWarehouse(txtFldAddress.text, txtFldName.text)
            parent.refresh()
            (txtFldAddress.scene.window as Stage).close()
        }
        else {
            val alert = Alert(Alert.AlertType.WARNING)
            alert.title = "Błąd!"
            alert.headerText = "Brak tekstu w polu tekstowym!"
            alert.show()
        }
    }
}