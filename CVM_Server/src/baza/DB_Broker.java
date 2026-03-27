/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package baza;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Apstraktni_Domenski_Objekat;
import model.VcvmCVM;

/**
 *
 * @author sreck
 */
public class DB_Broker {

    private static DB_Broker instance;
    private Connection connection;

    //  Konstruktor sada koristi zajednicku konekciju iz klase Konekcija
    public DB_Broker() {
        connection = Konekcija.getInstance().getConnection();
    }

    public static DB_Broker getInstance() {
        if (instance == null) {
            instance = new DB_Broker();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    public ArrayList<Apstraktni_Domenski_Objekat> select(Apstraktni_Domenski_Objekat ado) throws SQLException {
        String upit;
        if (ado instanceof VcvmCVM) {
            // koristi join samo za SELECT
            upit = "SELECT * FROM " + ((VcvmCVM) ado).vratiNazivTabeleJoin();
        } else {
            upit = "SELECT * FROM " + ado.vratiNazivTabele();
        }

        Statement s = connection.createStatement();
        ResultSet rs = s.executeQuery(upit);
        return new ArrayList<>(ado.vratiListu(rs));
    }

    public boolean insert(Apstraktni_Domenski_Objekat ado) throws SQLException {
        String upit = "INSERT INTO " + ado.vratiNazivTabele() + " "
                + ado.vratiKoloneZaInsert() + " VALUES (" + ado.vratiVrednostiZaInsert() + ")";
        System.out.println("SQL INSERT pokusaj: " + upit);
        Statement s = connection.createStatement();
        int rows = s.executeUpdate(upit);
        return rows > 0;
    }

    public int insertReturnID(Apstraktni_Domenski_Objekat ado) throws SQLException {
        String upit = "INSERT INTO " + ado.vratiNazivTabele() + " "
                + ado.vratiKoloneZaInsert() + " VALUES (" + ado.vratiVrednostiZaInsert() + ")";
        PreparedStatement ps = connection.prepareStatement(upit, Statement.RETURN_GENERATED_KEYS);

        int affectedRows = ps.executeUpdate();
        if (affectedRows == 0) {
            return -1;
        }

        ResultSet rs = ps.getGeneratedKeys();
        int newId = rs.next() ? rs.getInt(1) : -1;
        rs.close();
        ps.close();
        return newId;
    }

    public boolean update(Apstraktni_Domenski_Objekat ado) throws SQLException {
        String upit = "UPDATE " + ado.vratiNazivTabele()
                + " SET " + ado.vratiVrednostiZaUpdate()
                + " WHERE " + ado.vratiUslov();
        System.out.println("SQL UPDATE: " + upit);  // DEBUG
        Statement s = connection.createStatement();
        int rows = s.executeUpdate(upit);
        return rows > 0;
    }

    public boolean delete(Apstraktni_Domenski_Objekat ado) throws SQLException {
        String upit = "DELETE FROM " + ado.vratiNazivTabele()
                + " WHERE " + ado.vratiUslov();
        Statement s = connection.createStatement();
        int rows = s.executeUpdate(upit);
        return rows > 0;
    }

    public void commit() throws SQLException {
        connection.commit();
    }

    public void rollback() throws SQLException {
        connection.rollback();
    }

    public boolean isConnectionAlive() {
        try {
            return (connection != null && connection.isValid(2));
        } catch (SQLException e) {
            return false;
        }
    }

}

/*

// 🔹 Provera da li je konekcija živa pre svake operacije
    private void checkConnection() throws java.sql.SQLNonTransientConnectionException {
        try {
            if (connection == null || connection.isClosed() || !connection.isValid(2)) {
                throw new java.sql.SQLNonTransientConnectionException("Veza sa bazom prekinuta!");
            }
        } catch (SQLException ex) {
            throw new java.sql.SQLNonTransientConnectionException("Veza sa bazom prekinuta!", ex);
        }
    }

    public ArrayList<Apstraktni_Domenski_Objekat> select(Apstraktni_Domenski_Objekat ado) throws SQLException {
        checkConnection(); // 🔹 nova provera
        String upit;
        if (ado instanceof VcvmCVM) {
            upit = "SELECT * FROM " + ((VcvmCVM) ado).vratiNazivTabeleJoin();
        } else {
            upit = "SELECT * FROM " + ado.vratiNazivTabele();
        }

        try (Statement s = connection.createStatement(); ResultSet rs = s.executeQuery(upit)) {
            return new ArrayList<>(ado.vratiListu(rs));
        }
    }

    public boolean insert(Apstraktni_Domenski_Objekat ado) throws SQLException {
        checkConnection();
        String upit = "INSERT INTO " + ado.vratiNazivTabele() + " "
                + ado.vratiKoloneZaInsert() + " VALUES (" + ado.vratiVrednostiZaInsert() + ")";
        System.out.println("SQL INSERT pokušaj: " + upit);
        try (Statement s = connection.createStatement()) {
            int rows = s.executeUpdate(upit);
            return rows > 0;
        }
    }

    public int insertReturnID(Apstraktni_Domenski_Objekat ado) throws SQLException {
        checkConnection();
        String upit = "INSERT INTO " + ado.vratiNazivTabele() + " "
                + ado.vratiKoloneZaInsert() + " VALUES (" + ado.vratiVrednostiZaInsert() + ")";
        try (PreparedStatement ps = connection.prepareStatement(upit, Statement.RETURN_GENERATED_KEYS)) {
            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) return -1;

            try (ResultSet rs = ps.getGeneratedKeys()) {
                return rs.next() ? rs.getInt(1) : -1;
            }
        }
    }

    public boolean update(Apstraktni_Domenski_Objekat ado) throws SQLException {
        checkConnection();
        String upit = "UPDATE " + ado.vratiNazivTabele()
                + " SET " + ado.vratiVrednostiZaUpdate()
                + " WHERE " + ado.vratiUslov();
        System.out.println("SQL UPDATE: " + upit);
        try (Statement s = connection.createStatement()) {
            int rows = s.executeUpdate(upit);
            return rows > 0;
        }
    }

    public boolean delete(Apstraktni_Domenski_Objekat ado) throws SQLException {
        checkConnection();
        String upit = "DELETE FROM " + ado.vratiNazivTabele()
                + " WHERE " + ado.vratiUslov();
        try (Statement s = connection.createStatement()) {
            int rows = s.executeUpdate(upit);
            return rows > 0;
        }
    }

    public void commit() throws SQLException {
        checkConnection();
        connection.commit();
    }

    public void rollback() throws SQLException {
        checkConnection();
        connection.rollback();
    }   

 */
//
//    private static DB_Broker instance;
//    private Connection connection;
//
//    public DB_Broker() throws Exception {
//        Properties properties = new Properties();
//
//        // Prvo probaj iz classpath-a (resources folder)
//        InputStream input = getClass().getClassLoader().getResourceAsStream("resources/dbconfig.properties");
//
//        if (input == null) {
//            throw new FileNotFoundException("dbconfig.properties nije pronađen u classpath-u!");
//        }
//
//        properties.load(input);
//
//        String url = properties.getProperty("url");
//        String user = properties.getProperty("username");
//        String pass = properties.getProperty("password");
//
//        connection = DriverManager.getConnection(url, user, pass);
//        connection.setAutoCommit(false);
//    }
//
//    public static DB_Broker getInstance() throws Exception {
//        if (instance == null) {
//            instance = new DB_Broker();
//        }
//        return instance;
//    }
//
//    public Connection getConnection() {
//        return connection;
//    }
// --- CRUD ---
//    public ArrayList<Apstraktni_Domenski_Objekat> select(Apstraktni_Domenski_Objekat ado) throws SQLException {
//        String upit = "SELECT * FROM " + ado.vratiNazivTabele();
//        Statement s = connection.createStatement();
//        ResultSet rs = s.executeQuery(upit);
//        return new ArrayList<>(ado.vratiListu(rs));
//    }
//    public boolean update(Apstraktni_Domenski_Objekat ado) throws SQLException {
//        String upit = "UPDATE " + ado.vratiNazivTabele()
//                + " SET " + ado.vratiVrednostiZaUpdate()
//                + " WHERE " + ado.vratiUslov();
//        Statement s = connection.createStatement();
//        int rows = s.executeUpdate(upit);
//        return rows > 0;
//    }
