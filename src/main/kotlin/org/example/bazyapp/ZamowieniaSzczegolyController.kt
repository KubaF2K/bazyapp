package org.example.bazyapp

import javafx.beans.binding.Bindings
import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.cell.PropertyValueFactory
import org.example.bazyapp.models.ZamowienieSzcz
import java.net.URL
import java.util.*

class ZamowieniaSzczegolyController(val dbConn: DBConn, val id: Int): Initializable {
    @FXML lateinit var tvZamowieniaSzcz: TableView<ZamowienieSzcz>
    @FXML lateinit var tvZamowieniaSzczId: TableColumn<ZamowienieSzcz, Int>
    @FXML lateinit var tvZamowieniaSzczProdId: TableColumn<ZamowienieSzcz, Int>
    @FXML lateinit var tvZamowieniaSzczProd: TableColumn<ZamowienieSzcz, String>
    @FXML lateinit var tvZamowieniaSzczIlosc: TableColumn<ZamowienieSzcz, Int>

    @FXML
    override fun initialize(p0: URL?, p1: ResourceBundle?) {
        tvZamowieniaSzczId.cellValueFactory = PropertyValueFactory("idSzczegoly")
        tvZamowieniaSzczProdId.cellValueFactory = PropertyValueFactory("idProduktu")
        tvZamowieniaSzczProd.setCellValueFactory { i: TableColumn.CellDataFeatures<ZamowienieSzcz, String> ->
            var text = "Brak!"
            val produkt = dbConn.getProdukt(i.value.idProduktu)
            if (produkt != null)
                text = produkt.nazwa
            val finalText = text
            Bindings.createStringBinding({ finalText })
        }
        tvZamowieniaSzczIlosc.cellValueFactory = PropertyValueFactory("ilosc")
        tvZamowieniaSzcz.items = FXCollections.observableList(dbConn.getZamowienieSzczegoly(id))
    }
}