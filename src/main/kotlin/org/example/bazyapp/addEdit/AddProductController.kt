package org.example.bazyapp.addEdit

import javafx.fxml.FXML
import javafx.scene.control.Alert
import javafx.scene.control.TextField
import javafx.stage.Stage
import org.example.bazyapp.BaseController
import org.example.bazyapp.DBConn

class AddProductController(private val parent: BaseController, private val dbConn: DBConn) {
    @FXML lateinit var txtFldName: TextField
    @FXML lateinit var txtFldPrice: TextField
    @FXML lateinit var txtFldCount: TextField

    fun add() {
        try {
            if (txtFldName.text.isNotBlank()) {
                dbConn.addProduct(
                    txtFldName.text,
                    (txtFldPrice.text.toFloat() * 100).toInt(),
                    if (txtFldCount.text.isBlank()) 0 else txtFldCount.text.toInt()
                )
                parent.refresh()
                (txtFldName.scene.window as Stage).close()
            }
            else {
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