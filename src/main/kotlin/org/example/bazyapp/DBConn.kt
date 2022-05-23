package org.example.bazyapp

import javafx.scene.control.Alert
import oracle.jdbc.OracleStruct
import oracle.ucp.jdbc.PoolDataSource
import oracle.ucp.jdbc.PoolDataSourceFactory
import org.example.bazyapp.models.*
import java.math.BigDecimal
import java.sql.SQLException
import java.sql.Types

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

    fun checkDeliveries(klientId: Int): Int {
        var deliveries = 0
        val pds = dataSource
        try {
            pds.connection.use { conn ->
                conn.autoCommit = false
                val query = "SELECT COUNT(*) FROM ZAMOWIENIA WHERE ID_KLIENTA = ?"
                val stmt = conn.prepareStatement(query)
                stmt.setInt(1, klientId)
                val result = stmt.executeQuery()
                result.next()
                deliveries = result.getInt(1)
            }
        } catch (e: SQLException) {
            val error = Alert(Alert.AlertType.ERROR)
            error.title = "Błąd!"
            error.headerText = "Błąd połączenia z bazą!"
            error.contentText = e.message
            error.show()
        }
        return deliveries
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
        if (checkDeliveries(id) == 0)
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
        else {
            val alert = Alert(Alert.AlertType.WARNING)
            alert.title = "Uwaga"
            alert.headerText = "Klient ma aktywne zamówienia!"
            alert.show()
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

    fun getKlient(id: Int): Klient? {
        try {
            dataSource.connection.use { conn ->
                conn.autoCommit = false
                val query = "{? = call P_SELECTONE.SELECTONE_KLIENCI(?)}"
                val stmt = conn.prepareCall(query)
                stmt.registerOutParameter(1, Types.STRUCT, "P_TYPES.KLIENT")
                stmt.setInt(2, id)
                stmt.execute()
                val klientStruct = stmt.getObject(1) as OracleStruct
                val klientAttrs = klientStruct.attributes
                return Klient(
                    (klientAttrs[0] as BigDecimal).toInt(), klientAttrs[1] as String,
                    klientAttrs[2] as String, (klientAttrs[3] as BigDecimal).toLong()
                )
            }
        } catch (e: SQLException) {
            val error = Alert(Alert.AlertType.ERROR)
            error.title = "Błąd!"
            error.headerText = "Błąd połączenia z bazą!"
            error.contentText = e.message
            error.show()
        }
        return null
    }

    fun getMagazyn(id: Int): Magazyn? {
        try {
            dataSource.connection.use { conn ->
                conn.autoCommit = false
                val query = "{? = call P_SELECTONE.SELECTONE_MAGAZYNY(?)}"
                val stmt = conn.prepareCall(query)
                stmt.registerOutParameter(1, Types.STRUCT, "P_TYPES.MAGAZYN")
                stmt.setInt(2, id)
                stmt.execute()
                val magazynStruct = stmt.getObject(1) as OracleStruct
                val magazynAttrs = magazynStruct.attributes
                return Magazyn(
                    (magazynAttrs[0] as BigDecimal).toInt(), magazynAttrs[1] as String, magazynAttrs[2] as String
                )
            }
        } catch (e: SQLException) {
            val error = Alert(Alert.AlertType.ERROR)
            error.title = "Błąd!"
            error.headerText = "Błąd połączenia z bazą!"
            error.contentText = e.message
            error.show()
        }
        return null
    }

    fun getPracownik(id: Int): Pracownik? {
        try {
            dataSource.connection.use { conn ->
                conn.autoCommit = false
                val query = "{? = call P_SELECTONE.SELECTONE_PRACOWNICY(?)}"
                val stmt = conn.prepareCall(query)
                stmt.registerOutParameter(1, Types.STRUCT, "P_TYPES.PRACOWNIK")
                stmt.setInt(2, id)
                stmt.execute()
                val pracownikStruct = stmt.getObject(1) as OracleStruct
                val pracownikAttrs = pracownikStruct.attributes
                return Pracownik(
                    (pracownikAttrs[0] as BigDecimal).toInt(), (pracownikAttrs[1] as BigDecimal).toInt(),
                    pracownikAttrs[2] as String, pracownikAttrs[3] as String, pracownikAttrs[4] as String,
                    (pracownikAttrs[5] as BigDecimal).toLong(), (pracownikAttrs[6] as BigDecimal).toInt()
                )
            }
        } catch (e: SQLException) {
            val error = Alert(Alert.AlertType.ERROR)
            error.title = "Błąd!"
            error.headerText = "Błąd połączenia z bazą!"
            error.contentText = e.message
            error.show()
        }
        return null
    }

    fun getProdukt(id: Int): Produkt? {
        try {
            dataSource.connection.use { conn ->
                conn.autoCommit = false
                val query = "{? = call P_SELECTONE.SELECTONE_PRODUKTY(?)}"
                val stmt = conn.prepareCall(query)
                stmt.registerOutParameter(1, Types.STRUCT, "P_TYPES.PRODUKT")
                stmt.setInt(2, id)
                stmt.execute()
                val produktStruct = stmt.getObject(1) as OracleStruct
                val produktAttrs = produktStruct.attributes
                return Produkt(
                    (produktAttrs[0] as BigDecimal).toInt(), produktAttrs[1] as String,
                    (produktAttrs[2] as BigDecimal).toInt(), (produktAttrs[3] as BigDecimal).toInt()
                )
            }
        } catch (e: SQLException) {
            val error = Alert(Alert.AlertType.ERROR)
            error.title = "Błąd!"
            error.headerText = "Błąd połączenia z bazą!"
            error.contentText = e.message
            error.show()
        }
        return null
    }

    fun getZamowienie(id: Int): Zamowienie? {
        try {
            dataSource.connection.use { conn ->
                conn.autoCommit = false
                val query = "{? = call P_SELECTONE.SELECTONE_ZAMOWIENIA(?)}"
                val stmt = conn.prepareCall(query)
                stmt.registerOutParameter(1, Types.STRUCT, "P_TYPES.ZAMOWIENIE")
                stmt.setInt(2, id)
                stmt.execute()
                val zamowienieStruct = stmt.getObject(1) as OracleStruct
                val zamowienieAttrs = zamowienieStruct.attributes
                return Zamowienie(
                    (zamowienieAttrs[0] as BigDecimal).toInt(), (zamowienieAttrs[1] as BigDecimal).toInt(),
                    (zamowienieAttrs[2] as BigDecimal).toInt(), zamowienieAttrs[3] as String,
                    (zamowienieAttrs[4] as BigDecimal).toInt()
                )
            }
        } catch (e: SQLException) {
            val error = Alert(Alert.AlertType.ERROR)
            error.title = "Błąd!"
            error.headerText = "Błąd połączenia z bazą!"
            error.contentText = e.message
            error.show()
        }
        return null
    }
}