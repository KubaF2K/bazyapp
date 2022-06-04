package org.example.bazyapp

import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.*
import javafx.scene.control.cell.PropertyValueFactory
import javafx.stage.Stage
import org.example.bazyapp.models.Magazyn
import java.net.URL
import java.util.*

class MagazynPickerController(val parent: DetailWindowController): Initializable {

    @FXML lateinit var tvMagazyny: TableView<Magazyn>

    @FXML lateinit var tvMagazynyId: TableColumn<Magazyn, Int>
    @FXML lateinit var tvMagazynyMiejsce: TableColumn<Magazyn, String>
    @FXML lateinit var tvMagazynyNazwa: TableColumn<Magazyn, String>
    @FXML lateinit var tvMagazynyWybierz: TableColumn<Magazyn, Button>

    @FXML
    override fun initialize(p0: URL?, p1: ResourceBundle?) {
        tvMagazynyId.cellValueFactory = PropertyValueFactory("idMagazynu")
        tvMagazynyMiejsce.cellValueFactory = PropertyValueFactory("miejsce")
        tvMagazynyNazwa.cellValueFactory = PropertyValueFactory("nazwa")
        tvMagazynyWybierz.setCellFactory {
            val btnPick = Button("Wybierz")
            val pickCell = object : TableCell<Magazyn, Button?>() {
                override fun updateItem(p0: Button?, p1: Boolean) {
                    super.updateItem(p0, p1)
                    graphic = if (p1 || tableRow.item == null) null else btnPick
                }
            }
            btnPick.setOnAction {
                parent.txtFldNewMagazynId.text = pickCell.tableRow.item.idMagazynu.toString()
                (tvMagazyny.scene.window as Stage).close()
            }
            pickCell
        }
        tvMagazyny.items = parent.parent.magazynyList
    }
}