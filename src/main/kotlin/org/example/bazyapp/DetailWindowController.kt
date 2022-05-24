package org.example.bazyapp

import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.fxml.Initializable
import javafx.scene.Scene
import javafx.scene.control.Alert
import javafx.scene.control.Button
import javafx.scene.control.TextField
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.scene.text.Text
import javafx.stage.Stage
import java.net.URL
import java.util.*

class DetailWindowController(val type: BazyController.DetailWindowType, val dbConn: DBConn, val id: Int? = null): Initializable {
    @FXML lateinit var txtFldId: TextField
    @FXML lateinit var boxOut: VBox
    @FXML lateinit var textId: Text

    @FXML
    override fun initialize(p0: URL?, p1: ResourceBundle?) {
        if (id != null) {
            txtFldId.text = id.toString()
            display()
        }
    }

    fun display() {
        when (type) {
            BazyController.DetailWindowType.KLIENT -> {
                val textName = Text()
                val textPesel = Text()
                boxOut.children.addAll(textName, textPesel)
                try {
                    val klient = dbConn.getKlient(txtFldId.text.toInt())
                    textId.text = "ID: " + (klient?.idKlienta ?: "Brak klienta o podanym ID!")
                    textName.text = "Imię i nazwisko: " + (klient?.imie ?: "") + " " + (klient?.nazwisko ?: "")
                    textPesel.text = "PESEL: " + klient?.pesel
                } catch (e: NumberFormatException) {
                    val alert = Alert(Alert.AlertType.WARNING, e.message)
                    alert.headerText = "Błędny format liczby!"
                    alert.show()
                }
            }
            BazyController.DetailWindowType.MAGAZYN -> {
                val textAddr = Text()
                val textName = Text()
                boxOut.children.addAll(textAddr, textName)
                try {
                    val magazyn = dbConn.getMagazyn(txtFldId.text.toInt())
                    textId.text = "ID: " + (magazyn?.idMagazynu ?: "Brak magazynu o podanym ID!")
                    textAddr.text = "Adres: " + (magazyn?.miejsce ?: "")
                    textName.text = "Nazwa: " + (magazyn?.nazwa ?: "")
                } catch (e: NumberFormatException) {
                    val alert = Alert(Alert.AlertType.WARNING, e.message)
                    alert.headerText = "Błędny format liczby!"
                    alert.show()
                }
            }
            BazyController.DetailWindowType.PRACOWNIK -> {
                val boxMagazyn = HBox(10.0)
                val textMagazynId = Text()
                val btnMagazynShow = Button("Wyświetl")
                btnMagazynShow.isDisable = true
                boxMagazyn.children.addAll(textMagazynId, btnMagazynShow)

                val textName = Text()
                val textAddr = Text()
                val textPesel = Text()
                val textPayout = Text()
                boxOut.children.addAll(boxMagazyn, textName, textAddr, textPesel, textPayout)
                try {
                    val pracownik = dbConn.getPracownik(txtFldId.text.toInt())
                    textId.text = "ID: " + (pracownik?.idPracownika ?: "Brak pracownika o podanym ID!")
                    textMagazynId.text = "ID magazynu: " + (pracownik?.idMagazynu ?: "Brak magazynu o podanym ID!")
                    if (pracownik != null) {
                        btnMagazynShow.isDisable = false
                        btnMagazynShow.setOnAction {
                            val detailLoader = FXMLLoader(javaClass.getResource("detail-view.fxml"))
                            val detailController = DetailWindowController(BazyController.DetailWindowType.MAGAZYN,
                                dbConn, pracownik.idMagazynu)
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
            BazyController.DetailWindowType.PRODUKT -> {
                val textName = Text()
                val textPrice = Text()
                val textAmount = Text()
                boxOut.children.addAll(textName, textPrice, textAmount)
                try {
                    val produkt = dbConn.getProdukt(txtFldId.text.toInt())
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
            BazyController.DetailWindowType.ZAMOWIENIE -> {
                val boxKlient = HBox(10.0)
                val textKlientId = Text()
                val btnKlientShow = Button("Wyświetl")
                btnKlientShow.isDisable = true
                boxKlient.children.addAll(textKlientId, btnKlientShow)

                val textPrice = Text()
                val textStatus = Text()

                val boxPracownik = HBox(10.0)
                val textPracownikId = Text()
                val btnPracownikShow = Button("Wyświetl")
                btnPracownikShow.isDisable = true
                boxPracownik.children.addAll(textPracownikId, btnPracownikShow)

                val btnSzczegoly = Button("Wyświetl szczegóły")
                if (dbConn.getZamowienieSzczegoly(txtFldId.text.toInt()).isEmpty())
                    btnSzczegoly.isDisable = true
                else
                    btnSzczegoly.setOnAction {
                        val detailWindow = Stage()
                        detailWindow.title = "Szczegóły zamówienia"
                        val loader = FXMLLoader(javaClass.getResource("zam-szcz-view.fxml"))
                        val detailController = ZamowieniaSzczegolyController(dbConn, txtFldId.text.toInt())
                        loader.setController(detailController)
                        val scene = Scene(loader.load(), 600.0, 400.0)
                        detailWindow.scene = scene
                        detailWindow.show()
                    }

                boxOut.children.addAll(boxKlient, textPrice, textStatus, boxPracownik, btnSzczegoly)
                try {
                    val zamowienie = dbConn.getZamowienie(txtFldId.text.toInt())
                    textId.text = "ID: " + (zamowienie?.idZamowienia ?: "Brak zamówienia o podanym ID!")
                    textKlientId.text = "ID Klienta: " + (zamowienie?.idKlienta ?: "Brak klienta o podanym ID!")
                    if (zamowienie != null) {
                        btnKlientShow.isDisable = false
                        btnKlientShow.setOnAction {
                            val detailLoader = FXMLLoader(javaClass.getResource("detail-view.fxml"))
                            val detailController = DetailWindowController(BazyController.DetailWindowType.KLIENT,
                                dbConn, zamowienie.idKlienta)
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
                            val detailController = DetailWindowController(BazyController.DetailWindowType.PRACOWNIK,
                                dbConn, zamowienie.idPracownika)
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