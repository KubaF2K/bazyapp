package org.example.bazyapp

import javafx.scene.control.Alert
import oracle.jdbc.OracleConnection
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

    fun testConnection(): Boolean {
        try {
            dataSource.connection.use { conn -> return conn.isValid(0) }
        } catch (e: SQLException) {
            val error = Alert(Alert.AlertType.ERROR)
            error.title = "Błąd!"
            error.headerText = "Błąd połączenia z bazą!"
            error.contentText = e.message
            error.showAndWait()
        }
        return false
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

    // Create
    fun addOrder(id: Int, items: Array<Array<Any>>) {
        try {
            dataSource.connection.use { conn ->
                conn.autoCommit = false
                val itemArray = (conn as OracleConnection).createOracleArray("P_TYPES.T_ITEM", items)
                val query = "CALL P_ADD.ADD_ZAMOWIENIE(?, ?)"
                val stmt = conn.prepareCall(query)
                stmt.setInt(1, id)
                stmt.setArray(2, itemArray)
                stmt.execute()
            }
        } catch (e: SQLException) {
            val error = Alert(Alert.AlertType.ERROR)
            error.title = "Błąd!"
            error.headerText = "Błąd połączenia z bazą!"
            error.contentText = e.message
            error.show()
        }
    }

    fun addWarehouse(address: String, name: String) {
        try {
            dataSource.connection.use { conn ->
                conn.autoCommit = false
                val query = "CALL P_ADD.ADD_MAGAZYN(?, ?)"
                val stmt = conn.prepareCall(query)
                stmt.setString(1, address)
                stmt.setString(2, name)
                stmt.execute()
            }
        } catch (e: SQLException) {
            val error = Alert(Alert.AlertType.ERROR)
            error.title = "Błąd!"
            error.headerText = "Błąd połączenia z bazą!"
            error.contentText = e.message
            error.show()
        }
    }

    fun addClient(name: String, surname: String, pesel: Long) {
        try {
            dataSource.connection.use { conn ->
                conn.autoCommit = false
                val query = "CALL P_ADD.ADD_KLIENT(?, ?, ?)"
                val stmt = conn.prepareCall(query)
                stmt.setString(1, name)
                stmt.setString(2, surname)
                stmt.setLong(3, pesel)
                stmt.execute()
            }
        } catch (e: SQLException) {
            val error = Alert(Alert.AlertType.ERROR)
            error.title = "Błąd!"
            error.headerText = "Błąd połączenia z bazą!"
            error.contentText = e.message
            error.show()
        }
    }

    fun addProduct(name: String, price: Int, count: Int = 0) {
        try {
            dataSource.connection.use { conn ->
                conn.autoCommit = false
                val query = "CALL P_ADD.ADD_PRODUKT(?, ?, ?)"
                val stmt = conn.prepareCall(query)
                stmt.setString(1, name)
                stmt.setInt(2, price)
                stmt.setInt(3, count)
                stmt.execute()
            }
        } catch (e: SQLException) {
            val error = Alert(Alert.AlertType.ERROR)
            error.title = "Błąd!"
            error.headerText = "Błąd połączenia z bazą!"
            error.contentText = e.message
            error.show()
        }
    }

    fun addWorker(magazynId: Int, name: String, surname: String, address: String, pesel: Long, payout: Int) {
        try {
            dataSource.connection.use { conn ->
                conn.autoCommit = false
                val query = "CALL P_ADD.ADD_PRACOWNIK(?, ?, ?, ?, ?, ?)"
                val stmt = conn.prepareCall(query)
                stmt.setInt(1, magazynId)
                stmt.setString(2, name)
                stmt.setString(3, surname)
                stmt.setString(4, address)
                stmt.setLong(5, pesel)
                stmt.setInt(6, payout)
                stmt.execute()
            }
        } catch (e: SQLException) {
            val error = Alert(Alert.AlertType.ERROR)
            error.title = "Błąd!"
            error.headerText = "Błąd połączenia z bazą!"
            error.contentText = e.message
            error.show()
        }
    }

    //Read
    fun getClient(id: Int): Klient? {
        try {
            dataSource.connection.use { conn ->
                conn.autoCommit = false
                val query = "{? = call P_READ.SELECTONE_KLIENCI(?)}"
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

    fun getWarehouse(id: Int): Magazyn? {
        try {
            dataSource.connection.use { conn ->
                conn.autoCommit = false
                val query = "{? = call P_READ.SELECTONE_MAGAZYNY(?)}"
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

    fun getWorker(id: Int): Pracownik? {
        try {
            dataSource.connection.use { conn ->
                conn.autoCommit = false
                val query = "{? = call P_READ.SELECTONE_PRACOWNICY(?)}"
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

    fun getProduct(id: Int): Produkt? {
        try {
            dataSource.connection.use { conn ->
                conn.autoCommit = false
                val query = "{? = call P_READ.SELECTONE_PRODUKTY(?)}"
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

    fun getOrder(id: Int): Zamowienie? {
        try {
            dataSource.connection.use { conn ->
                conn.autoCommit = false
                val query = "{? = call P_READ.SELECTONE_ZAMOWIENIA(?)}"
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

    fun getOrderHistorical(id: Int): Zamowienie? {
        try {
            dataSource.connection.use { conn ->
                conn.autoCommit = false
                val query = "{? = call P_READ.SELECTONE_ZAMOWIENIA_HISTORYCZNE(?)}"
                val stmt = conn.prepareCall(query)
                stmt.registerOutParameter(1, Types.STRUCT, "P_TYPES.ZAMOWIENIE")
                stmt.setInt(2, id)
                stmt.execute()
                val zamowienieStruct = stmt.getObject(1) as OracleStruct
                val zamowienieAttrs = zamowienieStruct.attributes
                return Zamowienie(
                    (zamowienieAttrs[0] as BigDecimal).toInt(),
                    (zamowienieAttrs[1] as BigDecimal).toInt(),
                    (zamowienieAttrs[2] as BigDecimal).toInt(),
                    if(zamowienieAttrs[3] != null) zamowienieAttrs[3] as String else "Nieokreślony",
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

    fun getDeliveries(): Map<Int, Dostawa> {
        try {
            dataSource.connection.use { conn ->
                conn.autoCommit = false
                val query = "{ ? = call P_READ.SELECTALL_DOSTAWY }"
                val stmt = conn.prepareCall(query)
                stmt.registerOutParameter(1, Types.ARRAY, "P_TYPES.T_DOSTAWA")
                stmt.execute()
                val dostawySqlArray = stmt.getArray(1)
                val map = HashMap<Int, Dostawa>()
                val dostawyResult = dostawySqlArray.resultSet
                while (dostawyResult.next()) {
                    val dostawaStruct = dostawyResult.getObject(2) as OracleStruct
                    val dostawaAttrs = dostawaStruct.attributes
                    val dostawa = Dostawa(
                        (dostawaAttrs[0] as BigDecimal).toInt(),
                        (dostawaAttrs[1] as BigDecimal).toInt(),
                        (dostawaAttrs[2] as BigDecimal).toInt(),
                        (dostawaAttrs[3] as BigDecimal).toInt(),
                        (dostawaAttrs[4] as BigDecimal).toInt(),
                        (dostawaAttrs[5] as BigDecimal).toInt()
                    )
                    map[(dostawaAttrs[0] as BigDecimal).toInt()] = dostawa
                }
                return map
            }
        } catch (e: SQLException) {
            val error = Alert(Alert.AlertType.ERROR)
            error.title = "Błąd!"
            error.headerText = "Błąd połączenia z bazą!"
            error.contentText = e.message
            error.show()
        }
        return emptyMap()
    }

    fun getClients(): Map<Int, Klient> {
        try {
            dataSource.connection.use { conn ->
                conn.autoCommit = false
                val query = "{ ? = call P_READ.SELECTALL_KLIENCI }"
                val stmt = conn.prepareCall(query)
                stmt.registerOutParameter(1, Types.ARRAY, "P_TYPES.T_KLIENT")
                stmt.execute()
                val klienciSqlArray = stmt.getArray(1)
                val map = HashMap<Int, Klient>()
                val klienciResult = klienciSqlArray.resultSet
                while (klienciResult.next()) {
                    val klientStruct = klienciResult.getObject(2) as OracleStruct
                    val klientAttrs = klientStruct.attributes
                    val klient = Klient(
                        (klientAttrs[0] as BigDecimal).toInt(),
                        klientAttrs[1] as String,
                        klientAttrs[2] as String,
                        (klientAttrs[3] as BigDecimal).toLong()
                    )
                    map[(klientAttrs[0] as BigDecimal).toInt()] = klient
                }
                return map
            }
        } catch (e: SQLException) {
            val error = Alert(Alert.AlertType.ERROR)
            error.title = "Błąd!"
            error.headerText = "Błąd połączenia z bazą!"
            error.contentText = e.message
            error.show()
        }
        return emptyMap()
    }

    fun getWarehouses(): Map<Int, Magazyn> {
        try {
            dataSource.connection.use { conn ->
                conn.autoCommit = false
                val query = "{ ? = call P_READ.SELECTALL_MAGAZYNY }"
                val stmt = conn.prepareCall(query)
                stmt.registerOutParameter(1, Types.ARRAY, "P_TYPES.T_MAGAZYN")
                stmt.execute()
                val magazynySqlArray = stmt.getArray(1)
                val map = HashMap<Int, Magazyn>()
                val magazynyResult = magazynySqlArray.resultSet
                while (magazynyResult.next()) {
                    val magazynStruct = magazynyResult.getObject(2) as OracleStruct
                    val magazynAttrs = magazynStruct.attributes
                    val magazyn = Magazyn(
                        (magazynAttrs[0] as BigDecimal).toInt(),
                        magazynAttrs[1] as String,
                        magazynAttrs[2] as String
                    )
                    map[(magazynAttrs[0] as BigDecimal).toInt()] = magazyn
                }
                return map
            }
        } catch (e: SQLException) {
            val error = Alert(Alert.AlertType.ERROR)
            error.title = "Błąd!"
            error.headerText = "Błąd połączenia z bazą!"
            error.contentText = e.message
            error.show()
        }
        return emptyMap()
    }

    fun getWorkers(): Map<Int, Pracownik> {
        try {
            dataSource.connection.use { conn ->
                conn.autoCommit = false
                val query = "{ ? = call P_READ.SELECTALL_PRACOWNICY }"
                val stmt = conn.prepareCall(query)
                stmt.registerOutParameter(1, Types.ARRAY, "P_TYPES.T_PRACOWNIK")
                stmt.execute()
                val pracownicySqlArray = stmt.getArray(1)
                val map = HashMap<Int, Pracownik>()
                val pracownicyResult = pracownicySqlArray.resultSet
                while (pracownicyResult.next()) {
                    val pracownikStruct = pracownicyResult.getObject(2) as OracleStruct
                    val pracownikAttrs = pracownikStruct.attributes
                    val pracownik = Pracownik(
                        (pracownikAttrs[0] as BigDecimal).toInt(),
                        (pracownikAttrs[1] as BigDecimal).toInt(),
                        pracownikAttrs[2] as String,
                        pracownikAttrs[3] as String,
                        pracownikAttrs[4] as String,
                        (pracownikAttrs[5] as BigDecimal).toLong(),
                        (pracownikAttrs[6] as BigDecimal).toInt(),
                    )
                    map[(pracownikAttrs[0] as BigDecimal).toInt()] = pracownik
                }
                return map
            }
        } catch (e: SQLException) {
            val error = Alert(Alert.AlertType.ERROR)
            error.title = "Błąd!"
            error.headerText = "Błąd połączenia z bazą!"
            error.contentText = e.message
            error.show()
        }
        return emptyMap()
    }

    fun getProducts(): Map<Int, Produkt> {
        try {
            dataSource.connection.use { conn ->
                conn.autoCommit = false
                val query = "{ ? = call P_READ.SELECTALL_PRODUKTY }"
                val stmt = conn.prepareCall(query)
                stmt.registerOutParameter(1, Types.ARRAY, "P_TYPES.T_PRODUKT")
                stmt.execute()
                val produktySqlArray = stmt.getArray(1)
                val map = HashMap<Int, Produkt>()
                val produktyResult = produktySqlArray.resultSet
                while (produktyResult.next()) {
                    val produktStruct = produktyResult.getObject(2) as OracleStruct
                    val produktAttrs = produktStruct.attributes
                    val produkt = Produkt(
                        (produktAttrs[0] as BigDecimal).toInt(),
                        produktAttrs[1] as String,
                        (produktAttrs[2] as BigDecimal).toInt(),
                        (produktAttrs[3] as BigDecimal).toInt()
                    )
                    map[(produktAttrs[0] as BigDecimal).toInt()] = produkt
                }
                return map
            }
        } catch (e: SQLException) {
            val error = Alert(Alert.AlertType.ERROR)
            error.title = "Błąd!"
            error.headerText = "Błąd połączenia z bazą!"
            error.contentText = e.message
            error.show()
        }
        return emptyMap()
    }

    fun getOrders(): Map<Int, Zamowienie> {
        try {
            dataSource.connection.use { conn ->
                conn.autoCommit = false
                val query = "{ ? = call P_READ.SELECTALL_ZAMOWIENIA }"
                val stmt = conn.prepareCall(query)
                stmt.registerOutParameter(1, Types.ARRAY, "P_TYPES.T_ZAMOWIENIE")
                stmt.execute()
                val zamowieniaSqlArray = stmt.getArray(1)
                val map = HashMap<Int, Zamowienie>()
                val zamowieniaResult = zamowieniaSqlArray.resultSet
                while (zamowieniaResult.next()) {
                    val zamowienieStruct = zamowieniaResult.getObject(2) as OracleStruct
                    val zamowienieAttrs = zamowienieStruct.attributes
                    val zamowienie = Zamowienie(
                        (zamowienieAttrs[0] as BigDecimal).toInt(),
                        (zamowienieAttrs[1] as BigDecimal).toInt(),
                        (zamowienieAttrs[2] as BigDecimal).toInt(),
                        zamowienieAttrs[3] as String,
                        (zamowienieAttrs[4] as BigDecimal).toInt()
                    )
                    map[(zamowienieAttrs[0] as BigDecimal).toInt()] = zamowienie
                }
                return map
            }
        } catch (e: SQLException) {
            val error = Alert(Alert.AlertType.ERROR)
            error.title = "Błąd!"
            error.headerText = "Błąd połączenia z bazą!"
            error.contentText = e.message
            error.show()
        }
        return emptyMap()
    }

    fun getOrdersHistorical(): Map<Int, Zamowienie> {
        try {
            dataSource.connection.use { conn ->
                conn.autoCommit = false
                val query = "{ ? = call P_READ.SELECTALL_ZAMOWIENIA_HISTORYCZNE }"
                val stmt = conn.prepareCall(query)
                stmt.registerOutParameter(1, Types.ARRAY, "P_TYPES.T_ZAMOWIENIE")
                stmt.execute()
                val zamowieniaSqlArray = stmt.getArray(1)
                val map = HashMap<Int, Zamowienie>()
                val zamowieniaResult = zamowieniaSqlArray.resultSet
                while (zamowieniaResult.next()) {
                    val zamowienieStruct = zamowieniaResult.getObject(2) as OracleStruct
                    val zamowienieAttrs = zamowienieStruct.attributes
                    val zamowienie = Zamowienie(
                        (zamowienieAttrs[0] as BigDecimal).toInt(),
                        (zamowienieAttrs[1] as BigDecimal).toInt(),
                        (zamowienieAttrs[2] as BigDecimal).toInt(),
                        if(zamowienieAttrs[3] != null) zamowienieAttrs[3] as String else "Nieokreślony",
                        (zamowienieAttrs[4] as BigDecimal).toInt()
                    )
                    map[(zamowienieAttrs[0] as BigDecimal).toInt()] = zamowienie
                }
                return map
            }
        } catch (e: SQLException) {
            val error = Alert(Alert.AlertType.ERROR)
            error.title = "Błąd!"
            error.headerText = "Błąd połączenia z bazą!"
            error.contentText = e.message
            error.show()
        }
        return emptyMap()
    }

    fun getOrderDetails(id: Int): List<ZamowienieSzcz> {
        try {
            dataSource.connection.use { conn ->
                conn.autoCommit = false
                val query = "{ ? = call P_READ.SELECT_ZAMOWIENIA_SZCZEGOLY(?) }"
                val stmt = conn.prepareCall(query)
                stmt.registerOutParameter(1, Types.ARRAY, "P_TYPES.T_ZAMOWIENIE_SZCZEGOLY")
                stmt.setInt(2, id)
                stmt.execute()
                val szczegolySqlArray = stmt.getArray(1)
                val list = ArrayList<ZamowienieSzcz>()
                val szczegolyResult = szczegolySqlArray.resultSet
                while (szczegolyResult.next()) {
                    val szczegolyStruct = szczegolyResult.getObject(2) as OracleStruct
                    val szczegolyAttrs = szczegolyStruct.attributes
                    val item = ZamowienieSzcz(
                        (szczegolyAttrs[0] as BigDecimal).toInt(),
                        (szczegolyAttrs[1] as BigDecimal).toInt(),
                        (szczegolyAttrs[2] as BigDecimal).toInt(),
                        (szczegolyAttrs[3] as BigDecimal).toInt()
                    )
                    list.add(item)
                }
                return list
            }
        } catch (e: SQLException) {
            val error = Alert(Alert.AlertType.ERROR)
            error.title = "Błąd!"
            error.headerText = "Błąd połączenia z bazą!"
            error.contentText = e.message
            error.show()
        }
        return emptyList()
    }

    fun getOrderDetailsHistorical(id: Int): List<ZamowienieSzcz> {
        try {
            dataSource.connection.use { conn ->
                conn.autoCommit = false
                val query = "{ ? = call P_READ.SELECT_ZAMOWIENIA_SZCZEGOLY_HISTORYCZNE(?) }"
                val stmt = conn.prepareCall(query)
                stmt.registerOutParameter(1, Types.ARRAY, "P_TYPES.T_ZAMOWIENIE_SZCZEGOLY")
                stmt.setInt(2, id)
                stmt.execute()
                val szczegolySqlArray = stmt.getArray(1)
                val list = ArrayList<ZamowienieSzcz>()
                val szczegolyResult = szczegolySqlArray.resultSet
                while (szczegolyResult.next()) {
                    val szczegolyStruct = szczegolyResult.getObject(2) as OracleStruct
                    val szczegolyAttrs = szczegolyStruct.attributes
                    val item = ZamowienieSzcz(
                        (szczegolyAttrs[0] as BigDecimal).toInt(),
                        (szczegolyAttrs[1] as BigDecimal).toInt(),
                        (szczegolyAttrs[2] as BigDecimal).toInt(),
                        (szczegolyAttrs[3] as BigDecimal).toInt()
                    )
                    list.add(item)
                }
                return list
            }
        } catch (e: SQLException) {
            val error = Alert(Alert.AlertType.ERROR)
            error.title = "Błąd!"
            error.headerText = "Błąd połączenia z bazą!"
            error.contentText = e.message
            error.show()
        }
        return emptyList()
    }

    //Update
    fun editClient(id: Int, nazwisko: String) {
        try {
            dataSource.connection.use { conn ->
                conn.autoCommit = false
                val query = "CALL P_UPDATE.UPDATE_KLIENCI(?, ?)"
                val stmt = conn.prepareCall(query)
                stmt.setInt(1, id)
                stmt.setString(2, nazwisko)
                stmt.execute()
            }
        } catch (e: SQLException) {
            val error = Alert(Alert.AlertType.ERROR)
            error.title = "Błąd!"
            error.headerText = "Błąd połączenia z bazą!"
            error.contentText = e.message
            error.show()
        }
    }

    fun editWarehouse(id: Int, nazwa: String) {
        try {
            dataSource.connection.use { conn ->
                conn.autoCommit = false
                val query = "CALL P_UPDATE.UPDATE_MAGAZYNY(?, ?)"
                val stmt = conn.prepareCall(query)
                stmt.setInt(1, id)
                stmt.setString(2, nazwa)
                stmt.execute()
            }
        } catch (e: SQLException) {
            val error = Alert(Alert.AlertType.ERROR)
            error.title = "Błąd!"
            error.headerText = "Błąd połączenia z bazą!"
            error.contentText = e.message
            error.show()
        }
    }

    fun editWorker(
        id: Int,
        idMagazynu: Int? = null,
        nazwisko: String? = null,
        adres: String? = null,
        wyplata: Int? = null
    ) {
        try {
            dataSource.connection.use { conn ->
                conn.autoCommit = false
                val query = "CALL P_UPDATE.UPDATE_PRACOWNICY(?, ?, ?, ?, ?)"
                val stmt = conn.prepareCall(query)
                stmt.setInt(1, id)
                stmt.setInt(2, idMagazynu ?: 0)
                stmt.setString(3, nazwisko)
                stmt.setString(4, adres)
                stmt.setInt(5, wyplata ?: 0)
                stmt.execute()
            }
        } catch (e: SQLException) {
            val error = Alert(Alert.AlertType.ERROR)
            error.title = "Błąd!"
            error.headerText = "Błąd połączenia z bazą!"
            error.contentText = e.message
            error.show()
        }
    }

    fun editProduct(id: Int, cena: Int) {
        try {
            dataSource.connection.use { conn ->
                conn.autoCommit = false
                val query = "CALL P_UPDATE.UPDATE_PRODUKTY(?, ?)"
                val stmt = conn.prepareCall(query)
                stmt.setInt(1, id)
                stmt.setInt(2, cena)
                stmt.execute()
            }
        } catch (e: SQLException) {
            val error = Alert(Alert.AlertType.ERROR)
            error.title = "Błąd!"
            error.headerText = "Błąd połączenia z bazą!"
            error.contentText = e.message
            error.show()
        }
    }

    fun editOrder(id: Int, status: String) {
        try {
            dataSource.connection.use { conn ->
                conn.autoCommit = false
                val query = "CALL P_UPDATE.UPDATE_ZAMOWIENIA(?, ?)"
                val stmt = conn.prepareCall(query)
                stmt.setInt(1, id)
                stmt.setString(2, status)
                stmt.execute()
            }
        } catch (e: SQLException) {
            val error = Alert(Alert.AlertType.ERROR)
            error.title = "Błąd!"
            error.headerText = "Błąd połączenia z bazą!"
            error.contentText = e.message
            error.show()
        }
    }

    //Delete
    fun delWarehouse(id: Int) {
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

    fun delClient(id: Int) {
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

    fun delWorker(id: Int) {
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

    fun delProduct(id: Int) {
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

    fun delOrder(id: Int) {
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