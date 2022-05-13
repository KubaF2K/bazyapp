package org.example.bazyapp;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import oracle.ucp.jdbc.PoolDataSource;
import org.example.bazyapp.models.*;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class BazyController implements Initializable {
    public Map<Integer, Dostawa> dostawy = new HashMap<>();
    public ObservableList<Dostawa> dostawyList = FXCollections.observableArrayList();
    public Map<Integer, Klient> klienci = new HashMap<>();
    public ObservableList<Klient> klienciList = FXCollections.observableArrayList();

    public Map<Integer, Magazyn> magazyny = new HashMap<>();
    public ObservableList<Magazyn> magazynyList = FXCollections.observableArrayList();

    public Map<Integer, Pracownik> pracownicy = new HashMap<>();
    public ObservableList<Pracownik> pracownicyList = FXCollections.observableArrayList();

    public Map<Integer, Produkt> produkty = new HashMap<>();
    public ObservableList<Produkt> produktyList = FXCollections.observableArrayList();

    public Map<Integer, Zamowienie> zamowienia = new HashMap<>();
    public ObservableList<Zamowienie> zamowieniaList = FXCollections.observableArrayList();

    public HashMap<Integer, List<ZamowienieSzcz>> zamowieniaSzczegoly = new HashMap<>();



    @FXML public TableView<Dostawa> tvDostawy;
    @FXML public TableColumn<Dostawa, Integer> tvDostawyId;
    @FXML public TableColumn<Dostawa, String> tvDostawyProdukt;
    @FXML public TableColumn<Dostawa, String> tvDostawyMagazyn;
    @FXML public TableColumn<Dostawa, Integer> tvDostawyPracownik;
    @FXML public TableColumn<Dostawa, String> tvDostawyKoszt;
    @FXML public TableColumn<Dostawa, Integer> tvDostawyIlosc;

    @FXML public TableView<Magazyn> tvMagazyny;
    @FXML public TableColumn<Magazyn, Integer> tvMagazynyId;
    @FXML public TableColumn<Magazyn, String> tvMagazynyMiejsce;
    @FXML public TableColumn<Magazyn, String> tvMagazynyNazwa;

    @FXML public TableView<Klient> tvKlienci;
    @FXML public TableColumn<Klient, Integer> tvKlienciId;
    @FXML public TableColumn<Klient, String> tvKlienciImie;
    @FXML public TableColumn<Klient, String> tvKlienciNazwisko;
    @FXML public TableColumn<Klient, Integer> tvKlienciPesel;

    @FXML public TableView<Pracownik> tvPracownicy;
    @FXML public TableColumn<Pracownik, Integer> tvPracownicyId;
    @FXML public TableColumn<Pracownik, String> tvPracownicyMagazyn;
    @FXML public TableColumn<Pracownik, String> tvPracownicyImie;
    @FXML public TableColumn<Pracownik, String> tvPracownicyNazwisko;
    @FXML public TableColumn<Pracownik, String> tvPracownicyAdres;
    @FXML public TableColumn<Pracownik, Integer> tvPracownicyPesel;
    @FXML public TableColumn<Pracownik, String> tvPracownicyWyplata;

    @FXML public TableView<Produkt> tvProdukty;
    @FXML public TableColumn<Produkt, Integer> tvProduktyId;
    @FXML public TableColumn<Produkt, String> tvProduktyNazwa;
    @FXML public TableColumn<Produkt, String> tvProduktyCena;
    @FXML public TableColumn<Produkt, Integer> tvProduktyIlosc;

    @FXML public TableView<Zamowienie> tvZamowienia;
    @FXML public TableColumn<Zamowienie, Integer> tvZamowieniaId;
    @FXML public TableColumn<Zamowienie, Integer> tvZamowieniaKlient;
    @FXML public TableColumn<Zamowienie, String> tvZamowieniaCena;
    @FXML public TableColumn<Zamowienie, String> tvZamowieniaStatus;
    @FXML public TableColumn<Zamowienie, Integer> tvZamowieniaPracownik;
    @FXML public TableColumn<Zamowienie, Button> tvZamowieniaSzczegoly;
    private DBConn dbConn;

    @FXML
    public void initialize(URL url, ResourceBundle bundle){
        Dialog<String> passPopup = new Dialog<>();

        passPopup.setTitle("Logowanie");
        passPopup.setHeaderText("Podaj hasło do bazy:");
        passPopup.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        PasswordField textPassword = new PasswordField();
        passPopup.getDialogPane().setContent(textPassword);

        passPopup.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK)
                return textPassword.getText();
            return null;
        });

        Optional<String> password = passPopup.showAndWait();
        password.ifPresent(pass -> dbConn = new DBConn(pass));

        tvDostawyId.setCellValueFactory(new PropertyValueFactory<>("idDostawy"));
        tvDostawyProdukt.setCellValueFactory(i -> {
            String val = "Brak!";
            if (i.getValue() != null && produkty.containsKey(i.getValue().getIdProduktu()))
                val = produkty.get(i.getValue().getIdProduktu()).getNazwa();
            String finalVal = val;
            return Bindings.createStringBinding(() -> finalVal);
        });
        tvDostawyMagazyn.setCellValueFactory(i -> {
            String val = "Brak!";
            if (i.getValue() != null && magazyny.containsKey(i.getValue().getIdMagazynu()))
                val = magazyny.get(i.getValue().getIdMagazynu()).getNazwa();
            String finalVal = val;
            return Bindings.createStringBinding(() -> finalVal);
        });
        tvDostawyPracownik.setCellValueFactory(new PropertyValueFactory<>("idPracownika"));
        tvDostawyKoszt.setCellValueFactory(i -> {
            String val = "Brak!";
            if (i.getValue() != null) {
                int koszt = i.getValue().getKoszt();
                val = Integer.toString(koszt);
                if (val.length() < 3)
                    val += " gr";
                else {
                    val = new StringBuilder(val).insert(val.length()-2, ".").append(" zł").toString();
                }
            }
            String finalVal = val;
            return Bindings.createStringBinding(() -> finalVal);
        });
        tvDostawyIlosc.setCellValueFactory(new PropertyValueFactory<>("ilosc"));

        tvMagazynyId.setCellValueFactory(new PropertyValueFactory<>("idMagazynu"));
        tvMagazynyMiejsce.setCellValueFactory(new PropertyValueFactory<>("miejsce"));
        tvMagazynyNazwa.setCellValueFactory(new PropertyValueFactory<>("nazwa"));

        tvKlienciId.setCellValueFactory(new PropertyValueFactory<>("idKlienta"));
        tvKlienciImie.setCellValueFactory(new PropertyValueFactory<>("imie"));
        tvKlienciNazwisko.setCellValueFactory(new PropertyValueFactory<>("nazwisko"));
        tvKlienciPesel.setCellValueFactory(new PropertyValueFactory<>("pesel"));

        tvPracownicyId.setCellValueFactory(new PropertyValueFactory<>("idPracownika"));
        tvPracownicyMagazyn.setCellValueFactory(i -> {
            String val = "Brak!";
            if (i.getValue() != null && magazyny.containsKey(i.getValue().getIdMagazynu()))
                val = magazyny.get(i.getValue().getIdMagazynu()).getNazwa();
            String finalVal = val;
            return Bindings.createStringBinding(() -> finalVal);
        });
        tvPracownicyImie.setCellValueFactory(new PropertyValueFactory<>("imie"));
        tvPracownicyNazwisko.setCellValueFactory(new PropertyValueFactory<>("nazwisko"));
        tvPracownicyAdres.setCellValueFactory(new PropertyValueFactory<>("adresZamieszkania"));
        tvPracownicyPesel.setCellValueFactory(new PropertyValueFactory<>("pesel"));
        tvPracownicyWyplata.setCellValueFactory(i -> {
            String val = "Brak!";
            if (i.getValue() != null) {
                int wyplata = i.getValue().getWyplata();
                val = Integer.toString(wyplata);
                if (val.length() < 3)
                    val += " gr";
                else {
                    val = new StringBuilder(val).insert(val.length()-2, ".").append(" zł").toString();
                }
            }
            String finalVal = val;
            return Bindings.createStringBinding(() -> finalVal);
        });

        tvProduktyId.setCellValueFactory(new PropertyValueFactory<>("idProduktu"));
        tvProduktyNazwa.setCellValueFactory(new PropertyValueFactory<>("nazwa"));
        tvProduktyCena.setCellValueFactory(i -> {
            String val = "Brak!";
            if (i.getValue() != null) {
                int cena = i.getValue().getCena();
                val = Integer.toString(cena);
                if (val.length() < 3)
                    val += " gr";
                else {
                    val = new StringBuilder(val).insert(val.length()-2, ".").append(" zł").toString();
                }
            }
            String finalVal = val;
            return Bindings.createStringBinding(() -> finalVal);
        });
        tvProduktyIlosc.setCellValueFactory(new PropertyValueFactory<>("ilosc"));

        tvZamowieniaId.setCellValueFactory(new PropertyValueFactory<>("idZamowienia"));
        tvZamowieniaKlient.setCellValueFactory(new PropertyValueFactory<>("idKlienta"));
        tvZamowieniaCena.setCellValueFactory(i -> {
            String val = "Brak!";
            if (i.getValue() != null) {
                int cena = i.getValue().getCena();
                val = Integer.toString(cena);
                if (val.length() < 3)
                    val += " gr";
                else {
                    val = new StringBuilder(val).insert(val.length()-2, ".").append(" zł").toString();
                }
            }
            String finalVal = val;
            return Bindings.createStringBinding(() -> finalVal);
        });
        tvZamowieniaStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        tvZamowieniaPracownik.setCellValueFactory(new PropertyValueFactory<>("idPracownika"));
        tvZamowieniaSzczegoly.setCellFactory(e -> {
            Button viewButton = new Button("Pokaż");
            TableCell<Zamowienie, Button> detailCell = new TableCell<>(){
                @Override
                protected void updateItem(Button button, boolean b) {
                    super.updateItem(button, b);
                    if(b || getTableRow().getItem() == null)
                        setGraphic(null);
                    else
                        setGraphic(viewButton);
                }
            };
            viewButton.setOnAction(actionEvent -> {
                Stage detailWindow = new Stage();
                detailWindow.setTitle("Szczegóły zamówienia");

                TableView<ZamowienieSzcz> tvZamowieniaSzcz = new TableView<>();
                Scene detailScene = new Scene(tvZamowieniaSzcz, 640, 480);
                detailWindow.setScene(detailScene);

                TableColumn<ZamowienieSzcz, Integer> tvZamowieniaSzczId = new TableColumn<>("ID");
                TableColumn<ZamowienieSzcz, Integer> tvZamowieniaSzczProdId = new TableColumn<>("ID Produktu");
                TableColumn<ZamowienieSzcz, String> tvZamowieniaSzczProd = new TableColumn<>("Nazwa Produktu");
                TableColumn<ZamowienieSzcz, Integer> tvZamowieniaSzczIlosc = new TableColumn<>("Ilość");

                tvZamowieniaSzcz.getColumns().add(tvZamowieniaSzczId);
                tvZamowieniaSzcz.getColumns().add(tvZamowieniaSzczProdId);
                tvZamowieniaSzcz.getColumns().add(tvZamowieniaSzczProd);
                tvZamowieniaSzcz.getColumns().add(tvZamowieniaSzczIlosc);

                tvZamowieniaSzczId.setCellValueFactory(new PropertyValueFactory<>("idSzczegoly"));
                tvZamowieniaSzczProdId.setCellValueFactory(new PropertyValueFactory<>("idProduktu"));
                tvZamowieniaSzczProd.setCellValueFactory(i -> {
                    String val = "Brak!";
                    if (i.getValue() != null && produkty.containsKey(i.getValue().getIdProduktu()))
                        val = produkty.get(i.getValue().getIdProduktu()).getNazwa();
                    String finalVal = val;
                    return Bindings.createStringBinding(() -> finalVal);
                });
                tvZamowieniaSzczIlosc.setCellValueFactory(new PropertyValueFactory<>("ilosc"));

                tvZamowieniaSzcz.setItems(FXCollections.observableList(
                        zamowieniaSzczegoly.get(detailCell.getTableRow().getItem().getIdZamowienia()))
                );
                detailWindow.show();
            });
            return detailCell;
        });

        PoolDataSource pds = dbConn.getDataSource();

        try (Connection conn = pds.getConnection()){
            conn.setAutoCommit(false);

            String query = "SELECT * FROM DOSTAWY";

            Statement stmt = conn.createStatement();
            ResultSet resultSet = stmt.executeQuery(query);

            while (resultSet.next()) {
                Dostawa dostawa = new Dostawa(
                        resultSet.getInt(1),
                        resultSet.getInt(2),
                        resultSet.getInt(3),
                        resultSet.getInt(4),
                        resultSet.getInt(5),
                        resultSet.getInt(6)
                );
                dostawy.put(resultSet.getInt(1), dostawa);
                dostawyList.add(dostawa);
            }
            resultSet.close();
            stmt.close();
            stmt = conn.createStatement();
            query = "SELECT * FROM KLIENCI";
            resultSet = stmt.executeQuery(query);

            while (resultSet.next()) {
                Klient klient = new Klient(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getLong(4)
                );
                klienci.put(resultSet.getInt(1), klient);
                klienciList.add(klient);
            }
            resultSet.close();
            stmt.close();
            stmt = conn.createStatement();
            query = "SELECT * FROM MAGAZYNY";
            resultSet = stmt.executeQuery(query);

            while (resultSet.next()) {
                Magazyn magazyn = new Magazyn(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3)
                );
                magazyny.put(resultSet.getInt(1), magazyn);
                magazynyList.add(magazyn);
            }
            resultSet.close();
            stmt.close();
            stmt = conn.createStatement();
            query = "SELECT * FROM PRACOWNICY";
            resultSet = stmt.executeQuery(query);

            while (resultSet.next()) {
                Pracownik pracownik = new Pracownik(
                        resultSet.getInt(1),
                        resultSet.getInt(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getString(5),
                        resultSet.getLong(6),
                        resultSet.getInt(7)
                );
                pracownicy.put(resultSet.getInt(1), pracownik);
                pracownicyList.add(pracownik);
            }
            resultSet.close();
            stmt.close();
            stmt = conn.createStatement();
            query = "SELECT * FROM PRODUKTY";
            resultSet = stmt.executeQuery(query);

            while (resultSet.next()) {
                Produkt produkt = new Produkt(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getInt(3),
                        resultSet.getInt(4)
                );
                produkty.put(resultSet.getInt(1), produkt);
                produktyList.add(produkt);
            }
            resultSet.close();
            stmt.close();
            stmt = conn.createStatement();
            query = "SELECT * FROM ZAMOWIENIA";
            resultSet = stmt.executeQuery(query);

            while (resultSet.next()) {
                Zamowienie zamowienie = new Zamowienie(
                        resultSet.getInt(1),
                        resultSet.getInt(2),
                        resultSet.getInt(3),
                        resultSet.getString(4),
                        resultSet.getInt(5)
                );
                zamowienia.put(resultSet.getInt(1), zamowienie);
                zamowieniaList.add(zamowienie);
            }
            resultSet.close();
            stmt.close();
            stmt = conn.createStatement();
            query = "SELECT * FROM ZAMOWIENIA_SZCZEGOLY";
            resultSet = stmt.executeQuery(query);

            while (resultSet.next()) {
                if (!zamowieniaSzczegoly.containsKey(resultSet.getInt(2)))
                    zamowieniaSzczegoly.put(resultSet.getInt(2), new LinkedList<>());
                zamowieniaSzczegoly.get(resultSet.getInt(2)).add(new ZamowienieSzcz(
                        resultSet.getInt(1),
                        resultSet.getInt(2),
                        resultSet.getInt(3),
                        resultSet.getInt(4)
                ));
            }
            resultSet.close();
            stmt.close();


        } catch (SQLException e){
            Alert sqlAlert = new Alert(Alert.AlertType.ERROR);
            sqlAlert.setContentText("Błąd połączenia z bazą! " + e.getMessage());
            sqlAlert.showAndWait();
        }

        tvDostawy.setItems(dostawyList);
        tvMagazyny.setItems(magazynyList);
        tvKlienci.setItems(klienciList);
        tvPracownicy.setItems(pracownicyList);
        tvProdukty.setItems(produktyList);
        tvZamowienia.setItems(zamowieniaList);

    }
}