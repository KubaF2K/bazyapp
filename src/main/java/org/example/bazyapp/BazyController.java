package org.example.bazyapp;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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

    public HashMap<Integer, List<ZamowienieSzcz>> zamowienia_szczegoly = new HashMap<>();



    @FXML public TableView<Dostawa> tvDostawy;
    @FXML public TableColumn<Dostawa, Integer> tvDostawyId;
    @FXML public TableColumn<Dostawa, String> tvDostawyProdukt;
    @FXML public TableColumn<Dostawa, String> tvDostawyMagazyn;
    @FXML public TableColumn<Dostawa, Integer> tvDostawyPracownik;
    @FXML public TableColumn<Dostawa, String> tvDostawyKoszt;
    @FXML public TableColumn<Dostawa, Integer> tvDostawyIlosc;

    @FXML public TableView<Magazyn> tvMagazyny;
    @FXML public TableView<Klient> tvKlienci;
    @FXML public TableView<Pracownik> tvPracownicy;
    @FXML public TableView<Produkt> tvProdukty;
    @FXML public TableView<Zamowienie> tvZamowienia;
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

        tvDostawyId.setCellValueFactory(new PropertyValueFactory<>("id_dostawy"));
        tvDostawyProdukt.setCellValueFactory(i -> {
            String val = "Brak!";
            if (i.getValue() != null && produkty.containsKey(i.getValue().id_produktu))
                val = produkty.get(i.getValue().id_produktu).nazwa;
            String finalVal = val;
            return Bindings.createStringBinding(() -> finalVal);
        });
        tvDostawyMagazyn.setCellValueFactory(i -> {
            String val = "Brak!";
            if (i.getValue() != null && magazyny.containsKey(i.getValue().id_magazynu))
                val = magazyny.get(i.getValue().id_magazynu).nazwa;
            String finalVal = val;
            return Bindings.createStringBinding(() -> finalVal);
        });
        tvDostawyPracownik.setCellValueFactory(new PropertyValueFactory<>("id_pracownika"));
        tvDostawyKoszt.setCellValueFactory(i -> {
            String val = "Brak!";
            if (i.getValue() != null) {
                int koszt = i.getValue().koszt;
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

            query = "SELECT * FROM KLIENCI";
            resultSet = stmt.executeQuery(query);

            while (resultSet.next()) {
                Klient klient = new Klient(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getInt(4)
                );
                klienci.put(resultSet.getInt(1), klient);
                klienciList.add(klient);
            }

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

            query = "SELECT * FROM PRACOWNICY";
            resultSet = stmt.executeQuery(query);

            while (resultSet.next()) {
                Pracownik pracownik = new Pracownik(
                        resultSet.getInt(1),
                        resultSet.getInt(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getString(5),
                        resultSet.getInt(6),
                        resultSet.getInt(7)
                );
                pracownicy.put(resultSet.getInt(1), pracownik);
                pracownicyList.add(pracownik);
            }

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

            query = "SELECT * FROM ZAMOWIENIA_SZCZEGOLY";
            resultSet = stmt.executeQuery(query);

            while (resultSet.next()) {
                if (!zamowienia_szczegoly.containsKey(resultSet.getInt(2)))
                    zamowienia_szczegoly.put(resultSet.getInt(2), new LinkedList<>());
                zamowienia_szczegoly.get(resultSet.getInt(2)).add(new ZamowienieSzcz(
                        resultSet.getInt(1),
                        resultSet.getInt(2),
                        resultSet.getInt(3),
                        resultSet.getInt(4)
                ));
            }


        } catch (SQLException e){
            Alert sqlAlert = new Alert(Alert.AlertType.ERROR);
            sqlAlert.setContentText("Błąd połączenia z bazą!" + e.getMessage());
        }

        tvDostawy.setItems(dostawyList);

    }
}