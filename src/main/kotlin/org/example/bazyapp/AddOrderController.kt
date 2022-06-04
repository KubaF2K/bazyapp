package org.example.bazyapp

import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.fxml.Initializable
import javafx.scene.Scene
import javafx.scene.control.Alert
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.stage.Stage
import java.net.URL
import java.util.*

class AddOrderController (val root: BazyController, private val dbConn: DBConn, private var idKlienta: Int? = null): Initializable {
    @FXML lateinit var txtFldId: TextField
    @FXML lateinit var lblItem: Label
    @FXML lateinit var btnAdd: Button

    var itemsTable: Array<Array<Any>> = emptyArray()
        set(value) {
            field = value
            var itemString = ""
            for (pair in itemsTable) {
                itemString += """
                    ${root.produkty[pair[0]]?.nazwa ?: ("Nieznany produkt o id " + pair[0])}: ${pair[1]} sztuk
                    
                """.trimIndent()
            }
            lblItem.text = itemString
            if (itemsTable.isNotEmpty())
                btnAdd.isDisable = false
        }

    @FXML
    override fun initialize(p0: URL?, p1: ResourceBundle?) {
        txtFldId.text = idKlienta?.toString()
    }

    fun showItemSelection() {
        val selectorWindow = Stage()
        selectorWindow.title = "Wybierz produkty"
        val loader = FXMLLoader(javaClass.getResource("product-selector-view.fxml"))
        val selectorController = ProductSelectorController(this)
        loader.setController(selectorController)
        val scene = Scene(loader.load(), 600.0, 400.0)
        selectorWindow.scene = scene
        selectorWindow.show()
    }

    fun add() {
        try {
            idKlienta = txtFldId.text.toInt()
            if (itemsTable.isEmpty() || idKlienta == null)
                return
            dbConn.addZamowienie(idKlienta!!, itemsTable)
            root.refresh()
            (txtFldId.scene.window as Stage).close()
        } catch (e: NumberFormatException) {
            val alert = Alert(Alert.AlertType.WARNING)
            alert.title = "Błąd!"
            alert.headerText = "Błędny format liczby!"
            alert.contentText = e.message
            alert.showAndWait()
        }
    }
}