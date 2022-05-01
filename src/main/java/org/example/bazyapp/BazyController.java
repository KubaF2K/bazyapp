package org.example.bazyapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import oracle.ucp.jdbc.PoolDataSource;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.ResourceBundle;

public class BazyController implements Initializable {
    @FXML public TextArea dostawy;
    @FXML public TextArea magazyny;
    @FXML public TextArea klienci;
    @FXML public TextArea pracownicy;
    @FXML public TextArea produkty;
    @FXML public TextArea zamowienia;
    private DBConn dbConn;

    @FXML
    public void initialize(URL url, ResourceBundle bundle){
        TextInputDialog passPopup = new TextInputDialog();
        passPopup.setHeaderText("Podaj hasło do bazy:");
        Optional<String> password = passPopup.showAndWait();
        password.ifPresent(pass -> dbConn = new DBConn(pass));

        PoolDataSource pds = dbConn.getDataSource();

        try (Connection conn = pds.getConnection()){
            conn.setAutoCommit(false);

            String query = "SELECT * FROM DOSTAWY";

            Statement stmt = conn.createStatement();
            ResultSet resultSet = stmt.executeQuery(query);

            StringBuilder dostawyText = new StringBuilder("ID\tID_PRODUKTU\tID_MAGAZYNU\tID_PRACOWNIKA\tKOSZT\tILOSC\n");
            while (resultSet.next()) {
                dostawyText.append(resultSet.getInt(1)).append("\t").append(resultSet.getInt(2)).append("\t").append(resultSet.getInt(3)).append("\t").append(resultSet.getInt(4)).append("\t").append(resultSet.getInt(5)).append("\t").append(resultSet.getInt(6)).append("\n");
            }
            dostawy.setText(dostawyText.toString());
        } catch (SQLException e){
            Alert sqlAlert = new Alert(Alert.AlertType.ERROR);
            sqlAlert.setContentText("Błąd połączenia z bazą!" + e.getMessage());
        }

    }
}