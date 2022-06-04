package org.example.bazyapp

import javafx.beans.binding.Bindings
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.*
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.layout.HBox
import javafx.stage.Stage
import org.example.bazyapp.models.Produkt
import java.net.URL
import java.util.*

class ProductSelectorController(private val root: AddOrderController): Initializable {
    @FXML lateinit var tvProdukty: TableView<Produkt>

    @FXML lateinit var tvProduktyId: TableColumn<Produkt, Int>
    @FXML lateinit var tvProduktyNazwa: TableColumn<Produkt, String>
    @FXML lateinit var tvProduktyCena: TableColumn<Produkt, String>
    @FXML lateinit var tvProduktyIlosc: TableColumn<Produkt, Int>
    @FXML lateinit var tvProduktyDodaj: TableColumn<Produkt, HBox>

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
            val cellBox = HBox(10.0)
            val btnAdd = Button("Dodaj")
            val txtFldCount = TextField("0")
            cellBox.children.addAll(btnAdd, txtFldCount)
            val addCell = object : TableCell<Produkt, HBox?>() {
                var selected = false
                override fun updateItem(p0: HBox?, p1: Boolean) {
                    super.updateItem(p0, p1)
                    graphic = if (p1 || tableRow.item == null) null else cellBox
                }
            }
            btnAdd.setOnAction {
                val id = addCell.tableRow.item.idProduktu
                if (!addCell.selected) {
                    try {
                        val count = txtFldCount.text.toInt()
                        itemSet[id] = count
                        addCell.selected = true
                        btnAdd.text = "Usuń"
                    } catch (e: NumberFormatException) {
                        val alert = Alert(Alert.AlertType.WARNING)
                        alert.title = "Błąd!"
                        alert.headerText = "Błędny format liczby!"
                        alert.contentText = e.message
                        alert.showAndWait()
                    }
                }
                else {
                    itemSet.remove(id)
                    addCell.selected = false
                    btnAdd.text = "Dodaj"
                }
            }
            addCell
        }
        tvProdukty.items = root.root.produktyList
    }

    fun returnBack() {
        var i = 0
        val returnArray = Array<Array<Any>>(itemSet.size) { emptyArray() }
        for (item in itemSet)
            returnArray[i++] = arrayOf(item.key, item.value)
        root.itemsTable = returnArray
        (tvProdukty.scene.window as Stage).close()
    }
}