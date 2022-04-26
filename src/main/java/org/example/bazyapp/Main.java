package org.example.bazyapp;

import oracle.ucp.jdbc.PoolDataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        System.out.print("Podaj hasło do bazy: ");
        String DB_PASSWORD = scanner.nextLine();

        DBConn dbConn = new DBConn(DB_PASSWORD);

        PoolDataSource pds = dbConn.getDataSource();

        try (Connection conn = pds.getConnection()) {

            String queryStmt = "SELECT * FROM DOSTAWY WHERE ROWNUM < 20";

            conn.setAutoCommit(false);

            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(queryStmt);
            System.out.println("ID\tID_PRODUKTU\tID_MAGAZYNU\tID_PRACOWNIKA\tKOSZT\tILOSC");
            while (resultSet.next()) {
                System.out.println(resultSet.getInt(1) + "\t" + resultSet.getInt(2) + "\t" + resultSet.getInt(3) + "\t" + resultSet.getInt(4) + "\t" + resultSet.getInt(5) + "\t" + resultSet.getInt(6));
            }
        }
        catch (SQLException e) {
            System.out.println("SQLException:\n" + e.getMessage());
            System.exit(1);
        }
    }
}
