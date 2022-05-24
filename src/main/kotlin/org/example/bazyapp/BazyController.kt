package org.example.bazyapp

import javafx.application.Platform
import javafx.beans.binding.Bindings
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.fxml.Initializable
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.stage.Stage
import org.example.bazyapp.models.*
import java.net.URL
import java.util.*

class BazyController : Initializable {
    lateinit var dostawy: Map<Int, Dostawa>
    var dostawyList: ObservableList<Dostawa> = FXCollections.observableArrayList()

    lateinit var klienci: Map<Int, Klient>
    var klienciList: ObservableList<Klient> = FXCollections.observableArrayList()

    lateinit var magazyny: Map<Int, Magazyn>
    var magazynyList: ObservableList<Magazyn> = FXCollections.observableArrayList()

    lateinit var pracownicy: Map<Int, Pracownik>
    var pracownicyList: ObservableList<Pracownik> = FXCollections.observableArrayList()

    lateinit var produkty: Map<Int, Produkt>
    var produktyList: ObservableList<Produkt> = FXCollections.observableArrayList()

    lateinit var zamowienia: Map<Int, Zamowienie>
    var zamowieniaList: ObservableList<Zamowienie> = FXCollections.observableArrayList()
    var zamowieniaSzczegoly = HashMap<Int, MutableList<ZamowienieSzcz?>>()

    @FXML
    lateinit var tvDostawy: TableView<Dostawa>

    @FXML
    lateinit var tvDostawyId: TableColumn<Dostawa, Int>

    @FXML
    lateinit var tvDostawyProdukt: TableColumn<Dostawa, String>

    @FXML
    lateinit var tvDostawyMagazyn: TableColumn<Dostawa, String>

    @FXML
    lateinit var tvDostawyPracownik: TableColumn<Dostawa, Int>

    @FXML
    lateinit var tvDostawyKoszt: TableColumn<Dostawa, String>

    @FXML
    lateinit var tvDostawyIlosc: TableColumn<Dostawa, Int>

    @FXML
    lateinit var tvMagazyny: TableView<Magazyn>

    @FXML
    lateinit var tvMagazynyId: TableColumn<Magazyn, Int>

    @FXML
    lateinit var tvMagazynyMiejsce: TableColumn<Magazyn, String>

    @FXML
    lateinit var tvMagazynyNazwa: TableColumn<Magazyn, String>

    @FXML
    lateinit var tvMagazynyUsun: TableColumn<Magazyn, Button>

    @FXML
    lateinit var tvKlienci: TableView<Klient>

    @FXML
    lateinit var tvKlienciId: TableColumn<Klient, Int>

    @FXML
    lateinit var tvKlienciImie: TableColumn<Klient, String>

    @FXML
    lateinit var tvKlienciNazwisko: TableColumn<Klient, String>

    @FXML
    lateinit var tvKlienciPesel: TableColumn<Klient, Int>

    @FXML
    lateinit var tvKlienciUsun: TableColumn<Klient, Button>

    @FXML
    lateinit var tvPracownicy: TableView<Pracownik>

    @FXML
    lateinit var tvPracownicyId: TableColumn<Pracownik, Int>

    @FXML
    lateinit var tvPracownicyMagazyn: TableColumn<Pracownik?, String?>

    @FXML
    lateinit var tvPracownicyImie: TableColumn<Pracownik, String>

    @FXML
    lateinit var tvPracownicyNazwisko: TableColumn<Pracownik, String>

    @FXML
    lateinit var tvPracownicyAdres: TableColumn<Pracownik, String>

    @FXML
    lateinit var tvPracownicyPesel: TableColumn<Pracownik, Int>

    @FXML
    lateinit var tvPracownicyWyplata: TableColumn<Pracownik?, String?>

    @FXML
    lateinit var tvPracownicyUsun: TableColumn<Pracownik, Button>

    @FXML
    lateinit var tvProdukty: TableView<Produkt>

    @FXML
    lateinit var tvProduktyId: TableColumn<Produkt, Int>

    @FXML
    lateinit var tvProduktyNazwa: TableColumn<Produkt, String>

    @FXML
    lateinit var tvProduktyCena: TableColumn<Produkt?, String?>

    @FXML
    lateinit var tvProduktyIlosc: TableColumn<Produkt, Int>

    @FXML
    lateinit var tvProduktyUsun: TableColumn<Produkt, Button>

    @FXML
    lateinit var tvZamowienia: TableView<Zamowienie>

    @FXML
    lateinit var tvZamowieniaId: TableColumn<Zamowienie, Int>

    @FXML
    lateinit var tvZamowieniaKlient: TableColumn<Zamowienie, Int>

    @FXML
    lateinit var tvZamowieniaCena: TableColumn<Zamowienie?, String?>

    @FXML
    lateinit var tvZamowieniaStatus: TableColumn<Zamowienie, String>

    @FXML
    lateinit var tvZamowieniaPracownik: TableColumn<Zamowienie, Int>

    @FXML
    lateinit var tvZamowieniaSzczegoly: TableColumn<Zamowienie, Button>

    @FXML
    lateinit var tvZamowieniaAnuluj: TableColumn<Zamowienie, Button>

    private lateinit var dbConn: DBConn
    @FXML
    override fun initialize(url: URL?, bundle: ResourceBundle?) {
        val passPopup = Dialog<String>()

        passPopup.title = "Logowanie"
        passPopup.headerText = "Podaj hasło do bazy:"
        passPopup.dialogPane.buttonTypes.addAll(ButtonType.OK, ButtonType.CANCEL)

        val textPassword = PasswordField()
        passPopup.dialogPane.content = textPassword

        passPopup.setResultConverter { dialogButton: ButtonType ->
            if (dialogButton == ButtonType.OK)
                return@setResultConverter textPassword.text
            return@setResultConverter null
        }

        val password = passPopup.showAndWait()
        var passString = ""
        password.ifPresent { pass: String -> passString = pass }
        dbConn = DBConn(passString)

        tvDostawyId.cellValueFactory = PropertyValueFactory("idDostawy")
        tvDostawyProdukt.setCellValueFactory { i: TableColumn.CellDataFeatures<Dostawa?, String?> ->
            var `val` = "Brak!"
            if (i.value != null && produkty.containsKey(
                    i.value!!.idProduktu
                )
            ) `val` = produkty[i.value!!.idProduktu]!!.nazwa
            val finalVal = `val`
            Bindings.createStringBinding({ finalVal })
        }
        tvDostawyMagazyn.setCellValueFactory { i: TableColumn.CellDataFeatures<Dostawa?, String?> ->
            var `val` = "Brak!"
            if (i.value != null && magazyny.containsKey(
                    i.value!!.idMagazynu
                )
            ) `val` = magazyny[i.value!!.idMagazynu]!!.nazwa
            val finalVal = `val`
            Bindings.createStringBinding({ finalVal })
        }
        tvDostawyPracownik.cellValueFactory = PropertyValueFactory("idPracownika")
        tvDostawyKoszt.setCellValueFactory { i: TableColumn.CellDataFeatures<Dostawa?, String?> ->
            var `val` = "Brak!"
            if (i.value != null) {
                val koszt = i.value!!.koszt
                `val` = koszt.toString()
                if (`val`.length < 3) `val` += " gr" else {
                    `val` = StringBuilder(`val`).insert(`val`.length - 2, ".").append(" zł").toString()
                }
            }
            val finalVal = `val`
            Bindings.createStringBinding({ finalVal })
        }
        tvDostawyIlosc.cellValueFactory = PropertyValueFactory("ilosc")

        tvMagazynyId.cellValueFactory = PropertyValueFactory("idMagazynu")
        tvMagazynyMiejsce.cellValueFactory = PropertyValueFactory("miejsce")
        tvMagazynyNazwa.cellValueFactory = PropertyValueFactory("nazwa")
        tvMagazynyUsun.setCellFactory {
            val btnDel = Button("Usuń")
            val delCell: TableCell<Magazyn, Button?> = object : TableCell<Magazyn, Button?>() {
                override fun updateItem(p0: Button?, p1: Boolean) {
                    super.updateItem(p0, p1)
                    graphic = if (p1 || tableRow.item == null) null else btnDel
                }
            }
            btnDel.onAction = EventHandler {
                val alert = Alert(Alert.AlertType.CONFIRMATION, "Czy na pewno chcesz usunąć ten magazyn?",
                    ButtonType.YES, ButtonType.NO)
                alert.title = "Usuwanie"
                val alertResult = alert.showAndWait()
                alertResult.ifPresent { button: ButtonType ->
                    if (button == ButtonType.YES) {
                        dbConn.delMagazyn(delCell.tableRow.item.idMagazynu)
                    }
                }
            }

            delCell
        }

        tvKlienciId.cellValueFactory = PropertyValueFactory("idKlienta")
        tvKlienciImie.cellValueFactory = PropertyValueFactory("imie")
        tvKlienciNazwisko.cellValueFactory = PropertyValueFactory("nazwisko")
        tvKlienciPesel.cellValueFactory = PropertyValueFactory("pesel")
        tvKlienciUsun.setCellFactory {
            val btnDel = Button("Usuń")
            val delCell: TableCell<Klient, Button?> = object : TableCell<Klient, Button?>() {
                override fun updateItem(p0: Button?, p1: Boolean) {
                    super.updateItem(p0, p1)
                    graphic = if (p1 || tableRow.item == null) null else btnDel
                }
            }
            btnDel.onAction = EventHandler {
                val alert = Alert(Alert.AlertType.CONFIRMATION, "Czy na pewno chcesz usunąć tego klienta? " +
                        "Jeśli klient ma aktywne zamówienia, nie będzie można go usunąć.",
                    ButtonType.YES, ButtonType.NO)
                alert.title = "Usuwanie"
                val alertResult = alert.showAndWait()
                alertResult.ifPresent { button: ButtonType ->
                    if (button == ButtonType.YES) {
                        dbConn.delKlient(delCell.tableRow.item.idKlienta)
                    }
                }
            }

            delCell
        }

        tvPracownicyId.cellValueFactory = PropertyValueFactory("idPracownika")
        tvPracownicyMagazyn.setCellValueFactory { i: TableColumn.CellDataFeatures<Pracownik?, String?> ->
            var `val` = "Brak!"
            if (i.value != null && magazyny.containsKey(
                    i.value!!.idMagazynu
                )
            ) `val` = magazyny[i.value!!.idMagazynu]!!.nazwa
            val finalVal = `val`
            Bindings.createStringBinding({ finalVal })
        }
        tvPracownicyImie.cellValueFactory = PropertyValueFactory("imie")
        tvPracownicyNazwisko.cellValueFactory = PropertyValueFactory("nazwisko")
        tvPracownicyAdres.cellValueFactory = PropertyValueFactory("adresZamieszkania")
        tvPracownicyPesel.cellValueFactory = PropertyValueFactory("pesel")
        tvPracownicyWyplata.setCellValueFactory { i: TableColumn.CellDataFeatures<Pracownik?, String?> ->
            var `val` = "Brak!"
            if (i.value != null) {
                val wyplata = i.value!!.wyplata
                `val` = wyplata.toString()
                if (`val`.length < 3) `val` += " gr" else {
                    `val` = StringBuilder(`val`).insert(`val`.length - 2, ".").append(" zł").toString()
                }
            }
            val finalVal = `val`
            Bindings.createStringBinding({ finalVal })
        }
        tvPracownicyUsun.setCellFactory {
            val btnDel = Button("Usuń")
            val delCell: TableCell<Pracownik, Button?> = object : TableCell<Pracownik, Button?>() {
                override fun updateItem(p0: Button?, p1: Boolean) {
                    super.updateItem(p0, p1)
                    graphic = if (p1 || tableRow.item == null) null else btnDel
                }
            }
            btnDel.onAction = EventHandler {
                val alert = Alert(Alert.AlertType.CONFIRMATION, "Czy na pewno chcesz usunąć tego pracownika?",
                    ButtonType.YES, ButtonType.NO)
                alert.title = "Usuwanie"
                val alertResult = alert.showAndWait()
                alertResult.ifPresent { button: ButtonType ->
                    if (button == ButtonType.YES) {
                        dbConn.delPracownik(delCell.tableRow.item.idPracownika)
                    }
                }
            }

            delCell
        }

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
        tvProduktyIlosc.cellValueFactory = PropertyValueFactory("ilosc")
        tvProduktyUsun.setCellFactory {
            val btnDel = Button("Usuń")
            val delCell: TableCell<Produkt, Button?> = object : TableCell<Produkt, Button?>() {
                override fun updateItem(p0: Button?, p1: Boolean) {
                    super.updateItem(p0, p1)
                    graphic = if (p1 || tableRow.item == null) null else btnDel
                }
            }
            btnDel.onAction = EventHandler {
                val alert = Alert(Alert.AlertType.CONFIRMATION, "Czy na pewno chcesz usunąć ten produkt?",
                    ButtonType.YES, ButtonType.NO)
                alert.title = "Usuwanie"
                val alertResult = alert.showAndWait()
                alertResult.ifPresent { button: ButtonType ->
                    if (button == ButtonType.YES) {
                        dbConn.delProdukt(delCell.tableRow.item.idProduktu)
                    }
                }
            }

            delCell
        }

        tvZamowieniaId.cellValueFactory = PropertyValueFactory("idZamowienia")
        tvZamowieniaKlient.cellValueFactory = PropertyValueFactory("idKlienta")
        tvZamowieniaCena.setCellValueFactory { i: TableColumn.CellDataFeatures<Zamowienie?, String?> ->
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
        tvZamowieniaStatus.cellValueFactory = PropertyValueFactory("status")
        tvZamowieniaPracownik.cellValueFactory = PropertyValueFactory("idPracownika")
        tvZamowieniaSzczegoly.setCellFactory {
            val viewButton = Button("Pokaż")
            val detailCell: TableCell<Zamowienie, Button?> = object : TableCell<Zamowienie, Button?>() {
                override fun updateItem(button: Button?, b: Boolean) {
                    super.updateItem(button, b)
                    graphic = if (b || tableRow.item == null) null else viewButton
                }
            }
            viewButton.onAction = EventHandler {
                val detailWindow = Stage()
                detailWindow.title = "Szczegóły zamówienia"
                val tvZamowieniaSzcz = TableView<ZamowienieSzcz?>()
                val detailScene = Scene(tvZamowieniaSzcz, 640.0, 480.0)
                detailWindow.scene = detailScene
                val tvZamowieniaSzczId = TableColumn<ZamowienieSzcz?, Int>("ID")
                val tvZamowieniaSzczProdId = TableColumn<ZamowienieSzcz?, Int>("ID Produktu")
                val tvZamowieniaSzczProd = TableColumn<ZamowienieSzcz?, String>("Nazwa Produktu")
                val tvZamowieniaSzczIlosc = TableColumn<ZamowienieSzcz?, Int>("Ilość")
                tvZamowieniaSzcz.columns.add(tvZamowieniaSzczId)
                tvZamowieniaSzcz.columns.add(tvZamowieniaSzczProdId)
                tvZamowieniaSzcz.columns.add(tvZamowieniaSzczProd)
                tvZamowieniaSzcz.columns.add(tvZamowieniaSzczIlosc)
                tvZamowieniaSzczId.cellValueFactory = PropertyValueFactory("idSzczegoly")
                tvZamowieniaSzczProdId.cellValueFactory = PropertyValueFactory("idProduktu")
                tvZamowieniaSzczProd.setCellValueFactory { i: TableColumn.CellDataFeatures<ZamowienieSzcz?, String> ->
                    var `val` = "Brak!"
                    if (i.value != null && produkty.containsKey(
                            i.value!!.idProduktu
                        )
                    ) `val` = produkty[i.value!!.idProduktu]!!.nazwa
                    val finalVal = `val`
                    Bindings.createStringBinding({ finalVal })
                }
                tvZamowieniaSzczIlosc.cellValueFactory = PropertyValueFactory("ilosc")
                tvZamowieniaSzcz.items = FXCollections.observableList(
                    zamowieniaSzczegoly[detailCell.tableRow.item.idZamowienia]
                )
                detailWindow.show()
            }
            detailCell
        }
        tvZamowieniaAnuluj.setCellFactory {
            val btnDel = Button("Usuń")
            val delCell: TableCell<Zamowienie, Button?> = object : TableCell<Zamowienie, Button?>() {
                override fun updateItem(p0: Button?, p1: Boolean) {
                    super.updateItem(p0, p1)
                    graphic = if (p1 || tableRow.item == null) null else btnDel
                }
            }
            btnDel.onAction = EventHandler {
                val alert = Alert(Alert.AlertType.CONFIRMATION, "Czy na pewno chcesz anulować to zamówienie?",
                    ButtonType.YES, ButtonType.NO)
                alert.title = "Usuwanie"
                val alertResult = alert.showAndWait()
                alertResult.ifPresent { button: ButtonType ->
                    if (button == ButtonType.YES) {
                        dbConn.delZamowienie(delCell.tableRow.item.idZamowienia)
                    }
                }
            }

            delCell
        }

        refresh()
    }

    fun refresh() {
        val alert = Alert(Alert.AlertType.INFORMATION)
        alert.title = "Łączenie..."
        alert.headerText = "Łączenie z bazą..."
        alert.show()

        dostawyList.clear()
        klienciList.clear()
        magazynyList.clear()
        pracownicyList.clear()
        produktyList.clear()
        zamowieniaList.clear()
        zamowieniaSzczegoly.clear()

        dostawy = dbConn.getDostawy()
        klienci = dbConn.getKlienci()
        magazyny = dbConn.getMagazyny()
        pracownicy = dbConn.getPracownicy()
        produkty = dbConn.getProdukty()
        zamowienia = dbConn.getZamowienia()

        for (dostawa in dostawy) dostawyList.add(dostawa.value)
        for (klient in klienci) klienciList.add(klient.value)
        for (magazyn in magazyny) magazynyList.add(magazyn.value)
        for (pracownik in pracownicy) pracownicyList.add(pracownik.value)
        for (produkt in produkty) produktyList.add(produkt.value)
        for (zamowienie in zamowienia) zamowieniaList.add(zamowienie.value)
        //TODO zamowienia szczegoly

        tvDostawy.items = dostawyList
        tvMagazyny.items = magazynyList
        tvKlienci.items = klienciList
        tvPracownicy.items = pracownicyList
        tvProdukty.items = produktyList
        tvZamowienia.items = zamowieniaList

        alert.close()
    }

    fun showAddDeliveryWindow() {
        val addDeliveryWindow = Stage()
        val root = VBox(10.0)
        val scene = Scene(root, 640.0, 480.0)

        val boxProdukt = HBox(10.0)
        val labelProdukt = Label("ID Produktu:")
        val textProdukt = TextField()
        boxProdukt.children.addAll(labelProdukt, textProdukt)

        val boxMagazyn = HBox(10.0)
        val labelMagazyn = Label("ID Magazynu")
        val textMagazyn = TextField()
        boxMagazyn.children.addAll(labelMagazyn, textMagazyn)

        val boxPracownik = HBox(10.0)
        val labelPracownik = Label("ID Pracownika")
        val textPracownik = TextField()
        boxPracownik.children.addAll(labelPracownik, textPracownik)

        val boxIlosc = HBox(10.0)
        val labelIlosc = Label("Ilość")
        val textIlosc = TextField()
        boxIlosc.children.addAll(labelIlosc, textIlosc)

        val boxBtns = HBox(10.0)
        val btnAdd = Button("Dodaj")
        btnAdd.onAction = EventHandler {
            try {
                val produktId = textProdukt.text.toInt()
                val magazynId = textMagazyn.text.toInt()
                val pracownikId = textPracownik.text.toInt()
                val ilosc = textIlosc.text.toInt()
                dbConn.addDelivery(produktId, magazynId, pracownikId, ilosc)
                addDeliveryWindow.close()
            } catch (e: NumberFormatException) {
                val alert = Alert(Alert.AlertType.ERROR)
                alert.headerText = "Zły format liczby!"
                alert.show()
            }
        }
        val btnCancel = Button("Anuluj")
        btnCancel.onAction = EventHandler {
            addDeliveryWindow.close()
        }
        boxBtns.children.addAll(btnAdd, btnCancel)

        root.children.addAll(boxProdukt, boxMagazyn, boxPracownik, boxIlosc, boxBtns)

        addDeliveryWindow.scene = scene
        addDeliveryWindow.title = "Dodawanie dostawy"
        addDeliveryWindow.show()
    }

    fun exit() {
        Platform.exit()
    }

    enum class DetailWindowType {
        KLIENT, MAGAZYN, PRACOWNIK, PRODUKT, ZAMOWIENIE
    }

    fun showDetailWindow(type: DetailWindowType) {
        val detailLoader = FXMLLoader(javaClass.getResource("detail-view.fxml"))
        val detailController = DetailWindowController(type, dbConn)
        detailLoader.setController(detailController)
        val stage = Stage()
        val scene = Scene(detailLoader.load(), 640.0, 480.0)
        stage.title = "Wyświetl"
        stage.scene = scene
        stage.show()
    }

    fun showClientDetailWindow() {
        showDetailWindow(DetailWindowType.KLIENT)
    }

    fun showWarehouseDetailWindow() {
        showDetailWindow(DetailWindowType.MAGAZYN)
    }

    fun showWorkerDetailWindow() {
        showDetailWindow(DetailWindowType.PRACOWNIK)
    }

    fun showProductDetailWindow() {
        showDetailWindow(DetailWindowType.PRODUKT)
    }

    fun showOrderDetailWindow() {
        showDetailWindow(DetailWindowType.ZAMOWIENIE)
    }
}