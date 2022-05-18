package org.example.bazyapp

import javafx.scene.control.Alert
import oracle.ucp.jdbc.PoolDataSource
import oracle.ucp.jdbc.PoolDataSourceFactory
import java.sql.SQLException

class DBConn (private val DB_PASSWORD: String) {
    private val DB_URL = "jdbc:oracle:thin:@db202203141259_medium?TNS_ADMIN=Wallet_DB202203141259/"
    private val DB_USER = "ADMIN"
    private val CONN_FACTORY_CLASS_NAME = "oracle.jdbc.pool.OracleDataSource"

    private var pds: PoolDataSource? = null

    val dataSource: PoolDataSource get() {
        if (pds == null) {
            pds = PoolDataSourceFactory.getPoolDataSource()
            try {
                pds!!.connectionFactoryClassName = CONN_FACTORY_CLASS_NAME
                pds!!.url = DB_URL
                pds!!.user = DB_USER
                pds!!.password = DB_PASSWORD
                pds!!.connectionPoolName = "JDBC_UCP_POOL"
                pds!!.initialPoolSize = 5
                pds!!.minPoolSize = 5
                pds!!.maxPoolSize = 20
            } catch (e: SQLException) {
                val alert = Alert(Alert.AlertType.ERROR)
                alert.headerText = "Błąd połączenia z bazą!"
                alert.contentText = e.message
                alert.showAndWait()
            }
        }
        return pds!!
    }

    fun addDelivery(produktId: Int, magazynId: Int, pracownikId: Int, ilosc: Int) {
        val pds = dataSource
        try {
            pds.connection.use { conn ->
                conn.autoCommit = false
                val query = "CALL ADD_NEW_TO_DOSTAWY(?, ?, ?, ?)"
                val stmt = conn.prepareCall(query)
                stmt.setInt(1, produktId)
                stmt.setInt(2, magazynId)
                stmt.setInt(3, pracownikId)
                stmt.setInt(4, ilosc)
                stmt.execute()
                val alert = Alert(Alert.AlertType.CONFIRMATION)
                alert.headerText = "Dodano dostawę do bazy!"
                alert.showAndWait()
            }
        } catch (e: SQLException) {
            val alert = Alert(Alert.AlertType.ERROR)
            alert.title = "Błąd!"
            alert.headerText = "Błąd połączenia z bazą!"
            alert.contentText = e.message
            alert.show()
        }
    }

    fun delMagazyn(id: Int) {

        try {
            dataSource.connection.use { conn ->
                conn.autoCommit = false

                val query = "CALL P_DELETE.DELETE_MAGAZYNY(?)"
                val stmt = conn.prepareCall(query)
                stmt.setInt(1, id)
                stmt.execute()
                val alert = Alert(Alert.AlertType.INFORMATION)
                alert.title = "Sukces"
                alert.headerText = "Usunięto magazyn z bazy!"
                alert.show()
            }
        } catch (e: SQLException) {
            val error = Alert(Alert.AlertType.ERROR)
            error.title = "Błąd!"
            error.headerText = "Błąd połączenia z bazą!"
            error.contentText = e.message
            error.show()
        }
    }

    fun delKlient(id: Int) {
        try {
            dataSource.connection.use { conn ->
                conn.autoCommit = false

                val query = "CALL P_DELETE.DELETE_KLIENCI(?)"
                val stmt = conn.prepareCall(query)
                stmt.setInt(1, id)
                stmt.execute()
                val alert = Alert(Alert.AlertType.INFORMATION)
                alert.title = "Sukces"
                alert.headerText = "Usunięto klienta z bazy!"
                alert.show()
            }
        } catch (e: SQLException) {
            val error = Alert(Alert.AlertType.ERROR)
            error.title = "Błąd!"
            error.headerText = "Błąd połączenia z bazą!"
            error.contentText = e.message
            error.show()
        }
    }

    fun delPracownik(id: Int) {
        try {
            dataSource.connection.use { conn ->
                conn.autoCommit = false

                val query = "CALL P_DELETE.DELETE_PRACOWNICY(?)"
                val stmt = conn.prepareCall(query)
                stmt.setInt(1, id)
                stmt.execute()
                val alert = Alert(Alert.AlertType.INFORMATION)
                alert.title = "Sukces"
                alert.headerText = "Usunięto pracownika z bazy!"
                alert.show()
            }
        } catch (e: SQLException) {
            val error = Alert(Alert.AlertType.ERROR)
            error.title = "Błąd!"
            error.headerText = "Błąd połączenia z bazą!"
            error.contentText = e.message
            error.show()
        }
    }

    fun delProdukt(id: Int) {
        try {
            dataSource.connection.use { conn ->
                conn.autoCommit = false

                val query = "CALL P_DELETE.DELETE_PRODUKTY(?)"
                val stmt = conn.prepareCall(query)
                stmt.setInt(1, id)
                stmt.execute()
                val alert = Alert(Alert.AlertType.INFORMATION)
                alert.title = "Sukces"
                alert.headerText = "Usunięto produkt z bazy!"
                alert.show()
            }
        } catch (e: SQLException) {
            val error = Alert(Alert.AlertType.ERROR)
            error.title = "Błąd!"
            error.headerText = "Błąd połączenia z bazą!"
            error.contentText = e.message
            error.show()
        }
    }

    fun delZamowienie(id: Int) {
        try {
            dataSource.connection.use { conn ->
                conn.autoCommit = false

                val query = "CALL P_DELETE.DELETE_ZAMOWIENIA(?)"
                val stmt = conn.prepareCall(query)
                stmt.setInt(1, id)
                stmt.execute()
                val alert = Alert(Alert.AlertType.INFORMATION)
                alert.title = "Sukces"
                alert.headerText = "Usunięto zamówienie z bazy!"
                alert.show()
            }
        } catch (e: SQLException) {
            val error = Alert(Alert.AlertType.ERROR)
            error.title = "Błąd!"
            error.headerText = "Błąd połączenia z bazą!"
            error.contentText = e.message
            error.show()
        }
    }
}