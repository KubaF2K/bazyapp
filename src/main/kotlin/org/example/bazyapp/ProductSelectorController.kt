package org.example.bazyapp

import javafx.beans.binding.Bindings
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.*
import javafx.scene.control.cell.PropertyValueFactory
import javafx.stage.Stage
import org.example.bazyapp.addEdit.AddOrderController
import org.example.bazyapp.models.Produkt
import java.net.URL
import java.util.*

class ProductSelectorController(private val parent: AddOrderController): Initializable {
    @FXML lateinit var tvProdukty: TableView<Produkt>

    @FXML lateinit var tvProduktyId: TableColumn<Produkt, Int>
    @FXML lateinit var tvProduktyNazwa: TableColumn<Produkt, String>
    @FXML lateinit var tvProduktyCena: TableColumn<Produkt, String>
    @FXML lateinit var tvProduktyIlosc: TableColumn<Produkt, Int>
    @FXML lateinit var tvProduktyDodaj: TableColumn<Produkt, Button>

    @FXML lateinit var lblList: Label

    val itemSet = HashMap<Int, Int>()

    @FXML
    override fun initialize(p0: URL?, p1: ResourceBundle?) {
        tvProduktyId.cellValueFactory = PropertyValueFactory("idProduktu")
        tvProduktyNazwa.cellValueFactory = PropertyValueFactory("nazwa")
        tvProduktyCena.setCellValueFactory { i: TableColumn.CellDataFeatures<Produkt?, String?> ->
            var `val` = "Brak!"
            if (i.value != null) {
                val cena = i.value!!.cena
                `val` = cena.toString()
                if (`val`.length < 3) `val` += " gr" else {
                    `val` = StringBuilder(`val`).insert(`val`.length - 2, ".").append(" zł").toString()
                }
            }
            val finalVal = `val`
            Bindings.createStringBinding({ finalVal })
        }
//        TODO fix weird textbox and button behavior
        tvProduktyIlosc.cellValueFactory = PropertyValueFactory("ilosc")
        tvProduktyDodaj.setCellFactory {
            val btnAdd = Button("Dodaj/Usuń")
            val addCell = object : TableCell<Produkt, Button?>() {
                override fun updateItem(p0: Button?, p1: Boolean) {
                    super.updateItem(p0, p1)
                    graphic = if (p1 || tableRow.item == null) null else btnAdd
                }
            }
            btnAdd.setOnAction {
                val id = addCell.tableRow.item.idProduktu
                if (itemSet.containsKey(id)) {
                    itemSet.remove(id)
                    refreshList()
                }
                else {
                    try {
                        val inputDialog = TextInputDialog()
                        inputDialog.title = "Ilość"
                        inputDialog.headerText = "Podaj ilość:"
                        val count = inputDialog.showAndWait().get().toInt()
                        itemSet[id] = count
                        refreshList()
                    } catch (e: NumberFormatException) {
                        val alert = Alert(Alert.AlertType.WARNING)
                        alert.title = "Błąd!"
                        alert.headerText = "Błędny format liczby!"
                        alert.contentText = e.message
                        alert.showAndWait()
                    }
                }
            }
            addCell
        }
        tvProdukty.items = parent.parent.produktyList
    }

    fun refreshList() {
        var listString = ""
        for (item in itemSet) {
            listString += """
                ${parent.parent.produkty[item.key]?.nazwa ?: ("Nieznany produkt o id " + item.key)}: ${item.value} sztuk
                    
            """.trimIndent()
        }
        lblList.text = listString
    }

    fun returnBack() {
        var i = 0
        val returnArray = Array<Array<Any>>(itemSet.size) { emptyArray() }
        for (item in itemSet)
            returnArray[i++] = arrayOf(item.key, item.value)
        parent.itemsTable = returnArray
        (tvProdukty.scene.window as Stage).close()
    }
}