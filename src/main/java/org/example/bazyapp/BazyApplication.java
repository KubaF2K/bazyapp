package org.example.bazyapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import oracle.ucp.jdbc.PoolDataSource;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class BazyApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader bazyLoader = new FXMLLoader(BazyApplication.class.getResource("bazy-view.fxml"));
        Scene scene = new Scene(bazyLoader.load(), 800, 600);
        stage.setTitle("Magazyn");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
