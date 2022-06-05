package org.example.bazyapp

import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.fxml.Initializable
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.scene.text.Text
import javafx.stage.Stage
import java.net.URL
import java.util.*

class DetailWindowController(
    val parent: BaseController,
    private val type: BaseController.DetailWindowType,
    private val dbConn: DBConn, private val id: Int? = null,
    private val showingHistorical: Boolean = false
) : Initializable {
    var displayedId: Int = id ?: 0
    var displayedHistorical = showingHistorical

    lateinit var txtFldNewMagazynId: TextField

    @FXML
    lateinit var txtFldId: TextField

    @FXML
    lateinit var boxOut: VBox

    @FXML
    lateinit var textId: Text

    @FXML
    lateinit var chkHistorical: CheckBox

    @FXML
    override fun initialize(p0: URL?, p1: ResourceBundle?) {
        if (type == BaseController.DetailWindowType.ZAMOWIENIE) {
            chkHistorical.isVisible = true
            chkHistorical.isSelected = showingHistorical
        }
        if (id != null) {
            txtFldId.text = id.toString()
            display()
        }
    }

    fun refresh() {
        txtFldId.text = displayedId.toString()
        display()
    }

    fun display() {
        displayedId = txtFldId.text.toInt()
        when (type) {
            BaseController.DetailWindowType.KLIENT -> {
                val textName = Text()
                val textPesel = Text()
                val lblNewSurname = Label("Nowe nazwisko:")
                val txtFldNewSurname = TextField()
                val btnEdit = Button("Zmień nazwisko")
                btnEdit.setOnAction {
                    if (txtFldNewSurname.text.isNotBlank()) {
                        dbConn.editClient(displayedId, txtFldNewSurname.text)
                        parent.refresh()
                        refresh()
                    }
                    else {
                        val alert = Alert(Alert.AlertType.WARNING)
                        alert.title = "Błąd!"
                        alert.headerText = "Brak tekstu w polu tekstowym!"
                        alert.contentText = "Wpisz nową nazwę!"
                        alert.show()
                    }
                }
                boxOut.children.setAll(textId, textName, textPesel, lblNewSurname, txtFldNewSurname, btnEdit)
                try {
                    val klient = dbConn.getClient(displayedId)
                    textId.text = "ID: " + (klient?.idKlienta ?: "Brak klienta o podanym ID!")
                    textName.text = "Imię i nazwisko: " + (klient?.imie ?: "") + " " + (klient?.nazwisko ?: "")
                    textPesel.text = "PESEL: " + klient?.pesel
                } catch (e: NumberFormatException) {
                    val alert = Alert(Alert.AlertType.WARNING, e.message)
                    alert.headerText = "Błędny format liczby!"
                    alert.show()
                }
            }
            BaseController.DetailWindowType.MAGAZYN -> {
                val textAddr = Text()
                val textName = Text()
                val lblNewName = Label("Nowa nazwa:")
                val txtFldNewName = TextField()
                val btnEdit = Button("Zmień nazwę")
                btnEdit.setOnAction {
                    if (txtFldNewName.text.isNotBlank()) {
                        dbConn.editWarehouse(displayedId, txtFldNewName.text)
                        parent.refresh()
                        refresh()
                    }
                    else {
                        val alert = Alert(Alert.AlertType.WARNING)
                        alert.title = "Błąd!"
                        alert.headerText = "Brak tekstu w polu tekstowym!"
                        alert.contentText = "Wpisz nową nazwę!"
                        alert.show()
                    }
                }
                boxOut.children.setAll(textId, textAddr, textName, lblNewName, txtFldNewName, btnEdit)
                try {
                    val magazyn = dbConn.getWarehouse(displayedId)
                    textId.text = "ID: " + (magazyn?.idMagazynu ?: "Brak magazynu o podanym ID!")
                    textAddr.text = "Adres: " + (magazyn?.miejsce ?: "")
                    textName.text = "Nazwa: " + (magazyn?.nazwa ?: "")
                } catch (e: NumberFormatException) {
                    val alert = Alert(Alert.AlertType.WARNING, e.message)
                    alert.headerText = "Błędny format liczby!"
                    alert.show()
                }
            }
            BaseController.DetailWindowType.PRACOWNIK -> {
                val boxMagazyn = HBox(10.0)
                val textMagazynId = Text()
                val btnMagazynShow = Button("Wyświetl")
                btnMagazynShow.isDisable = true
                boxMagazyn.children.addAll(textMagazynId, btnMagazynShow)

                val boxNewMagazyn = HBox(10.0)
                val lblNewMagazynId = Label("ID nowego magazynu: ")
                txtFldNewMagazynId = TextField()
                val btnPickNewMagazyn = Button("Wybierz z listy...")
                btnPickNewMagazyn.setOnAction {
                    val pickerWindow = Stage()
                    pickerWindow.title = "Wybierz magazyn"
                    val loader = FXMLLoader(javaClass.getResource("warehouse-picker-view.fxml"))
                    val pickerController = WarehousePickerController(this)
                    loader.setController(pickerController)
                    val scene = Scene(loader.load(), 600.0, 400.0)
                    pickerWindow.scene = scene
                    pickerWindow.show()
                }
                boxNewMagazyn.children.addAll(lblNewMagazynId, txtFldNewMagazynId, btnPickNewMagazyn)

                val textName = Text()
                val lblNewName = Label("Nowe nazwisko:")
                val txtFldNewName = TextField()

                val textAddr = Text()
                val lblNewAddr = Label("Nowy adres:")
                val txtFldNewAddr = TextField()

                val textPesel = Text()

                val textPayout = Text()
                val lblNewPayout = Label("Nowa kwota wypłaty:")
                val txtFldNewPayout = TextField()

                val btnEdit = Button("Zapisz zmiany")
                btnEdit.setOnAction {
                    try {
                        dbConn.editWorker(
                            displayedId,
                            if (txtFldNewMagazynId.text.isNotBlank()) txtFldNewMagazynId.text.toInt() else null,
                            if (txtFldNewName.text.isNotBlank()) txtFldNewName.text else null,
                            if (txtFldNewAddr.text.isNotBlank()) txtFldNewAddr.text else null,
                            if (txtFldNewPayout.text.isNotBlank()) (txtFldNewPayout.text.toFloat() * 100).toInt() else null
                        )
                        parent.refresh()
                        refresh()
                    } catch (e: NumberFormatException) {
                        val alert = Alert(Alert.AlertType.WARNING, e.message)
                        alert.headerText = "Błędny format liczby!"
                        alert.show()
                    }
                }

                boxOut.children.setAll(textId, boxMagazyn, boxNewMagazyn, textName, lblNewName, txtFldNewName, textAddr,
                    lblNewAddr, txtFldNewAddr, textPesel, textPayout, lblNewPayout, txtFldNewPayout, btnEdit)
                try {
                    val pracownik = dbConn.getWorker(displayedId)
                    textId.text = "ID: " + (pracownik?.idPracownika ?: "Brak pracownika o podanym ID!")
                    textMagazynId.text = "ID magazynu: " + (pracownik?.idMagazynu ?: "Brak magazynu o podanym ID!")
                    if (pracownik != null) {
                        btnMagazynShow.isDisable = false
                        btnMagazynShow.setOnAction {
                            val detailLoader = FXMLLoader(javaClass.getResource("detail-view.fxml"))
                            val detailController = DetailWindowController(
                                parent,
                                BaseController.DetailWindowType.MAGAZYN,
                                dbConn, pracownik.idMagazynu
                            )
                            detailLoader.setController(detailController)
                            val stage = Stage()
                            val scene = Scene(detailLoader.load(), 640.0, 480.0)
                            stage.title = "Wyświetl"
                            stage.scene = scene
                            stage.show()
                        }
                    }
                    textName.text = "Imię i nazwisko: " + (pracownik?.imie ?: "") + " " + (pracownik?.nazwisko ?: "")
                    textAddr.text = "Adres zamieszkania: " + (pracownik?.adresZamieszkania ?: "")
                    textPesel.text = "PESEL: " + (pracownik?.pesel ?: "")
                    if (pracownik != null) {
                        var wyplata = pracownik.wyplata.toString()
                        if (wyplata.length < 3) wyplata += " gr" else {
                            wyplata = StringBuilder(wyplata).insert(wyplata.length - 2, ".").append(" zł")
                                .toString()
                        }
                        textPayout.text = wyplata
                    }
                } catch (e: NumberFormatException) {
                    val alert = Alert(Alert.AlertType.WARNING, e.message)
                    alert.headerText = "Błędny format liczby!"
                    alert.show()
                }
            }
            BaseController.DetailWindowType.PRODUKT -> {
                val textName = Text()
                val textPrice = Text()
                val lblNewPrice = Label("Nowa cena:")
                val txtFldNewPrice = TextField()
                val btnEdit = Button("Zmień cenę")
                btnEdit.setOnAction {
                    try {
                        dbConn.editProduct(displayedId, (txtFldNewPrice.text.toFloat() * 100).toInt())
                        parent.refresh()
                        refresh()
                    } catch (e: NumberFormatException) {
                        val alert = Alert(Alert.AlertType.WARNING, e.message)
                        alert.headerText = "Błędny format liczby!"
                        alert.show()
                    }
                }
                val textAmount = Text()
                boxOut.children.setAll(textId, textName, textPrice, lblNewPrice, txtFldNewPrice, btnEdit, textAmount)
                try {
                    val produkt = dbConn.getProduct(displayedId)
                    textId.text = "ID: " + (produkt?.idProduktu ?: "Brak produktu o podanym ID!")
                    textName.text = "Nazwa: " + (produkt?.nazwa ?: "")
                    if (produkt != null) {
                        var cena = produkt.cena.toString()
                        if (cena.length < 3) cena += " gr" else {
                            cena = StringBuilder(cena).insert(cena.length - 2, ".").append(" zł").toString()
                        }
                        textPrice.text = cena
                    }
                    textAmount.text = "Ilość: " + (produkt?.ilosc ?: "")
                } catch (e: NumberFormatException) {
                    val alert = Alert(Alert.AlertType.WARNING, e.message)
                    alert.headerText = "Błędny format liczby!"
                    alert.show()
                }
            }
            BaseController.DetailWindowType.ZAMOWIENIE -> {
                displayedHistorical = chkHistorical.isSelected
                val boxKlient = HBox(10.0)
                val textKlientId = Text()
                val btnKlientShow = Button("Wyświetl")
                btnKlientShow.isDisable = true
                boxKlient.children.addAll(textKlientId, btnKlientShow)

                val textPrice = Text()
                val textStatus = Text()
                val lblNewStatus = Label("Nowy status:")
                val comboNewStatus = ComboBox<String>()
                val statuses = listOf("przygotowywanie", "nadawanie", "wyslane", "dostarczone", "anulowane")
                comboNewStatus.items = FXCollections.observableList(statuses)
                val btnEdit = Button("Zmień status")
                btnEdit.setOnAction {
                    when (comboNewStatus.value) {
                        "dostarczone" -> {
                            val alert = Alert(Alert.AlertType.CONFIRMATION, "Czy na pewno chcesz zakończyć to zamówienie?",
                                ButtonType.YES, ButtonType.NO)
                            alert.title = "Usuwanie"
                            val alertResult = alert.showAndWait()
                            alertResult.ifPresent { button: ButtonType ->
                                if (button == ButtonType.YES) {
                                    dbConn.editOrder(displayedId, comboNewStatus.value)
                                    parent.refresh()
                                    refresh()
                                }
                            }
                        }
                        "anulowane" -> {
                            val alert = Alert(Alert.AlertType.CONFIRMATION, "Czy na pewno chcesz anulować to zamówienie?",
                                ButtonType.YES, ButtonType.NO)
                            alert.title = "Usuwanie"
                            val alertResult = alert.showAndWait()
                            alertResult.ifPresent { button: ButtonType ->
                                if (button == ButtonType.YES) {
                                    dbConn.delOrder(displayedId)
                                    parent.refresh()
                                    refresh()
                                }
                            }
                        }
                        else -> {
                            dbConn.editOrder(displayedId, comboNewStatus.value)
                            parent.refresh()
                            refresh()
                        }
                    }
                }

                val boxPracownik = HBox(10.0)
                val textPracownikId = Text()
                val btnPracownikShow = Button("Wyświetl")
                btnPracownikShow.isDisable = true
                boxPracownik.children.addAll(textPracownikId, btnPracownikShow)

                val btnSzczegoly = Button("Wyświetl szczegóły")
                if ((if (displayedHistorical) dbConn.getOrderDetailsHistorical(displayedId).isEmpty()
                    else dbConn.getOrderDetails(displayedId).isEmpty())
                )   btnSzczegoly.isDisable = true
                else
                    btnSzczegoly.setOnAction {
                        val detailWindow = Stage()
                        detailWindow.title = "Szczegóły zamówienia"
                        val loader = FXMLLoader(javaClass.getResource("order-details-view.fxml"))
                        val detailController = OrderDetailsController(dbConn, displayedId, displayedHistorical)
                        loader.setController(detailController)
                        val scene = Scene(loader.load(), 600.0, 400.0)
                        detailWindow.scene = scene
                        detailWindow.show()
                    }

                boxOut.children.setAll(textId, boxKlient, textPrice, textStatus, lblNewStatus, comboNewStatus, btnEdit,
                    boxPracownik, btnSzczegoly)
                try {
                    val zamowienie = if (displayedHistorical)
                        dbConn.getOrderHistorical(displayedId)
                    else
                        dbConn.getOrder(displayedId)
                    textId.text = "ID: " + (zamowienie?.idZamowienia ?: "Brak zamówienia o podanym ID!")
                    textKlientId.text = "ID Klienta: " + (zamowienie?.idKlienta ?: "Brak klienta o podanym ID!")
                    if (zamowienie != null) {
                        btnKlientShow.isDisable = false
                        btnKlientShow.setOnAction {
                            val detailLoader = FXMLLoader(javaClass.getResource("detail-view.fxml"))
                            val detailController = DetailWindowController(
                                parent,
                                BaseController.DetailWindowType.KLIENT,
                                dbConn, zamowienie.idKlienta
                            )
                            detailLoader.setController(detailController)
                            val stage = Stage()
                            val scene = Scene(detailLoader.load(), 640.0, 480.0)
                            stage.title = "Wyświetl"
                            stage.scene = scene
                            stage.show()
                        }
                        var cena = zamowienie.cena.toString()
                        if (cena.length < 3) cena += " gr" else {
                            cena = StringBuilder(cena).insert(cena.length - 2, ".").append(" zł")
                                .toString()
                        }
                        textPrice.text = cena
                    }
                    textStatus.text = "Status: " + (zamowienie?.status ?: "")
                    textPracownikId.text = "ID Pracownika: " + (zamowienie?.idPracownika ?: "")
                    if (zamowienie != null) {
                        btnPracownikShow.isDisable = false
                        btnPracownikShow.setOnAction {
                            val detailLoader = FXMLLoader(javaClass.getResource("detail-view.fxml"))
                            val detailController = DetailWindowController(
                                parent,
                                BaseController.DetailWindowType.PRACOWNIK,
                                dbConn, zamowienie.idPracownika
                            )
                            detailLoader.setController(detailController)
                            val stage = Stage()
                            val scene = Scene(detailLoader.load(), 640.0, 480.0)
                            stage.title = "Wyświetl"
                            stage.scene = scene
                            stage.show()
                        }
                    }
                } catch (e: NumberFormatException) {
                    val alert = Alert(Alert.AlertType.WARNING, e.message)
                    alert.headerText = "Błędny format liczby!"
                    alert.show()
                }
            }
        }
    }
}