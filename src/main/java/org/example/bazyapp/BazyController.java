package org.example.bazyapp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import oracle.ucp.jdbc.PoolDataSource;
import org.example.bazyapp.models.*;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Optional;
import java.util.ResourceBundle;

public class BazyController implements Initializable {
    public ObservableList<Dostawa> dostawy = FXCollections.emptyObservableList();
    public ObservableList<Klient> klienci = FXCollections.emptyObservableList();
    public ObservableList<Magazyn> magazyny = FXCollections.emptyObservableList();
    public ObservableList<Pracownik> pracownicy = FXCollections.emptyObservableList();
    public ObservableList<Produkt> produkty = FXCollections.emptyObservableList();
    public ObservableList<Zamowienie> zamowienia = FXCollections.emptyObservableList();
    public HashMap<Integer, LinkedList<Integer>> zamowienia_szczegoly = new HashMap<>();



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

        PoolDataSource pds = dbConn.getDataSource();

        try (Connection conn = pds.getConnection()){
            conn.setAutoCommit(false);

            String query = "SELECT * FROM DOSTAWY";

            Statement stmt = conn.createStatement();
            ResultSet resultSet = stmt.executeQuery(query);

            while (resultSet.next()) {
                dostawy.add(new Dostawa(
                        resultSet.getInt(1),
                        resultSet.getInt(2),
                        resultSet.getInt(3),
                        resultSet.getInt(4),
                        resultSet.getInt(5),
                        resultSet.getInt(6)
                ));
            }

            query = "SELECT * FROM KLIENCI";
            resultSet = stmt.executeQuery(query);

            while (resultSet.next()) {
                klienci.add(new Klient(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getInt(4)
                ));
            }

            query = "SELECT * FROM MAGAZYNY";
            resultSet = stmt.executeQuery(query);

            while (resultSet.next()) {
                magazyny.add(new Magazyn(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3)
                ));
            }

            query = "SELECT * FROM PRACOWNICY";
            resultSet = stmt.executeQuery(query);

            while (resultSet.next()) {
                pracownicy.add(new Pracownik(
                        resultSet.getInt(1),
                        resultSet.getInt(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getString(5),
                        resultSet.getInt(6),
                        resultSet.getInt(7)
                ));
            }

            query = "SELECT * FROM PRODUKTY";
            resultSet = stmt.executeQuery(query);

            while (resultSet.next()) {
                produkty.add(new Pracownik())
                        //TODO
            }
        } catch (SQLException e){
            Alert sqlAlert = new Alert(Alert.AlertType.ERROR);
            sqlAlert.setContentText("Błąd połączenia z bazą!" + e.getMessage());
        }

    }
}