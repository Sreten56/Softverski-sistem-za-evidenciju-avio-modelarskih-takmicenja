/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package baza;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sreck
 */
public class DBConnection {

    private static DBConnection instance;
    private Connection connection;

    private DBConnection() {
        try {
            Properties properties = new Properties();

            // pokušaj da učita iz resources classpath
            InputStream input = getClass().getClassLoader().getResourceAsStream("dbconfig.properties");

            if (input == null) {
                // ako nema u classpath-u, probaj iz root foldera
                input = new FileInputStream("dbconfig.properties");
            }

            properties.load(input);

            String url = properties.getProperty("url");
            String user = properties.getProperty("username");
            String pass = properties.getProperty("password");

            connection = DriverManager.getConnection(url, user, pass);
            connection.setAutoCommit(false); // da možeš sam da commit/rollback

        } catch (Exception ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, "Greška pri konekciji!", ex);
        }
    }

    public static DBConnection getInstance() {
        if (instance == null) {
            instance = new DBConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}
